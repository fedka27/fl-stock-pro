package com.stockroompro.models.contracts;

import android.net.Uri;

import com.stockroompro.models.columns.CategoryColumns;
import com.stockroompro.utils.ContentProviderHelper;

import static com.artjoker.core.database.constants.DatabaseTypes.INTEGER;
import static com.artjoker.core.database.constants.DatabaseTypes.TEXT;

public interface CategoryContract extends CategoryColumns {
    String TABLE_NAME = ContentProviderHelper.tableName("category");
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + ID + " " + INTEGER + " NOT NULL, "
            + NAME + " " + TEXT + " NOT NULL, "
            + SYSTEM_NAME + " " + TEXT + " NOT NULL, "
            + DESCRIPTION + " " + INTEGER + " NOT NULL, "
            + IMAGE_URL + " " + TEXT + " , "
            + CREATED_AT + " " + INTEGER + " , "
            + UPDATED_AT + " " + INTEGER + " , "
            + PARENT_ID + " " + INTEGER + " );";
    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);

    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);

    String SEARCH_BY_ADVERT_SUGGEST_URI = "advert_search_suggest_query";
    String SEARCH_BY_USERS_SUGGEST_URI = "user_search_suggest_query";

    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);
}