package com.stockroompro.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.stockroompro.models.Message;
import com.stockroompro.models.contracts.ChatMessageContract;

/**
 * Created by user on 14.04.15.
 */
public class ChatMessageCursorConverter extends AbstractCursorConverter<Message> {
    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_MESSAGE_TEXT;
    private int COLUMN_INDEX_DATE;
    private int COLUMN_INDEX_RECIPIENT_ID;
    private int COLUMN_INDEX_SENDER_ID;
    private int COLUMN_INDEX_SEND_STATUS;
    private int COLUMN_INDEX_AD_ID;
    private int COLUMN_INDEX_UNSEND;
    private int COLUMN_INDEX_TEMP_ID;
    private int COLUMN_INDEX_CREATED_AT;
    private int COLUMN_INDEX_UPDATED_AT;

    @Override
    public Message getObject() {
        Message messages = null;
        if (isValid()) {
            messages = new Message();
            messages.setId(getCursor().getLong(COLUMN_INDEX_ID));
            messages.setDate(getCursor().getLong(COLUMN_INDEX_DATE));
            messages.setText(getCursor().getString(COLUMN_INDEX_MESSAGE_TEXT));
            messages.setRecipientId(getCursor().getInt(COLUMN_INDEX_RECIPIENT_ID));
            messages.setSenderId(getCursor().getInt(COLUMN_INDEX_SENDER_ID));
            messages.setSendingStatus(getCursor().getInt(COLUMN_INDEX_SEND_STATUS));
            messages.setAdId(getCursor().getInt(COLUMN_INDEX_AD_ID));
            messages.setUnsend(getCursor().getInt(COLUMN_INDEX_UNSEND));
            messages.setTempId(getCursor().getString(COLUMN_INDEX_TEMP_ID));
            messages.setCreatedAt(getCursor().getLong(COLUMN_INDEX_CREATED_AT));
            messages.setUpdatedAt(getCursor().getLong(COLUMN_INDEX_UPDATED_AT));
        }
        return messages;
    }

    public Message getObject(int position) {
        Message messages = null;
        getCursor().moveToPosition(position);
        if (isValid()) {
            messages = new Message();
            messages.setId(getCursor().getLong(COLUMN_INDEX_ID));
            messages.setDate(getCursor().getLong(COLUMN_INDEX_DATE));
            messages.setText(getCursor().getString(COLUMN_INDEX_MESSAGE_TEXT));
            messages.setRecipientId(getCursor().getInt(COLUMN_INDEX_RECIPIENT_ID));
            messages.setSenderId(getCursor().getInt(COLUMN_INDEX_SENDER_ID));
            messages.setSendingStatus(getCursor().getInt(COLUMN_INDEX_SEND_STATUS));
            messages.setAdId(getCursor().getInt(COLUMN_INDEX_AD_ID));
            messages.setUnsend(getCursor().getInt(COLUMN_INDEX_UNSEND));
            messages.setTempId(getCursor().getString(COLUMN_INDEX_TEMP_ID));
            messages.setCreatedAt(getCursor().getLong(COLUMN_INDEX_CREATED_AT));
            messages.setUpdatedAt(getCursor().getLong(COLUMN_INDEX_UPDATED_AT));
        }
        return messages;
    }


    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_ID = cursor.getColumnIndex(ChatMessageContract.ID);
            COLUMN_INDEX_MESSAGE_TEXT = cursor.getColumnIndex(ChatMessageContract.MESSAGE_TEXT);
            COLUMN_INDEX_DATE = cursor.getColumnIndex(ChatMessageContract.DATE);
            COLUMN_INDEX_RECIPIENT_ID = cursor.getColumnIndex(ChatMessageContract.RECIPIENT_ID);
            COLUMN_INDEX_SENDER_ID = cursor.getColumnIndex(ChatMessageContract.SENDER_ID);
            COLUMN_INDEX_SEND_STATUS = cursor.getColumnIndex(ChatMessageContract.SENDING_STATUS);
            COLUMN_INDEX_AD_ID = cursor.getColumnIndex(ChatMessageContract.AD_ID);
            COLUMN_INDEX_UNSEND = cursor.getColumnIndex(ChatMessageContract.UNSEND);
            COLUMN_INDEX_TEMP_ID = cursor.getColumnIndex(ChatMessageContract.TEMP_ID_FOR_UNSENDED);
            COLUMN_INDEX_CREATED_AT = cursor.getColumnIndex(ChatMessageContract.CREATED_AT);
            COLUMN_INDEX_UPDATED_AT = cursor.getColumnIndex(ChatMessageContract.UPDATED_AT);

        }
    }
}
