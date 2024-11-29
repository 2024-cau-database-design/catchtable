package com.example.catchtable.repository;

import com.example.catchtable.domain.Booking;
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
public class BookingRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  BookingRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Booking> bookingRowMapper = (rs, rowNum) ->
      Booking.fromEntity(
          rs.getInt("id"),
          rs.getString("type")
      );


  public Optional<Booking> save(Booking entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<Booking> insert(Booking entity) {
    String sql = "INSERT INTO booking (type) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getType());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue());
  }

  private Optional<Booking> update(Booking entity) {
    String sql = "UPDATE booking SET type = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getType(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<Booking> saveAll(Iterable<Booking> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<Booking> findById(Integer id) {
    String sql = "SELECT * FROM booking WHERE id = ?";
    List<Booking> result = jdbcTemplate.query(sql, bookingRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM booking WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<Booking> findAll() {
    String sql = "SELECT * FROM booking";
    return jdbcTemplate.query(sql, bookingRowMapper);
  }

  public Iterable<Booking> findAll(Iterable<Booking> entities) {
    List<Booking> resultList = new ArrayList<>();
    for (Booking entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM booking";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM booking WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(Booking entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<Booking> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM booking";
    jdbcTemplate.update(sql);
  }
}