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
          rs.getInt("id"),
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
    return findById(Objects.requireNonNull(key).intValue());
  }

  private Optional<OrderStatus> update(OrderStatus entity) {
    String sql = "UPDATE order_status SET type = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getType(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<OrderStatus> saveAll(Iterable<OrderStatus> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<OrderStatus> findById(Integer id) {
    String sql = "SELECT * FROM order_status WHERE id = ?";
    List<OrderStatus> result = jdbcTemplate.query(sql, orderStatusRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM order_status WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<OrderStatus> findAll() {
    String sql = "SELECT * FROM order_status";
    return jdbcTemplate.query(sql, orderStatusRowMapper);
  }

  public Iterable<OrderStatus> findAll(Iterable<OrderStatus> entities) {
    List<OrderStatus> resultList = new ArrayList<>();
    for (OrderStatus entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM order_status";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
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