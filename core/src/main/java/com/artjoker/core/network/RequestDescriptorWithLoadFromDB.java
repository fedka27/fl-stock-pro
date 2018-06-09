package com.artjoker.core.network;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by bagach.alexandr on 12.05.15.
 */
public class RequestDescriptorWithLoadFromDB extends CursorLoader {

    private RequestDescriptor requestDescriptor;
    private RequestProcessor requestProcessor;

    public RequestDescriptorWithLoadFromDB(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs,
                                           String sortOrder, RequestDescriptor requestDescriptor, RequestProcessor requestProcessor) {

        super(context, uri, projection, selection, selectionArgs, sortOrder);
        this.requestDescriptor = requestDescriptor;
        this.requestProcessor = requestProcessor;
    }

    @Override
    public Cursor loadInBackground() {
        loadFromNetwork();
        return super.loadInBackground();
    }

    private void loadFromNetwork() {
        requestProcessor.process(requestDescriptor);
    }
}
