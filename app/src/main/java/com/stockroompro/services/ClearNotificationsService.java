package com.stockroompro.services;

import android.app.IntentService;
import android.content.Intent;

import com.stockroompro.models.contracts.NotificationContract;

/**
 * Created by user on 26.05.15.
 */

public class ClearNotificationsService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ClearNotificationsService(String name) {
        super(name);
    }

    public ClearNotificationsService() {
        super(ClearFavouritesService.class.getCanonicalName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getContentResolver().delete(NotificationContract.CONTENT_URI, null, null);
    }
}
