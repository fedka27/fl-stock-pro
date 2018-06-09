package com.stockroompro.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import com.stockroompro.models.contracts.AdvertisementContract;

/**
 * Created by artjoker on 19.05.2015.
 */
public class ClearFavouritesService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ClearFavouritesService(String name) {
        super(name);
    }

    public ClearFavouritesService() {
        super(ClearFavouritesService.class.getCanonicalName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AdvertisementContract.FAVOURITE, 0);
        if (getContentResolver().query(AdvertisementContract.CONTENT_URI, null, null, null, null).getCount() > 0) {
            getContentResolver().update(AdvertisementContract.CONTENT_URI, contentValues, "1=1", null);
        }
    }
}
