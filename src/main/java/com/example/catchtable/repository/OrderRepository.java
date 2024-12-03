package com.example.catchtable.repository;

import com.example.catchtable.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class OrderRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  OrderRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Order> orderRowMapper = (rs, rowNum) ->
      Order.fromEntity(
          rs.getLong("id"), // id 필드 추가
          rs.getInt("restaurant_id"),
          rs.getInt("customer_id"),
          rs.getInt("booking_id"),
          rs.getInt("status_id"),
          rs.getInt("total_price"),
          rs.getInt("reservation_fee"),
          rs.getTimestamp("created_at")
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
      ps.setInt(1, entity.getRestaurantId());
      ps.setInt(2, entity.getCustomerId());
      ps.setInt(3, entity.getBookingId());
      ps.setInt(4, entity.getStatusId());
      ps.setInt(5, entity.getTotalPrice());
      ps.setInt(6, entity.getReservationFee());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById((long) Objects.requireNonNull(key).intValue());
  }

  private Optional<Order> update(Order entity) {
    String sql = "UPDATE `order` SET restaurant_id = ?, customer_id = ?, booking_id = ?, status_id = ?, " +
        "total_price = ?, reservation_fee = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getRestaurantId(), entity.getCustomerId(), entity.getBookingId(),
        entity.getStatusId(), entity.getTotalPrice(), entity.getReservationFee(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<Order> saveAll(Iterable<Order> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<Order> findById(Long id) {
    String sql = "SELECT * FROM `order` WHERE id = ?";
    List<Order> result = jdbcTemplate.query(sql, orderRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM `order` WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<Order> findAll() {
    String sql = "SELECT * FROM `order`";
    return jdbcTemplate.query(sql, orderRowMapper);
  }

  public Iterable<Order> findAll(Iterable<Order> entities) {
    List<Order> resultList = new ArrayList<>();
    for (Order entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM `order`";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) {
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

  public Map<String, Object> createOrderAndItems(
          Long bookingId,
          Long restaurantId,
          Long customerId,
          Integer reservationFee,
          String menuJson
  ) {
    System.out.println("Calling createOrderAndItems procedure...");
    System.out.println("bookingId: " + bookingId);
    System.out.println("restaurantId: " + restaurantId);
    System.out.println("customerId: " + customerId);
    System.out.println("reservationFee: " + reservationFee);
    System.out.println("menuJson: " + menuJson);

    return jdbcTemplate.execute((PreparedStatementCreator) connection -> {
      CallableStatement callableStatement = connection.prepareCall(
              "{CALL create_order_and_items(?, ?, ?, ?, ?)}"
      );
      // Set input parameters
      callableStatement.setLong(1, bookingId);
      callableStatement.setLong(2, restaurantId);
      callableStatement.setLong(3, customerId);
      callableStatement.setInt(4, reservationFee);
      callableStatement.setString(5, menuJson);
      return callableStatement;
    }, callableStatement -> {
      // Execute the procedure
      callableStatement.execute();

      Map<String, Object> result = new HashMap<>();
      ResultSet rs = callableStatement.getResultSet();
      while (rs.next()) {
        result.put("order_id", rs.getLong("order_id"));
        result.put("total_price", rs.getInt("total_price"));
      }
      rs.close();

      System.out.println("createOrderAndItems Result: " + result);
      return result;
    });
  }
}