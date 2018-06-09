package com.stockroompro.api.models.requests;

/**
 * Created by user on 14.04.15.
 */

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
import com.stockroompro.models.contracts.MessageByAdvertContract;
import com.stockroompro.models.converters.MessageByAdvertContentValuesConverter;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.INTERLOCUTOR_UID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by user on 10.04.15.
 */
public class MessagesByAdvertRequest extends ButchUpdateArrayDatabaseRequest<ResponseItemHolder<Message>, MessageByAdvertContentValuesConverter> implements RequestWithNotifications {

    public MessagesByAdvertRequest(Context context, Bundle bundle) {
        super(context, MessageByAdvertContract.CONTENT_URI, MessageByAdvertContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder<ResponseItemHolder<Message>> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getMessagesByAdvert(getBundle().getLong(UID), getBundle().getLong(INTERLOCUTOR_UID), new RequestParams()
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .addParameter(DATE, date)
                .buildJSONWithPathParams(AppServerSpecs.MESSAGE_BY_ADVERT_PATH,
                        AppServerSpecs.USER_ID, getBundle().getLong(UID),
                        AppServerSpecs.INTERLOCUTOR_ID, getBundle().getLong(INTERLOCUTOR_UID)
                ));
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

