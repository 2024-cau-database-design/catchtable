package com.example.catchtable.repository;

import com.example.catchtable.domain.RestaurantInfo;
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
public class RestaurantInfoRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  RestaurantInfoRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<RestaurantInfo> restaurantInfoRowMapper = (rs, rowNum) ->
      RestaurantInfo.fromEntity(
          rs.getInt("restaurant_id"),
          rs.getString("phone_number"),
          rs.getString("full_address"),
          rs.getString("website_url"),
          rs.getString("description"),
          rs.getTimestamp("created_at"), // No conversion to LocalDateTime
          rs.getTimestamp("updated_at"), // No conversion to LocalDateTime
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")  // No conversion to LocalDateTime, no null check
      );

  public Optional<RestaurantInfo> save(RestaurantInfo entity) {
    if (existsById(entity.getRestaurantId())) {
      return update(entity);
    } else {
      return insert(entity);
    }
  }

  private Optional<RestaurantInfo> insert(RestaurantInfo entity) {
    String sql = "INSERT INTO restaurant_info (restaurant_id, phone_number, full_address, website_url, description) " +
        "VALUES (?, ?, ?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();  // KeyHolder 추가
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);  // 생성된 키 반환 옵션 추가
      ps.setInt(1, entity.getRestaurantId());
      ps.setString(2, entity.getPhoneNumber());
      ps.setString(3, entity.getFullAddress());
      ps.setString(4, entity.getWebsiteUrl());
      ps.setString(5, entity.getDescription());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue()); // Return the created entity
  }

  private Optional<RestaurantInfo> update(RestaurantInfo entity) {
    String sql = "UPDATE restaurant_info SET phone_number = ?, full_address = ?, website_url = ?, description = ?, updated_at = ? " +
        "WHERE restaurant_id = ?";
    jdbcTemplate.update(sql,
        entity.getPhoneNumber(),
        entity.getFullAddress(),
        entity.getWebsiteUrl(),
        entity.getDescription(),
        Timestamp.valueOf(entity.getUpdatedAt()),
        entity.getRestaurantId());
    return findById(entity.getRestaurantId()); // Return the updated entity
  }


  public Iterable<RestaurantInfo> saveAll(Iterable<RestaurantInfo> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities); // 존재하는 객체들만 반환
  }

  public Optional<RestaurantInfo> findById(Integer id) {
    String sql = "SELECT * FROM restaurant_info WHERE restaurant_id = ?";
    List<RestaurantInfo> result = jdbcTemplate.query(sql, restaurantInfoRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM restaurant_info WHERE restaurant_id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0; // count가 0보다 크면 존재하는 것으로 간주
  }

  public Iterable<RestaurantInfo> findAll() {
    String sql = "SELECT * FROM restaurant_info";
    return jdbcTemplate.query(sql, restaurantInfoRowMapper);
  }

  public Iterable<RestaurantInfo> findAll(Iterable<RestaurantInfo> entities) {
    List<RestaurantInfo> resultList = new ArrayList<>();
    for (RestaurantInfo entity : entities) {
      if (existsById(entity.getRestaurantId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_info";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM restaurant_info WHERE restaurant_id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(RestaurantInfo entity) {
    deleteById(entity.getRestaurantId());
  }

  public void deleteAll(Iterable<RestaurantInfo> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant_info";
    jdbcTemplate.update(sql);
  }
}