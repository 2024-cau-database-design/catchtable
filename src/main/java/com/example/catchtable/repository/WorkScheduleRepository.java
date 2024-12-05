package com.example.catchtable.repository;

import com.example.catchtable.domain.WorkSchedule;
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
public class WorkScheduleRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  WorkScheduleRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<WorkSchedule> workScheduleRowMapper = (rs, rowNum) ->
      WorkSchedule.fromEntity(
          rs.getLong("id"),
          rs.getString("day_of_week"),
          rs.getTime("start_time"),
          rs.getTime("end_time"),
          rs.getLong("restaurant_id")
      );

  public Optional<WorkSchedule> save(WorkSchedule entity) {
    if (entity.getId() == null) {
      return insert(entity);
    } else {
      return update(entity);
    }
  }

  private Optional<WorkSchedule> insert(WorkSchedule entity) {
    String sql = "INSERT INTO work_schedule (day_of_week, start_time, end_time, restaurant_id) " +
        "VALUES (?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getDayOfWeek());
      ps.setTime(2, entity.getStartTime());
      ps.setTime(3, entity.getEndTime());
      ps.setLong(4, entity.getRestaurantId());
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    return findById((long) Objects.requireNonNull(key).intValue());
  }

  private Optional<WorkSchedule> update(WorkSchedule entity) {
    String sql = "UPDATE work_schedule SET day_of_week = ?, start_time = ?, end_time = ?, " +
        "restaurant_id = ? WHERE id = ?";
    jdbcTemplate.update(sql, entity.getDayOfWeek(), entity.getStartTime(),
        entity.getEndTime(), entity.getRestaurantId(), entity.getId());
    return findById(entity.getId());
  }

  public Iterable<WorkSchedule> saveAll(Iterable<WorkSchedule> entities) {
    entities.iterator().forEachRemaining(this::save);
    return findAll(entities);
  }

  public Optional<WorkSchedule> findById(Long id) {
    String sql = "SELECT * FROM work_schedule WHERE id = ?";
    List<WorkSchedule> result = jdbcTemplate.query(sql, workScheduleRowMapper, id);
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public boolean existsById(Long id) {
    String sql = "SELECT count(*) FROM work_schedule WHERE id = ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, id);
    return Optional.ofNullable(result).orElse(0L) > 0;
  }

  public Iterable<WorkSchedule> findAll() {
    String sql = "SELECT * FROM work_schedule";
    return jdbcTemplate.query(sql, workScheduleRowMapper);
  }

  public Iterable<WorkSchedule> findAll(Iterable<WorkSchedule> entities) {
    List<WorkSchedule> resultList = new ArrayList<>();
    for (WorkSchedule entity : entities) {
      if (existsById(entity.getId())) {
        resultList.add(entity);
      }
    }
    return resultList;
  }

  public long count() {
    String sql = "SELECT count(*) FROM work_schedule";
    var result = jdbcTemplate.queryForObject(sql, Long.class);
    return Optional.ofNullable(result).orElse(0L);
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM work_schedule WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  public void delete(WorkSchedule entity) {
    deleteById(entity.getId());
  }

  public void deleteAll(Iterable<WorkSchedule> entities) {
    entities.iterator().forEachRemaining(this::delete);
  }

  public void deleteAll() {
    String sql = "DELETE FROM work_schedule";
    jdbcTemplate.update(sql);
  }
}