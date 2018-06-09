package com.stockroompro.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.stockroompro.api.models.requests.FiltersRequest;
import com.stockroompro.api.models.requests.SettingsRequest;
import com.stockroompro.loaders.LocalBroadcastRequestProcessor;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;

/**
 * Created by bagach.alexandr on 05.05.15.
 */
public class UpdateService extends IntentService {

    private static final String NAME = "update_service";
    private static final int DEFAULT_CATEGORY_ID = 0;

    public UpdateService() {
        super(NAME);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle args = new Bundle();
        LocalBroadcastRequestProcessor processor = new LocalBroadcastRequestProcessor(getApplicationContext());
        processor.process(new SettingsRequest(getApplicationContext()));

        args.putLong(CATEGORY_ID, DEFAULT_CATEGORY_ID);
        processor.process(new FiltersRequest(getApplicationContext(), args));
    }
}
