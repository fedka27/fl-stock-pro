package com.stockroompro.models.contracts;

import android.net.Uri;

import com.stockroompro.models.columns.NotificationColumns;
import com.stockroompro.utils.ContentProviderHelper;

import static com.artjoker.core.database.constants.DatabaseTypes.INTEGER;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public interface NotificationContract extends NotificationColumns {

    String TABLE_NAME = ContentProviderHelper.tableName("notifications");
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + ID + " " + INTEGER + " NOT NULL, "
            + TEXT + " " + TEXT + " , "
            + TYPE + " " + INTEGER + " NOT NULL, "
            + USER_ID + " " + INTEGER + " , "
            + AD_ID + " " + INTEGER + " , "
            + USER_NAME + " " + TEXT + " , "
            + USER_PHOTO + " " + TEXT + " , "
            + RATING + " " + INTEGER + " , "
            + COMMENT_TEXT + " " + TEXT + " , "
            + CREATED_AT + " " + INTEGER + " , "
            + READ_STATUS + " " + INTEGER + " DEFAULT 0, "
            + UPDATED_AT + " " + INTEGER + " , "
            + DATE + " " + INTEGER + " );";

    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);

    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);
    int STATUS_READ = 1;
    int STATUS_UNREAD = 0;
}
