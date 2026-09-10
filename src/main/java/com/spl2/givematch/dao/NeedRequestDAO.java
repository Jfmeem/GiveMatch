package com.spl2.givematch.dao;

import com.spl2.givematch.model.NeedRequest;
import com.spl2.givematch.model.NeedRequestStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NeedRequestDAO {

    public NeedRequest insert(NeedRequest need) {
        String sql = "INSERT INTO need_requests (receiver_id, category_id, quantity_needed, status, created_at) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, need.getReceiverId());
            statement.setInt(2, need.getCategoryId());
            statement.setInt(3, need.getQuantityNeeded());
            statement.setString(4, need.getStatus().name());
            statement.setString(5, need.getCreatedAt().toString());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    need.setId(keys.getInt(1));
                }
            }
            return need;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to insert standing need.", e);
        }
    }

    public void updateStatus(int id, NeedRequestStatus status) {
        String sql = "UPDATE need_requests SET status = ? WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update standing need status.", e);
        }
    }

    public List<NeedRequest> findOpenByCategory(int categoryId) {
        String sql = "SELECT * FROM need_requests WHERE category_id = ? AND status = 'OPEN'";
        List<NeedRequest> needs = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    needs.add(mapRow(rs));
                }
            }
            return needs;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list standing needs by category.", e);
        }
    }

    public List<NeedRequest> findByReceiver(int receiverId) {
        String sql = "SELECT * FROM need_requests WHERE receiver_id = ? ORDER BY created_at DESC";
        List<NeedRequest> needs = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, receiverId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    needs.add(mapRow(rs));
                }
            }
            return needs;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list standing needs by receiver.", e);
        }
    }

    public List<NeedRequest> findAllOpen() {
        String sql = "SELECT * FROM need_requests WHERE status = 'OPEN' ORDER BY created_at";
        List<NeedRequest> needs = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                needs.add(mapRow(rs));
            }
            return needs;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list open standing needs.", e);
        }
    }

    private NeedRequest mapRow(ResultSet rs) throws SQLException {
        return new NeedRequest(
                rs.getInt("id"),
                rs.getInt("receiver_id"),
                rs.getInt("category_id"),
                rs.getInt("quantity_needed"),
                NeedRequestStatus.valueOf(rs.getString("status")),
                LocalDateTime.parse(rs.getString("created_at")));
    }
}
