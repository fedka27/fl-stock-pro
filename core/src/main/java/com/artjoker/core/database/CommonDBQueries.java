package com.artjoker.core.database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import static android.provider.BaseColumns._ID;
import static com.artjoker.core.database.constants.ExtendedColumns.CREATED_AT;
import static com.artjoker.core.database.constants.ExtendedColumns.UPDATED_AT;

/**
 * Created by alexsergienko on 24.03.15.
 */
public class CommonDBQueries {
    private CommonDBQueries() {
    }

    ;

    public static final long getMaxUpdateDateUnix(Context context, Uri uri) {
        return getMaxLongValueByField(context, uri, UPDATED_AT);
    }

    public static final long getMaxCreateDateUnix(Context context, Uri uri) {
        return getMaxLongValueByField(context, uri, CREATED_AT);
    }

    public static final long getMaxId(Context context, Uri uri) {
        return getMaxLongValueByField(context, uri, _ID);
    }

    public static final long getMaxLongValueByField(Context context, Uri uri, String field) {
        long date = 0;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"MAX(" + field + ") as " + field}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                date = cursor.getLong(0);
            }
            cursor.close();
        }
        return date;
    }

    public static final long getMaxLongValueByField(Context context, Uri uri, String field, String selection, String [] args) {
        long date = 0;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"MAX(" + field + ") as " + field}, selection, args, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                date = cursor.getLong(0);
            }
            cursor.close();
        }
        return date;
    }
}
