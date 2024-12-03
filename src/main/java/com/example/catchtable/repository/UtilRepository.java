package com.example.catchtable.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class UtilRepository {

    private final JdbcTemplate jdbcTemplate;

    public UtilRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Soft delete a record by ID.
     *
     * @param tableName the name of the table
     * @param id        the ID of the record to delete
     * @return the number of affected rows
     */
    public int softDeleteById(String tableName, Object id) {
        String sql = String.format("UPDATE %s SET deleted_at = ?, is_deleted = 1 WHERE id = ?", tableName);
        return jdbcTemplate.update(sql, Timestamp.valueOf(LocalDateTime.now()), id);
    }
}