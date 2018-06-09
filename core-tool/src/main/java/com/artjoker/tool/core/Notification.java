package com.artjoker.tool.core;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public final class Notification {
    private static final Notification INSTANCE = new Notification();

    public static Notification getInstance() {
        return INSTANCE;
    }

    public final void show(final Context context, @StringRes final int resourceId) {
        show(context, context.getString(resourceId));
    }

    public final void show(final Context context, final String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}