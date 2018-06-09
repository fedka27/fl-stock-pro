package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.ButchUpdateArrayDatabaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.ResponseItemHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.Message;
import com.stockroompro.models.contracts.MessageByUserContract;
import com.stockroompro.models.converters.MessageByUserContentValuesConverter;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;


public class MessagesByUserRequest extends ButchUpdateArrayDatabaseRequest<ResponseItemHolder<Message>, MessageByUserContentValuesConverter> implements RequestWithNotifications {

    public MessagesByUserRequest(Context context, Bundle bundle) {
        super(context, MessageByUserContract.CONTENT_URI, MessageByUserContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ResponseItemHolder<Message>> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getMessagesByUsers(getBundle().getLong(UID), new RequestParams()
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .buildJSONWithPathParams(AppServerSpecs.MESSAGES_BY_USER_PATH, AppServerSpecs.USER_ID, getBundle().getLong(UID)));
    }

    @Override
    public void processResponse(ResponseHolder<ResponseItemHolder<Message>> responseHolder) throws Exception {
        super.processResponse(responseHolder);

    }

    @Override
    public int getRequestName() {
        return R.string.request_messages_by_user;
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
