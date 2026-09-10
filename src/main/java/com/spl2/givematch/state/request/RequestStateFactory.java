package com.spl2.givematch.state.request;

import com.spl2.givematch.model.RequestStatus;

public final class RequestStateFactory {

    private static final RequestState PENDING = new PendingState();
    private static final RequestState ACCEPTED = new AcceptedState();
    private static final RequestState REJECTED = new RejectedState();
    private static final RequestState COMPLETED = new CompletedState();

    private RequestStateFactory() {
    }

    public static RequestState of(RequestStatus status) {
        return switch (status) {
            case PENDING -> PENDING;
            case ACCEPTED -> ACCEPTED;
            case REJECTED -> REJECTED;
            case COMPLETED -> COMPLETED;
        };
    }
}
