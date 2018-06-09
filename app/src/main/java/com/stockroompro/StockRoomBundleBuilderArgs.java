package com.stockroompro;

/**
 * Created by alexsergienko on 18.03.15.
 */
public interface StockRoomBundleBuilderArgs {
    String PRICE_LOW = "PRICE_LOW";
    String PRICE_HIGH = "PRICE_HIGH";
    String ORDER_BY_PRICE_ASC = "ORDER_BY_PRICE_ASC";
    String ORDER_BY_PRICE_DESC = "ORDER_BY_PRICE_DESC";
    String ORDER_BY_LAST_ADDED = "ORDER_BY_LAST_ADDED";
    String ORDER_BY_RATING = "ORDER_BY_RATING";
    String FILTER_NEW = "FILTER_NEW";
    String FILTER_USED = "FILTER_USED";
    String FILTER_BUY = "FILTER_BUY";
    /**
     * Type of advertisement
     */
    String FILTER_SELL = "FILTER_SELL";
    /**
     * Type of prices
     */
    String FILTER_SALE = "FILTER_SALE";
    String FILTER_FREE = "FILTER_FREE";
    String FILTER_EXCHANGE = "FILTER_EXCHANGE";
    String FILTER_CITY = "FILTER_CITY";
    String FILTER_MORE_FIELDS = "FILTER_MORE_FIELDS";
}
