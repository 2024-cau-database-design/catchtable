package com.example.catchtable.repository;

import com.example.catchtable.domain.PickupStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class PickupStatusRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  PickupStatusRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<PickupStatus> pickupStatusRowMapper = (rs, rowNum) ->
      PickupStatus.fromEntity(
          rs.getLong("id"),
          rs.getString("type")
      );

  public Optional<PickupStatus> save(PickupStatus entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<PickupStatus> insert(PickupStatus entity) {
    String sql = "INSERT INTO pickup_status (type) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getType());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById((long) Objects.requireNonNull(key).intValue());
  }

  private Optional<PickupStatus> update(PickupStatus entity) {
    String sql = "UPDATE pickup_status SET type = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getType(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<PickupStatus> saveAll(Iterable<PickupStatus> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<PickupStatus> findById(Long id) {
    String sql = "SELECT * FROM pickup_status WHERE id = ?";
    List<PickupStatus> result = jdbcTemplate.query(sql, pickupStatusRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM pickup_status WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<PickupStatus> findAll() {
    String sql = "SELECT * FROM pickup_status";
    return jdbcTemplate.query(sql, pickupStatusRowMapper);
  }

  public Iterable<PickupStatus> findAll(Iterable<PickupStatus> entities) {
    List<PickupStatus> resultList = new ArrayList<>();
    for (PickupStatus entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM pickup_status";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM pickup_status WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(PickupStatus entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<PickupStatus> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM pickup_status";
    jdbcTemplate.update(sql);
  }
}