package com.artjoker.core.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.RequestDescriptor;

/**
 * Created by artjoker on 20.04.2015.
 */
public class RequestDescriptorLoader extends AsyncTaskLoader<Boolean> {

    RequestDescriptor requestDescriptor;

    public RequestDescriptorLoader(Context context, RequestDescriptor requestDescriptor) {
        super(context);
        this.requestDescriptor = requestDescriptor;
    }

    @Override
    public Boolean loadInBackground() {
        try {
            requestDescriptor.processResponse(requestDescriptor.makeRequest());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}