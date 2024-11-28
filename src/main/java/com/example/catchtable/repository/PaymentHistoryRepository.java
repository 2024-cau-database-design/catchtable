package com.example.catchtable.repository;

import com.example.catchtable.domain.PaymentHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class PaymentHistoryRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  PaymentHistoryRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<PaymentHistory> paymentHistoryRowMapper = (rs, rowNum) ->
      PaymentHistory.fromEntity(
          rs.getInt("id"),
          rs.getInt("method"),
          rs.getInt("amount"),
          rs.getInt("status"),
          rs.getDate("transaction_date"),
          rs.getInt("payment_id")
      );

  public Optional<PaymentHistory> save(PaymentHistory entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<PaymentHistory> insert(PaymentHistory entity) {
    String sql = "INSERT INTO payment_history (method, amount, status, transaction_date, payment_id) VALUES (?, ?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, entity.getMethod());
      ps.setInt(2, entity.getAmount());
      ps.setInt(3, entity.getStatus());
      ps.setDate(4, entity.getTransactionDate());
      ps.setInt(5, entity.getPaymentId());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue());
  }

  private Optional<PaymentHistory> update(PaymentHistory entity) {
    String sql = "UPDATE payment_history SET method = ?, amount = ?, status = ?, transaction_date = ?, payment_id = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getMethod(), entity.getAmount(), entity.getStatus(),
        entity.getTransactionDate(), entity.getPaymentId(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<PaymentHistory> saveAll(Iterable<PaymentHistory> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<PaymentHistory> findById(Integer id) {
    String sql = "SELECT * FROM payment_history WHERE id = ?";
    List<PaymentHistory> result = jdbcTemplate.query(sql, paymentHistoryRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM payment_history WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<PaymentHistory> findAll() {
    String sql = "SELECT * FROM payment_history";
    return jdbcTemplate.query(sql, paymentHistoryRowMapper);
  }

  public Iterable<PaymentHistory> findAll(Iterable<PaymentHistory> entities) {
    List<PaymentHistory> resultList = new ArrayList<>();
    for (PaymentHistory entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM payment_history";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM payment_history WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(PaymentHistory entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<PaymentHistory> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM payment_history";
    jdbcTemplate.update(sql);
  }
}