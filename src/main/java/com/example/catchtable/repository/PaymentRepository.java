package com.example.catchtable.repository;

import com.example.catchtable.domain.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class PaymentRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  PaymentRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Payment> paymentRowMapper = (rs, rowNum) ->
      Payment.fromEntity(
          rs.getLong("id"),
          rs.getInt("amount"),
          rs.getInt("order_id"),
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getString("method")
      );

  public Optional<Payment> save(Payment entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<Payment> insert(Payment entity) {
    String sql = "INSERT INTO payment (amount, order_id, method) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, entity.getAmount());
      ps.setInt(2, entity.getOrderId());
      ps.setString(3, entity.getMethod());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById((long) Objects.requireNonNull(key).intValue());
  }

  private Optional<Payment> update(Payment entity) {
    String sql = "UPDATE payment SET amount = ?, order_id = ?, method = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getAmount(), entity.getOrderId(), entity.getMethod(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<Payment> saveAll(Iterable<Payment> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<Payment> findById(Long id) {
    String sql = "SELECT * FROM payment WHERE id = ?";
    List<Payment> result = jdbcTemplate.query(sql, paymentRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM payment WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<Payment> findAll() {
    String sql = "SELECT * FROM payment";
    return jdbcTemplate.query(sql, paymentRowMapper);
  }

  public Iterable<Payment> findAll(Iterable<Payment> entities) {
    List<Payment> resultList = new ArrayList<>();
    for (Payment entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM payment";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM payment WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(Payment entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<Payment> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM payment";
    jdbcTemplate.update(sql);
  }

  public Map<String, Object> createPaymentAndHistory(Long orderId, Integer paymentAmount, String paymentMethod, LocalDateTime transactionAt) {
    String sql = "{CALL create_payment_and_history(?, ?, ?, ?)}";

    return jdbcTemplate.execute((ConnectionCallback<Map<String, Object>>) connection -> {
      try (CallableStatement callableStatement = connection.prepareCall(sql)) {
        callableStatement.setLong(1, orderId);
        callableStatement.setInt(2, paymentAmount);
        callableStatement.setString(3, paymentMethod);
        callableStatement.setTimestamp(4, Timestamp.valueOf(transactionAt));

        boolean hasResults = callableStatement.execute();

        Map<String, Object> result = new HashMap<>();
        if (hasResults) {
          try (ResultSet rs = callableStatement.getResultSet()) {
            if (rs.next()) {
              result.put("payment_id", rs.getLong("payment_id"));
              result.put("payment_history_id", rs.getLong("payment_history_id"));
            }
          }
        }

        return result;
      } catch (SQLException e) {
        //에러 로그 찍기
        e.printStackTrace();
        throw new RuntimeException("Error executing create_payment_and_history procedure", e);
      }
    });
  }
}