package com.spl2.givematch.state.request;

import com.spl2.givematch.model.Request;
import com.spl2.givematch.model.RequestStatus;

public class AcceptedState implements RequestState {

    @Override
    public RequestStatus getStatus() {
        return RequestStatus.ACCEPTED;
    }

    @Override
    public void accept(Request request, int allocatedQuantity) {
        throw new IllegalStateException("Request #" + request.getId() + " is already accepted.");
    }

    @Override
    public void reject(Request request) {
        throw new IllegalStateException(
                "Request #" + request.getId() + " is already accepted and cannot be rejected.");
    }

    @Override
    public void complete(Request request) {
        request.setStatus(RequestStatus.COMPLETED);
    }
}
