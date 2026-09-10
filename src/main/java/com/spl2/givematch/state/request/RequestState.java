package com.spl2.givematch.state.request;

import com.spl2.givematch.model.Request;
import com.spl2.givematch.model.RequestStatus;

public interface RequestState {

    RequestStatus getStatus();

    void accept(Request request, int allocatedQuantity);

    void reject(Request request);

    void complete(Request request);
}
