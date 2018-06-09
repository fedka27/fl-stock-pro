package com.artjoker.core.database;

import android.database.Cursor;

/**
 * Created by alexsergienko on 12.03.15.
 */
public class CursorProcessor<T extends CursorConverter<R>, R> {
    private CursorConverter<R> converter;

    public CursorProcessor(Cursor cursor, Class<T> convertClass) throws IllegalAccessException, InstantiationException {
        this.converter = convertClass.newInstance();
        converter.setCursor(cursor);
    }

    public R getConvertedObject() {
        return converter.getObject();
    }

    public T getConverter() {
        return (T) converter;
    }
}
