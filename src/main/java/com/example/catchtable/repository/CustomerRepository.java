package com.example.catchtable.repository;

import com.example.catchtable.domain.Customer;
import com.example.catchtable.domain.RestaurantInfo;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
          rs.getInt("id"),
          rs.getString("name"),
          rs.getString("phone_number"),
          rs.getTimestamp("created_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")
      );

  public Optional<Customer> save(Customer entity) {
    if (existsById(entity.getId())) {
      return update(entity);
    } else {
      return insert(entity);
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
    return findById(Objects.requireNonNull(key).intValue()); // Return the updated entity
  }

  private Optional<Customer> update(Customer entity) {
    String sql = "UPDATE customer SET name = ?, phone_number = ? " +
        "WHERE id = ?";
    jdbcTemplate.update(sql,
        entity.getName(),
        entity.getPhoneNumber(),
        entity.getId());
    return findById(entity.getId()); // Return the updated entity
  }

  public Iterable<Customer> saveAll(Iterable<Customer> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities); // 존재하는 객체들만 반환
  }

  public Optional<Customer> findById(Integer id) {
    String sql = "SELECT * FROM customer WHERE id = ?";
    List<Customer> result = jdbcTemplate.query(sql, customerRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM customer WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0; // count가 0보다 크면 존재하는 것으로 간주
  }

  public Iterable<Customer> findAll() {
    String sql = "SELECT * FROM customer";
    return jdbcTemplate.query(sql, customerRowMapper);
  }

  public Iterable<Customer> findAll(Iterable<Customer> entities) {
    List<Customer> resultList = new ArrayList<>();
    for (Customer entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM customer";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
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