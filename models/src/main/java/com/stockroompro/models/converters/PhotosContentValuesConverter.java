package com.stockroompro.models.converters;

import android.content.ContentValues;

import com.artjoker.core.network.AbstractContentValuesConverter;
import com.artjoker.tool.core.SystemHelper;
import com.stockroompro.models.contracts.PhotosContract;

/**
 * Created by bagach.alexandr on 08.04.15.
 */
public class PhotosContentValuesConverter extends AbstractContentValuesConverter<String> {

    private long advertisementId;
    private long createdAt;
    private long updatedAt;

    public void setAdvertisementId(long advertisementId) {
        this.advertisementId = advertisementId;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    //TODO: fixed created_at and updated_at values
    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PhotosContract.ADVERTISEMENT_ID, advertisementId);
        contentValues.put(PhotosContract.PHOTO_URL, getObjectModel());
        contentValues.put(PhotosContract.CREATED_AT, createdAt);
        contentValues.put(PhotosContract.UPDATED_AT, updatedAt);
        return contentValues;
    }

    @Override
    public String getUpdateWhere() {
        StringBuilder builder = new StringBuilder();
        builder.append(PhotosContract.ADVERTISEMENT_ID);
        builder.append(" = ? AND ");
        builder.append(PhotosContract.PHOTO_URL);
        builder.append(" = ? ");
        return builder.toString();
    }

    @Override
    public String[] getUpdateArgs() {
        return new String[]{
                String.valueOf(advertisementId),
                String.valueOf(getObjectModel())};
    }


}
