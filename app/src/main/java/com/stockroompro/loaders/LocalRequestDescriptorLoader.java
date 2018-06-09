package com.stockroompro.loaders;

import android.content.Context;

import com.artjoker.core.network.RequestDescriptor;
import com.artjoker.core.network.RequestDescriptorWithProcessorLoader;

/**
 * Created by user on 14.04.15.
 */
public class LocalRequestDescriptorLoader extends RequestDescriptorWithProcessorLoader {
    public LocalRequestDescriptorLoader(Context context, RequestDescriptor requestDescriptor) {
        super(context, requestDescriptor, new LocalBroadcastRequestProcessor(context));
    }
}
