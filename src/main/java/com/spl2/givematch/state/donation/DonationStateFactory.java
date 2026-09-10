package com.spl2.givematch.state.donation;

import com.spl2.givematch.model.DonationStatus;

public final class DonationStateFactory {

    private static final DonationState AVAILABLE = new AvailableState();
    private static final DonationState PARTIALLY_ALLOCATED = new PartiallyClaimedState();
    private static final DonationState FULLY_ALLOCATED = new FullyClaimedState();
    private static final DonationState COMPLETED = new ClosedState();

    private DonationStateFactory() {
    }

    public static DonationState of(DonationStatus status) {
        return switch (status) {
            case AVAILABLE -> AVAILABLE;
            case PARTIALLY_ALLOCATED -> PARTIALLY_ALLOCATED;
            case FULLY_ALLOCATED -> FULLY_ALLOCATED;
            case COMPLETED -> COMPLETED;
        };
    }
}