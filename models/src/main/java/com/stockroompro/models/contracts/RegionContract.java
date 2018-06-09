package com.stockroompro.models.contracts;

import android.net.Uri;

import com.stockroompro.models.columns.RegionColumns;
import com.stockroompro.utils.ContentProviderHelper;

import static com.artjoker.core.database.constants.DatabaseTypes.INTEGER;
import static com.artjoker.core.database.constants.DatabaseTypes.TEXT;

/**
 * Created by bagach.alexandr on 05.05.15.
 */
public interface RegionContract extends RegionColumns {
    String TABLE_NAME = ContentProviderHelper.tableName("region");
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + ID + " " + INTEGER + " NOT NULL, "
            + NAME + " " + TEXT + " , "
            + COUNTRY_ID + " " + INTEGER + " , "
            + CREATED_AT + " " + INTEGER + " , "
            + UPDATED_AT + " " + INTEGER + " );";
    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);

    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);
}
