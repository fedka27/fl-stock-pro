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
import com.stockroompro.models.PersonalData;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ACTION;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COMMENT_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;

public class AddCommentLikeRequest extends BaseRequest implements RequestWithNotifications {


    public AddCommentLikeRequest(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    public ResponseHolder makeRequest() throws Exception {
        return Communicator.getAppServer().setLike(getBundle().getLong(COMMENT_ID),
                new RequestParams()
                        .addParameter(TOKEN, getBundle().getString(TOKEN))
                        .addParameter(ACTION, getBundle().getInt(ACTION))
                        .buildJSONWithPathParams(AppServerSpecs.LIKE_COMMENTS_PATH, AppServerSpecs.COMMENT_ID, getBundle().getLong(COMMENT_ID)));
    }

    @Override
    public void processResponse(ResponseHolder responseHolder) throws Exception {

    }

    @Override
    public int getRequestName() {
        return R.string.request_like_comment;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING | NotificationsPolicy.NOTIFY_SUCCESS | NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }

    @Override
    public boolean isValid() {
        return PersonalData.getInstance(getContext()).getToken() != null;
    }
}
