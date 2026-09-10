package com.spl2.givematch.state.donation;

import com.spl2.givematch.model.DonationStatus;

public final class DonationStateFactory {

    private static final DonationState AVAILABLE = new AvailableState();
    private static final DonationState PARTIALLY_CLAIMED = new PartiallyClaimedState();
    private static final DonationState FULLY_CLAIMED = new FullyClaimedState();
    private static final DonationState CLOSED = new ClosedState();

    private DonationStateFactory() {
    }

    public static DonationState of(DonationStatus status) {
        return switch (status) {
            case AVAILABLE -> AVAILABLE;
            case PARTIALLY_CLAIMED -> PARTIALLY_CLAIMED;
            case FULLY_CLAIMED -> FULLY_CLAIMED;
            case CLOSED -> CLOSED;
        };
    }
}