package com.artjoker.core;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by artjoker on 16.04.2015.
 */
public class BackgroundUtils {

    public static final String REQUEST_COUNT_CHANGED_ACTION = "REQUEST_COUNT_CHANGED_ACTION";
    public static final String REQUEST_COUNT_VALUE = "REQUEST_COUNT_VALUE";

    public static void requestsCountChanged(Context context, boolean increase) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(REQUEST_COUNT_CHANGED_ACTION)
                .putExtra(REQUEST_COUNT_VALUE, increase));
    }

}
