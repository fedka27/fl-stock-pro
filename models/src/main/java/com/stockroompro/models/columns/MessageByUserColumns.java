package com.stockroompro.models.columns;

import com.artjoker.core.database.constants.ExtendedColumns;

/**
 * Created by user on 10.04.15.
 */
public interface MessageByUserColumns extends ExtendedColumns {
    String SENDER_ID = "sender_id";
    String INTERLOCATOR_NAME = "interlocator_name";
    String INTERLOCATOR_ID = "interlocator_id";

    String PARENT_ID = "parent_id";
    String AD_ID = "ad_id";
    String RECIPIENT_ID = "recipient_id";
    String NEW = "new";
    String TITLE = "title";
    String TYPE = "type";
    String MESSAGE_TEXT = "text";
    String DATE = "date";
    String STATUS = "status";
    String PRICE = "price";
    String USER_NAME = "user_name";
    String USER_ID = "user_id";

}
