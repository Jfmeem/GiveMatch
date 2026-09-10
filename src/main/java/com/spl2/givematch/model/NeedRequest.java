package com.spl2.givematch.model;

import java.time.LocalDateTime;

public class NeedRequest {
    private int id;
    private int receiverId;
    private int categoryId;
    private int quantityNeeded;
    private NeedRequestStatus status;
    private LocalDateTime createdAt;

    public NeedRequest() {
    }

    public NeedRequest(int id, int receiverId, int categoryId, int quantityNeeded,
                       NeedRequestStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.receiverId = receiverId;
        this.categoryId = categoryId;
        this.quantityNeeded = quantityNeeded;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getQuantityNeeded() {
        return quantityNeeded;
    }

    public void setQuantityNeeded(int quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }

    public NeedRequestStatus getStatus() {
        return status;
    }

    public void setStatus(NeedRequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
