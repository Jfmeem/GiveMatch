package com.spl2.givematch.strategy;

import com.spl2.givematch.model.Request;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PriorityBasedAllocation implements AllocationStrategy {

    @Override
    public List<AllocationResult> allocate(int quantityAvailable, List<Request> pendingRequests,
                                           Map<Integer, Integer> receiverPriorities) {
        List<Request> ordered = new ArrayList<>(pendingRequests);
        ordered.sort(Comparator
                .comparing((Request r) -> receiverPriorities.getOrDefault(r.getReceiverId(), 0))
                .reversed()
                .thenComparing(Request::getCreatedAt)
                .thenComparing(Request::getId));

        List<AllocationResult> results = new ArrayList<>();
        int remaining = quantityAvailable;
        for (Request request : ordered) {
            int granted = Math.min(remaining, request.getQuantityRequested());
            results.add(new AllocationResult(request, granted));
            remaining -= granted;
        }
        return results;
    }

    @Override
    public String getDisplayName() {
        return "Priority-Based";
    }
}
