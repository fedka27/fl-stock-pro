package com.artjoker.core.network;

import android.support.annotation.StringRes;

import java.net.UnknownHostException;

/**
 * Created by alexsergienko on 16.03.15.
 */
public interface RequestDescriptor<T> {

    ResponseHolder<T> makeRequest() throws Exception;

    void processResponse(ResponseHolder<T> responseHolder) throws Exception;

    @StringRes
    int getRequestName();

}
