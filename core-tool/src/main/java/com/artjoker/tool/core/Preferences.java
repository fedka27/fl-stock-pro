package com.artjoker.tool.core;

import android.content.Context;
import android.content.SharedPreferences;

public final class Preferences {
    private final SharedPreferences preferences;

    public Preferences(Context context, String name) {
        this.preferences = context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public void clearPreferences() {
        preferences.edit().clear().apply();
    }

    public boolean putLong(final String key, final long value) {
        return preferences.edit().putLong(key, value).commit();
    }

    public long getLong(final String key) {
        return preferences.getLong(key, 0);
    }

    public boolean putInt(final String key, final int value) {
        return preferences.edit().putInt(key, value).commit();
    }

    public int getInt(final String key) {
        return preferences.getInt(key, 0);
    }

    public boolean putBoolean(final String key, final boolean value) {
        return preferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(final String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean putString(final String key, final String value) {
        return preferences.edit().putString(key, value).commit();
    }

    public String getString(final String key) {
        return preferences.getString(key, "");
    }

}