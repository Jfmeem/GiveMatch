package com.spl2.givematch.state.donation;

import com.spl2.givematch.model.Donation;
import com.spl2.givematch.model.DonationStatus;

public interface DonationState {
    DonationStatus getStatus();

    void claim(Donation donation, int quantity);

    void close(Donation donation);

    boolean canBeRequested();
}
