package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.stockroompro.models.Message;
import com.stockroompro.models.contracts.MessageByUserContract;

/**
 * Created by user on 10.04.15.
 */
public class MessageByUserContentValuesConverter extends AbstractContentValuesConverter<Message> {
    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MessageByUserContract.ID, getObjectModel().getUserId());
        contentValues.put(MessageByUserContract.TITLE, getObjectModel().getTitle());
        contentValues.put(MessageByUserContract.SENDER_ID, getObjectModel().getSenderId());
        contentValues.put(MessageByUserContract.AD_ID, getObjectModel().getAdId());
        contentValues.put(MessageByUserContract.RECIPIENT_ID, getObjectModel().getRecipientId());
        contentValues.put(MessageByUserContract.PARENT_ID, getObjectModel().getParentId());
        contentValues.put(MessageByUserContract.NEW, getObjectModel().getNewCount());
        contentValues.put(MessageByUserContract.TYPE, getObjectModel().getType());
        contentValues.put(MessageByUserContract.MESSAGE_TEXT, getObjectModel().getText());
        contentValues.put(MessageByUserContract.DATE, getObjectModel().getDate());
        contentValues.put(MessageByUserContract.STATUS, getObjectModel().getSendingStatus());
        contentValues.put(MessageByUserContract.USER_NAME, getObjectModel().getUserName());
        contentValues.put(MessageByUserContract.USER_ID, getObjectModel().getUserId());
        contentValues.put(MessageByUserContract.CREATED_AT, getObjectModel().getCreatedAt());
        contentValues.put(MessageByUserContract.UPDATED_AT, getObjectModel().getUpdatedAt());
        contentValues.put(MessageByUserContract.INTERLOCATOR_ID, getObjectModel().getInterlocatorId());
        contentValues.put(MessageByUserContract.INTERLOCATOR_NAME, getObjectModel().getInterlocatorName());

        return contentValues;
    }

    @Override
    public String getUpdateWhere() {

        StringBuilder builder = new StringBuilder();
        builder.append(MessageByUserContract.INTERLOCATOR_ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getInterlocatorId())};
    }
}
