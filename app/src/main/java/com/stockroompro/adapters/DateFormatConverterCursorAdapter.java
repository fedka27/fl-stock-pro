package com.stockroompro.adapters;

import android.content.Context;
import android.database.Cursor;

import com.artjoker.core.database.ConverterCursorAdapter;
import com.artjoker.core.database.CursorConverter;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by user on 28.04.15.
 */
public abstract class DateFormatConverterCursorAdapter<T extends CursorConverter> extends ConverterCursorAdapter<T> {
    public final SimpleDateFormat simpleDateFormat;

    public static final String DATE_FORMAT = "H:mm, d MMM yyyy";

    {
        simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    }

    public DateFormatConverterCursorAdapter(Context context, Cursor c, Class<T> converterClass) {
        super(context, c, converterClass);
    }
}
