package com.stockroompro.api.models.requests;


import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.ResponseHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.SERVICE_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.SERVICE_TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.SERVICE_UID;


public class SyncWithSocialNetworkRequest extends BaseRequest  {

    public SyncWithSocialNetworkRequest(Context context,Bundle bundle) {
        super(context,bundle);
    }

    @Override
    public ResponseHolder makeRequest() throws Exception {
        return Communicator.getAppServer().syncWithSocial(new RequestParams()
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .addParameter(SERVICE_UID,getBundle().getLong(SERVICE_UID))
                .addParameter(SERVICE_ID,getBundle().getInt(SERVICE_ID))
                .addParameter(SERVICE_TOKEN,getBundle().getString(SERVICE_TOKEN))
                .buildJSON(AppServerSpecs.SYNC_WITH_SOCIAL_PATH));
    }

    @Override
    public void processResponse(ResponseHolder responseHolder) throws Exception {
    }

    @Override
    public int getRequestName() {
        return R.string.request_social_synchronization;
    }

}
