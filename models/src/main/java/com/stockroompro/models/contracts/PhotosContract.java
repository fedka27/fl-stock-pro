package com.stockroompro.models.contracts;

import android.net.Uri;

import com.stockroompro.models.columns.PhotosColumns;
import com.stockroompro.utils.ContentProviderHelper;

import static com.artjoker.core.database.constants.DatabaseTypes.INTEGER;
import static com.artjoker.core.database.constants.DatabaseTypes.TEXT;

/**
 * Created by bagach.alexandr on 24.03.15.
 */
public interface PhotosContract extends PhotosColumns {

    String TABLE_NAME = ContentProviderHelper.tableName("photos");
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + ADVERTISEMENT_ID + " " + INTEGER + " NOT NULL, "
            + PHOTO_URL + " " + TEXT + " NOT NULL, "
            + CREATED_AT + " " + INTEGER + " NOT NULL, "
            + UPDATED_AT + " " + INTEGER + " NOT NULL);";
    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);

    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);
}
