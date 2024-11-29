package com.example.catchtable.repository;

import com.example.catchtable.domain.OrderItem;
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
public class OrderItemRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  OrderItemRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<OrderItem> orderItemRowMapper = (rs, rowNum) ->
      OrderItem.fromEntity(
          rs.getLong("id"), // int unsigned -> Long
          rs.getLong("quantity"), // int unsigned -> Long
          rs.getLong("price"), // int unsigned -> Long
          rs.getLong("order_id"), // int unsigned -> Long
          rs.getLong("menu_id") // int unsigned -> Long
      );

  public Optional<OrderItem> save(OrderItem entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<OrderItem> insert(OrderItem entity) {
    String sql = "INSERT INTO order_item (order_id, menu_id, quantity, price) VALUES (?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getOrderId()); // int -> Long
      ps.setLong(2, entity.getMenuId()); // int -> Long
      ps.setLong(3, entity.getQuantity()); // int -> Long
      ps.setLong(4, entity.getPrice()); // int -> Long
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue()); // int -> Long
  }

  private Optional<OrderItem> update(OrderItem entity) {
    String sql = "UPDATE order_item SET order_id = ?, menu_id = ?, quantity = ?, price = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getOrderId(), entity.getMenuId(), entity.getQuantity(),
        entity.getPrice(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<OrderItem> saveAll(Iterable<OrderItem> entities) {
    List<OrderItem> result = new ArrayList<>();
    for (OrderItem entity : entities) {
      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
    }
    return result;
  }

  public Optional<OrderItem> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM order_item WHERE id = ?";
    List<OrderItem> result = jdbcTemplate.query(sql, orderItemRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM order_item WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<OrderItem> findAll() {
    String sql = "SELECT * FROM order_item";
    return jdbcTemplate.query(sql, orderItemRowMapper);
  }

  public Iterable<OrderItem> findAll(Iterable<OrderItem> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getId()));
    String sql = "SELECT * FROM order_item WHERE id IN (?)";
    return jdbcTemplate.query(sql, orderItemRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM order_item";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
    String sql = "DELETE FROM order_item WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(OrderItem entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<OrderItem> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM order_item";
    jdbcTemplate.update(sql);
  }
}