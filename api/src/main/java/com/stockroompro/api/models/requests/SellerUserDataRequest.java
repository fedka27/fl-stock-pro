package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.ResponseHolder;
import com.stockroompro.models.UserData;

/**
 * Created by user on 22.04.15.
 */
public class SellerUserDataRequest extends UserDataRequest {

    public SellerUserDataRequest(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    public void processResponse(ResponseHolder<UserData> responseHolder) throws Exception {
    }
}
