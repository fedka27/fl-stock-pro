package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.artjoker.tool.core.SystemHelper;
import com.stockroompro.models.Comment;
import com.stockroompro.models.contracts.CommentsContract;

public class CommentsContentValuesConverter extends AbstractContentValuesConverter<Comment> {

    //TODO: fixed created_at and updated_at
    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        Comment comment = getObjectModel();
        contentValues.put(CommentsContract.ID, comment.getId());
        contentValues.put(CommentsContract.ADVERTISEMENT_ID, comment.getAdvertId());
        contentValues.put(CommentsContract.DATE, comment.getDate());
        contentValues.put(CommentsContract.PARENT_ID, comment.getParentId());
        contentValues.put(CommentsContract.SENDER_ID, comment.getSenderId());
        contentValues.put(CommentsContract.TEXT, comment.getText());
        contentValues.put(CommentsContract.CREATED_AT, comment.getCreatedAt());
        contentValues.put(CommentsContract.UPDATED_AT, comment.getUpdatedAt());
        contentValues.put(CommentsContract.SENDER_NAME, comment.getSenderName());
        contentValues.put(CommentsContract.LIKES_COUNT, comment.getLikes());
        contentValues.put(CommentsContract.DISLIKES_COUNT, comment.getDislikes());
        contentValues.put(CommentsContract.USER_CHOICE, comment.getUserChoice());
        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(CommentsContract.ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getId())};
    }

}
