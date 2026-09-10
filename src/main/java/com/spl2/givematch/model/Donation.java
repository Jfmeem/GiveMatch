package com.spl2.givematch.model;

import com.spl2.givematch.state.donation.DonationState;
import com.spl2.givematch.state.donation.DonationStateFactory;

import java.time.LocalDateTime;

public class Donation {
    private int id;
    private int donorId;
    private int categoryId;
    private int quantity;
    private int quantityRemaining;
    private String description;
    private DonationStatus status;
    private LocalDateTime createdAt;

    public Donation() {
    }

    public Donation(int id, int donorId, int categoryId, int quantity, int quantityRemaining,
                    String description, DonationStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.donorId = donorId;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.quantityRemaining = quantityRemaining;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public DonationState getState() {
        return DonationStateFactory.of(status);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDonorId() {
        return donorId;
    }

    public void setDonorId(int donorId) {
        this.donorId = donorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantityRemaining() {
        return quantityRemaining;
    }

    public void setQuantityRemaining(int quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DonationStatus getStatus() {
        return status;
    }

    public void setStatus(DonationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return description + " (x" + quantityRemaining + " left, " + status + ")";
    }
}
