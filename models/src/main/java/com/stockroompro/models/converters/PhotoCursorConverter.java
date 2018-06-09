package com.stockroompro.models.converters;

import android.database.Cursor;

import com.artjoker.core.database.AbstractCursorConverter;
import com.stockroompro.models.Photo;
import com.stockroompro.models.contracts.PhotosContract;

/**
 * Created by bagach.alexandr on 08.04.15.
 */
public class PhotoCursorConverter extends AbstractCursorConverter<Photo> {

    private int COLUMN_INDEX_NATIVE_ID;
    private int COLUMN_INDEX_ADVERTISEMENT_ID;
    private int COLUMN_INDEX_PHOTO_URL;

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        if (isValid()) {
            COLUMN_INDEX_NATIVE_ID = cursor.getColumnIndex(PhotosContract._ID);
            COLUMN_INDEX_ADVERTISEMENT_ID = cursor.getColumnIndex(PhotosContract.ADVERTISEMENT_ID);
            COLUMN_INDEX_PHOTO_URL = cursor.getColumnIndex(PhotosContract.PHOTO_URL);
        }
    }

    @Override
    public Photo getObject() {
        Photo photo = null;
        if (isValid()) {
            photo = new Photo();
            photo.setAdvertId(getCursor().getLong(COLUMN_INDEX_ADVERTISEMENT_ID));
            photo.setPhotoUrl(getCursor().getString(COLUMN_INDEX_PHOTO_URL));
        }
        return photo;
    }

    public long getAdvertisementId() {
        if (isValid()) {
            return getCursor().getLong(COLUMN_INDEX_ADVERTISEMENT_ID);
        }
        return 0;
    }
}
