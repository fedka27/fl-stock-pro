package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.artjoker.core.network.ButchUpdateArrayDatabaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.ResponseItemHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.Comment;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.contracts.CommentsContract;
import com.stockroompro.models.converters.CommentsContentValuesConverter;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;

/**
 * Created by bagach.alexandr on 03.04.15.
 */
public class AdvertCommentsRequest extends ButchUpdateArrayDatabaseRequest<ResponseItemHolder<Comment>, CommentsContentValuesConverter> implements RequestWithNotifications {

    public AdvertCommentsRequest(Context context, Bundle args) {
        super(context, CommentsContract.CONTENT_URI, CommentsContentValuesConverter.class, args);
    }

    @Override
    public ResponseHolder<ResponseItemHolder<Comment>> makeRequest(long date) throws Exception {
        return Communicator.getAppServer().getComments(getBundle().getLong(AD_ID), new RequestParams()
                .addParameter(RequestParams.ParamNames.TOKEN, PersonalData.getInstance(getContext()).getToken())
                .buildJSONWithPathParams(AppServerSpecs.COMMENTS_PATH, AppServerSpecs.ADD_ID, getBundle().getLong(AD_ID)));
    }

    @Override
    public void processResponse(ResponseHolder<ResponseItemHolder<Comment>> responseHolder) throws Exception {
        super.processResponse(responseHolder);
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING | NotificationsPolicy.NOTIFY_SUCCESS | NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }


    @Override
    public int getRequestName() {
        return R.string.request_get_comments;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
