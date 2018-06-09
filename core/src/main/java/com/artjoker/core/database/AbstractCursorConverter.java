package com.artjoker.core.database;

import android.database.Cursor;

/**
 * Created by alexsergienko on 23.03.15.
 */
public abstract class AbstractCursorConverter<T> implements CursorConverter<T> {
    private Cursor cursor;

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public boolean isValid() {
        return getCursor() != null && !getCursor().isClosed() && !getCursor().isBeforeFirst() && !getCursor().isAfterLast();
    }

}
