package com.example.catchtable.repository;

import com.example.catchtable.domain.PickupHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
public class PickupHistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    PickupHistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<PickupHistory> ROW_MAPPER = (ResultSet rs, int rowNum) -> PickupHistory.fromEntity(
            rs.getLong("id"),
            rs.getLong("status_id"),
            rs.getTimestamp("picked_at"),
            rs.getLong("pickup_time_id"),
            rs.getTimestamp("pickup_at").toLocalDateTime(),
            rs.getLong("pickup_id"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public List<PickupHistory> findAll() {
        String sql = "SELECT * FROM pickup_history";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Optional<PickupHistory> findById(Long id) {
        String sql = "SELECT * FROM pickup_history WHERE id = ?";
        List<PickupHistory> results = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<PickupHistory> findLatestByPickupId(Long pickupId) {
        String sql = "SELECT * FROM pickup_history WHERE pickup_id = ? ORDER BY created_at DESC LIMIT 1";
        List<PickupHistory> results = jdbcTemplate.query(sql, ROW_MAPPER, pickupId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void insert(PickupHistory pickupHistory) {
        String sql = "INSERT INTO pickup_history (status_id, picked_at, pickup_time_id, pickup_at, pickup_id, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                pickupHistory.getStatusId(),
                pickupHistory.getPickedAt(),
                pickupHistory.getPickupTimeId(),
                pickupHistory.getPickupAt(),
                pickupHistory.getPickupId(),
                pickupHistory.getCreatedAt()
        );
    }

    public void update(PickupHistory pickupHistory) {
        String sql = "UPDATE pickup_history SET status_id = ?, picked_at = ?, pickup_time_id = ?, " +
                "pickup_at = ?, pickup_id = ?, created_at = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                pickupHistory.getStatusId(),
                pickupHistory.getPickedAt(),
                pickupHistory.getPickupTimeId(),
                pickupHistory.getPickupAt(),
                pickupHistory.getPickupId(),
                pickupHistory.getCreatedAt(),
                pickupHistory.getId()
        );
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM pickup_history WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}