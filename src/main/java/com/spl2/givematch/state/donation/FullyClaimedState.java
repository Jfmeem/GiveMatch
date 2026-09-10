package com.spl2.givematch.state.donation;

import com.spl2.givematch.model.Donation;
import com.spl2.givematch.model.DonationStatus;

public class FullyClaimedState implements DonationState {

    @Override
    public DonationStatus getStatus() {
        return DonationStatus.FULLY_CLAIMED;
    }

    @Override
    public void claim(Donation donation, int quantity) {
        throw new IllegalStateException("Donation #" + donation.getId()
                + " is fully claimed — nothing left to give.");
    }

    @Override
    public void close(Donation donation) {
        donation.setStatus(DonationStatus.CLOSED);
    }

    @Override
    public boolean canBeRequested() {
        return false;
    }
}