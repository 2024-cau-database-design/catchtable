package com.example.catchtable.repository;

import com.example.catchtable.domain.ReviewImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReviewImageRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  ReviewImageRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<ReviewImage> reviewImageRowMapper = (rs, rowNum) ->
      ReviewImage.fromEntity(
          rs.getLong("id"),
          rs.getLong("review_id"),
          rs.getString("name"),
          rs.getString("url")
      );

  public Optional<ReviewImage> save(ReviewImage entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<ReviewImage> insert(ReviewImage entity) {
    String sql = "INSERT INTO review_image (review_id, name, url) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getReviewId());
      ps.setString(2, entity.getName());
      ps.setString(3, entity.getUrl());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue());
  }

  private Optional<ReviewImage> update(ReviewImage entity) {
    String sql = "UPDATE review_image SET review_id = ?, name = ?, url = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getReviewId(), entity.getName(), entity.getUrl(), entity.getId());
    return findById(entity.getId());
  }

  public Optional<ReviewImage> findById(Long id) {
    String sql = "SELECT * FROM review_image WHERE id = ?";
    List<ReviewImage> result = jdbcTemplate.query(sql, reviewImageRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM review_image WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<ReviewImage> findAll() {
    String sql = "SELECT * FROM review_image";
    return jdbcTemplate.query(sql, reviewImageRowMapper);
  }

  public Iterable<ReviewImage> saveAll(Iterable<ReviewImage> entities) {
    entities.forEach(this::save);
    return findAll(entities);
  }

  public Iterable<ReviewImage> findAll(Iterable<ReviewImage> entities) {
    List<ReviewImage> resultList = new ArrayList<>();
    for (ReviewImage entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM review_image";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM review_image WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(ReviewImage entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<ReviewImage> entities) {
    entities.forEach(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM review_image";
    jdbcTemplate.update(sql);
  }
}
