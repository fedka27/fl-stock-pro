package com.stockroompro.utils;

import android.net.Uri;

import static com.stockroompro.utils.ContentProviderConfig.BASE_CONTENT_URI;
import static com.stockroompro.utils.ContentProviderConfig.CURSOR_DIR;
import static com.stockroompro.utils.ContentProviderConfig.CURSOR_ITEM;

public final class ContentProviderHelper {

    private ContentProviderHelper() {

    }

    public static String tableName(final String prefix) {
        return prefix + "_db_table";
    }

    public static String dropTable(final String tableName) {
        return StatementTemplates.DROP_TABLE + tableName;
    }

    public static String contentType(final String tableName) {
        return CURSOR_DIR + tableName;
    }

    public static String contentItemType(final String tableName) {
        return CURSOR_ITEM + tableName;
    }

    public static Uri contentUri(final String tableName) {
        return BASE_CONTENT_URI.buildUpon().appendPath(tableName).build();
    }

    private interface StatementTemplates {
        String DROP_TABLE = "DROP TABLE IF EXISTS ";
    }

}