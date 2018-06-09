package com.artjoker.core.database;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by alexsergienko on 12.03.15.
 */
public abstract class PreNetworkCursorLoader extends CursorLoader {

    public PreNetworkCursorLoader(Context context) {
        super(context);
    }

    public PreNetworkCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Cursor loadInBackground() {
        if (isNeedLoadFromNetwork()) {
            loadFromNetwork();
        }
        return super.loadInBackground();
    }

    /**
     * This method will processed in background thread
     */
    protected abstract void loadFromNetwork();

    /**
     * This method will processed in background thread
     */
    protected abstract boolean isNeedLoadFromNetwork();

    protected Cursor loadCursor() {
        return super.loadInBackground();
    }
}
