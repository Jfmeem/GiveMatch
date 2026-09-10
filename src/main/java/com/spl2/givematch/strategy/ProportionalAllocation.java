package com.spl2.givematch.strategy;

import com.spl2.givematch.model.Request;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ProportionalAllocation implements AllocationStrategy {

    @Override
    public List<AllocationResult> allocate(int quantityAvailable, List<Request> pendingRequests,
                                           Map<Integer, Integer> receiverPriorities) {
        List<Request> ordered = new ArrayList<>(pendingRequests);
        long totalRequested = ordered.stream().mapToLong(Request::getQuantityRequested).sum();

        List<AllocationResult> results = new ArrayList<>();
        if (totalRequested == 0) {
            return results;
        }

        if (totalRequested <= quantityAvailable) {
            for (Request request : ordered) {
                results.add(new AllocationResult(request, request.getQuantityRequested()));
            }
            return results;
        }

        int[] baseShare = new int[ordered.size()];
        double[] remainder = new double[ordered.size()];
        int distributed = 0;

        for (int i = 0; i < ordered.size(); i++) {
            double exact = (quantityAvailable * (double) ordered.get(i).getQuantityRequested()) / totalRequested;
            baseShare[i] = (int) Math.floor(exact);
            remainder[i] = exact - baseShare[i];
            distributed += baseShare[i];
        }

        List<Integer> byRemainderDesc = new ArrayList<>();
        for (int i = 0; i < ordered.size(); i++) {
            byRemainderDesc.add(i);
        }
        byRemainderDesc.sort(Comparator.comparingDouble((Integer i) -> remainder[i]).reversed());

        int leftoverUnits = quantityAvailable - distributed;
        for (int i = 0; i < leftoverUnits && i < byRemainderDesc.size(); i++) {
            baseShare[byRemainderDesc.get(i)]++;
        }

        for (int i = 0; i < ordered.size(); i++) {
            int granted = Math.min(baseShare[i], ordered.get(i).getQuantityRequested());
            results.add(new AllocationResult(ordered.get(i), granted));
        }
        return results;
    }

    @Override
    public String getDisplayName() {
        return "Proportional";
    }
}
