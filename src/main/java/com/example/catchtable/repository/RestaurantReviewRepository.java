package com.example.catchtable.repository;

import com.example.catchtable.domain.RestaurantReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RestaurantReviewRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  RestaurantReviewRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<RestaurantReview> restaurantReviewRowMapper = (rs, rowNum) ->
      RestaurantReview.fromEntity(
          rs.getLong("id"),
          rs.getLong("restaurant_id"),
          rs.getLong("customer_id"),
          rs.getBigDecimal("review_rating"),
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at"),
          rs.getString("review_caption")
      );

  public Optional<RestaurantReview> save(RestaurantReview entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<RestaurantReview> insert(RestaurantReview entity) {
    String sql = "INSERT INTO restaurant_review (restaurant_id, customer_id, review_rating, review_caption) " +
        "VALUES (?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getRestaurantId());
      ps.setLong(2, entity.getCustomerId());
      ps.setBigDecimal(3, entity.getRating());
      ps.setString(4, entity.getText());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue());
  }

  private Optional<RestaurantReview> update(RestaurantReview entity) {
    String sql = "UPDATE restaurant_review SET restaurant_id = ?, customer_id = ?, review_rating = ?, " +
        "review_caption = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getRestaurantId(), entity.getCustomerId(), entity.getRating(),
        entity.getText(), entity.getId());
    return findById(entity.getId());
  }

  public Optional<RestaurantReview> findById(Long id) {
    String sql = "SELECT * FROM restaurant_review WHERE id = ?";
    List<RestaurantReview> result = jdbcTemplate.query(sql, restaurantReviewRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM restaurant_review WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<RestaurantReview> findAll() {
    String sql = "SELECT * FROM restaurant_review";
    return jdbcTemplate.query(sql, restaurantReviewRowMapper);
  }

  public Iterable<RestaurantReview> saveAll(Iterable<RestaurantReview> entities) {
    entities.forEach(this::save);
    return findAll(entities);
  }

  public Iterable<RestaurantReview> findAll(Iterable<RestaurantReview> entities) {
    List<RestaurantReview> resultList = new ArrayList<>();
    for (RestaurantReview entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM restaurant_review";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM restaurant_review WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(RestaurantReview entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<RestaurantReview> entities) {
    entities.forEach(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM restaurant_review";
    jdbcTemplate.update(sql);
  }
}
