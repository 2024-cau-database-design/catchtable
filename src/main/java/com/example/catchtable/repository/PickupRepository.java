package com.example.catchtable.repository;

import com.example.catchtable.domain.Pickup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.type.TypeReference;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PickupRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  PickupRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Pickup> pickupRowMapper = (rs, rowNum) ->
      Pickup.fromEntity(
          rs.getLong("id"), // int unsigned -> Long
          rs.getLong("restaurant_id"),
          rs.getLong("customer_id"),
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")
      );

  public Long insert(Pickup entity) {
    String sql = "INSERT INTO pickup (id, restaurant_id, customer_id) VALUES (?, ?, ?)"; // restaurant_id 추가
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getId()); // LocalDateTime -> Timestamp
      ps.setLong(2, entity.getRestaurantId());
      ps.setLong(3, entity.getCustomerId());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
      assert key != null;
      return key.longValue(); // int -> Long
  }

  public Optional<Pickup> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM pickup WHERE id = ?";
    List<Pickup> result = jdbcTemplate.query(sql, pickupRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM pickup WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public boolean existsByIdAndDate(Long id, LocalDateTime pickupAt) {
    try {
      Optional<Map<String, Object>> pickupInfo = getPickupDetail(id);
      if (pickupInfo.isEmpty()) {
        return false;
      }

      // pickup_at 값을 가져와서 비교
      Timestamp dbPickupAt = (Timestamp) pickupInfo.get().get("pickup_at");
      return dbPickupAt != null && dbPickupAt.toLocalDateTime().equals(pickupAt);
    } catch (Exception e) {
      // 예외 처리 (로깅 등)
      e.printStackTrace();
      return false;
    }
  }

  public Iterable<Pickup> findAll() {
    String sql = "SELECT * FROM pickup";
    return jdbcTemplate.query(sql, pickupRowMapper);
  }

  public Iterable<Pickup> findAll(Iterable<Pickup> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getId()));
    String sql = "SELECT * FROM pickup WHERE id IN (?)";
    return jdbcTemplate.query(sql, pickupRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM pickup";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
    String sql = "DELETE FROM pickup WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(Pickup entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<Pickup> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM pickup";
    jdbcTemplate.update(sql);
  }

  public Pickup createBookingAndPickup(LocalDateTime pickupAt, Long restaurantId, Long customerId, Long pickupTimeId) {
    String sql = "{CALL create_booking_and_pickup(?, ?, ?, ?)}";

    return jdbcTemplate.execute((ConnectionCallback<Pickup>) connection -> {
      try (CallableStatement callableStatement = connection.prepareCall(sql)) {
        // Set input parameters
        callableStatement.setTimestamp(1, Timestamp.valueOf(pickupAt));
        callableStatement.setLong(2, restaurantId);
        callableStatement.setLong(3, customerId);
        callableStatement.setLong(4, pickupTimeId);

        // Execute the procedure
        boolean hasResultSet = callableStatement.execute();

        // Process the ResultSet
        if (hasResultSet) {
          try (ResultSet resultSet = callableStatement.getResultSet()) {
            if (resultSet.next()) {
              return Pickup.fromEntity(
                      resultSet.getLong("id"),
                      resultSet.getLong("restaurant_id"),
                      resultSet.getLong("customer_id"),
                      resultSet.getTimestamp("created_at"),
                      resultSet.getTimestamp("updated_at"),
                      resultSet.getBoolean("is_deleted"),
                      resultSet.getTimestamp("deleted_at")
              );
            }
          }
        }

        throw new RuntimeException("No pickup record returned from procedure");
      } catch (SQLException e) {
        System.out.println("SQL State: " + e.getSQLState());
        System.out.println("Error Code: " + e.getErrorCode());
        System.out.println("Message: " + e.getMessage());
        throw new RuntimeException("Error executing create_booking_and_pickup procedure", e);
      }
    });
  }

  public List<Map<String, Object>> findPickupsByRestaurantId(Long restaurantId, Optional<LocalDate> pickupDate) {
    // 1. restaurant_id와 pickup_date로 pickup ID들을 조회
    String sql = "SELECT p.id FROM pickup p " +
            "JOIN pickup_history ph ON p.id = ph.pickup_id " +
            "WHERE p.restaurant_id = ? " +
            "AND (ph.pickup_id, ph.created_at) IN (" +
            "    SELECT pickup_id, MAX(created_at) " +
            "    FROM pickup_history " +
            "    GROUP BY pickup_id" +
            ")";

    if (pickupDate.isPresent()) {
      sql += " AND DATE(ph.pickup_at) = ?";
    }

    List<Long> pickupIds = jdbcTemplate.query(sql,
            (rs, rowNum) -> rs.getLong("id"),
            pickupDate.isPresent()
                    ? new Object[]{restaurantId, java.sql.Date.valueOf(pickupDate.get())}
                    : new Object[]{restaurantId}
    );

    // 2. 조회된 pickup ID들로 getPickupDetails 호출
    if (pickupIds.isEmpty()) {
      return Collections.emptyList();
    }

    return getPickupDetails(pickupIds);
  }

  public List<Map<String, Object>> getPickupDetails(List<Long> pickupIds) {
    String sql = "{CALL get_pickup_info_list(?)}";

    return jdbcTemplate.execute((ConnectionCallback<List<Map<String, Object>>>) connection -> {
      try (CallableStatement callableStatement = connection.prepareCall(sql)) {
        String pickupIdsString = pickupIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        callableStatement.setString(1, pickupIdsString);

        boolean hasResultSet = callableStatement.execute();

        if (hasResultSet) {
          try (ResultSet resultSet = callableStatement.getResultSet()) {
            List<Map<String, Object>> results = new ArrayList<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
              Map<String, Object> result = new HashMap<>();
              for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                Object columnValue = resultSet.getObject(i);
                result.put(columnName, columnValue);
              }

              // JSON 문자열을 파싱하여 Map으로 변환
              if (result.containsKey("order_items")) {
                String orderItemsJson = (String) result.get("order_items");
                if (orderItemsJson != null) {
                  ObjectMapper objectMapper = new ObjectMapper();
                  List<Map<String, Object>> orderItems = objectMapper.readValue(orderItemsJson, new TypeReference<List<Map<String, Object>>>(){});
                  result.put("order_items", orderItems);
                }
              }

              results.add(result);
            }

            return results;
          }
        }

        return new ArrayList<>();
      } catch (SQLException | JsonProcessingException e) {
        throw new RuntimeException("Error executing get_pickup_info_list procedure", e);
      }
    });
  }

  public Optional<Map<String, Object>> getPickupDetail(Long pickupId) {
    List<Map<String, Object>> results = getPickupDetails(Collections.singletonList(pickupId));

    if (results.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(results.get(0));
  }
}