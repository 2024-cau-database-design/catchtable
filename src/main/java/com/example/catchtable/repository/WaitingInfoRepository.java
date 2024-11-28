package com.example.catchtable.repository;

import com.example.catchtable.domain.WaitingInfo;
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
public class WaitingInfoRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  WaitingInfoRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<WaitingInfo> waitingInfoRowMapper = (rs, rowNum) ->
      WaitingInfo.fromEntity(
          rs.getInt("waiting_id"),
          rs.getInt("party_size"),
          rs.getInt("restaurant_id"),
          rs.getInt("customer_id")
      );

  public Optional<WaitingInfo> save(WaitingInfo entity) {
    if (entity.getWaitingId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<WaitingInfo> insert(WaitingInfo entity) {
    String sql = "INSERT INTO waiting_info (party_size, restaurant_id, customer_id) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, entity.getPartySize());
      ps.setInt(2, entity.getRestaurantId());
      ps.setInt(3, entity.getCustomerId());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findByWaitingId(Objects.requireNonNull(key).intValue());
  }

  private Optional<WaitingInfo> update(WaitingInfo entity) {
    String sql = "UPDATE waiting_info SET party_size = ?, restaurant_id = ?, customer_id = ? WHERE waiting_id = ?";
    jdbcTemplate.update(sql, entity.getPartySize(), entity.getRestaurantId(), entity.getCustomerId(), entity.getWaitingId());
    return findByWaitingId(entity.getWaitingId());
  }

  public Iterable<WaitingInfo> saveAll(Iterable<WaitingInfo> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<WaitingInfo> findByWaitingId(Integer waitingId) {
    String sql = "SELECT * FROM waiting_info WHERE waiting_id = ?";
    List<WaitingInfo> result = jdbcTemplate.query(sql, waitingInfoRowMapper, waitingId);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer waitingId) {
    String sql = "SELECT count(*) FROM waiting_info WHERE waiting_id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, waitingId);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<WaitingInfo> findAll() {
    String sql = "SELECT * FROM waiting_info";
    return jdbcTemplate.query(sql, waitingInfoRowMapper);
  }

  public Iterable<WaitingInfo> findAll(Iterable<WaitingInfo> entities) {
    List<WaitingInfo> resultList = new ArrayList<>();
    for (WaitingInfo entity : entities) {
      if (existsById(entity.getWaitingId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM waiting_info";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteByWaitingId(Integer waitingId) {
    String sql = "DELETE FROM waiting_info WHERE waiting_id = ?";
    jdbcTemplate.update(sql, waitingId);
  }

  public void delete(WaitingInfo entity) {
    deleteByWaitingId(entity.getWaitingId());
  }

  public void deleteAll(Iterable<WaitingInfo> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM waiting_info";
    jdbcTemplate.update(sql);
  }
}