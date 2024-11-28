package com.example.catchtable.repository;

import com.example.catchtable.domain.UserAuth;
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
public class UserAuthRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  UserAuthRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<UserAuth> userAuthRowMapper = (rs, rowNum) ->
      UserAuth.fromEntity(
          rs.getInt("id"),
          rs.getString("password_hash"),
          rs.getString("ident"),
          rs.getString("email"),
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")
      );

  public Optional<UserAuth> save(UserAuth entity) {
    if (existsById(entity.getId())) {
      return update(entity);
    } else {
      return insert(entity);
    }
  }

  private Optional<UserAuth> insert(UserAuth entity) {
    String sql = "INSERT INTO user_auth (password_hash, ident, email, created_at, updated_at, is_deleted) "
        + "VALUES (?, ?, ?, ?, ?, ?)"; // id 컬럼 제거

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getPasswordHash());
      ps.setString(2, entity.getIdent());
      ps.setString(3, entity.getEmail());
      ps.setTimestamp(4, Timestamp.valueOf(entity.getCreatedAt()));
      ps.setTimestamp(5, Timestamp.valueOf(entity.getUpdatedAt()));
      ps.setBoolean(6, entity.getIsDeleted());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue());
  }

  private Optional<UserAuth> update(UserAuth entity) {
    String sql = "UPDATE user_auth SET password_hash = ?, ident = ?, email = ?, updated_at = ? " +
        "WHERE id = ?";
    jdbcTemplate.update(sql,
        entity.getPasswordHash(),
        entity.getIdent(),
        entity.getEmail(),
        Timestamp.valueOf(entity.getUpdatedAt()),
        entity.getId());
    return findById(entity.getId()); // Return the updated entity
  }

  public Iterable<UserAuth> saveAll(Iterable<UserAuth> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities); // 존재하는 객체들만 반환
  }

  public Optional<UserAuth> findById(Integer id) {
    String sql = "SELECT * FROM user_auth WHERE id = ?";
    List<UserAuth> result = jdbcTemplate.query(sql, userAuthRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM user_auth WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0; // count가 0보다 크면 존재하는 것으로 간주
  }

  public Iterable<UserAuth> findAll() {
    String sql = "SELECT * FROM user_auth";
    return jdbcTemplate.query(sql, userAuthRowMapper);
  }

  public Iterable<UserAuth> findAll(Iterable<UserAuth> entities) {
    List<UserAuth> resultList = new ArrayList<>();
    for (UserAuth entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM user_auth";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM user_auth WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(UserAuth entity) {
    deleteById(entity.getId());
  }


  public void deleteAll(Iterable<UserAuth> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM user_auth";
    jdbcTemplate.update(sql);
  }
}