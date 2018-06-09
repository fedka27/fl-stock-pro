package com.stockroompro.models.columns;

import com.artjoker.core.database.constants.ExtendedColumns;

/**
 * Created by bagach.alexandr on 24.03.15.
 */
public interface CommentsColumns extends ExtendedColumns {

    String ADVERTISEMENT_ID = "ad_id";
    String SENDER_ID = "sender_id";
    String RECEIVER_ID = "receiver_id";
    String TEXT = "text";
    String SENDER_NAME = "sender_name";
    String DATE = "date";
    String LIKES_COUNT = "likes_count";
    String DISLIKES_COUNT = "dislikes_count";
    String USER_CHOICE = "user_choice";
}
