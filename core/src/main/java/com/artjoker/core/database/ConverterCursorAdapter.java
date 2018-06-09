package com.artjoker.core.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.MergeCursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by alexsergienko on 12.03.15.
 */
public abstract class ConverterCursorAdapter<T extends CursorConverter> extends CursorAdapter {

    private final Class<T> converterClass;
    private T converter;

    public ConverterCursorAdapter(Context context, Cursor c, Class<T> converterClass) {
        super(context, c, 0);
        this.converterClass = converterClass;
    }

    public ConverterCursorAdapter(Context context, Cursor c, int flags, Class<T> converterClass) {
        super(context, c, flags);
        this.converterClass = converterClass;
    }

    @Deprecated
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return newView(context, cursor, getCursorConverter(cursor), parent);
    }

    @Deprecated
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        bindView(view, context, cursor, getCursorConverter(cursor));
    }

    public abstract View newView(Context context, Cursor cursor, T cursorConverter, ViewGroup parent);

    public abstract void bindView(View view, Context context, Cursor cursor, T cursorConverter);

    protected T getCursorConverter(Cursor cursor) {
        try {
            if (converter == null) {
                converter = converterClass.newInstance();
                converter.setCursor(cursor);
            }
            return converter;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public T getItem(int position) {
        return getCursorConverter((Cursor) super.getItem(position));
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        if (converter != null) {
            converter.setCursor(cursor);
        }
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        if (converter != null) {
            converter.setCursor(newCursor);
        }
        return super.swapCursor(newCursor);
    }

    @Override
    protected void onContentChanged() {
        super.onContentChanged();
        if (converter != null) {
            converter.setCursor(getCursor());
        }
    }
}
