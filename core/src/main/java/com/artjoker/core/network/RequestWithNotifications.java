package com.artjoker.core.network;

import com.artjoker.core.Validator;

/**
 * Created by alexsergienko on 12.03.15.
 */
public interface RequestWithNotifications extends Validator {

   // @NotificationsPolicy.NetworkRequestPolicy
    int getNotificationPolicy();
}
