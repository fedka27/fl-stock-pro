package com.stockroompro.loaders;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.artjoker.core.database.PreNetworkCursorLoader;
import com.stockroompro.api.models.requests.SubCategoriesRequest;

/**
 * Created by bagach.alexandr on 22.04.15.
 */
public class GetCategoriesForSpinnerLoader extends PreNetworkCursorLoader {

    private Context context;
    private Bundle bundle;

    public GetCategoriesForSpinnerLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, Bundle bundle) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
        this.context = context;
        this.bundle = bundle;
    }

    @Override
    protected void loadFromNetwork() {
        LocalBroadcastRequestProcessor localBroadcastRequestProcessor = new LocalBroadcastRequestProcessor(context);
        localBroadcastRequestProcessor.process(new SubCategoriesRequest(context, bundle));
    }

    @Override
    protected boolean isNeedLoadFromNetwork() {
        return true;
    }
}
