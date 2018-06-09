package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.stockroompro.models.Message;
import com.stockroompro.models.contracts.ChatMessageContract;

/**
 * Created by user on 17.04.15.
 */
public class ChatMessagesContentValuesConverter extends AbstractContentValuesConverter<Message> {
    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ChatMessageContract.ID, getObjectModel().getId());
        contentValues.put(ChatMessageContract.SENDING_STATUS, getObjectModel().getSendingStatus());
        contentValues.put(ChatMessageContract.MESSAGE_TEXT, getObjectModel().getText());
        contentValues.put(ChatMessageContract.DATE, getObjectModel().getDate());
        contentValues.put(ChatMessageContract.RECIPIENT_ID, getObjectModel().getRecipientId());
        contentValues.put(ChatMessageContract.SENDER_ID, getObjectModel().getSenderId());
        contentValues.put(ChatMessageContract.AD_ID, getObjectModel().getAdId());
        contentValues.put(ChatMessageContract.CREATED_AT, getObjectModel().getCreatedAt());
        contentValues.put(ChatMessageContract.UPDATED_AT, getObjectModel().getUpdatedAt());
        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatMessageContract.ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getId())};
    }
}
