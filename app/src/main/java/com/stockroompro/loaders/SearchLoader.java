package com.stockroompro.loaders;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.stockroompro.models.contracts.CategoryContract;
import com.stockroompro.utils.ContentProviderConfig;

/**
 * Created by bagach.alexandr on 03.04.15.
 */
public class SearchLoader extends CursorLoader {

    private String[] buildSelectionArgs;
    private int searchType;

    public SearchLoader(Context context, String[] selectionArgs, int searchType) {
        super(context);
        this.buildSelectionArgs = selectionArgs;
        this.searchType = searchType;
    }

    @Override
    public Cursor loadInBackground() {
        return getCursor();
    }

    private Cursor getCursor() {
        switch (searchType) {
            case SearchLoaderCallback.SEARCH_BY_USERS:
                return getContext()
                        .getContentResolver()
                        .query(Uri.withAppendedPath(ContentProviderConfig.BASE_CONTENT_URI,
                                CategoryContract.SEARCH_BY_USERS_SUGGEST_URI), null, null, buildSelectionArgs, null);

            case SearchLoaderCallback.SEARCH_BY_ADVERT:
                return getContext()
                        .getContentResolver()
                        .query(Uri.withAppendedPath(ContentProviderConfig.BASE_CONTENT_URI,
                                CategoryContract.SEARCH_BY_ADVERT_SUGGEST_URI), null, null, buildSelectionArgs, null);

            default:
                return null;
        }
    }
}
