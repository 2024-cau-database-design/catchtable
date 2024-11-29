package com.example.catchtable.repository;

import com.example.catchtable.domain.RestaurantLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
          rs.getLong("restaurant_id"), // int unsigned -> Long
          rs.getBigDecimal("latitude"),
          rs.getBigDecimal("longitude")
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
        "VALUES (?, ?, ?)";

    jdbcTemplate.update(sql,
        entity.getRestaurantId(),
        entity.getLatitude(),
        entity.getLongitude());
    return findById(entity.getRestaurantId());
  }

  private Optional<RestaurantLocation> update(RestaurantLocation entity) {
    String sql = "UPDATE restaurant_location SET latitude = ?, longitude = ? " +
        "WHERE restaurant_id = ?";
    jdbcTemplate.update(sql,
        entity.getLatitude(),
        entity.getLongitude(),
        entity.getRestaurantId());
    return findById(entity.getRestaurantId());
  }

  public Iterable<RestaurantLocation> saveAll(Iterable<RestaurantLocation> entities) {
    List<RestaurantLocation> result = new ArrayList<>();
    for (RestaurantLocation entity : entities) {
      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
    }
    return result;
  }

  public Optional<RestaurantLocation> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM restaurant_location WHERE restaurant_id = ?";
    List<RestaurantLocation> result = jdbcTemplate.query(sql, restaurantLocationRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM restaurant_location WHERE restaurant_id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0; // count가 0보다 크면 존재하는 것으로 간주
  }

  public Iterable<RestaurantLocation> findAll() {
    String sql = "SELECT * FROM restaurant_location";
    return jdbcTemplate.query(sql, restaurantLocationRowMapper);
  }

  public Iterable<RestaurantLocation> findAll(Iterable<RestaurantLocation> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getRestaurantId()));
    String sql = "SELECT * FROM restaurant_location WHERE restaurant_id IN (?)";
    return jdbcTemplate.query(sql, restaurantLocationRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_location";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
    String sql = "DELETE FROM restaurant_location WHERE restaurant_id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(RestaurantLocation entity) {
    deleteById(entity.getRestaurantId());
  }

  public void deleteAll(Iterable<? extends RestaurantLocation> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant_location";
    jdbcTemplate.update(sql);
  }
}