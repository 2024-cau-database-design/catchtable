package com.example.catchtable.repository;

import com.example.catchtable.domain.Pickup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
          rs.getTimestamp("picked_at"), // int -> Timestamp
          rs.getTimestamp("pickup_at").toLocalDateTime(),
          rs.getLong("pickup_time_id"), // int unsigned -> Long, 추가
          rs.getLong("restaurant_id"), // int unsigned -> Long, 추가
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")
      );

  public Long insert(Pickup entity) {
    String sql = "INSERT INTO pickup (id, picked_at, pickup_time_id, pickup_at, restaurant_id) VALUES (?, ?, ?, ?, ?)"; // restaurant_id 추가
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getId()); // LocalDateTime -> Timestamp
      ps.setTimestamp(2, null); // LocalDateTime -> Timestamp
      ps.setLong(3, entity.getPickupTimeId()); // LocalDateTime -> Timestamp
      ps.setTimestamp(4, Timestamp.valueOf(entity.getPickupAt()));
      ps.setLong(5, entity.getRestaurantId()); // Long, 추가
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
      assert key != null;
      return key.longValue(); // int -> Long
  }

  public Optional<Pickup> update(Pickup entity) {
    String sql = "UPDATE pickup SET picked_at = ?, pickup_time_id = ?, pickup_date = ?, restaurant_id = ? WHERE id = ?"; // restaurant_id 추가
    jdbcTemplate.update(sql,
        Timestamp.valueOf(entity.getPickedAt()), // LocalDateTime -> Timestamp
        Timestamp.valueOf(entity.getPickupAt()),
        entity.getRestaurantId(), // Long, 추가
        entity.getId());
    return findById(entity.getId());
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
    String sql = "SELECT count(*) FROM pickup WHERE id = ? AND pickup_at = ?";
    int[] types = {java.sql.Types.BIGINT, java.sql.Types.TIMESTAMP};

    try {
      Long result = jdbcTemplate.queryForObject(
              sql,
              new Object[]{id, Timestamp.valueOf(pickupAt)}, // 파라미터 값
              types, // SQL 타입
              Long.class // 반환 타입
      );
      return Optional.ofNullable(result).orElse(0L) > 0;
    } catch (Exception e) {
      // 에러 로그 찍기
      e.printStackTrace(); // 스택 트레이스 출력
      throw new RuntimeException("Error executing SQL: " + sql, e);
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
                      resultSet.getTimestamp("picked_at"),
                      resultSet.getTimestamp("pickup_at").toLocalDateTime(),
                      resultSet.getLong("pickup_time_id"),
                      resultSet.getLong("restaurant_id"),
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
        throw new RuntimeException("Error executing create_booking_and_pickup procedure", e);
      }
    });
  }

  public List<Pickup> findPickupsByRestaurantId(Long restaurantId, Optional<LocalDate> pickupDate) {
    StringBuilder sql = new StringBuilder("SELECT * FROM pickup WHERE restaurant_id = ?");
    List<Object> params = new ArrayList<>();
    params.add(restaurantId);

    if (pickupDate.isPresent()) {
      sql.append(" AND DATE(pickup_at) = ?");
      params.add(java.sql.Date.valueOf(pickupDate.get()));
    }

    return jdbcTemplate.query(sql.toString(), pickupRowMapper, params.toArray());
  }
}