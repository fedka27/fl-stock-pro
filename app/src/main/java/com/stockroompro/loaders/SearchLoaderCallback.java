package com.stockroompro.loaders;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;

import com.stockroompro.adapters.AdvertSuggestionAdapter;
import com.stockroompro.api.models.requests.RequestParams;

/**
 * Created by artjoker on 14.04.2015.
 */
public class SearchLoaderCallback implements LoaderManager.LoaderCallbacks {

    public static final String QUERY_KEY = "query";
    public static final String CATEGORY_ID_KEY = "category_key";
    public static final String SEARCH_TYPE_ID_KEY = "search_type_key";
    public static final String SEARCH_BY_USERS_KEY = "search_by_users";
    public static final int SEARCH_BY_ADVERT = 1;
    public static final int SEARCH_BY_USERS = 2;

    private CursorAdapter suggestionAdapter;
    private Context context;

    public SearchLoaderCallback(CursorAdapter suggestionAdapter, Context context) {
        this.suggestionAdapter = suggestionAdapter;
        this.context = context;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.SEARCH_LOADER:
                if (args != null) {
                    String[] selectionArgs = new String[5];
                    selectionArgs[0] = args.getString(QUERY_KEY);
                    selectionArgs[1] = String.valueOf(args.getLong(RequestParams.ParamNames.COUNTRY_ID, 0));
                    selectionArgs[2] = String.valueOf(args.getLong(RequestParams.ParamNames.REGION_ID, 0));
                    selectionArgs[3] = String.valueOf(args.getLong(RequestParams.ParamNames.ADVERT_CITY_ID, 0));
                    if (args.containsKey(SEARCH_BY_USERS_KEY)) {
                        return new SearchLoader(context, selectionArgs, SEARCH_BY_USERS);
                    } else {
                        selectionArgs[4] = String.valueOf(args.getLong(CATEGORY_ID_KEY));
                        return new SearchLoader(context, selectionArgs, SEARCH_BY_ADVERT);
                    }
                } else {
                    return new SearchLoader(context, null, SEARCH_BY_ADVERT);
                }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LoadersId.SEARCH_LOADER:
                suggestionAdapter.changeCursor((Cursor) data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        switch (loader.getId()) {
            case LoadersId.SEARCH_LOADER:
                suggestionAdapter.changeCursor(null);
        }
    }
}
