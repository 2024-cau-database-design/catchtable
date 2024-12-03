package com.example.catchtable.repository;

import com.example.catchtable.domain.WaitingHistory;
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
public class WaitingHistoryRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  WaitingHistoryRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<WaitingHistory> waitingHistoryRowMapper = (rs, rowNum) ->
      WaitingHistory.fromEntity(
          rs.getLong("id"),
          rs.getLong("waiting_id"),
          rs.getTimestamp("created_at"),
          rs.getLong("waiting_status_id")
      );

  public Optional<WaitingHistory> save(WaitingHistory entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<WaitingHistory> insert(WaitingHistory entity) {
    String sql = "INSERT INTO waiting_history (waiting_id, waiting_status_id) VALUES (?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getWaitingId());
      ps.setLong(2, entity.getWaitingStatusId());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue());
  }

  private Optional<WaitingHistory> update(WaitingHistory entity) {
    String sql = "UPDATE waiting_history SET waiting_id = ?, waiting_status_id = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getWaitingId(), entity.getWaitingStatusId(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<WaitingHistory> saveAll(Iterable<WaitingHistory> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<WaitingHistory> findById(Long id) {
    String sql = "SELECT * FROM waiting_history WHERE id = ?";
    List<WaitingHistory> result = jdbcTemplate.query(sql, waitingHistoryRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM waiting_history WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<WaitingHistory> findAll() {
    String sql = "SELECT * FROM waiting_history";
    return jdbcTemplate.query(sql, waitingHistoryRowMapper);
  }

  public Iterable<WaitingHistory> findAll(Iterable<WaitingHistory> entities) {
    List<WaitingHistory> resultList = new ArrayList<>();
    for (WaitingHistory entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM waiting_history";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM waiting_history WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(WaitingHistory entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<WaitingHistory> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM waiting_history";
    jdbcTemplate.update(sql);
  }
}