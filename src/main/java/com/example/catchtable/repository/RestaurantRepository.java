package com.example.catchtable.repository;

import com.example.catchtable.domain.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RestaurantRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  RestaurantRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Restaurant> restaurantRowMapper = (rs, rowNum) ->
      Restaurant.fromEntity(
          rs.getLong("id"),
          rs.getString("name"),
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")
      );


  public List<Restaurant> findByName(String name) {
    String sql = "SELECT * FROM restaurant WHERE name = ?";
    return jdbcTemplate.query(sql, restaurantRowMapper, name);
  }

  public Restaurant save(Restaurant entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Restaurant insert(Restaurant entity) {
    String sql = "INSERT INTO restaurant (name, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getName());
      ps.setTimestamp(2, Timestamp.valueOf(entity.getCreatedAt()));
      ps.setTimestamp(3, Timestamp.valueOf(entity.getUpdatedAt()));
      ps.setBoolean(4, entity.isDeleted());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    entity.setId(Objects.requireNonNull(key).longValue());
    return entity;
  }

  private Restaurant update(Restaurant entity) {
    String sql = "UPDATE restaurant SET name = ?, updated_at = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getName(), Timestamp.valueOf(entity.getUpdatedAt()), entity.getId());
    return entity; // Or return findById(entity.getId()).orElseThrow(); to get the updated entity from DB
  }

  public Iterable<Restaurant> saveAll(Iterable<Restaurant> entities) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public Optional<Restaurant> findById(Long id) {
    String sql = "SELECT * FROM restaurant WHERE id = ?";
    List<Restaurant> result = jdbcTemplate.query(sql, restaurantRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM restaurant WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
  }

  public Iterable<Restaurant> findAll() {
    String sql = "SELECT * FROM restaurant";
    return jdbcTemplate.query(sql, restaurantRowMapper);
  }

  public Iterable<Restaurant> findAllById(Iterable<Long> longs) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant";
    return jdbcTemplate.queryForObject(sql, Long.class);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM restaurant WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(Restaurant entity) {
    deleteById(entity.getId());
  }

  public void deleteAllById(Iterable<? extends Long> longs) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public void deleteAll(Iterable<? extends Restaurant> entities) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant";
    jdbcTemplate.update(sql);
  }
}