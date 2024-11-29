package com.example.catchtable.repository;

import com.example.catchtable.domain.Waiting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class WaitingRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  WaitingRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Waiting> waitingRowMapper = (rs, rowNum) ->
      Waiting.fromEntity(
          rs.getLong("id"),
          rs.getTimestamp("created_at"),
          rs.getLong("customer_id"),
          rs.getInt("guest_count"),
          rs.getLong("restaurant_id")
      );

  public Optional<Waiting> save(Waiting entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<Waiting> insert(Waiting entity) {
    String sql = "INSERT INTO waiting (customer_id, guest_count, restaurant_id) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getCustomerId());
      ps.setLong(2, entity.getGuestCount());
      ps.setLong(3, entity.getRestaurantId());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue());
  }

  private Optional<Waiting> update(Waiting entity) {
    String sql = "UPDATE waiting SET customer_id = ?, guest_count = ?, restaurant_id = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getCustomerId(), entity.getGuestCount(), entity.getRestaurantId(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<Waiting> saveAll(Iterable<Waiting> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<Waiting> findById(Long id) {
    String sql = "SELECT * FROM waiting WHERE id = ?";
    List<Waiting> result = jdbcTemplate.query(sql, waitingRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM waiting WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<Waiting> findAll() {
    String sql = "SELECT * FROM waiting";
    return jdbcTemplate.query(sql, waitingRowMapper);
  }

  public Iterable<Waiting> findAll(Iterable<Waiting> entities) {
    List<Waiting> resultList = new ArrayList<>();
    for (Waiting entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM waiting";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM waiting WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(Waiting entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<Waiting> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM waiting";
    jdbcTemplate.update(sql);
  }
}