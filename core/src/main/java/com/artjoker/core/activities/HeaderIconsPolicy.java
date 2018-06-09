package com.artjoker.core.activities;

/**
 * Created by artjoker on 21.04.2015.
 */
public interface HeaderIconsPolicy {
    public static final int SHOW_ALL = 0xFFFFFFFF;
    public static final int DO_NOT_SHOW_ALL = 0;
    public static final int SHOW_BUTTON_EDIT = 1 << 1;
    public static final int SHOW_BUTTON_FILTERS = 1 << 2;
    public static final int SHOW_BUTTON_DELETE = 1 << 3;
    public static final int SHOW_BUTTON_CLOSE = 1 << 4;
    public static final int SHOW_BUTTON_ADD = 1 << 5;

}
