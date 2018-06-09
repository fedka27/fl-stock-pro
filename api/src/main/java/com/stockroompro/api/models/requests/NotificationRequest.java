package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.TimeUtils;

import com.artjoker.core.network.ButchUpdateArrayDatabaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.tool.core.SystemHelper;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.NotificationItem;
import com.stockroompro.models.contracts.NotificationContract;
import com.stockroompro.models.converters.NotificationContentValuesConverter;

import java.util.ArrayList;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;

/**
 * Created by user on 15.05.15.
 */
public class NotificationRequest extends ButchUpdateArrayDatabaseRequest<ArrayList<NotificationItem>, NotificationContentValuesConverter> implements RequestWithNotifications {
    public NotificationRequest(Context context, Bundle bundle) {
        super(context, NotificationContract.CONTENT_URI, NotificationContentValuesConverter.class, bundle);
    }

    public static final long TWO_WEEKS_IN_SEC = 2 * DateUtils.WEEK_IN_MILLIS;

    @Override
    public ResponseHolder<ArrayList<NotificationItem>> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getNotifications(new RequestParams()
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .addParameter(DATE, SystemHelper.getInstance().getTimeInSecFromMillis(System.currentTimeMillis()) - TWO_WEEKS_IN_SEC)
                .buildJSON(AppServerSpecs.NOTIFICATION_PATH));
    }

    @Override
    public void processResponse(ResponseHolder<ArrayList<NotificationItem>> responseHolder) throws Exception {
        super.processResponse(responseHolder);


    }

    @Override
    public int getRequestName() {
        return R.string.request_get_notification;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.DO_NOT_NOTIFY_ALL;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
