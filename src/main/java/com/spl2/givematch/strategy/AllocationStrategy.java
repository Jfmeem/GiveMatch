package com.spl2.givematch.strategy;

import com.spl2.givematch.model.Request;

import java.util.List;
import java.util.Map;

public interface AllocationStrategy {

    /**
     * @param quantityAvailable how much of the donation is left to hand out
     * @param pendingRequests   every PENDING request currently competing for this donation
     * @param receiverPriorities receiverId -> priority level, higher served first (may be empty)
     * @return one AllocationResult per input request, same order not guaranteed
     */
    List<AllocationResult> allocate(int quantityAvailable, List<Request> pendingRequests, Map<Integer, Integer> receiverPriorities);

    /** Human-readable name shown in the Admin dashboard's strategy picker. */
    String getDisplayName();
}

