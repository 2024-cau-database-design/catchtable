package com.example.catchtable.repository;

import com.example.catchtable.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class OrderRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  OrderRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Order> orderRowMapper = (rs, rowNum) ->
      Order.fromEntity(
          rs.getLong("id"), // int unsigned -> Long
          rs.getLong("status_id"), // int unsigned -> Long
          rs.getTimestamp("created_at"), // Timestamp -> LocalDateTime
          rs.getLong("total_price"), // int unsigned -> Long
          rs.getLong("restaurant_id"), // int unsigned -> Long
          rs.getLong("customer_id"), // int unsigned -> Long
          rs.getLong("reservation_fee"), // int unsigned -> Long
          rs.getLong("booking_id") // int unsigned -> Long
      );

  public Optional<Order> save(Order entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<Order> insert(Order entity) {
    String sql = "INSERT INTO `order` (restaurant_id, customer_id, booking_id, status_id, total_price, reservation_fee) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getRestaurantId()); // int -> Long
      ps.setLong(2, entity.getCustomerId()); // int -> Long
      ps.setLong(3, entity.getBookingId()); // int -> Long
      ps.setLong(4, entity.getStatusId()); // int -> Long
      ps.setLong(5, entity.getTotalPrice()); // int -> Long
      ps.setLong(6, entity.getReservationFee()); // int -> Long
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue()); // int -> Long
  }

  private Optional<Order> update(Order entity) {
    String sql = "UPDATE `order` SET restaurant_id = ?, customer_id = ?, booking_id = ?, status_id = ?, " +
        "total_price = ?, reservation_fee = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getRestaurantId(), entity.getCustomerId(), entity.getBookingId(),
        entity.getStatusId(), entity.getTotalPrice(), entity.getReservationFee(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<Order> saveAll(Iterable<Order> entities) {
    List<Order> result = new ArrayList<>();
    for (Order entity : entities) {
      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
    }
    return result;
  }

  public Optional<Order> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM `order` WHERE id = ?";
    List<Order> result = jdbcTemplate.query(sql, orderRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM `order` WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<Order> findAll() {
    String sql = "SELECT * FROM `order`";
    return jdbcTemplate.query(sql, orderRowMapper);
  }

  public Iterable<Order> findAll(Iterable<Order> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getId()));
    String sql = "SELECT * FROM `order` WHERE id IN (?)";
    return jdbcTemplate.query(sql, orderRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM `order`";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
    String sql = "DELETE FROM `order` WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(Order entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<Order> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM `order`";
    jdbcTemplate.update(sql);
  }
}