package com.example.catchtable.repository;

import com.example.catchtable.domain.RestaurantFoodCategory;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RestaurantFoodCategoryRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  RestaurantFoodCategoryRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<RestaurantFoodCategory> restaurantFoodCategoryRowMapper = (rs, rowNum) ->
      RestaurantFoodCategory.fromEntity(
          rs.getLong("restaurant_id"),
          rs.getString("name")
      );

  public Optional<RestaurantFoodCategory> save(RestaurantFoodCategory entity) {
    if (existsById(entity.getRestaurantId())) {
      return update(entity);
    } else {
      return insert(entity);
    }
  }

  private Optional<RestaurantFoodCategory> insert(RestaurantFoodCategory entity) {
    String sql = "INSERT INTO restaurant_food_category (restaurant_id, name) " +
        "VALUES (?, ?)";
    jdbcTemplate.update(sql, entity.getRestaurantId(), entity.getName());
    return findById(entity.getRestaurantId());
  }

  private Optional<RestaurantFoodCategory> update(RestaurantFoodCategory entity) {
    String sql = "UPDATE restaurant_food_category SET name = ? " +
        "WHERE restaurant_id = ?";
    jdbcTemplate.update(sql, entity.getName(), entity.getRestaurantId());
    return findById(entity.getRestaurantId());
  }

  public Iterable<RestaurantFoodCategory> saveAll(Iterable<RestaurantFoodCategory> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<RestaurantFoodCategory> findById(Long restaurantId) {
    String sql = "SELECT * FROM restaurant_food_category WHERE restaurant_id = ?";
    List<RestaurantFoodCategory> result = jdbcTemplate.query(sql, restaurantFoodCategoryRowMapper, restaurantId);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long restaurantId) {
    String sql = "SELECT count(*) FROM restaurant_food_category WHERE restaurant_id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, restaurantId);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<RestaurantFoodCategory> findAll() {
    String sql = "SELECT * FROM restaurant_food_category";
    return jdbcTemplate.query(sql, restaurantFoodCategoryRowMapper);
  }

  public Iterable<RestaurantFoodCategory> findAll(Iterable<RestaurantFoodCategory> entities) {
    List<RestaurantFoodCategory> resultList = new ArrayList<>();
    for (RestaurantFoodCategory entity : entities) {
      if (existsById(entity.getRestaurantId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_food_category";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long restaurantId) {
    String sql = "DELETE FROM restaurant_food_category WHERE restaurant_id = ?";
    jdbcTemplate.update(sql, restaurantId);
  }

  public void delete(RestaurantFoodCategory entity) {
    deleteById(entity.getRestaurantId());
  }

  public void deleteAll(Iterable<RestaurantFoodCategory> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant_food_category";
    jdbcTemplate.update(sql);
  }
}
