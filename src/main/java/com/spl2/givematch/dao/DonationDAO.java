package com.spl2.givematch.dao;

import com.spl2.givematch.model.Donation;
import com.spl2.givematch.model.DonationStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonationDAO {

    public Donation insert(Donation donation) {
        String sql = "INSERT INTO donations (donor_id, category_id, quantity, quantity_remaining, "
                + "description, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, donation.getDonorId());
            statement.setInt(2, donation.getCategoryId());
            statement.setInt(3, donation.getQuantity());
            statement.setInt(4, donation.getQuantityRemaining());
            statement.setString(5, donation.getDescription());
            statement.setString(6, donation.getStatus().name());
            statement.setString(7, donation.getCreatedAt().toString());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    donation.setId(keys.getInt(1));
                }
            }
            return donation;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to insert donation.", e);
        }
    }

    /** Persists whatever the current quantityRemaining/status are — call after getState().claim()/close(). */
    public void updateState(Donation donation) {
        String sql = "UPDATE donations SET quantity_remaining = ?, status = ? WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, donation.getQuantityRemaining());
            statement.setString(2, donation.getStatus().name());
            statement.setInt(3, donation.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update donation state.", e);
        }
    }

    public Optional<Donation> findById(int id) {
        String sql = "SELECT * FROM donations WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to look up donation.", e);
        }
    }

    public List<Donation> findByCategory(int categoryId) {
        return query("SELECT * FROM donations WHERE category_id = ? ORDER BY created_at", categoryId);
    }

    public List<Donation> findByDonor(int donorId) {
        return query("SELECT * FROM donations WHERE donor_id = ? ORDER BY created_at DESC", donorId);
    }

    public List<Donation> findRequestable() {
        String sql = "SELECT * FROM donations WHERE status IN ('AVAILABLE','PARTIALLY_CLAIMED') ORDER BY created_at";
        List<Donation> donations = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                donations.add(mapRow(rs));
            }
            return donations;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list requestable donations.", e);
        }
    }

    public List<Donation> findAll() {
        return query("SELECT * FROM donations ORDER BY created_at DESC", null);
    }

    private List<Donation> query(String sql, Integer param) {
        List<Donation> donations = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (param != null) {
                statement.setInt(1, param);
            }
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    donations.add(mapRow(rs));
                }
            }
            return donations;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query donations.", e);
        }
    }

    private Donation mapRow(ResultSet rs) throws SQLException {
        return new Donation(
                rs.getInt("id"),
                rs.getInt("donor_id"),
                rs.getInt("category_id"),
                rs.getInt("quantity"),
                rs.getInt("quantity_remaining"),
                rs.getString("description"),
                DonationStatus.valueOf(rs.getString("status")),
                LocalDateTime.parse(rs.getString("created_at")));
    }
}
