package com.artjoker.core.network;

import com.artjoker.core.database.ContentValuesConverter;

/**
 * Created by alexsergienko on 25.03.15.
 */
public abstract class AbstractContentValuesConverter<T> implements ContentValuesConverter<T> {
    private T model;

    public T getObjectModel() {
        return model;
    }

    @Override
    public void setObjectModel(T model) {
        this.model = model;
    }

    @Override
    public boolean isValid() {
        return getObjectModel() != null;
    }
}
