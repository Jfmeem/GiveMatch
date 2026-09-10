package com.spl2.givematch.strategy;

public final class AllocationStrategyFactory {

    public enum Type {
        FIFO,
        PRIORITY,
        PROPORTIONAL
    }

    private AllocationStrategyFactory() {
    }

    public static AllocationStrategy create(Type type) {
        return switch (type) {
            case FIFO -> new FIFOAllocation();
            case PRIORITY -> new PriorityBasedAllocation();
            case PROPORTIONAL -> new ProportionalAllocation();
        };
    }
}
