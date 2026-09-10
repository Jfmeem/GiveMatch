package com.spl2.givematch.state.donation;

import com.spl2.givematch.model.Donation;
import com.spl2.givematch.model.DonationStatus;

public class PartiallyClaimedState implements DonationState {

    @Override
    public DonationStatus getStatus() {
        return DonationStatus.PARTIALLY_ALLOCATED;
    }

    @Override
    public void claim(Donation donation, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Claim quantity must be positive.");
        }
        if (quantity > donation.getQuantityRemaining()) {
            throw new IllegalArgumentException("Cannot claim more than what remains.");
        }
        donation.setQuantityRemaining(donation.getQuantityRemaining() - quantity);
        donation.setStatus(donation.getQuantityRemaining() == 0
                ? DonationStatus.FULLY_ALLOCATED
                : DonationStatus.PARTIALLY_ALLOCATED);
    }

    @Override
    public void close(Donation donation) {
        donation.setStatus(DonationStatus.COMPLETED);
    }

    @Override
    public boolean canBeRequested() {
        return true;
    }
}
