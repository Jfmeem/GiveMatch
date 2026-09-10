package com.spl2.givematch.dao;

import com.spl2.givematch.model.Distribution;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DistributionDAO {

    public Distribution insert(Distribution distribution) {
        String sql = "INSERT INTO distributions (request_id, quantity_distributed, distributed_at) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, distribution.getRequestId());
            statement.setInt(2, distribution.getQuantityDistributed());
            statement.setString(3, distribution.getDistributedAt().toString());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    distribution.setId(keys.getInt(1));
                }
            }
            return distribution;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to insert distribution.", e);
        }
    }

    public List<Distribution> findAll() {
        String sql = "SELECT * FROM distributions ORDER BY distributed_at DESC";
        List<Distribution> distributions = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                distributions.add(mapRow(rs));
            }
            return distributions;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list distributions.", e);
        }
    }

    private Distribution mapRow(ResultSet rs) throws SQLException {
        return new Distribution(
                rs.getInt("id"),
                rs.getInt("request_id"),
                rs.getInt("quantity_distributed"),
                LocalDateTime.parse(rs.getString("distributed_at")));
    }
}
