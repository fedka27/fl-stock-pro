package com.stockroompro.api.models.requests;

import android.content.Context;

import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.api.models.responses.user.UserData;
import com.stockroompro.models.PersonalData;

import java.util.HashSet;


public class SelfDataRequest extends BaseRequest<UserData> implements RequestWithNotifications {
    private long userId;

    public SelfDataRequest(Context context, long userId) {
        super(context);
        this.userId = userId;
    }

    @Override
    public ResponseHolder<UserData> makeRequest() throws Exception {
        return Communicator.getAppServer().getPersonalData(userId,
                new RequestParams().buildJSONWithPathParams(AppServerSpecs.USER_PATH, AppServerSpecs.USER_ID, userId));
    }

    @Override
    public void processResponse(ResponseHolder<UserData> responseHolder) throws Exception {
        PersonalData.getInstance(getContext()).setUserFirstName(responseHolder.getData().getFirstName());
        PersonalData.getInstance(getContext()).setUserLastName(responseHolder.getData().getLastName());
        PersonalData.getInstance(getContext()).setUserId(responseHolder.getData().getId());
        PersonalData.getInstance(getContext()).setUserPicture(responseHolder.getData().getPictureUrl());
        PersonalData.getInstance(getContext()).setUserPhones(new HashSet<String>(responseHolder.getData().getPhones()));
        PersonalData.getInstance(getContext()).setUserServiceId(responseHolder.getData().getServiceId());
        PersonalData.getInstance(getContext()).setUserRating(responseHolder.getData().getRating());
        PersonalData.getInstance(getContext()).setUserVoices(responseHolder.getData().getVoices());
        PersonalData.getInstance(getContext()).setCountUnreadMessages(responseHolder.getData().getNumberUnreadMessages());
        PersonalData.getInstance(getContext()).setUnreadNotificationsCount(responseHolder.getData().getNumberUnreadNotifications());
        PersonalData.getInstance(getContext()).setUserRegistrationDate(responseHolder.getData().getRegistrationDate());
    }

    @Override
    public int getRequestName() {
        return R.string.request_personal_data;
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
