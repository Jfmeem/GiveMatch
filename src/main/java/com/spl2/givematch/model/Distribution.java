package com.spl2.givematch.model;

import java.time.LocalDateTime;

public class Distribution {
    private int id;
    private int requestId;
    private int quantityDistributed;
    private LocalDateTime distributedAt;

    public Distribution() {
    }

    public Distribution(int id, int requestId, int quantityDistributed, LocalDateTime distributedAt) {
        this.id = id;
        this.requestId = requestId;
        this.quantityDistributed = quantityDistributed;
        this.distributedAt = distributedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getQuantityDistributed() {
        return quantityDistributed;
    }

    public void setQuantityDistributed(int quantityDistributed) {
        this.quantityDistributed = quantityDistributed;
    }

    public LocalDateTime getDistributedAt() {
        return distributedAt;
    }

    public void setDistributedAt(LocalDateTime distributedAt) {
        this.distributedAt = distributedAt;
    }
}
