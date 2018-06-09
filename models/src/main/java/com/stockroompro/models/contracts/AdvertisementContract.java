package com.stockroompro.models.contracts;

import android.net.Uri;

import com.stockroompro.models.columns.AdvertisementColumns;
import com.stockroompro.utils.ContentProviderHelper;

import static com.artjoker.core.database.constants.DatabaseTypes.INTEGER;
import static com.artjoker.core.database.constants.DatabaseTypes.REAL;
import static com.artjoker.core.database.constants.DatabaseTypes.TEXT;

/**
 * Created by bagach.alexandr on 24.03.15.
 */
public interface AdvertisementContract extends AdvertisementColumns {
    String TABLE_NAME = ContentProviderHelper.tableName("advertisement");
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + ID + " " + INTEGER + " NOT NULL, "
            + TITLE + " " + TEXT + " NOT NULL, "
            + DESCRIPTION + " " + TEXT + " NOT NULL, "
            + USER_ID + " " + INTEGER + " NOT NULL, "
            + CATEGORY_ID + " " + INTEGER + " NOT NULL, "
            + CATEGORY_NAME + " " + TEXT + " , "
            + COUNTRY_ID + " " + INTEGER + " , "
            + REGION_ID + " " + INTEGER + " , "
            + CITY_ID + " " + INTEGER + " , "
            + COUNTRY_NAME + " " + TEXT + " , "
            + REGION_NAME + " " + TEXT + " , "
            + CITY_NAME + " " + TEXT + " , "
            + USED + " " + INTEGER + " , "
            + BARGAIN + " " + INTEGER + " , "
            + RENEWAL_DATE + " " + INTEGER + " NOT NULL, "
            + EXPIRY_DATE + " " + INTEGER + " , "
            + PRICE + " " + REAL + " NOT NULL, "
            + PRICE_TYPE + " " + TEXT + " NOT NULL, "
            + TYPE + " " + INTEGER + " , "
            + ACTIVE + " " + INTEGER + " DEFAULT -1, "
            + APPROVED + " " + INTEGER + " , "
            + EXPIRED + " " + INTEGER + " , "
            + FAVOURITE + " " + INTEGER + " DEFAULT 0, "
            + PHOTO_URL + " " + TEXT + " , "
            + CURRENCY_ID + " " + INTEGER + " , "
            + PHONE + " " + TEXT + " , "
            + FILTERS_NAMES + " " + TEXT + " , "
            + PATH + " " + TEXT + " , "
            + CREATED_AT + " " + INTEGER + " NOT NULL, "
            + UPDATED_AT + " " + INTEGER + " NOT NULL);";
    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);

    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);

    int TYPE_NEW = 0;
    int TYPE_USED = 1;
    String SEARCH_ADVERT_URI = "search_advert";
}
