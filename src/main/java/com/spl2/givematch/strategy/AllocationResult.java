package com.spl2.givematch.strategy;

import com.spl2.givematch.model.Request;

public class AllocationResult {

    private final Request request;
    private final int quantityGranted;

    public AllocationResult(Request request, int quantityGranted) {
        this.request = request;
        this.quantityGranted = quantityGranted;
    }

    public Request getRequest() {
        return request;
    }

    public int getQuantityGranted() {
        return quantityGranted;
    }

    public boolean isGranted() {
        return quantityGranted > 0;
    }
}

