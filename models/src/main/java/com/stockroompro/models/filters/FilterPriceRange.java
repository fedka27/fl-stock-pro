package com.stockroompro.models.filters;

/**
 * Created by alexsergienko on 20.05.15.
 * Contains range of prices in USD currency
 */
public class FilterPriceRange {

    private static final double ROUNDING_OF_THE_PRICE = 1d;

    private double minValue;
    private double maxValue;
    private double currentMinValue;
    private double currentMaxValue;

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue + ROUNDING_OF_THE_PRICE;
    }

    public double getCurrentMinValue() {
        return currentMinValue;
    }

    public void setCurrentMinValue(double currentMinValue) {
        this.currentMinValue = currentMinValue;
    }

    public double getCurrentMaxValue() {
        return currentMaxValue;
    }

    public void setCurrentMaxValue(double currentMaxValue) {
        this.currentMaxValue = currentMaxValue;
    }

}
