package com.stockroompro.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.stockroompro.models.Comment;
import com.stockroompro.models.contracts.CommentsContract;

/**
 * Created by alexsergienko on 23.03.15.
 */
public class CommentCursorConverter extends AbstractCursorConverter<Comment> {

    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_NATIVE_ID;
    private int COLUMN_INDEX_ADVERT_ID;
    private int COLUMN_INDEX_TEXT;
    private int COLUMN_INDEX_PARENT_ID;
    private int COLUMN_INDEX_DATE;
    private int COLUMN_INDEX_SENDER_ID;
    private int COLUMN_INDEX_SENDER_NAME;
    private int COLUMN_INDEX_LIKES;
    private int COLUMN_INDEX_DISLIKES;
    private int COLUMN_INDEX_USER_CHOICE;
    private int COLUMN_INDEX_CREATED_AT;
    private int COLUMN_INDEX_UPDATED_AT;

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_ID = cursor.getColumnIndex(CommentsContract.ID);
            COLUMN_INDEX_NATIVE_ID = cursor.getColumnIndex(CommentsContract._ID);
            COLUMN_INDEX_ADVERT_ID = cursor.getColumnIndex(CommentsContract.ADVERTISEMENT_ID);
            COLUMN_INDEX_TEXT = cursor.getColumnIndex(CommentsContract.TEXT);
            COLUMN_INDEX_PARENT_ID = cursor.getColumnIndex(CommentsContract.PARENT_ID);
            COLUMN_INDEX_DATE = cursor.getColumnIndex(CommentsContract.DATE);
            COLUMN_INDEX_SENDER_ID = cursor.getColumnIndex(CommentsContract.SENDER_ID);
            COLUMN_INDEX_SENDER_NAME = cursor.getColumnIndex(CommentsContract.SENDER_NAME);
            COLUMN_INDEX_LIKES = cursor.getColumnIndex(CommentsContract.LIKES_COUNT);
            COLUMN_INDEX_DISLIKES = cursor.getColumnIndex(CommentsContract.DISLIKES_COUNT);
            COLUMN_INDEX_USER_CHOICE = cursor.getColumnIndex(CommentsContract.USER_CHOICE);
            COLUMN_INDEX_CREATED_AT = cursor.getColumnIndex(CommentsContract.CREATED_AT);
            COLUMN_INDEX_UPDATED_AT = cursor.getColumnIndex(CommentsContract.UPDATED_AT);
        }
    }

    @Override
    public Comment getObject() {
        Comment comment = null;
        if (isValid()) {
            comment = new Comment();
            comment.setId(getCursor().getLong(COLUMN_INDEX_ID));
            comment.setAdvertId(getCursor().getLong(COLUMN_INDEX_ADVERT_ID));
            comment.setText(getCursor().getString(COLUMN_INDEX_TEXT));
            comment.setDate(getCursor().getLong(COLUMN_INDEX_DATE));
            comment.setParentId(getCursor().getLong(COLUMN_INDEX_PARENT_ID));
            comment.setSenderId(getCursor().getLong(COLUMN_INDEX_SENDER_ID));
            comment.setSenderName(getCursor().getString(COLUMN_INDEX_SENDER_NAME));
            comment.setLikes(getCursor().getInt(COLUMN_INDEX_LIKES));
            comment.setDislikes(getCursor().getInt(COLUMN_INDEX_DISLIKES));
            comment.setUserChoice(getCursor().getInt(COLUMN_INDEX_USER_CHOICE));
            comment.setCreatedAt(getCursor().getLong(COLUMN_INDEX_CREATED_AT));
            comment.setUpdatedAt(getCursor().getLong(COLUMN_INDEX_UPDATED_AT));
        }
        return comment;
    }
}
