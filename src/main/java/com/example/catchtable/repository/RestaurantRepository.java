package com.example.catchtable.repository;

import com.example.catchtable.domain.Restaurant;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
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
          rs.getLong("id"), // int unsigned -> Long
          rs.getString("name"),
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at"),
          rs.getLong("owner_id") // owner_id 필드 추가, int unsigned -> Long
      );


  // 복합 조건 검색
  public List<Restaurant> searchByConditions(String keyword, Double latitude, Double longitude, String category, String sort) {
    StringBuilder sql = new StringBuilder("SELECT * FROM restaurant WHERE is_deleted = false");
    List<Object> params = new ArrayList<>();

    // 키워드 검색 (이름만 포함)
    if (keyword != null && !keyword.isBlank()) {
      sql.append(" AND name LIKE ?");
      params.add("%" + keyword + "%");
    }

    // 위치 검색 (좌표 기반)
    if (latitude != null && longitude != null) {
      sql.append(" AND ST_Distance_Sphere(point(longitude, latitude), point(?, ?)) <= ?");
      params.add(longitude);
      params.add(latitude);
      params.add(5000); // 5km 반경
    }

    // 카테고리 검색
    if (category != null && !category.isBlank()) {
      sql.append(" AND category = ?");
      params.add(category);
    }

    // 정렬
    sql.append(" ORDER BY ").append(switch (sort) {
      case "name" -> "name ASC";
      case "rating" -> "rating DESC";
      case "distance" -> "ST_Distance_Sphere(point(longitude, latitude), point(?, ?)) ASC";
      default -> "created_at DESC";
    });

    // 거리 정렬의 경우 좌표 추가
    if ("distance".equals(sort)) {
      params.add(longitude);
      params.add(latitude);
    }

    return jdbcTemplate.query(sql.toString(), params.toArray(), restaurantRowMapper);
  }

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
    String sql = "INSERT INTO restaurant (name, owner_id) VALUES (?, ?)"; // owner_id 추가
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getName());
      ps.setLong(2, entity.getOwnerId()); // owner_id 추가
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue()); // int -> Long
  }

  private Optional<Restaurant> update(Restaurant entity) {
    String sql = "UPDATE restaurant SET name = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getName(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<Restaurant> saveAll(Iterable<Restaurant> entities) {
    List<Restaurant> result = new ArrayList<>();
    for (Restaurant entity : entities) {
      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
    }
    return result;
  }

  public Optional<Restaurant> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM restaurant WHERE id = ?";
    List<Restaurant> result = jdbcTemplate.query(sql, restaurantRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM restaurant WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<Restaurant> findAll() {
    String sql = "SELECT * FROM restaurant";
    return jdbcTemplate.query(sql, restaurantRowMapper);
  }

  public Iterable<Restaurant> findAll(Iterable<Restaurant> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getId()));
    String sql = "SELECT * FROM restaurant WHERE id IN (?)";
    return jdbcTemplate.query(sql, restaurantRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
    String sql = "DELETE FROM restaurant WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(Restaurant entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<? extends Restaurant> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant";
    jdbcTemplate.update(sql);
  }
}