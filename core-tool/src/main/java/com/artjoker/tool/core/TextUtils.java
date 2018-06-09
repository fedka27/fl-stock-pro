package com.artjoker.tool.core;

import android.content.Context;

/**
 * Created by bagach.alexandr on 20.05.15.
 */
public final class TextUtils {

    private Context context;

    private static class TextUtilsHolder {
        private static final TextUtils INSTANCE = new TextUtils();
    }

    private TextUtils() {}

    public static TextUtils getInstance() {
        return TextUtilsHolder.INSTANCE;
    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public String getStringFromDoubleValue(double value) {
        return (int)value - value > 0 ? String.format("%.2f", value) : String.format("%.0f", value);
    }

}
