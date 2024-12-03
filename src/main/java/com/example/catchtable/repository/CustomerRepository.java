package com.example.catchtable.repository;

import com.example.catchtable.domain.Customer;
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
public class CustomerRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  CustomerRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Customer> customerRowMapper = (rs, rowNum) ->
      Customer.fromEntity(
          rs.getLong("id"), // int unsigned -> Long
          rs.getString("name"),
          rs.getString("phone_number")
      );

  public Optional<Customer> save(Customer entity) {
    if (entity.getId() == null) { // id가 null이면 insert, 아니면 update
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<Customer> insert(Customer entity) {
    String sql = "INSERT INTO customer (name, phone_number) " +
        "VALUES (?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getName());
      ps.setString(2, entity.getPhoneNumber());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue()); // Long 타입으로 변경
  }

  private Optional<Customer> update(Customer entity) {
    String sql = "UPDATE customer SET name = ?, phone_number = ? " +
        "WHERE id = ?";
    jdbcTemplate.update(sql,
        entity.getName(),
        entity.getPhoneNumber(),
        entity.getId());
    return findById(entity.getId());
  }

  public Iterable<Customer> saveAll(Iterable<Customer> entities) {
    List<Customer> result = new ArrayList<>();
    for (Customer entity : entities) {
      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
    }
    return result;
  }

  public Optional<Customer> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM customer WHERE id = ?";
    List<Customer> result = jdbcTemplate.query(sql, customerRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM customer WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<Customer> findAll() {
    String sql = "SELECT * FROM customer";
    return jdbcTemplate.query(sql, customerRowMapper);
  }

  public Iterable<Customer> findAll(Iterable<Customer> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getId()));
    String sql = "SELECT * FROM customer WHERE id IN (?)";
    return jdbcTemplate.query(sql, customerRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM customer";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
    String sql = "DELETE FROM customer WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(Customer entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<? extends Customer> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM customer";
    jdbcTemplate.update(sql);
  }
}