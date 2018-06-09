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

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.RECIPIENT_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TEXT;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;

/**
 * Created by user on 15.04.15.
 */
public class SendMessageRequest extends BaseRequest implements RequestWithNotifications {
    public SendMessageRequest(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    public ResponseHolder makeRequest() throws Exception {
        return Communicator.getAppServer().sendMessage(new RequestParams()
                .addParameter(AD_ID, getBundle().getLong(AD_ID))
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .addParameter(RECIPIENT_ID, getBundle().getLong(RECIPIENT_ID))
                .addParameter(TEXT, getBundle().getString(TEXT))
                .buildJSON(AppServerSpecs.MESSAGES_PATH));
    }

    @Override
    public void processResponse(ResponseHolder responseHolder) throws Exception {
    }

    @Override
    public int getRequestName() {
        return R.string.request_send_message_status;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE | NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING| NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
