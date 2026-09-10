package com.spl2.givematch.observer;

import java.time.LocalDateTime;

public class NotificationMessage {

    private final int receiverId;
    private final int donationId;
    private final int categoryId;
    private final String text;
    private final LocalDateTime occurredAt;

    public NotificationMessage(int receiverId, int donationId, int categoryId, String text) {
        this.receiverId = receiverId;
        this.donationId = donationId;
        this.categoryId = categoryId;
        this.text = text;
        this.occurredAt = LocalDateTime.now();
    }

    public int getReceiverId() {
        return receiverId;
    }

    public int getDonationId() {
        return donationId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
