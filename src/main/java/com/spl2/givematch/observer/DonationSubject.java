package com.spl2.givematch.observer;

public interface DonationSubject {

    void addObserver(DonationObserver observer);

    void removeObserver(DonationObserver observer);

    void notifyObservers(NotificationMessage message);
}