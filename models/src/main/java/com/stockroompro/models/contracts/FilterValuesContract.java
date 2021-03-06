package com.stockroompro.models.contracts;

import android.net.Uri;

import com.stockroompro.models.columns.FiltersColumns;
import com.stockroompro.utils.ContentProviderHelper;

import static com.artjoker.core.database.constants.DatabaseTypes.INTEGER;
import static com.artjoker.core.database.constants.DatabaseTypes.TEXT;

/**
 * Created by bagach.alexandr on 24.04.15.
 */
public interface FilterValuesContract extends FiltersColumns {
    String TABLE_NAME = ContentProviderHelper.tableName("filter_values");
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + ID + " " + INTEGER + " NOT NULL, "
            + NAME + " " + TEXT + " , "
            + FILTER_ID + " " + INTEGER + " , "
            + CREATED_AT + " " + INTEGER + " NOT NULL, "
            + UPDATED_AT + " " + INTEGER + " NOT NULL);";
    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);

    String VALUES_URI = "get_values";

    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);
}
