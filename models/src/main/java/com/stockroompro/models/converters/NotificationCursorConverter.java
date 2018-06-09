package com.stockroompro.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.stockroompro.models.NotificationItem;
import com.stockroompro.models.contracts.NotificationContract;

/**
 * Created by user on 15.05.15.
 */
public class NotificationCursorConverter extends AbstractCursorConverter<NotificationItem> {
    private int COLUMN_INDEX_ID;
    private int COLUMN_INDEX_TEXT;
    private int COLUMN_INDEX_DATE;
    private int COLUMN_INDEX_USER_ID;
    private int COLUMN_INDEX_AD_ID;
    private int COLUMN_INDEX_TYPE;

    private int COLUMN_INDEX_USER_NAME;
    private int COLUMN_INDEX_USER_PHOTO;
    private int COLUMN_INDEX_RATING;
    private int COLUMN_INDEX_COMMENT_TEXT;
    private int COLUMN_INDEX_READ_STATUS;

    @Override
    public NotificationItem getObject() {
        NotificationItem notificationItem = null;
        if (isValid()) {
            notificationItem = new NotificationItem();
            notificationItem.setId(getCursor().getLong(COLUMN_INDEX_ID));
            notificationItem.setDate(getCursor().getLong(COLUMN_INDEX_DATE));
            notificationItem.setText(getCursor().getString(COLUMN_INDEX_TEXT));
            notificationItem.setUserId(getCursor().getLong(COLUMN_INDEX_USER_ID));
            notificationItem.setAdId(getCursor().getLong(COLUMN_INDEX_AD_ID));
            notificationItem.setType(getCursor().getInt(COLUMN_INDEX_TYPE));

            notificationItem.setUserName(getCursor().getString(COLUMN_INDEX_USER_NAME));
            notificationItem.setUserPhoto(getCursor().getString(COLUMN_INDEX_USER_PHOTO));
            notificationItem.setRating(getCursor().getInt(COLUMN_INDEX_RATING));
            notificationItem.setCommentText(getCursor().getString(COLUMN_INDEX_COMMENT_TEXT));
            notificationItem.setReadStatus(getCursor().getInt(COLUMN_INDEX_READ_STATUS));

        }
        return notificationItem;
    }

    public NotificationItem getObject(int position) {
        NotificationItem notificationItem = null;
        getCursor().moveToPosition(position);
        if (isValid()) {
            notificationItem = new NotificationItem();
            notificationItem.setId(getCursor().getLong(COLUMN_INDEX_ID));
            notificationItem.setDate(getCursor().getLong(COLUMN_INDEX_DATE));
            notificationItem.setText(getCursor().getString(COLUMN_INDEX_TEXT));
            notificationItem.setUserId(getCursor().getLong(COLUMN_INDEX_USER_ID));
            notificationItem.setAdId(getCursor().getLong(COLUMN_INDEX_AD_ID));
            notificationItem.setType(getCursor().getInt(COLUMN_INDEX_TYPE));

            notificationItem.setUserName(getCursor().getString(COLUMN_INDEX_USER_NAME));
            notificationItem.setUserPhoto(getCursor().getString(COLUMN_INDEX_USER_PHOTO));
            notificationItem.setRating(getCursor().getInt(COLUMN_INDEX_RATING));
            notificationItem.setCommentText(getCursor().getString(COLUMN_INDEX_COMMENT_TEXT));
            notificationItem.setReadStatus(getCursor().getInt(COLUMN_INDEX_READ_STATUS));
        }
        return notificationItem;
    }

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_ID = cursor.getColumnIndex(NotificationContract.ID);
            COLUMN_INDEX_TEXT = cursor.getColumnIndex(NotificationContract.TEXT);
            COLUMN_INDEX_DATE = cursor.getColumnIndex(NotificationContract.DATE);
            COLUMN_INDEX_USER_ID = cursor.getColumnIndex(NotificationContract.USER_ID);
            COLUMN_INDEX_AD_ID = cursor.getColumnIndex(NotificationContract.AD_ID);
            COLUMN_INDEX_TYPE = cursor.getColumnIndex(NotificationContract.TYPE);

            COLUMN_INDEX_USER_NAME = cursor.getColumnIndex(NotificationContract.USER_NAME);
            COLUMN_INDEX_USER_PHOTO = cursor.getColumnIndex(NotificationContract.USER_PHOTO);
            COLUMN_INDEX_RATING = cursor.getColumnIndex(NotificationContract.RATING);
            COLUMN_INDEX_COMMENT_TEXT = cursor.getColumnIndex(NotificationContract.COMMENT_TEXT);
            COLUMN_INDEX_READ_STATUS = cursor.getColumnIndex(NotificationContract.READ_STATUS);
        }
    }
}
