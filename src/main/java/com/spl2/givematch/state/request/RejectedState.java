package com.spl2.givematch.state.request;

import com.spl2.givematch.model.Request;
import com.spl2.givematch.model.RequestStatus;

public class RejectedState implements RequestState {

    @Override
    public RequestStatus getStatus() {
        return RequestStatus.REJECTED;
    }

    @Override
    public void accept(Request request, int allocatedQuantity) {
        throw new IllegalStateException("Request #" + request.getId() + " was already rejected.");
    }

    @Override
    public void reject(Request request) {
    }

    @Override
    public void complete(Request request) {
        throw new IllegalStateException("Request #" + request.getId() + " was rejected, it cannot be completed.");
    }
}

