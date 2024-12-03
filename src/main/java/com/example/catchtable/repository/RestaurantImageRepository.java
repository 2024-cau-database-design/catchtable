package com.example.catchtable.repository;

import com.example.catchtable.domain.RestaurantImage;
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
public class RestaurantImageRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  RestaurantImageRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<RestaurantImage> restaurantImageRowMapper = (rs, rowNum) ->
      RestaurantImage.fromEntity(
          rs.getLong("id"), // int unsigned -> Long
          rs.getLong("restaurant_id"), // int unsigned -> Long
          rs.getString("name"),
          rs.getString("url"),
          rs.getString("description")
      );

  public Optional<RestaurantImage> save(RestaurantImage entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<RestaurantImage> insert(RestaurantImage entity) {
    String sql = "INSERT INTO restaurant_image (restaurant_id, name, url, description) VALUES (?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getRestaurantId()); // int -> Long
      ps.setString(2, entity.getName());
      ps.setString(3, entity.getUrl());
      ps.setString(4, entity.getDescription());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue()); // int -> Long
  }

  private Optional<RestaurantImage> update(RestaurantImage entity) {
    String sql = "UPDATE restaurant_image SET restaurant_id = ?, name = ?, url = ?, description = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getRestaurantId(), entity.getName(), entity.getUrl(),
        entity.getDescription(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<RestaurantImage> saveAll(Iterable<RestaurantImage> entities) {
    List<RestaurantImage> result = new ArrayList<>();
    for (RestaurantImage entity : entities) {
      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
    }
    return result;
  }

  public Optional<RestaurantImage> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM restaurant_image WHERE id = ?";
    List<RestaurantImage> result = jdbcTemplate.query(sql, restaurantImageRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM restaurant_image WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<RestaurantImage> findAll() {
    String sql = "SELECT * FROM restaurant_image";
    return jdbcTemplate.query(sql, restaurantImageRowMapper);
  }

  public Iterable<RestaurantImage> findAll(Iterable<RestaurantImage> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getId()));
    String sql = "SELECT * FROM restaurant_image WHERE id IN (?)";
    return jdbcTemplate.query(sql, restaurantImageRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_image";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
    String sql = "DELETE FROM restaurant_image WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(RestaurantImage entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<RestaurantImage> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant_image";
    jdbcTemplate.update(sql);
  }
}