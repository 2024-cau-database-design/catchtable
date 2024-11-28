package com.example.catchtable.repository;

import com.example.catchtable.domain.RestaurantMenu;
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
public class RestaurantMenuRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  RestaurantMenuRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<RestaurantMenu> restaurantMenuRowMapper = (rs, rowNum) ->
      RestaurantMenu.fromEntity(
          rs.getInt("id"),
          rs.getInt("restaurant_id"),
          rs.getString("menu_name"),
          rs.getString("menu_description"),
          rs.getInt("menu_price"),
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at")
      );

  public Optional<RestaurantMenu> save(RestaurantMenu entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<RestaurantMenu> insert(RestaurantMenu entity) {
    String sql = "INSERT INTO restaurant_menu (restaurant_id, menu_name, menu_description, menu_price) " +
        "VALUES (?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, entity.getRestaurantId());
      ps.setString(2, entity.getMenuName());
      ps.setString(3, entity.getMenuDescription());
      ps.setInt(4, entity.getMenuPrice());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue());
  }

  private Optional<RestaurantMenu> update(RestaurantMenu entity) {
    String sql = "UPDATE restaurant_menu SET restaurant_id = ?, menu_name = ?, menu_description = ?, " +
        "menu_price = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getRestaurantId(), entity.getMenuName(), entity.getMenuDescription(),
        entity.getMenuPrice(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<RestaurantMenu> saveAll(Iterable<RestaurantMenu> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<RestaurantMenu> findById(Integer id) {
    String sql = "SELECT * FROM restaurant_menu WHERE id = ?";
    List<RestaurantMenu> result = jdbcTemplate.query(sql, restaurantMenuRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM restaurant_menu WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<RestaurantMenu> findAll() {
    String sql = "SELECT * FROM restaurant_menu";
    return jdbcTemplate.query(sql, restaurantMenuRowMapper);
  }

  public Iterable<RestaurantMenu> findAll(Iterable<RestaurantMenu> entities) {
    List<RestaurantMenu> resultList = new ArrayList<>();
    for (RestaurantMenu entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_menu";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM restaurant_menu WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(RestaurantMenu entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<RestaurantMenu> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant_menu";
    jdbcTemplate.update(sql);
  }
}