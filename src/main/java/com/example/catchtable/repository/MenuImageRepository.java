package com.example.catchtable.repository;

import com.example.catchtable.domain.MenuImage;
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
public class MenuImageRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  MenuImageRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<MenuImage> menuImageRowMapper = (rs, rowNum) ->
      MenuImage.fromEntity(
          rs.getLong("id"), // int unsigned -> Long
          rs.getString("name"),
          rs.getString("url"),
          rs.getLong("menu_id") // int unsigned -> Long
      );

  public Optional<MenuImage> save(MenuImage entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<MenuImage> insert(MenuImage entity) {
    String sql = "INSERT INTO menu_image (name, url, menu_id) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getName());
      ps.setString(2, entity.getUrl());
      ps.setLong(3, entity.getMenuId()); // int -> Long
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue()); // int -> Long
  }

  private Optional<MenuImage> update(MenuImage entity) {
    String sql = "UPDATE menu_image SET name = ?, url = ?, menu_id = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getName(), entity.getUrl(), entity.getMenuId(), entity.getId());
    return findById(entity.getId());
  }


  public Iterable<MenuImage> saveAll(Iterable<MenuImage> entities) {
    List<MenuImage> result = new ArrayList<>();
    for (MenuImage entity : entities) {
      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
    }
    return result;
  }

  public Optional<MenuImage> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM menu_image WHERE id = ?";
    List<MenuImage> result = jdbcTemplate.query(sql, menuImageRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public Optional<MenuImage> findByName(String name) {
    String sql = "SELECT * FROM menu_image WHERE name = ?";
    List<MenuImage> result = jdbcTemplate.query(sql, menuImageRowMapper, name);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM menu_image WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<MenuImage> findAll() {
    String sql = "SELECT * FROM menu_image";
    return jdbcTemplate.query(sql, menuImageRowMapper);
  }

  public Iterable<MenuImage> findAll(Iterable<MenuImage> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getId()));
    String sql = "SELECT * FROM menu_image WHERE id IN (?)";
    return jdbcTemplate.query(sql, menuImageRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM menu_image";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
    String sql = "DELETE FROM menu_image WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(MenuImage entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<MenuImage> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM menu_image";
    jdbcTemplate.update(sql);
  }
}