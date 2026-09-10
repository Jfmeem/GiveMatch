package com.spl2.givematch.state.request;

import com.spl2.givematch.model.Request;
import com.spl2.givematch.model.RequestStatus;

public class PendingState implements RequestState {

    @Override
    public RequestStatus getStatus() {
        return RequestStatus.PENDING;
    }

    @Override
    public void accept(Request request, int allocatedQuantity) {
        if (allocatedQuantity <= 0) {
            throw new IllegalArgumentException("Allocated quantity must be positive.");
        }
        request.setQuantityAllocated(allocatedQuantity);
        request.setStatus(RequestStatus.ACCEPTED);
    }

    @Override
    public void reject(Request request) {
        request.setQuantityAllocated(0);
        request.setStatus(RequestStatus.REJECTED);
    }

    @Override
    public void complete(Request request) {
        throw new IllegalStateException(
                "Request #" + request.getId() + " must be accepted before it can be completed.");
    }
}
