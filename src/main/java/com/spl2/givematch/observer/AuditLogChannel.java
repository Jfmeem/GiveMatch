package com.spl2.givematch.observer;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class AuditLogChannel implements DonationObserver {

    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String logFilePath;

    public AuditLogChannel(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    @Override
    public void onDonationMatchedNeed(NotificationMessage message) {
        String line = String.format("[%s] MATCH donation#%d category#%d -> receiver#%d :: %s%n",
                message.getOccurredAt().format(TIMESTAMP),
                message.getDonationId(), message.getCategoryId(),
                message.getReceiverId(), message.getText());

        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write(line);
        } catch (IOException e) {
            System.err.println("AuditLogChannel: failed to write audit log: " + e.getMessage());
        }
    }
}
