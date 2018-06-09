package com.stockroompro.models.contracts;

import android.net.Uri;

import com.stockroompro.models.columns.AdvertFiltersColumns;
import com.stockroompro.utils.ContentProviderHelper;

import static com.artjoker.core.database.constants.DatabaseTypes.INTEGER;

/**
 * Created by bagach.alexandr on 27.05.15.
 */
public interface AdvertFiltersContract extends AdvertFiltersColumns {
    String TABLE_NAME = ContentProviderHelper.tableName("advert_filters");
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + ID + " " + INTEGER + " NOT NULL, "
            + ADVERT_ID + " " + INTEGER + " NOT NULL, "
            + FILTER_VALUE_ID + " " + INTEGER + " NOT NULL);";

    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);
    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);
}
