package com.example.catchtable.repository;

import com.example.catchtable.domain.PickupTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class PickupTimeRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  PickupTimeRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<PickupTime> pickupTimeRowMapper = (rs, rowNum) ->
      PickupTime.fromEntity(
          rs.getLong("id"), // int unsigned -> Long
          rs.getTime("time"),
          rs.getLong("restaurant_id") // int unsigned -> Long
      );

  public Optional<PickupTime> save(PickupTime entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<PickupTime> insert(PickupTime entity) {
    String sql = "INSERT INTO pickup_time (restaurant_id, time) VALUES (?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getRestaurantId()); // int -> Long
      ps.setTime(2, entity.getTime());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue()); // int -> Long
  }

  private Optional<PickupTime> update(PickupTime entity) {
    String sql = "UPDATE pickup_time SET restaurant_id = ?, time = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getRestaurantId(), entity.getTime(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<PickupTime> saveAll(Iterable<PickupTime> entities) {
    List<PickupTime> result = new ArrayList<>();
    for (PickupTime entity : entities) {
      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
    }
    return result;
  }

  public Optional<PickupTime> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM pickup_time WHERE id = ?";
    List<PickupTime> result = jdbcTemplate.query(sql, pickupTimeRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM pickup_time WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<PickupTime> findAll() {
    String sql = "SELECT * FROM pickup_time";
    return jdbcTemplate.query(sql, pickupTimeRowMapper);
  }

  public Iterable<PickupTime> findAll(Iterable<PickupTime> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getId()));
    String sql = "SELECT * FROM pickup_time WHERE id IN (?)";
    return jdbcTemplate.query(sql, pickupTimeRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM pickup_time";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
    String sql = "DELETE FROM pickup_time WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(PickupTime entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<PickupTime> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM pickup_time";
    jdbcTemplate.update(sql);
  }
}