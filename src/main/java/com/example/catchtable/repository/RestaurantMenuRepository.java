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
          rs.getLong("id"), // int unsigned -> Long
          rs.getLong("restaurant_id"), // int unsigned -> Long
          rs.getString("name"), // menu_name -> name
          rs.getString("description"), // menu_description -> description
          rs.getLong("price"), // int unsigned -> Long, menu_price -> price
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getBoolean("is_hidden") // is_hidden 필드 추가
      );

  public Optional<RestaurantMenu> save(RestaurantMenu entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<RestaurantMenu> insert(RestaurantMenu entity) {
    String sql = "INSERT INTO restaurant_menu (restaurant_id, name, description, price, is_hidden) " + // is_hidden 추가
        "VALUES (?, ?, ?, ?, ?)"; // is_hidden 추가
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getRestaurantId()); // int -> Long
      ps.setString(2, entity.getName());
      ps.setString(3, entity.getDescription());
      ps.setLong(4, entity.getPrice()); // int -> Long
      ps.setBoolean(5, entity.getIsHidden()); // is_hidden 추가
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue()); // int -> Long
  }

  private Optional<RestaurantMenu> update(RestaurantMenu entity) {
    String sql = "UPDATE restaurant_menu SET restaurant_id = ?, name = ?, description = ?, " +
        "price = ?, is_hidden = ? WHERE id = ?"; // is_hidden 추가
    jdbcTemplate.update(sql, entity.getRestaurantId(), entity.getName(), entity.getDescription(),
        entity.getPrice(), entity.getIsHidden(), entity.getId()); // is_hidden 추가
    return findById(entity.getId());
  }

  public Iterable<RestaurantMenu> saveAll(Iterable<RestaurantMenu> entities) {
    List<RestaurantMenu> result = new ArrayList<>();
    for (RestaurantMenu entity : entities) {
      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
    }
    return result;
  }

  public Optional<RestaurantMenu> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM restaurant_menu WHERE id = ?";
    List<RestaurantMenu> result = jdbcTemplate.query(sql, restaurantMenuRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM restaurant_menu WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<RestaurantMenu> findAll() {
    String sql = "SELECT * FROM restaurant_menu";
    return jdbcTemplate.query(sql, restaurantMenuRowMapper);
  }

  public Iterable<RestaurantMenu> findAll(Iterable<RestaurantMenu> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getId()));
    String sql = "SELECT * FROM restaurant_menu WHERE id IN (?)";
    return jdbcTemplate.query(sql, restaurantMenuRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_menu";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
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