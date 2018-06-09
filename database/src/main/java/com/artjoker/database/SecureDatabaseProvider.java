package com.artjoker.database;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public abstract class SecureDatabaseProvider extends ContentProvider {
    private static final String TAG = SecureDatabaseProvider.class.getSimpleName();
    private static boolean LOG = false;
    private SQLiteOpenHelper databaseHelper;

    public static void printLogs(boolean log) {
        LOG = log;
        SelectionBuilder.LOG = log;
    }

    protected static void addUri(UriMatcher uriMatcher, String authorityContent, String tableName, int dirId, int itemId) {
        uriMatcher.addURI(authorityContent, tableName, dirId);
        uriMatcher.addURI(authorityContent, tableName + SecureConfig.ITEM_POINTER_ID, itemId);
    }

    public static void logCursor(Cursor c) {
        if (LOG) {
            if (c != null) {
                if (c.moveToFirst()) {
                    String str;
                    do {
                        str = "";
                        for (String cn : c.getColumnNames()) {
                            str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                        }
                        Log.d(SecureDatabaseProvider.class.getSimpleName(), str);
                    } while (c.moveToNext());
                }
            } else
                Log.d(SecureDatabaseProvider.class.getSimpleName(), "Cursor is null");
        }
    }

    /**
     * Invoke in first line super method if need use secure sqlite file and second line define sqliteOpenHelper
     */
    @Override
    public boolean onCreate() {
//        SQLiteDatabase.loadLibs(getContext());
        SelectionBuilder.LOG = LOG;
        databaseHelper = getNewDatabaseHelper();
        return true;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final int operationsCount = operations.size();
        final ContentProviderResult[] results = new ContentProviderResult[operationsCount];
        final SQLiteDatabase db = getInstance();
        db.beginTransaction();
        try {
            for (int i = 0; i < operationsCount; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
        } catch (OperationApplicationException e) {
            e.getStackTrace();
        } finally {
            db.endTransaction();
        }
        return results;
    }

    /**
     * Return object of SQLiteOpenHelper databaseHelper also AuthorityContent in build.gradle
     */
    protected abstract SQLiteOpenHelper getNewDatabaseHelper();


    protected synchronized SQLiteDatabase getInstance() {
        try {
            return databaseHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            Log.w(TAG, "open database error: " + e.toString());
            databaseHelper = getNewDatabaseHelper();
            return databaseHelper.getWritableDatabase();
        }
    }

    protected Uri buildInsertUri(SQLiteDatabase database, Uri contentUri, String tableName, ContentValues contentValues) {
        return ContentUris.withAppendedId(contentUri, database.insertOrThrow(tableName, null, contentValues));
    }

    protected int insertOrUpdate(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int count = getSimpleSelectionBuilder(uri, selection, selectionArgs).update(getInstance(), contentValues);
        if (count == 0) {
            insert(uri, contentValues);
            return 1;
        }
        return count;
    }

    protected void appendSelection(SelectionBuilder builder, String tableName, String selection, String[] selectionArgs) {
        builder.table(tableName);
        if (TextUtils.isEmpty(selection)) {
            builder.where(null, (String[]) null);
        } else {
            builder.where(selection, selectionArgs);
        }
    }

    protected void appendId(SelectionBuilder builder, String tableName, Uri uri) {
        builder.table(tableName).where(_ID + "=?", uri.getPathSegments().get(1));
    }

    protected abstract SelectionBuilder getSimpleSelectionBuilder(Uri uri, String selection, String[] selectionArgs);

    protected interface SecureConfig {
        String ITEM_POINTER = "id";
        String ITEM_POINTER_ID = "/" + ITEM_POINTER + "/#";
        String ALL = "*";
    }

}