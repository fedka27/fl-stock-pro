package com.artjoker.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
 * Copyright (C) 2010 The Android Open Source Project Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

/*
 * Modifications: -Imported from AOSP frameworks/base/core/java/com/android/internal/content -Changed package
 * name
 */
public final class SelectionBuilder {
    private static final String TAG = SelectionBuilder.class.getSimpleName();
    public static boolean LOG = false;
    private final Map<String, String> mProjectionMap = new HashMap<String, String>();
    private final StringBuilder selectionBuilder = new StringBuilder();
    private final ArrayList<String> selectionArgsList = new ArrayList<String>();
    private String tableName = null;

    private void assertTable() {
        if (tableName == null) {
            throw new IllegalStateException("Table not specified");
        }
    }

    public final int delete(SQLiteDatabase db) {
        assertTable();
        if (LOG) {
            Log.v(TAG, "delete() " + this);
        }
        return db.delete(tableName, getSelection(), getSelectionArgs());
    }

    public final String getSelection() {
        return selectionBuilder.toString();
    }

    public final String[] getSelectionArgs() {
        return selectionArgsList.toArray(new String[selectionArgsList.size()]);
    }

    public final SelectionBuilder map(String fromColumn, String toClause) {
        mProjectionMap.put(fromColumn, toClause + " AS " + fromColumn);
        return this;
    }

    private final void mapColumns(String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            final String target = mProjectionMap.get(columns[i]);
            if (target != null) {
                columns[i] = target;
            }
        }
    }

    public final SelectionBuilder mapToTable(String column, String table) {
        mProjectionMap.put(column, table + "." + column);
        return this;
    }

    public final Cursor query(SQLiteDatabase db, String[] columns, String orderBy) {
        return query(db, columns, null, null, orderBy, null);
    }

    public final Cursor query(SQLiteDatabase db, String[] columns, String groupBy, String having, String orderBy, String limit) {
        assertTable();
        if (columns != null) {
            mapColumns(columns);
        }
        if (LOG) {
            Log.v(TAG, "query(columns=" + Arrays.toString(columns) + ") " + this);
        }
        return db.query(tableName, columns, getSelection(), getSelectionArgs(), groupBy, having, orderBy, limit);
    }

    public final SelectionBuilder reset() {
        tableName = null;
        selectionBuilder.setLength(0);
        selectionArgsList.clear();
        return this;
    }

    public final SelectionBuilder table(String table) {
        tableName = table;
        return this;
    }

    public final int update(SQLiteDatabase db, ContentValues values) {
        assertTable();
        if (LOG) {
            Log.v(TAG, "update() " + this);
        }
        return db.update(tableName, values, getSelection(), getSelectionArgs());
    }

    public final SelectionBuilder where(String selection, String... selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            if ((selectionArgs != null) && (selectionArgs.length > 0)) {
                throw new IllegalArgumentException("Valid selection required when including arguments=");
            }
            return this;
        }
        if (selectionBuilder.length() > 0) {
            selectionBuilder.append(" AND ");
        }
        selectionBuilder.append("(").append(selection).append(")");
        if (selectionArgs != null) {
            Collections.addAll(selectionArgsList, selectionArgs);
        }
        return this;
    }

    @Override
    public String toString() {
        return TAG + "[" +
                "table=" + tableName + ", " +
                "selection=" + getSelection() + ", " +
                "selectionArgs=" + Arrays.toString(getSelectionArgs()) +
                "]";
    }

}
