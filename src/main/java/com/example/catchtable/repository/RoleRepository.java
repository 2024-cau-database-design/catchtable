package com.example.catchtable.repository;

import com.example.catchtable.domain.Role;
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
public class RoleRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  RoleRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Role> roleRowMapper = (rs, rowNum) ->
      Role.fromEntity(
          rs.getInt("id"),
          rs.getString("role_name")
      );

  public Optional<Role> save(Role entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<Role> insert(Role entity) {
    String sql = "INSERT INTO role (role_name) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getRoleName());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue());
  }

  private Optional<Role> update(Role entity) {
    String sql = "UPDATE role SET role_name = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getRoleName(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<Role> saveAll(Iterable<Role> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<Role> findById(Integer id) {
    String sql = "SELECT * FROM role WHERE id = ?";
    List<Role> result = jdbcTemplate.query(sql, roleRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM role WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<Role> findAll() {
    String sql = "SELECT * FROM role";
    return jdbcTemplate.query(sql, roleRowMapper);
  }

  public Iterable<Role> findAll(Iterable<Role> entities) {
    List<Role> resultList = new ArrayList<>();
    for (Role entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM role";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM role WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(Role entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<Role> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM role";
    jdbcTemplate.update(sql);
  }
}