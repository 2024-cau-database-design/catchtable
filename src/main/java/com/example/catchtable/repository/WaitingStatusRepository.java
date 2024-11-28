package com.example.catchtable.repository;

import com.example.catchtable.domain.WaitingStatus;
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
public class WaitingStatusRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  WaitingStatusRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<WaitingStatus> waitingStatusRowMapper = (rs, rowNum) ->
      WaitingStatus.fromEntity(
          rs.getInt("id"),
          rs.getString("type")
      );

  public Optional<WaitingStatus> save(WaitingStatus entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<WaitingStatus> insert(WaitingStatus entity) {
    String sql = "INSERT INTO waiting_status (type) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getType());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue());
  }

  private Optional<WaitingStatus> update(WaitingStatus entity) {
    String sql = "UPDATE waiting_status SET type = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getType(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<WaitingStatus> saveAll(Iterable<WaitingStatus> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<WaitingStatus> findById(Integer id) {
    String sql = "SELECT * FROM waiting_status WHERE id = ?";
    List<WaitingStatus> result = jdbcTemplate.query(sql, waitingStatusRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM waiting_status WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<WaitingStatus> findAll() {
    String sql = "SELECT * FROM waiting_status";
    return jdbcTemplate.query(sql, waitingStatusRowMapper);
  }

  public Iterable<WaitingStatus> findAll(Iterable<WaitingStatus> entities) {
    List<WaitingStatus> resultList = new ArrayList<>();
    for (WaitingStatus entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM waiting_status";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM waiting_status WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(WaitingStatus entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<WaitingStatus> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM waiting_status";
    jdbcTemplate.update(sql);
  }
}