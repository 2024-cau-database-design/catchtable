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
import java.time.LocalDate;
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
          rs.getLong("id"), // int unsigned -> Long
          rs.getLong("reservation_time_id"), // int unsigned -> Long
          rs.getDate("booking_date").toLocalDate(),
          rs.getInt("guests_count"), // byte -> int
          rs.getLong("restaurant_table_id"), // int unsigned -> Long
          rs.getBoolean("is_hidden"), // tinyint(1) -> Boolean, 추가
          rs.getLong("restaurant_id"), // int unsigned -> Long, 추가
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
    String sql = "INSERT INTO reservation (reservation_time_id, booking_date, guests_count, restaurant_table_id, is_hidden, restaurant_id) " + // is_hidden, restaurant_id 추가
        "VALUES (?, ?, ?, ?, ?, ?)"; // is_hidden, restaurant_id 추가
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, entity.getReservationTimeId()); // int -> Long
      ps.setDate(2, Date.valueOf(entity.getBookingDate()));
      ps.setInt(3, entity.getGuestsCount()); // byte -> int
      ps.setLong(4, entity.getRestaurantTableId()); // int -> Long
      ps.setBoolean(5, entity.getIsHidden()); // Boolean, 추가
      ps.setLong(6, entity.getRestaurantId()); // Long, 추가
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById(Objects.requireNonNull(key).longValue()); // int -> Long
  }

  private Optional<Reservation> update(Reservation entity) {
    String sql = "UPDATE reservation SET reservation_time_id = ?, booking_date = ?, guests_count = ?, " +
        "restaurant_table_id = ?, is_hidden = ?, restaurant_id = ? WHERE id = ?"; // is_hidden, restaurant_id 추가
    jdbcTemplate.update(sql,
        entity.getReservationTimeId(),
        Date.valueOf(entity.getBookingDate()),
        entity.getGuestsCount(),
        entity.getRestaurantTableId(),
        entity.getIsHidden(), // Boolean, 추가
        entity.getRestaurantId(), // Long, 추가
        entity.getId());
    return findById(entity.getId());
  }

  public Iterable<Reservation> saveAll(Iterable<Reservation> entities) {
    List<Reservation> result = new ArrayList<>();
    for (Reservation entity : entities) {
      save(entity).ifPresent(result::add); // save 결과를 리스트에 추가
    }
    return result;
  }

  public Optional<Reservation> findById(Long id) { // Integer -> Long
    String sql = "SELECT * FROM reservation WHERE id = ?";
    List<Reservation> result = jdbcTemplate.query(sql, reservationRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) { // Integer -> Long
    String sql = "SELECT count(*) FROM reservation WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<Reservation> findAll() {
    String sql = "SELECT * FROM reservation";
    return jdbcTemplate.query(sql, reservationRowMapper);
  }

  public Iterable<Reservation> findAll(Iterable<Reservation> entities) {
    List<Long> ids = new ArrayList<>();
    entities.forEach(entity -> ids.add(entity.getId()));
    String sql = "SELECT * FROM reservation WHERE id IN (?)";
    return jdbcTemplate.query(sql, reservationRowMapper, ids);
  }

  public long count() {
    String sql = "SELECT count(*) FROM reservation";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) { // Integer -> Long
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