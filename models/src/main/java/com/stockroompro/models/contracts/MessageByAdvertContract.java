package com.stockroompro.models.contracts;

import android.net.Uri;

import com.stockroompro.models.columns.MessageByAdvertColumns;
import com.stockroompro.utils.ContentProviderHelper;

import static com.artjoker.core.database.constants.DatabaseTypes.INTEGER;
import static com.artjoker.core.database.constants.DatabaseTypes.TEXT;

/**
 * Created by user on 14.04.15.
 */
public interface MessageByAdvertContract extends MessageByAdvertColumns {
    String TABLE_NAME = ContentProviderHelper.tableName("message_by_advert");

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + ID + " " + INTEGER + " NOT NULL, "
            + TITLE + " " + TEXT + " NOT NULL, "
            + PRICE + " " + TEXT + " , "
            + IMAGE + " " + TEXT + " , "
            + SENDER_ID + " " + INTEGER + " , "
            + USER_ID + " " + INTEGER + " , "
            + SENDER_NAME + " " + INTEGER + " , "
            + INTERLOCATOR_ID + " " + INTEGER + " NOT NULL, "
            + INTERLOCATOR_NAME + " " + TEXT + " , "
            + AD_ID + " " + INTEGER + " NOT NULL, "
            + RECIPIENT_ID + " " + INTEGER + " , "
            + NEW + " " + INTEGER + " , "
            + TYPE + " " + TEXT + " , "
            + MESSAGE_TEXT + " " + TEXT + " , "
            + DATE + " " + INTEGER + " NOT NULL, "
            + CURRENCY_ID + " " + INTEGER + " , "
            + PARENT_ID + " " + INTEGER + " , "
            + CREATED_AT + " " + INTEGER + " , "
            + UPDATED_AT + " " + INTEGER + " , "
            + STATUS + " " + INTEGER + " NOT NULL);";


    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);

    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);

    String SEARCH_MESSAGES_URI = "search_messages";
}
