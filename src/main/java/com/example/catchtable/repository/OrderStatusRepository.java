package com.example.catchtable.repository;

import com.example.catchtable.domain.OrderStatus;
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
public class OrderStatusRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  OrderStatusRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<OrderStatus> orderStatusRowMapper = (rs, rowNum) ->
      OrderStatus.fromEntity(
          rs.getLong("id"), // int unsigned -> Long
          rs.getString("type")
      );

  public Optional<OrderStatus> save(OrderStatus entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<OrderStatus> insert(OrderStatus entity) {
    String sql = "INSERT INTO order_status (type) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getType());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue()); // int -> Long
  }

  private Optional<OrderStatus> update(OrderStatus entity) {
    String sql = "UPDATE order_status SET type = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getType(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<OrderStatus> saveAll(Iterable<OrderStatus> entities) {
    List<OrderStatus> result = new ArrayList<>();
    for (OrderStatus entity : entities) {
      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
    }
    return result;
  }

  public Optional<OrderStatus> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM order_status WHERE id = ?";
    List<OrderStatus> result = jdbcTemplate.query(sql, orderStatusRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM order_status WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<OrderStatus> findAll() {
    String sql = "SELECT * FROM order_status";
    return jdbcTemplate.query(sql, orderStatusRowMapper);
  }

  public Iterable<OrderStatus> findAll(Iterable<OrderStatus> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getId()));
    String sql = "SELECT * FROM order_status WHERE id IN (?)";
    return jdbcTemplate.query(sql, orderStatusRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM order_status";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
    String sql = "DELETE FROM order_status WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(OrderStatus entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<OrderStatus> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM order_status";
    jdbcTemplate.update(sql);
  }
}