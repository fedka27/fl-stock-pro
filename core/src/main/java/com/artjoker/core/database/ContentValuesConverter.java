package com.artjoker.core.database;

import android.content.ContentValues;

import com.artjoker.core.Validator;

/**
 * Created by alexsergienko on 24.03.15.
 */
public interface ContentValuesConverter<T> extends Validator {
    ContentValues getContentValues();

    String getUpdateWhere();

    String[] getUpdateArgs();

    void setObjectModel(T model);
}
