package com.example.catchtable.repository;

import com.example.catchtable.domain.RestaurantTable;
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
public class RestaurantTableRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  RestaurantTableRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<RestaurantTable> restaurantTableRowMapper = (rs, rowNum) ->
      RestaurantTable.fromEntity(
          rs.getLong("id"),
          rs.getLong("restaurant_id"),
          rs.getLong("table_type_id"),
          rs.getInt("seat_capacity"),
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")
      );

  public Optional<RestaurantTable> save(RestaurantTable entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<RestaurantTable> insert(RestaurantTable entity) {
    String sql = "INSERT INTO restaurant_table (restaurant_id, table_type_id, seat_capacity) " +
        "VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getRestaurantId());
      ps.setLong(2, entity.getTableTypeId());
      ps.setInt(3, entity.getSeatCapacity());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue());
  }

  private Optional<RestaurantTable> update(RestaurantTable entity) {
    String sql = "UPDATE restaurant_table SET restaurant_id = ?, table_type_id = ?, " +
        "seat_capacity = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getRestaurantId(), entity.getTableTypeId(),
        entity.getSeatCapacity(), entity.getId());
    return findById(entity.getId());
  }

  public Optional<RestaurantTable> findById(Long id) {
    String sql = "SELECT * FROM restaurant_table WHERE id = ?";
    List<RestaurantTable> result = jdbcTemplate.query(sql, restaurantTableRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM restaurant_table WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<RestaurantTable> findAll() {
    String sql = "SELECT * FROM restaurant_table";
    return jdbcTemplate.query(sql, restaurantTableRowMapper);
  }

  public Iterable<RestaurantTable> saveAll(Iterable<RestaurantTable> entities) {
    entities.forEach(this::save);
    return findAll(entities);
  }

  public Iterable<RestaurantTable> findAll(Iterable<RestaurantTable> entities) {
    List<RestaurantTable> resultList = new ArrayList<>();
    for (RestaurantTable entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_table";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM restaurant_table WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(RestaurantTable entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<RestaurantTable> entities) {
    entities.forEach(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant_table";
    jdbcTemplate.update(sql);
  }
}
