package com.stockroompro.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.stockroompro.models.Message;
import com.stockroompro.models.contracts.MessageByUserContract;

/**
 * Created by user on 10.04.15.
 */
public class MessageByUserCursorConverter extends AbstractCursorConverter<Message> {
    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_SENDER_ID;
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
    private int COLUMN_INDEX_USER_NAME;
    private int COLUMN_INDEX_USER_ID;
    private int COLUMN_INDEX_CREATED_AT;
    private int COLUMN_INDEX_UPDATED_AT;
    private int COLUMN_INDEX_INTERLOCUTOR_NAME;
    private int COLUMN_INDEX_INTERLOCUTOR_ID;

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_ID = cursor.getColumnIndex(MessageByUserContract.ID);
            COLUMN_INDEX_NATIVE_ID = cursor.getColumnIndex(MessageByUserContract._ID);
            COLUMN_INDEX_TITLE = cursor.getColumnIndex(MessageByUserContract.TITLE);
            COLUMN_INDEX_SENDER_ID = cursor.getColumnIndex(MessageByUserContract.SENDER_ID);
            COLUMN_INDEX_RECIPIENT_ID = cursor.getColumnIndex(MessageByUserContract.RECIPIENT_ID);
            COLUMN_INDEX_PARENT_ID = cursor.getColumnIndex(MessageByUserContract.PARENT_ID);
            COLUMN_INDEX_AD_ID = cursor.getColumnIndex(MessageByUserContract.AD_ID);
            COLUMN_INDEX_NEW = cursor.getColumnIndex(MessageByUserContract.NEW);
            COLUMN_INDEX_TYPE = cursor.getColumnIndex(MessageByUserContract.TYPE);
            COLUMN_INDEX_MESSAGE_TEXT = cursor.getColumnIndex(MessageByUserContract.MESSAGE_TEXT);
            COLUMN_INDEX_DATE = cursor.getColumnIndex(MessageByUserContract.DATE);
            COLUMN_INDEX_STATUS = cursor.getColumnIndex(MessageByUserContract.STATUS);
            COLUMN_INDEX_USER_NAME = cursor.getColumnIndex(MessageByUserContract.USER_NAME);
            COLUMN_INDEX_USER_ID = cursor.getColumnIndex(MessageByUserContract.USER_ID);
            COLUMN_INDEX_CREATED_AT = cursor.getColumnIndex(MessageByUserContract.CREATED_AT);
            COLUMN_INDEX_UPDATED_AT = cursor.getColumnIndex(MessageByUserContract.UPDATED_AT);
            COLUMN_INDEX_INTERLOCUTOR_NAME = cursor.getColumnIndex(MessageByUserContract.INTERLOCATOR_NAME);
            COLUMN_INDEX_INTERLOCUTOR_ID = cursor.getColumnIndex(MessageByUserContract.INTERLOCATOR_ID);
        }
    }

    @Override
    public Message getObject() {
        Message messagesByUser = null;
        if (isValid()) {
            messagesByUser = new Message();
            messagesByUser.setId(getCursor().getLong(COLUMN_INDEX_ID));
            messagesByUser.setAdId(getCursor().getLong(COLUMN_INDEX_AD_ID));
            messagesByUser.setDate(getCursor().getLong(COLUMN_INDEX_DATE));
            messagesByUser.setNewCount(getCursor().getLong(COLUMN_INDEX_NEW));
            messagesByUser.setRecipientId(getCursor().getLong(COLUMN_INDEX_RECIPIENT_ID));
            messagesByUser.setSenderId(getCursor().getLong(COLUMN_INDEX_SENDER_ID));
            messagesByUser.setSendingStatus(getCursor().getInt(COLUMN_INDEX_STATUS));
            messagesByUser.setText(getCursor().getString(COLUMN_INDEX_MESSAGE_TEXT));
            messagesByUser.setTitle(getCursor().getString(COLUMN_INDEX_TITLE));
            messagesByUser.setType(getCursor().getString(COLUMN_INDEX_TYPE));
            messagesByUser.setParentId(getCursor().getLong(COLUMN_INDEX_PARENT_ID));
            messagesByUser.setUserName(getCursor().getString(COLUMN_INDEX_USER_NAME));
            messagesByUser.setUserId(getCursor().getLong(COLUMN_INDEX_USER_ID));
            messagesByUser.setCreatedAt(getCursor().getLong(COLUMN_INDEX_CREATED_AT));
            messagesByUser.setUpdatedAt(getCursor().getLong(COLUMN_INDEX_UPDATED_AT));
            messagesByUser.setInterlocatorId(getCursor().getLong(COLUMN_INDEX_INTERLOCUTOR_ID));
            messagesByUser.setInterlocatorName(getCursor().getString(COLUMN_INDEX_INTERLOCUTOR_NAME));
        }
        return messagesByUser;

    }

    public long getSenderId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_SENDER_ID);
        }
        return 0;
    }

    public long getRecipientId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_RECIPIENT_ID);
        } else return 0;
    }

}
