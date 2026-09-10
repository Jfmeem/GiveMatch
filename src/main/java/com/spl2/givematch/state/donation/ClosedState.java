package com.spl2.givematch.state.donation;

import com.spl2.givematch.model.Donation;
import com.spl2.givematch.model.DonationStatus;

public class ClosedState implements DonationState {

    @Override
    public DonationStatus getStatus() {
        return DonationStatus.CLOSED;
    }

    @Override
    public void claim(Donation donation, int quantity) {
        throw new IllegalStateException("Donation #" + donation.getId() + " is closed.");
    }

    @Override
    public void close(Donation donation) {
    }

    @Override
    public boolean canBeRequested() {
        return false;
    }
}
