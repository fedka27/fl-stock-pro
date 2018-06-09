package com.artjoker.core.network;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by alexsergienko on 12.03.15.
 */
public interface NotificationsPolicy {

    public static final int NOTIFY_ALL = 0xFFFFFFFF;
    public static final int DO_NOT_NOTIFY_ALL = 0;
    public static final int NOTIFY_IF_IS_NOT_VALID = 1;
    public static final int NOTIFY_IF_NO_INTERNET_CONNECTION = 1 << 1;
    public static final int NOTIFY_IF_ERROR_REQUEST = 1 << 2;
    public static final int NOTIFY_SUCCESS = 1 << 3;
    public static final int NOTIFY_IF_ERROR_PROCESSING = 1 << 4;
    public static final int NOTIFY_IF_SERVER_ERROR_RESPONSE = 1 << 5;
    public static final int SHOW_TOAST = 1;
    public static final int OPEN_AUTH_WINDOW = 1 << 1;
    public static final int SHOW_INTERNET_CONNECTION_LOST = 1 << 6;

    @IntDef({NOTIFY_ALL, NOTIFY_SUCCESS, NOTIFY_IF_ERROR_REQUEST, NOTIFY_IF_ERROR_PROCESSING, NOTIFY_IF_NO_INTERNET_CONNECTION, NOTIFY_IF_IS_NOT_VALID, NOTIFY_IF_SERVER_ERROR_RESPONSE, DO_NOT_NOTIFY_ALL, SHOW_INTERNET_CONNECTION_LOST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkRequestPolicy {
    }

    @IntDef({NOTIFY_ALL, DO_NOT_NOTIFY_ALL, SHOW_TOAST, OPEN_AUTH_WINDOW, SHOW_INTERNET_CONNECTION_LOST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AuthPolicy {
    }

}
