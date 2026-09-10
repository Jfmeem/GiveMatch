package com.spl2.givematch.dao;

import com.spl2.givematch.model.Request;
import com.spl2.givematch.model.RequestStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestDAO {

    public Request insert(Request request) {
        String sql = "INSERT INTO requests (donation_id, receiver_id, quantity_requested, "
                + "quantity_allocated, status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, request.getDonationId());
            statement.setInt(2, request.getReceiverId());
            statement.setInt(3, request.getQuantityRequested());
            statement.setInt(4, request.getQuantityAllocated());
            statement.setString(5, request.getStatus().name());
            statement.setString(6, request.getCreatedAt().toString());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    request.setId(keys.getInt(1));
                }
            }
            return request;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to insert request.", e);
        }
    }

    public void updateState(Request request) {
        String sql = "UPDATE requests SET status = ?, quantity_allocated = ? WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, request.getStatus().name());
            statement.setInt(2, request.getQuantityAllocated());
            statement.setInt(3, request.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update request state.", e);
        }
    }

    public Optional<Request> findById(int id) {
        String sql = "SELECT * FROM requests WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to look up request.", e);
        }
    }

    public List<Request> findPendingByDonation(int donationId) {
        String sql = "SELECT * FROM requests WHERE donation_id = ? AND status = 'PENDING'";
        return queryByDonation(sql, donationId);
    }

    public List<Request> findByDonation(int donationId) {
        String sql = "SELECT * FROM requests WHERE donation_id = ? ORDER BY created_at";
        return queryByDonation(sql, donationId);
    }

    public List<Request> findByReceiver(int receiverId) {
        String sql = "SELECT * FROM requests WHERE receiver_id = ? ORDER BY created_at DESC";
        List<Request> requests = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, receiverId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }
            return requests;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list requests by receiver.", e);
        }
    }

    public List<Request> findAll() {
        String sql = "SELECT * FROM requests ORDER BY created_at DESC";
        List<Request> requests = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                requests.add(mapRow(rs));
            }
            return requests;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list requests.", e);
        }
    }

    private List<Request> queryByDonation(String sql, int donationId) {
        List<Request> requests = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, donationId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }
            return requests;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query requests by donation.", e);
        }
    }

    private Request mapRow(ResultSet rs) throws SQLException {
        return new Request(
                rs.getInt("id"),
                rs.getInt("donation_id"),
                rs.getInt("receiver_id"),
                rs.getInt("quantity_requested"),
                rs.getInt("quantity_allocated"),
                RequestStatus.valueOf(rs.getString("status")),
                LocalDateTime.parse(rs.getString("created_at")));
    }
}
