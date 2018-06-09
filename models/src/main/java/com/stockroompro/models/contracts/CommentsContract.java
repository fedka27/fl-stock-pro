package com.stockroompro.models.contracts;

import android.net.Uri;

import com.stockroompro.models.columns.CommentsColumns;
import com.stockroompro.utils.ContentProviderHelper;

import static com.artjoker.core.database.constants.DatabaseTypes.INTEGER;
import static com.artjoker.core.database.constants.DatabaseTypes.TEXT;

/**
 * Created by bagach.alexandr on 24.03.15.
 */
public interface CommentsContract extends CommentsColumns{

    String TABLE_NAME = ContentProviderHelper.tableName("comments");
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + ID + " " + INTEGER + " NOT NULL, "
            + ADVERTISEMENT_ID + " " + INTEGER + " NOT NULL, "
            + SENDER_ID + " " + INTEGER + " NOT NULL, "
            + RECEIVER_ID + " " + INTEGER + ", "
            + PARENT_ID + " " + INTEGER + " NOT NULL, "
            + TEXT + " " + TEXT + " NOT NULL, "
            + SENDER_NAME + " " + TEXT + " NOT NULL, "
            + DATE + " " + INTEGER + " NOT NULL, "
            + LIKES_COUNT + " " + INTEGER + " NOT NULL DEFAULT 0, "
            + DISLIKES_COUNT + " " + INTEGER + " NOT NULL DEFAULT 0, "
            + USER_CHOICE + " " + INTEGER + " NOT NULL DEFAULT 0, "
            + CREATED_AT + " " + INTEGER + " NOT NULL, "
            + UPDATED_AT + " " + INTEGER + " NOT NULL);";
    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);

    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);
}
