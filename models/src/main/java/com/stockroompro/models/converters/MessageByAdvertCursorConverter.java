package com.stockroompro.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.stockroompro.models.Message;
import com.stockroompro.models.contracts.MessageByAdvertContract;
import com.stockroompro.models.contracts.MessageByUserContract;

/**
 * Created by user on 10.04.15.
 */
public class MessageByAdvertCursorConverter extends AbstractCursorConverter<Message> {
    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_SENDER_ID;
    private int COLUMN_INDEX_SENDER_NAME;
    private int COLUMN_INDEX_NATIVE_ID;
    private int COLUMN_INDEX_RECIPIENT_ID;
    private int COLUMN_INDEX_PARENT_ID;
    private int COLUMN_INDEX_AD_ID;
    private int COLUMN_INDEX_NEW;
    private int COLUMN_INDEX_TITLE;
    private int COLUMN_INDEX_TYPE;
    private int COLUMN_INDEX_MESSAGE_TEXT;
    private int COLUMN_INDEX_DATE;
    private int COLUMN_INDEX_STATUS;
    private int COLUMN_INDEX_PRICE;
    private int COLUMN_INDEX_CURRENCY_ID;
    private int COLUMN_INDEX_IMAGE;
    private int COLUMN_INDEX_CREATED_AT;
    private int COLUMN_INDEX_UPDATED_AT;
    private int COLUMN_INDEX_INTERLOCUTOR_NAME;
    private int COLUMN_INDEX_INTERLOCUTOR_ID;
    private int COLUMN_INDEX_USER_ID;

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_ID = cursor.getColumnIndex(MessageByAdvertContract.ID);
            COLUMN_INDEX_NATIVE_ID = cursor.getColumnIndex(MessageByAdvertContract._ID);
            COLUMN_INDEX_TITLE = cursor.getColumnIndex(MessageByAdvertContract.TITLE);
            COLUMN_INDEX_SENDER_ID = cursor.getColumnIndex(MessageByAdvertContract.SENDER_ID);
            COLUMN_INDEX_SENDER_NAME = cursor.getColumnIndex(MessageByAdvertContract.SENDER_NAME);
            COLUMN_INDEX_RECIPIENT_ID = cursor.getColumnIndex(MessageByAdvertContract.RECIPIENT_ID);
            COLUMN_INDEX_PARENT_ID = cursor.getColumnIndex(MessageByAdvertContract.PARENT_ID);
            COLUMN_INDEX_AD_ID = cursor.getColumnIndex(MessageByAdvertContract.AD_ID);
            COLUMN_INDEX_NEW = cursor.getColumnIndex(MessageByAdvertContract.NEW);
            COLUMN_INDEX_TYPE = cursor.getColumnIndex(MessageByAdvertContract.TYPE);
            COLUMN_INDEX_MESSAGE_TEXT = cursor.getColumnIndex(MessageByAdvertContract.MESSAGE_TEXT);
            COLUMN_INDEX_DATE = cursor.getColumnIndex(MessageByAdvertContract.DATE);
            COLUMN_INDEX_STATUS = cursor.getColumnIndex(MessageByAdvertContract.STATUS);
            COLUMN_INDEX_PRICE = cursor.getColumnIndex(MessageByAdvertContract.PRICE);
            COLUMN_INDEX_CURRENCY_ID = cursor.getColumnIndex(MessageByAdvertContract.CURRENCY_ID);
            COLUMN_INDEX_IMAGE = cursor.getColumnIndex(MessageByAdvertContract.IMAGE);
            COLUMN_INDEX_CREATED_AT = cursor.getColumnIndex(MessageByAdvertContract.CREATED_AT);
            COLUMN_INDEX_UPDATED_AT = cursor.getColumnIndex(MessageByAdvertContract.UPDATED_AT);
            COLUMN_INDEX_INTERLOCUTOR_NAME = cursor.getColumnIndex(MessageByUserContract.INTERLOCATOR_NAME);
            COLUMN_INDEX_INTERLOCUTOR_ID = cursor.getColumnIndex(MessageByUserContract.INTERLOCATOR_ID);
            COLUMN_INDEX_USER_ID = cursor.getColumnIndex(MessageByUserContract.USER_ID);
        }
    }

    @Override
    public Message getObject() {
        Message messages = null;
        if (isValid()) {
            messages = new Message();
            messages.setId(getCursor().getLong(COLUMN_INDEX_ID));
            messages.setAdId(getCursor().getLong(COLUMN_INDEX_AD_ID));
            messages.setDate(getCursor().getLong(COLUMN_INDEX_DATE));
            messages.setNewCount(getCursor().getLong(COLUMN_INDEX_NEW));
            messages.setRecipientId(getCursor().getLong(COLUMN_INDEX_RECIPIENT_ID));
            messages.setSenderId(getCursor().getLong(COLUMN_INDEX_SENDER_ID));
            messages.setSendingStatus(getCursor().getInt(COLUMN_INDEX_STATUS));
            messages.setText(getCursor().getString(COLUMN_INDEX_MESSAGE_TEXT));
            messages.setTitle(getCursor().getString(COLUMN_INDEX_TITLE));
            messages.setType(getCursor().getString(COLUMN_INDEX_TYPE));
            messages.setParentId(getCursor().getLong(COLUMN_INDEX_PARENT_ID));
            messages.setPrice(getCursor().getString(COLUMN_INDEX_PRICE));
            messages.setSenderName(getCursor().getString(COLUMN_INDEX_SENDER_NAME));
            messages.setCurrencyId(getCursor().getInt(COLUMN_INDEX_CURRENCY_ID));
            messages.setImage(getCursor().getString(COLUMN_INDEX_IMAGE));
            messages.setCreatedAt(getCursor().getLong(COLUMN_INDEX_CREATED_AT));
            messages.setUpdatedAt(getCursor().getLong(COLUMN_INDEX_UPDATED_AT));
            messages.setInterlocatorId(getCursor().getLong(COLUMN_INDEX_INTERLOCUTOR_ID));
            messages.setInterlocatorName(getCursor().getString(COLUMN_INDEX_INTERLOCUTOR_NAME));
            messages.setUserId(getCursor().getLong(COLUMN_INDEX_USER_ID));

        }
        return messages;

    }

    public long getAvertId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_AD_ID);
        }
        return 0;
    }

    public long getRecipientId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_RECIPIENT_ID);
        }
        return 0;
    }

    public long getSenderId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_SENDER_ID);
        }
        return 0;
    }

    public String getSenderName() {
        if (isValid()) {
            return getCursor().getString(COLUMN_INDEX_SENDER_NAME);
        }
        return null;
    }
}
