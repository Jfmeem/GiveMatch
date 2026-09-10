package com.spl2.givematch.observer;

import java.util.*;

public class InAppNotificationChannel implements DonationObserver {

    private final Map<Integer, List<NotificationMessage>> inboxByReceiver = new HashMap<>();

    @Override
    public void onDonationMatchedNeed(NotificationMessage message) {
        inboxByReceiver
                .computeIfAbsent(message.getReceiverId(), id -> new ArrayList<>())
                .add(message);
    }

    public List<NotificationMessage> getInbox(int receiverId) {
        return Collections.unmodifiableList(
                inboxByReceiver.getOrDefault(receiverId, Collections.emptyList()));
    }

    public void clearInbox(int receiverId) {
        inboxByReceiver.remove(receiverId);
    }
}
