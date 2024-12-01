package com.example.catchtable.repository;

import com.example.catchtable.domain.Pickup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

//  public Optional<Pickup> save(Pickup entity) {
//    if (entity.getId() == null) {
//      return insert(entity);
//    } else {
//      return update(entity);
//    }
//  }

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

//  public Iterable<Pickup> saveAll(Iterable<Pickup> entities) {
//    List<Pickup> result = new ArrayList<>();
//    for (Pickup entity : entities) {
//      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
//    }
//    return result;
//  }

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
}