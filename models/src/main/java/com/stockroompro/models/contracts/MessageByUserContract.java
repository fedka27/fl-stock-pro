package com.stockroompro.models.contracts;

import android.net.Uri;

import com.stockroompro.models.columns.MessageByUserColumns;
import com.stockroompro.utils.ContentProviderHelper;

import static com.artjoker.core.database.constants.DatabaseTypes.INTEGER;
import static com.artjoker.core.database.constants.DatabaseTypes.TEXT;


/**
 * Created by user on 10.04.15.
 */
public interface MessageByUserContract extends MessageByUserColumns {
    String TABLE_NAME = ContentProviderHelper.tableName("message_by_user");

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + ID + " " + INTEGER + " NOT NULL, "
            + TITLE + " " + TEXT + " , "
            + SENDER_ID + " " + INTEGER + " NOT NULL, "
            + USER_ID + " " + INTEGER + " NOT NULL, "
            + AD_ID + " " + INTEGER + " , "
            + INTERLOCATOR_ID + " " + INTEGER + " NOT NULL, "
            + INTERLOCATOR_NAME + " " + TEXT + " , "
            + RECIPIENT_ID + " " + INTEGER + " NOT NULL, "
            + NEW + " " + INTEGER + " , "
            + PRICE + " " + INTEGER + " , "
            + TYPE + " " + TEXT + " , "
            + MESSAGE_TEXT + " " + TEXT + " , "
            + USER_NAME + " " + TEXT + " , "
            + DATE + " " + INTEGER + " , "
            + PARENT_ID + " " + INTEGER + " , "
            + CREATED_AT + " " + INTEGER + " , "
            + UPDATED_AT + " " + INTEGER + " , "
            + STATUS + " " + INTEGER + " NOT NULL);";


    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);

    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);

    String SEARCH_USER_URI = "search_user";
}
