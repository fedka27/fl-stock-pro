package com.stockroompro.models;

import com.artjoker.core.network.ResponseHolder;

/**
 * Created by user on 15.05.15.
 */
public class PushModel extends ResponseHolder<NotificationItem> {
    public final NotificationItem getData() {
        return data;
    }
}
