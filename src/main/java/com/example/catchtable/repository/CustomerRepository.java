package com.example.catchtable.repository;

import com.example.catchtable.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  CustomerRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Customer> customerRowMapper = (rs, rowNum) ->
      Customer.fromEntity(
          rs.getLong("id"),
          rs.getString("name"),
          rs.getString("phone_number"),
          rs.getTimestamp("created_at"), // No conversion to LocalDateTime
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")  // No conversion to LocalDateTime, no null check
      );

  public Customer save(Customer entity) {
    if (existsById(entity.getId())) {
      return update(entity);
    } else {
      return insert(entity);
    }
  }

  private Customer insert(Customer entity) {
    String sql = "INSERT INTO customer (id, name, phone_number, created_at, is_deleted) " +
        "VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql,
        entity.getId(),
        entity.getName(),
        entity.getPhoneNumber(),
        Timestamp.valueOf(entity.getCreatedAt()),
        entity.isDeleted());
    return entity;
  }

  private Customer update(Customer entity) {
    String sql = "UPDATE customer SET name = ?, phone_number = ? " +
        "WHERE id = ?";
    jdbcTemplate.update(sql,
        entity.getName(),
        entity.getPhoneNumber(),
        entity.getId());
    return entity; // Or return findById(entity.getId()).orElseThrow(); to get updated entity
  }

  public <S extends Customer> Iterable<S> saveAll(Iterable<S> entities) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public Optional<Customer> findById(Long id) {
    String sql = "SELECT * FROM customer WHERE id = ?";
    List<Customer> result = jdbcTemplate.query(sql, customerRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM customer WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
  }

  public Iterable<Customer> findAll() {
    String sql = "SELECT * FROM customer";
    return jdbcTemplate.query(sql, customerRowMapper);
  }

  public Iterable<Customer> findAllById(Iterable<Long> longs) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public long count() {
    String sql = "SELECT count(*) FROM customer";
    return jdbcTemplate.queryForObject(sql, Long.class);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM customer WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(Customer entity) {
    deleteById(entity.getId());
  }

  public void deleteAllById(Iterable<? extends Long> longs) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public void deleteAll(Iterable<? extends Customer> entities) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public void deleteAll() {
    String sql = "DELETE FROM customer";
    jdbcTemplate.update(sql);
  }
}