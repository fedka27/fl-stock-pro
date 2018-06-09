package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.database.constants.ExtendedColumns;
import com.artjoker.core.network.AbstractContentValuesConverter;
import com.stockroompro.models.NotificationItem;
import com.stockroompro.models.contracts.NotificationContract;

/**
 * Created by user on 15.05.15.
 */
public class NotificationContentValuesConverter extends AbstractContentValuesConverter<NotificationItem> {
    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationContract.ID, getObjectModel().getId());
        contentValues.put(NotificationContract.DATE, getObjectModel().getDate());
        contentValues.put(NotificationContract.TEXT, getObjectModel().getText());
        contentValues.put(NotificationContract.USER_ID, getObjectModel().getUserId());
        contentValues.put(NotificationContract.AD_ID, getObjectModel().getAdId());
        contentValues.put(NotificationContract.TYPE, getObjectModel().getType());

        contentValues.put(NotificationContract.USER_NAME, getObjectModel().getUserName());
        contentValues.put(NotificationContract.USER_PHOTO, getObjectModel().getUserPhoto());
        contentValues.put(NotificationContract.RATING, getObjectModel().getRating());
        contentValues.put(NotificationContract.COMMENT_TEXT, getObjectModel().getCommentText());


        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(ExtendedColumns.ID);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{String.valueOf(getObjectModel().getId())};
    }
}
