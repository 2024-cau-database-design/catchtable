package com.example.catchtable.repository;

import com.example.catchtable.domain.Restaurant;
import com.example.catchtable.domain.RestaurantInfo;
import java.util.ArrayList;
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
          rs.getInt("id"),
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

  public Optional<Restaurant> save(Restaurant entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<Restaurant> insert(Restaurant entity) {
    String sql = "INSERT INTO restaurant (name, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getName());
      ps.setTimestamp(2, Timestamp.valueOf(entity.getCreatedAt()));
      ps.setTimestamp(3, Timestamp.valueOf(entity.getUpdatedAt()));
      ps.setBoolean(4, entity.getIsDeleted());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue()); // Return the created entity
  }

  private Optional<Restaurant> update(Restaurant entity) {
    String sql = "UPDATE restaurant SET name = ?, updated_at = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getName(), Timestamp.valueOf(entity.getUpdatedAt()), entity.getId());
    return findById(entity.getId()); // Return the updated entity
  }

  public Iterable<Restaurant> saveAll(Iterable<Restaurant> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities); // 존재하는 객체들만 반환
  }

  public Optional<Restaurant> findById(Integer id) {
    String sql = "SELECT * FROM restaurant WHERE id = ?";
    List<Restaurant> result = jdbcTemplate.query(sql, restaurantRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM restaurant WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0; // count가 0보다 크면 존재하는 것으로 간주
  }

  public Iterable<Restaurant> findAll() {
    String sql = "SELECT * FROM restaurant";
    return jdbcTemplate.query(sql, restaurantRowMapper);
  }

  public Iterable<Restaurant> findAll(Iterable<Restaurant> entities) {
    List<Restaurant> resultList = new ArrayList<>();
    for (Restaurant entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM restaurant WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(Restaurant entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<Restaurant> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant";
    jdbcTemplate.update(sql);
  }
}