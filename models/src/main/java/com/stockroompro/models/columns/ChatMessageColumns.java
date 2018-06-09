package com.stockroompro.models.columns;

import com.artjoker.core.database.constants.ExtendedColumns;

/**
 * Created by user on 14.04.15.
 */
public interface ChatMessageColumns extends ExtendedColumns {
    String MESSAGE_TEXT = "message_text";
    String DATE = "date";
    String SENDER_ID = "sender_id";
    String RECIPIENT_ID = "recipient_id";
    String SENDING_STATUS = "status";
    String AD_ID = "ad_id";
    String UNSEND = "unsend";
    String TEMP_ID_FOR_UNSENDED = "temp_id_only_for_unsended";
}
