package com.artjoker.core.database;

import android.database.Cursor;

/**
 * Created by alexsergienko on 12.03.15.
 */
public interface CursorConverter<T> {

    void setCursor(Cursor cursor);

    T getObject();
}
