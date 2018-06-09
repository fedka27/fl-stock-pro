package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.GCM_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.USER_ID;

/**
 * Created by user on 30.04.15.
 */
public class UnsubscribeGCMRequest extends BaseRequest implements RequestWithNotifications {
    public UnsubscribeGCMRequest(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    public ResponseHolder makeRequest() throws Exception {
        return Communicator.getAppServer().unsubscribeGCM(new RequestParams()
                .addParameter(USER_ID, getBundle().getLong(USER_ID))
                .addParameter(GCM_ID, getBundle().getString(GCM_ID))
                .buildJSON(AppServerSpecs.SUBSCRIBE_GCM_PATH));
    }

    @Override
    public void processResponse(ResponseHolder responseHolder) throws Exception {

    }

    @Override
    public int getRequestName() {
        return R.string.request_unsubscribe_gcm;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE | NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
