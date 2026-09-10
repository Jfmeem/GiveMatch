package com.spl2.givematch.service;

public class CategoryDemand {

    private final String categoryName;
    private final int openNeedCount;
    private final int totalQuantityNeeded;

    public CategoryDemand(String categoryName, int openNeedCount, int totalQuantityNeeded) {
        this.categoryName = categoryName;
        this.openNeedCount = openNeedCount;
        this.totalQuantityNeeded = totalQuantityNeeded;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getOpenNeedCount() {
        return openNeedCount;
    }

    public int getTotalQuantityNeeded() {
        return totalQuantityNeeded;
    }
}
