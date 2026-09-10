package com.spl2.givematch.observer;

import java.util.ArrayList;
import java.util.List;

public class DonationNotificationCenter implements DonationSubject {

    private final List<DonationObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(DonationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(DonationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(NotificationMessage message) {
        for (DonationObserver observer : observers) {
            observer.onDonationMatchedNeed(message);
        }
    }
}
