package com.artjoker.core.network;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by alexsergienko on 24.03.15.
 */
public abstract class BaseRequest<T> implements RequestDescriptor<T> {

    private Context context;
    private Bundle bundle;
    public ResponseCallback callback;
    public UIResponseCallback uiCallback;

    public BaseRequest(Context context) {
        this.context = context;
    }

    public BaseRequest(Context context, Bundle bundle) {
        this.context = context;
        this.bundle = bundle;
    }

    public Context getContext() {
        return context;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public interface ResponseCallback {
        public void dataResponse(ResponseHolder data);
    }

    public interface UIResponseCallback {
        public void uiDataResponse(ResponseHolder data);
    }

    public BaseRequest<T> setUiCallback(BaseRequest.UIResponseCallback uiCallback) {
        this.uiCallback = uiCallback;
        return this;
    }

    public BaseRequest<T> setCallback(BaseRequest.ResponseCallback callback) {
        this.callback = callback;
        return this;
    }

}
