package com.stockroompro.services;

import android.app.IntentService;
import android.content.Intent;

import com.stockroompro.api.models.requests.SelfDataRequest;
import com.stockroompro.loaders.LocalBroadcastRequestProcessor;
import com.stockroompro.models.PersonalData;

/**
 * Created by user on 04.06.15.
 */
public class UpdateMessagesAndNotificationsService extends IntentService {
    public UpdateMessagesAndNotificationsService() {
        super(UpdateMessagesAndNotificationsService.class.getCanonicalName());
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */


    public UpdateMessagesAndNotificationsService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LocalBroadcastRequestProcessor processor = new LocalBroadcastRequestProcessor(getApplicationContext());
        processUnreadCount(processor);
    }

    private void processUnreadCount(LocalBroadcastRequestProcessor processor) {
        processor.process(new SelfDataRequest(getApplicationContext(), PersonalData.getInstance(getApplicationContext()).getUserId()));
    }


}
