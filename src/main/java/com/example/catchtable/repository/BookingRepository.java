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
import java.util.stream.Collectors;

@Repository
public class BookingRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  BookingRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Booking> bookingRowMapper = (rs, rowNum) ->
      Booking.fromEntity(
      null,
          rs.getString("type")
      );


//  public Long save(Booking entity) {
//    System.out.println(entity.getType());
//    if (entity.getId() == null) {
//      return insert(entity);
//    } else {
//      return update(entity);
//    }
//  }

  public Long insert(Booking entity) {
    String sql = "INSERT INTO booking (type) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
      ps.setString(1, entity.getType());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    if (key == null) {
      throw new IllegalStateException("Failed to retrieve generated key.");
    }
    System.out.println("Generated Key: " + key);
    System.out.println("Generated Key: " + key.longValue());
    return key.longValue();
  }

  private Optional<Booking> update(Booking entity) {
    String sql = "UPDATE booking SET type = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getType(), entity.getId());
    return findById(entity.getId());
  }

//  public Iterable<Booking> saveAll(Iterable<Booking> entities) {
//    entities.iterator().forEachRemaining(this::save);
//    return findAll(entities);
//  }

  public Optional<Booking> findById(Long id) {
    String sql = "SELECT * FROM booking WHERE id = ?";
    List<Booking> result = jdbcTemplate.query(sql, bookingRowMapper, id);
    System.out.println("result22: [" + result.stream()
            .map(Booking::getId)
            .map(String::valueOf)
            .collect(Collectors.joining(", ")));

    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
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

  public void deleteById(Long id) {
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