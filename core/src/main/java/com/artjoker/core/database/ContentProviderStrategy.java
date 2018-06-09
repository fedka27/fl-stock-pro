package com.artjoker.core.database;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import java.util.ArrayList;

public abstract class ContentProviderStrategy<T> {
    private static final String TAG = ContentProviderStrategy.class.getSimpleName();
    protected final ContentResolver contentResolver;
    protected final ArrayList<ContentProviderOperation> operations;
    protected T model;

    {
        operations = new ArrayList<ContentProviderOperation>();
    }

    public ContentProviderStrategy(Context context) {
        this(context.getApplicationContext().getContentResolver());
    }

    public ContentProviderStrategy(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public void insertInDatabase() {
        if (valid()) {
            synchronized (operations) {
                operations.clear();
                insertInDatabaseInternally();
                apply();
            }
        }
    }

    public void updateInDatabase() {
        if (valid()) {
            synchronized (operations) {
                operations.clear();
                updateInDatabaseInternally();
                apply();
            }
        }
    }

    public void deleteFromDatabase() {
        if (valid()) {
            synchronized (operations) {
                operations.clear();
                deleteFromDatabaseInternally();
                apply();
            }
        }
    }

    public abstract boolean entriesExist();

    public abstract T buildModel();

    protected void insertInDatabaseInternally() {

    }

    protected void updateInDatabaseInternally() {

    }

    protected void deleteFromDatabaseInternally() {

    }

    protected final void apply() {
        try {
            contentResolver.applyBatch(getAuthorityContent(), operations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    protected abstract String getAuthorityContent();

    protected final ContentProviderOperation insert(Uri contentUri, ContentValues contentValues) {
        return ContentProviderOperation.newInsert(contentUri).withValues(contentValues).build();
    }

    protected final ContentProviderOperation update(Uri contentUri, ContentValues contentValues, String where, String[] selectionArgs) {
        return ContentProviderOperation.newUpdate(contentUri).withSelection(where, selectionArgs).withValues(contentValues).build();
    }

    protected final ContentProviderOperation delete(Uri contentUri, String where, String[] selectionArgs) {
        return ContentProviderOperation.newDelete(contentUri).withSelection(where, selectionArgs).build();
    }

    protected final boolean valid() {
        return (contentResolver != null) && (model != null);
    }

    protected final boolean notEmpty(Uri contentUri) {
        return notEmpty(contentUri, null, null);
    }

    protected final boolean notEmpty(Uri contentUri, String selection, String[] selectionArgs) {
        boolean notEmptyValue = false;
        Cursor cursor = contentResolver.query(contentUri, new String[]{"COUNT(*)"}, selection, selectionArgs, null);
        if (cursor != null) {
            if (rowsExist(cursor)) {
                notEmptyValue = true;
            }
            cursor.close();
        }
        return notEmptyValue;
    }

    private boolean rowsExist(Cursor cursor) {
        return (cursor.moveToFirst()) && (cursor.getInt(0) > 0);
    }

}