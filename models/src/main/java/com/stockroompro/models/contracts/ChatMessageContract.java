package com.stockroompro.models.contracts;

import android.net.Uri;

import com.stockroompro.models.columns.ChatMessageColumns;
import com.stockroompro.utils.ContentProviderHelper;

import static com.artjoker.core.database.constants.DatabaseTypes.INTEGER;
import static com.artjoker.core.database.constants.DatabaseTypes.TEXT;

/**
 * Created by user on 14.04.15.
 */
public interface ChatMessageContract extends ChatMessageColumns {
    String TABLE_NAME = ContentProviderHelper.tableName("chat_messages");

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + ID + " " + INTEGER + " , "
            + TEMP_ID_FOR_UNSENDED + " " + TEXT + " , "
            + MESSAGE_TEXT + " " + TEXT + " , "
            + AD_ID + " " + INTEGER + " , "
            + RECIPIENT_ID + " " + INTEGER + " , "
            + DATE + " " + INTEGER + " , "
            + UNSEND + " " + INTEGER + " NOT NULL DEFAULT 0, "
            + CREATED_AT + " " + INTEGER + " , "
            + UPDATED_AT + " " + INTEGER + " , "
            + SENDING_STATUS + " " + INTEGER + " NOT NULL DEFAULT 0, "
            + SENDER_ID + " " + INTEGER + " );";

    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);

    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);
}
