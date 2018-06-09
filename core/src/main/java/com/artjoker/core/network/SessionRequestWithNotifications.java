package com.artjoker.core.network;

/**
 * Created by alexsergienko on 12.03.15.
 */
public interface SessionRequestWithNotifications extends RequestWithNotifications {

    @NotificationsPolicy.AuthPolicy
    int getAuthNotificationPolicy();

    boolean isSessionValid();

}
