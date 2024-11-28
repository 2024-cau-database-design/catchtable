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
          rs.getInt("id"),
          rs.getInt("picked_at"),
          rs.getInt("pickup_time_id"),
          rs.getDate("pickup_date").toLocalDate(),
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")
      );

  public Optional<Pickup> save(Pickup entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<Pickup> insert(Pickup entity) {
    String sql = "INSERT INTO pickup (picked_at, pickup_time_id, pickup_date) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, entity.getPickedAt());
      ps.setInt(2, entity.getPickupTimeId());
      ps.setDate(3, Date.valueOf(entity.getPickupDate()));
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue());
  }

  private Optional<Pickup> update(Pickup entity) {
    String sql = "UPDATE pickup SET picked_at = ?, pickup_time_id = ?, pickup_date = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getPickedAt(), entity.getPickupTimeId(),
        Date.valueOf(entity.getPickupDate()), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<Pickup> saveAll(Iterable<Pickup> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<Pickup> findById(Integer id) {
    String sql = "SELECT * FROM pickup WHERE id = ?";
    List<Pickup> result = jdbcTemplate.query(sql, pickupRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM pickup WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<Pickup> findAll() {
    String sql = "SELECT * FROM pickup";
    return jdbcTemplate.query(sql, pickupRowMapper);
  }

  public Iterable<Pickup> findAll(Iterable<Pickup> entities) {
    List<Pickup> resultList = new ArrayList<>();
    for (Pickup entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM pickup";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
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