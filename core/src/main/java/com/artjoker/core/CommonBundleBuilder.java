package com.artjoker.core;

import android.os.Bundle;

import static com.artjoker.core.CommonBundleBuilderArgs.COUNT;
import static com.artjoker.core.CommonBundleBuilderArgs.LIMIT;
import static com.artjoker.core.CommonBundleBuilderArgs.OFFSET;

/**
 * Created by alexsergienko on 18.03.15.
 */
public class CommonBundleBuilder extends BundleBuilder {

    public CommonBundleBuilder(Bundle bundle) {
        super(bundle);
    }

    public CommonBundleBuilder() {
        super();
    }

    public CommonBundleBuilder putIntOffset(int value) {
        bundle.putInt(OFFSET, value);
        return this;
    }

    public CommonBundleBuilder putIntCount(int value) {
        bundle.putInt(COUNT, value);
        return this;
    }

    public CommonBundleBuilder putIntLimit(int value) {
        bundle.putInt(LIMIT, value);
        return this;
    }
}
