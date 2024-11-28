package com.example.catchtable.repository;

import com.example.catchtable.domain.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReservationRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  ReservationRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) ->
      Reservation.fromEntity(
          rs.getInt("id"),
          rs.getInt("reservation_time_id"),
          rs.getDate("booking_date").toLocalDate(),
          rs.getByte("guests_count"),
          rs.getInt("restaurant_table_id"),
          rs.getTimestamp("created_at"),
          rs.getTimestamp("updated_at"),
          rs.getBoolean("is_deleted"),
          rs.getTimestamp("deleted_at")
      );

  public Optional<Reservation> save(Reservation entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<Reservation> insert(Reservation entity) {
    String sql = "INSERT INTO reservation (reservation_time_id, booking_date, guests_count, restaurant_table_id) " +
        "VALUES (?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, entity.getReservationTimeId());
      ps.setDate(2, Date.valueOf(entity.getBookingDate()));
      ps.setByte(3, entity.getGuestsCount());
      ps.setInt(4, entity.getRestaurantTableId());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).intValue());
  }

  private Optional<Reservation> update(Reservation entity) {
    String sql = "UPDATE reservation SET reservation_time_id = ?, booking_date = ?, guests_count = ?, " +
        "restaurant_table_id = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getReservationTimeId(), Date.valueOf(entity.getBookingDate()),
        entity.getGuestsCount(), entity.getRestaurantTableId(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<Reservation> saveAll(Iterable<Reservation> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<Reservation> findById(Integer id) {
    String sql = "SELECT * FROM reservation WHERE id = ?";
    List<Reservation> result = jdbcTemplate.query(sql, reservationRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Integer id) {
    String sql = "SELECT count(*) FROM reservation WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<Reservation> findAll() {
    String sql = "SELECT * FROM reservation";
    return jdbcTemplate.query(sql, reservationRowMapper);
  }

  public Iterable<Reservation> findAll(Iterable<Reservation> entities) {
    List<Reservation> resultList = new ArrayList<>();
    for (Reservation entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM reservation";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM reservation WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(Reservation entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<Reservation> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM reservation";
    jdbcTemplate.update(sql);
  }
}