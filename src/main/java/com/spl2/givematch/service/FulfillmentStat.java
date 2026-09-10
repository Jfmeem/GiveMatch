package com.spl2.givematch.service;

public class FulfillmentStat {

    private final String label;
    private final int totalRequested;
    private final int totalDistributed;

    public FulfillmentStat(String label, int totalRequested, int totalDistributed) {
        this.label = label;
        this.totalRequested = totalRequested;
        this.totalDistributed = totalDistributed;
    }

    public String getLabel() {
        return label;
    }

    public int getTotalRequested() {
        return totalRequested;
    }

    public int getTotalDistributed() {
        return totalDistributed;
    }

    public double getFulfillmentRatePercent() {
        return totalRequested == 0 ? 0.0 : (100.0 * totalDistributed / totalRequested);
    }
}
