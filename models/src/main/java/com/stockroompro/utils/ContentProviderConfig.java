package com.stockroompro.utils;

import android.net.Uri;

public interface ContentProviderConfig {

    String AUTHORITY_CONTENT = "com.stockroompro";
    Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY_CONTENT);
    String CURSOR_DIR = "vnd.android.cursor.dir/" + AUTHORITY_CONTENT.replace("com", "vnd") + ".";
    String CURSOR_ITEM = "vnd.android.cursor.item/" + AUTHORITY_CONTENT.replace("com", "vnd") + ".";
}