package com.stockroompro.models.columns;

import com.artjoker.core.database.constants.ExtendedColumns;

/**
 * Created by bagach.alexandr on 24.03.15.
 */
public interface AdvertisementColumns extends ExtendedColumns {

    String TITLE = "title";
    String DESCRIPTION = "description";
    String USER_ID = "user_id";
    String CATEGORY_ID = "category_id";
    String CATEGORY_NAME = "category_name";
    String COUNTRY_ID = "country_id";
    String REGION_ID = "region_id";
    String CITY_ID = "city_id";
    String COUNTRY_NAME = "country_name";
    String REGION_NAME = "region_name";
    String CITY_NAME = "city_name";
    String USED = "used";
    String BARGAIN = "bargain";
    String RENEWAL_DATE = "renewal_date";
    String EXPIRY_DATE = "expiry_date";
    String PRICE = "price";
    String PRICE_TYPE = "price_type";
    String TYPE = "type";
    String FAVOURITE = "favourite";
    String PATH = "path";
    String ACTIVE = "active";
    String APPROVED = "approved";
    String EXPIRED = "expired";
    String PHOTO_URL = "photo_url";
    String CURRENCY_ID = "currency_id";
    /**
     * Used only in projection. Not have real field in database
     */
    String INDEPENDENT_PRICE = "independent_price";
    String CURRENCY = "currency";
    String PHONE = "phone";
    String FILTERS_NAMES = "filters_names";
}
