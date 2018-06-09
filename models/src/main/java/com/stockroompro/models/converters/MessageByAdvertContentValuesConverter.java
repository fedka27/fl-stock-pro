package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.stockroompro.models.Message;
import com.stockroompro.models.contracts.MessageByAdvertContract;
import com.stockroompro.models.contracts.MessageByUserContract;

/**
 * Created by user on 14.04.15.
 */
public class MessageByAdvertContentValuesConverter extends AbstractContentValuesConverter<Message> {
    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MessageByAdvertContract.ID, getObjectModel().getId());
        contentValues.put(MessageByAdvertContract.IMAGE, getObjectModel().getImage());
        contentValues.put(MessageByAdvertContract.TITLE, getObjectModel().getTitle());
        contentValues.put(MessageByAdvertContract.SENDER_ID, getObjectModel().getSenderId());
        contentValues.put(MessageByAdvertContract.AD_ID, getObjectModel().getAdId());
        contentValues.put(MessageByAdvertContract.RECIPIENT_ID, getObjectModel().getRecipientId());
        contentValues.put(MessageByAdvertContract.PARENT_ID, getObjectModel().getParentId());
        contentValues.put(MessageByAdvertContract.NEW, getObjectModel().getNewCount());
        contentValues.put(MessageByAdvertContract.TYPE, getObjectModel().getType());
        contentValues.put(MessageByAdvertContract.MESSAGE_TEXT, getObjectModel().getText());
        contentValues.put(MessageByAdvertContract.DATE, getObjectModel().getDate());
        contentValues.put(MessageByAdvertContract.STATUS, getObjectModel().getSendingStatus());
        contentValues.put(MessageByAdvertContract.PRICE, getObjectModel().getPrice());
        contentValues.put(MessageByAdvertContract.CURRENCY_ID, getObjectModel().getCurrencyId());
        contentValues.put(MessageByAdvertContract.CREATED_AT, getObjectModel().getCreatedAt());
        contentValues.put(MessageByAdvertContract.UPDATED_AT, getObjectModel().getUpdatedAt());
        contentValues.put(MessageByAdvertContract.INTERLOCATOR_ID, getObjectModel().getInterlocatorId());
        contentValues.put(MessageByAdvertContract.INTERLOCATOR_NAME, getObjectModel().getInterlocatorName());
        contentValues.put(MessageByAdvertContract.USER_ID, getObjectModel().getUserId());
        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(MessageByAdvertContract.ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getId())};
    }
}
