package com.spl2.givematch.state.request;

import com.spl2.givematch.model.Request;
import com.spl2.givematch.model.RequestStatus;

public class CompletedState implements RequestState {

    @Override
    public RequestStatus getStatus() {
        return RequestStatus.COMPLETED;
    }

    @Override
    public void accept(Request request, int allocatedQuantity) {
        throw new IllegalStateException("Request #" + request.getId() + " is already completed.");
    }

    @Override
    public void reject(Request request) {
        throw new IllegalStateException("Request #" + request.getId() + " is already completed, it cannot be rejected.");
    }

    @Override
    public void complete(Request request) {
    }
}
