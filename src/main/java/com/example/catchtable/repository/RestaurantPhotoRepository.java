package com.example.catchtable.repository;

import com.example.catchtable.domain.RestaurantPhoto;
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
public class RestaurantPhotoRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  RestaurantPhotoRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<RestaurantPhoto> restaurantPhotoRowMapper = (rs, rowNum) ->
      RestaurantPhoto.fromEntity(
          rs.getInt("restaurant_id"),
          rs.getString("photo_path"),
          rs.getString("photo_type"),
          rs.getInt("photo_size"),
          rs.getString("description"),
          rs.getTimestamp("created_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")
      );

  public Optional<RestaurantPhoto> save(RestaurantPhoto entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<RestaurantPhoto> insert(RestaurantPhoto entity) {
    String sql = "INSERT INTO restaurant_photo (restaurant_id, photo_path, photo_type, photo_size, description) " +
        "VALUES (?, ?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, entity.getRestaurantId());
      ps.setString(2, entity.getPhotoPath());
      ps.setString(3, entity.getPhotoType());
      ps.setInt(4, entity.getPhotoSize());
      ps.setString(5, entity.getDescription());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue());
  }

  private Optional<RestaurantPhoto> update(RestaurantPhoto entity) {
    String sql = "UPDATE restaurant_photo SET restaurant_id = ?, photo_path = ?, photo_type = ?, " +
        "photo_size = ?, description = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getRestaurantId(), entity.getPhotoPath(), entity.getPhotoType(),
        entity.getPhotoSize(), entity.getDescription(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<RestaurantPhoto> saveAll(Iterable<RestaurantPhoto> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<RestaurantPhoto> findById(Integer id) {
    String sql = "SELECT * FROM restaurant_photo WHERE id = ?";
    List<RestaurantPhoto> result = jdbcTemplate.query(sql, restaurantPhotoRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM restaurant_photo WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<RestaurantPhoto> findAll() {
    String sql = "SELECT * FROM restaurant_photo";
    return jdbcTemplate.query(sql, restaurantPhotoRowMapper);
  }

  public Iterable<RestaurantPhoto> findAll(Iterable<RestaurantPhoto> entities) {
    List<RestaurantPhoto> resultList = new ArrayList<>();
    for (RestaurantPhoto entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_photo";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM restaurant_photo WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(RestaurantPhoto entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<RestaurantPhoto> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant_photo";
    jdbcTemplate.update(sql);
  }
}