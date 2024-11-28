package com.example.catchtable.repository;

import com.example.catchtable.domain.RestaurantLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
          rs.getLong("restaurant_id"),
          rs.getBigDecimal("latitude"),
          rs.getBigDecimal("longitude"),
          rs.getTimestamp("created_at"), // No conversion to LocalDateTime
          rs.getTimestamp("updated_at"), // No conversion to LocalDateTime
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")  // No conversion to LocalDateTime, no null check
      );

  public RestaurantLocation save(RestaurantLocation entity) {
    if (existsById(entity.getRestaurantId())) {
      return update(entity);
    } else {
      return insert(entity);
    }
  }

  private RestaurantLocation insert(RestaurantLocation entity) {
    String sql = "INSERT INTO restaurant_location (restaurant_id, latitude, longitude, created_at, updated_at, is_deleted) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql,
        entity.getRestaurantId(),
        entity.getLatitude(),
        entity.getLongitude(),
        Timestamp.valueOf(entity.getCreatedAt()),
        Timestamp.valueOf(entity.getUpdatedAt()),
        entity.isDeleted());
    return entity;
  }

  private RestaurantLocation update(RestaurantLocation entity) {
    String sql = "UPDATE restaurant_location SET latitude = ?, longitude = ?, updated_at = ? " +
        "WHERE restaurant_id = ?";
    jdbcTemplate.update(sql,
        entity.getLatitude(),
        entity.getLongitude(),
        Timestamp.valueOf(entity.getUpdatedAt()),
        entity.getRestaurantId());
    return entity; // Or return findById(entity.getRestaurantId()).orElseThrow(); to get updated entity
  }


  public <S extends RestaurantLocation> Iterable<S> saveAll(Iterable<S> entities) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public Optional<RestaurantLocation> findById(Long id) {
    String sql = "SELECT * FROM restaurant_location WHERE restaurant_id = ?";
    List<RestaurantLocation> result = jdbcTemplate.query(sql, restaurantLocationRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM restaurant_location WHERE restaurant_id = ?";
    return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
  }

  public Iterable<RestaurantLocation> findAll() {
    String sql = "SELECT * FROM restaurant_location";
    return jdbcTemplate.query(sql, restaurantLocationRowMapper);
  }

  public Iterable<RestaurantLocation> findAllById(Iterable<Long> longs) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_location";
    return jdbcTemplate.queryForObject(sql, Long.class);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM restaurant_location WHERE restaurant_id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(RestaurantLocation entity) {
    deleteById(entity.getRestaurantId());
  }

  public void deleteAllById(Iterable<? extends Long> longs) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public void deleteAll(Iterable<? extends RestaurantLocation> entities) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant_location";
    jdbcTemplate.update(sql);
  }
}