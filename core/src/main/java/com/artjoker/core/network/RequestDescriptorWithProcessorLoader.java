package com.artjoker.core.network;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by user on 10.04.15.
 */
public class RequestDescriptorWithProcessorLoader extends AsyncTaskLoader<Boolean> {

    private RequestDescriptor requestDescriptor;
    private RequestProcessor requestProcessor;

    public RequestDescriptorWithProcessorLoader(Context context, RequestDescriptor requestDescriptor, RequestProcessor requestProcessor) {
        super(context);
        this.requestDescriptor = requestDescriptor;
        this.requestProcessor = requestProcessor;
    }

    @Override
    public Boolean loadInBackground() {
        requestProcessor.process(requestDescriptor);
        return null;
    }
}
