package com.example.catchtable.repository;

import com.example.catchtable.domain.TableType;
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
public class TableTypeRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  TableTypeRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<TableType> tableTypeRowMapper = (rs, rowNum) ->
      TableType.fromEntity(
          rs.getInt("id"),
          rs.getString("type_name")
      );

  public Optional<TableType> save(TableType entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<TableType> insert(TableType entity) {
    String sql = "INSERT INTO table_type (type_name) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getTypeName());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue());
  }

  private Optional<TableType> update(TableType entity) {
    String sql = "UPDATE table_type SET type_name = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getTypeName(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<TableType> saveAll(Iterable<TableType> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<TableType> findById(Integer id) {
    String sql = "SELECT * FROM table_type WHERE id = ?";
    List<TableType> result = jdbcTemplate.query(sql, tableTypeRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM table_type WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<TableType> findAll() {
    String sql = "SELECT * FROM table_type";
    return jdbcTemplate.query(sql, tableTypeRowMapper);
  }

  public Iterable<TableType> findAll(Iterable<TableType> entities) {
    List<TableType> resultList = new ArrayList<>();
    for (TableType entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM table_type";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM table_type WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(TableType entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<TableType> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM table_type";
    jdbcTemplate.update(sql);
  }
}