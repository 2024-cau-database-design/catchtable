package com.example.catchtable.repository;

import com.example.catchtable.domain.RestaurantInfo;
import com.example.catchtable.domain.RestaurantLocation;
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
public class RestaurantLocationRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  RestaurantLocationRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<RestaurantLocation> restaurantLocationRowMapper = (rs, rowNum) ->
      RestaurantLocation.fromEntity(
          rs.getInt("restaurant_id"),
          rs.getBigDecimal("latitude"),
          rs.getBigDecimal("longitude"),
          rs.getTimestamp("created_at"), // No conversion to LocalDateTime
          rs.getTimestamp("updated_at"), // No conversion to LocalDateTime
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")  // No conversion to LocalDateTime, no null check
      );

  public Optional<RestaurantLocation> save(RestaurantLocation entity) {
    if (existsById(entity.getRestaurantId())) {
      return update(entity);
    } else {
      return insert(entity);
    }
  }

  private Optional<RestaurantLocation> insert(RestaurantLocation entity) {
    String sql = "INSERT INTO restaurant_location (restaurant_id, latitude, longitude) " +
        "VALUES (?, ?, ?)"; // 쿼리 수정

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, entity.getRestaurantId());
      ps.setBigDecimal(2, entity.getLatitude());
      ps.setBigDecimal(3, entity.getLongitude());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue()); // Return the created entity
  }

  private Optional<RestaurantLocation> update(RestaurantLocation entity) {
    String sql = "UPDATE restaurant_location SET latitude = ?, longitude = ?, updated_at = ? " +
        "WHERE restaurant_id = ?";
    jdbcTemplate.update(sql,
        entity.getLatitude(),
        entity.getLongitude(),
        Timestamp.valueOf(entity.getUpdatedAt()),
        entity.getRestaurantId());
    return findById(entity.getRestaurantId()); // Return the updated entity
  }

  public Iterable<RestaurantLocation> saveAll(Iterable<RestaurantLocation> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities); // 존재하는 객체들만 반환
  }

  public Optional<RestaurantLocation> findById(Integer id) {
    String sql = "SELECT * FROM restaurant_location WHERE restaurant_id = ?";
    List<RestaurantLocation> result = jdbcTemplate.query(sql, restaurantLocationRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM restaurant_location WHERE restaurant_id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0; // count가 0보다 크면 존재하는 것으로 간주
  }

  public Iterable<RestaurantLocation> findAll() {
    String sql = "SELECT * FROM restaurant_location";
    return jdbcTemplate.query(sql, restaurantLocationRowMapper);
  }

  public Iterable<RestaurantLocation> findAll(Iterable<RestaurantLocation> entities) {
    List<RestaurantLocation> resultList = new ArrayList<>();
    for (RestaurantLocation entity : entities) {
      if (existsById(entity.getRestaurantId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_location";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM restaurant_location WHERE restaurant_id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(RestaurantLocation entity) {
    deleteById(entity.getRestaurantId());
  }

  public void deleteAll(Iterable<RestaurantLocation> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant_location";
    jdbcTemplate.update(sql);
  }
}