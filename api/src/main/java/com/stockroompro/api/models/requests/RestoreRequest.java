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

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.EMAIL;
/**
 * Created by user on 01.04.15.
 */
public class RestoreRequest extends BaseRequest implements RequestWithNotifications {

    public RestoreRequest(Context context, Bundle bundle) {
        super(context,bundle);

    }

    @Override
    public ResponseHolder makeRequest() throws Exception {
        return Communicator.getAppServer().passwordRecovery(new RequestParams()
                .addParameter(EMAIL,getBundle().getString(EMAIL))
                .buildJSON(AppServerSpecs.PASSWORD_RECOVERY_PATH));
    }

    @Override
    public void processResponse(ResponseHolder responseHolder) throws Exception {

    }

    @Override
    public int getRequestName() {
        return R.string.request_restore;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE | NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING | NotificationsPolicy.NOTIFY_SUCCESS| NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
