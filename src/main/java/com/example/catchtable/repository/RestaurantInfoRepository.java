package com.example.catchtable.repository;

import com.example.catchtable.domain.RestaurantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
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
          rs.getLong("restaurant_id"), // int unsigned -> Long
          rs.getString("phone_number"),
          rs.getString("website_url"), // full_address 제거
          rs.getString("description"),
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")
      );

  public Optional<RestaurantInfo> save(RestaurantInfo entity) {
    if (existsById(entity.getRestaurantId())) {
      return update(entity);
    } else {
      return insert(entity);
    }
  }

  private Optional<RestaurantInfo> insert(RestaurantInfo entity) {
    String sql = "INSERT INTO restaurant_info (restaurant_id, phone_number, website_url, description) " + // full_address 제거
        "VALUES (?, ?, ?, ?)"; // full_address 제거

    jdbcTemplate.update(sql,
        entity.getRestaurantId(),
        entity.getPhoneNumber(),
        entity.getWebsiteUrl(),
        entity.getDescription());
    return findById(entity.getRestaurantId());
  }

  private Optional<RestaurantInfo> update(RestaurantInfo entity) {
    String sql = "UPDATE restaurant_info SET phone_number = ?, website_url = ?, description = ?, updated_at = ? " + // full_address 제거
        "WHERE restaurant_id = ?";
    jdbcTemplate.update(sql,
        entity.getPhoneNumber(),
        entity.getWebsiteUrl(),
        entity.getDescription(),
        Timestamp.valueOf(entity.getUpdatedAt()),
        entity.getRestaurantId());
    return findById(entity.getRestaurantId());
  }


  public Iterable<RestaurantInfo> saveAll(Iterable<RestaurantInfo> entities) {
    List<RestaurantInfo> result = new ArrayList<>();
    for (RestaurantInfo entity : entities) {
      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
    }
    return result;
  }

  public Optional<RestaurantInfo> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM restaurant_info WHERE restaurant_id = ?";
    List<RestaurantInfo> result = jdbcTemplate.query(sql, restaurantInfoRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM restaurant_info WHERE restaurant_id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0; // count가 0보다 크면 존재하는 것으로 간주
  }

  public Iterable<RestaurantInfo> findAll() {
    String sql = "SELECT * FROM restaurant_info";
    return jdbcTemplate.query(sql, restaurantInfoRowMapper);
  }

  public Iterable<RestaurantInfo> findAll(Iterable<RestaurantInfo> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getRestaurantId()));
    String sql = "SELECT * FROM restaurant_info WHERE restaurant_id IN (?)";
    return jdbcTemplate.query(sql, restaurantInfoRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_info";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
    String sql = "DELETE FROM restaurant_info WHERE restaurant_id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(RestaurantInfo entity) {
    deleteById(entity.getRestaurantId());
  }

  public void deleteAll(Iterable<? extends RestaurantInfo> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant_info";
    jdbcTemplate.update(sql);
  }
}