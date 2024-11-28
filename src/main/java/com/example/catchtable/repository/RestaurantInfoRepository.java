package com.example.catchtable.repository;

import com.example.catchtable.domain.RestaurantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class RestaurantInfoRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  RestaurantInfoRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<RestaurantInfo> restaurantInfoRowMapper = (rs, rowNum) ->
      RestaurantInfo.fromEntity(
          rs.getLong("restaurant_id"),
          rs.getString("phone_number"),
          rs.getString("full_address"),
          rs.getString("website_url"),
          rs.getString("description"),
          rs.getTimestamp("created_at"), // No conversion to LocalDateTime
          rs.getTimestamp("updated_at"), // No conversion to LocalDateTime
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")  // No conversion to LocalDateTime, no null check
      );

  public RestaurantInfo save(RestaurantInfo entity) {
    if (existsById(entity.getRestaurantId())) {
      return update(entity);
    } else {
      return insert(entity);
    }
  }

  private RestaurantInfo insert(RestaurantInfo entity) {
    String sql = "INSERT INTO restaurant_info (restaurant_id, phone_number, full_address, website_url, description, created_at, updated_at, is_deleted) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql,
        entity.getRestaurantId(),
        entity.getPhoneNumber(),
        entity.getFullAddress(),
        entity.getWebsiteUrl(),
        entity.getDescription(),
        Timestamp.valueOf(entity.getCreatedAt()),
        Timestamp.valueOf(entity.getUpdatedAt()),
        entity.isDeleted());
    return entity;
  }

  private RestaurantInfo update(RestaurantInfo entity) {
    String sql = "UPDATE restaurant_info SET phone_number = ?, full_address = ?, website_url = ?, description = ?, updated_at = ? " +
        "WHERE restaurant_id = ?";
    jdbcTemplate.update(sql,
        entity.getPhoneNumber(),
        entity.getFullAddress(),
        entity.getWebsiteUrl(),
        entity.getDescription(),
        Timestamp.valueOf(entity.getUpdatedAt()),
        entity.getRestaurantId());
    return entity; // Or return findById(entity.getRestaurantId()).orElseThrow(); to get updated entity
  }


  public <S extends RestaurantInfo> Iterable<S> saveAll(Iterable<S> entities) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public Optional<RestaurantInfo> findById(Long id) {
    String sql = "SELECT * FROM restaurant_info WHERE restaurant_id = ?";
    List<RestaurantInfo> result = jdbcTemplate.query(sql, restaurantInfoRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM restaurant_info WHERE restaurant_id = ?";
    return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
  }

  public Iterable<RestaurantInfo> findAll() {
    String sql = "SELECT * FROM restaurant_info";
    return jdbcTemplate.query(sql, restaurantInfoRowMapper);
  }

  public Iterable<RestaurantInfo> findAllById(Iterable<Long> longs) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_info";
    return jdbcTemplate.queryForObject(sql, Long.class);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM restaurant_info WHERE restaurant_id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(RestaurantInfo entity) {
    deleteById(entity.getRestaurantId());
  }

  public void deleteAllById(Iterable<? extends Long> longs) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public void deleteAll(Iterable<? extends RestaurantInfo> entities) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant_info";
    jdbcTemplate.update(sql);
  }
}