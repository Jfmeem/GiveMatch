package com.spl2.givematch.model;

import com.spl2.givematch.state.request.RequestState;
import com.spl2.givematch.state.request.RequestStateFactory;

import java.time.LocalDateTime;

public class Request {
    private int id;
    private int donationId;
    private int receiverId;
    private int quantityRequested;
    private int quantityAllocated;
    private RequestStatus status;
    private LocalDateTime createdAt;

    public Request() {
    }

    public Request(int id, int donationId, int receiverId, int quantityRequested,
                   int quantityAllocated, RequestStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.donationId = donationId;
        this.receiverId = receiverId;
        this.quantityRequested = quantityRequested;
        this.quantityAllocated = quantityAllocated;
        this.status = status;
        this.createdAt = createdAt;
    }

    public RequestState getState() {
        return RequestStateFactory.of(status);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDonationId() {
        return donationId;
    }

    public void setDonationId(int donationId) {
        this.donationId = donationId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(int quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public int getQuantityAllocated() {
        return quantityAllocated;
    }

    public void setQuantityAllocated(int quantityAllocated) {
        this.quantityAllocated = quantityAllocated;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
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
        return "Request#" + id + " qty=" + quantityRequested + " (" + status + ")";
    }
}
