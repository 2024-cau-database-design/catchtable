package com.example.catchtable.repository;

import com.example.catchtable.domain.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
          rs.getLong("id"),
          rs.getString("password_hash"),
          rs.getString("ident"),
          rs.getString("email"),
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")
      );

  public UserAuth save(UserAuth entity) {
    if (existsById(entity.getId())) {
      return update(entity);
    } else {
      return insert(entity);
    }
  }

  private UserAuth insert(UserAuth entity) {
    String sql = "INSERT INTO user_auth (id, password_hash, ident, email, created_at, updated_at, is_deleted) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql,
        entity.getId(),
        entity.getPasswordHash(),
        entity.getIdent(),
        entity.getEmail(),
        Timestamp.valueOf(entity.getCreatedAt()),
        Timestamp.valueOf(entity.getUpdatedAt()),
        entity.isDeleted());
    return entity;
  }

  private UserAuth update(UserAuth entity) {
    String sql = "UPDATE user_auth SET password_hash = ?, ident = ?, email = ?, updated_at = ? " +
        "WHERE id = ?";
    jdbcTemplate.update(sql,
        entity.getPasswordHash(),
        entity.getIdent(),
        entity.getEmail(),
        Timestamp.valueOf(entity.getUpdatedAt()),
        entity.getId());
    return findById(entity.getId()).orElseThrow(); // Return the updated entity
  }

  public Iterable<UserAuth> saveAll(Iterable<UserAuth> entities) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public Optional<UserAuth> findById(Long id) {
    String sql = "SELECT * FROM user_auth WHERE id = ?";
    List<UserAuth> result = jdbcTemplate.query(sql, userAuthRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM user_auth WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
  }

  public Iterable<UserAuth> findAll() {
    String sql = "SELECT * FROM user_auth";
    return jdbcTemplate.query(sql, userAuthRowMapper);
  }

  public Iterable<UserAuth> findAllById(Iterable<Long> longs) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public long count() {
    String sql = "SELECT count(*) FROM user_auth";
    return jdbcTemplate.queryForObject(sql, Long.class);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM user_auth WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(UserAuth entity) {
    deleteById(entity.getId());
  }

  public void deleteAllById(Iterable<? extends Long> longs) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public void deleteAll(Iterable<? extends UserAuth> entities) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public void deleteAll() {
    String sql = "DELETE FROM user_auth";
    jdbcTemplate.update(sql);
  }
}