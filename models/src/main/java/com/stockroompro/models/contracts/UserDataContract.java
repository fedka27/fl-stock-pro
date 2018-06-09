package com.stockroompro.models.contracts;

import android.net.Uri;

import com.stockroompro.models.Photo;
import com.stockroompro.models.columns.AdvertisementColumns;
import com.stockroompro.models.columns.UserDataColumns;
import com.stockroompro.utils.ContentProviderHelper;

import static com.artjoker.core.database.constants.DatabaseTypes.INTEGER;
import static com.artjoker.core.database.constants.DatabaseTypes.REAL;
import static com.artjoker.core.database.constants.DatabaseTypes.TEXT;

/**
 * Created by bagach.alexandr on 27.04.15.
 */
public interface UserDataContract  extends UserDataColumns {
    String TABLE_NAME = ContentProviderHelper.tableName("user_data");
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + ID + " " + INTEGER + " NOT NULL, "
            + SERVICE_ID + " " + INTEGER + " , "
            + FIRST_NAME + " " + TEXT + " , "
            + LAST_NAME + " " + TEXT + " , "
            + EMAIL + " " + TEXT + " , "
            + CITY_ID + " " + INTEGER + " , "
            + PHONES + " " + TEXT + " , "
            + REGISTRATION_DATE + " " + INTEGER + " , "
            + NUMBER_UNREAD_MESSAGES + " " + INTEGER + " , "
            + PICTURE_URL + " " + TEXT + " , "
            + RATING + " " + REAL + " , "
            + VOICES + " " + INTEGER + " , "
            + COUNTRY_NAME + " " + TEXT + " , "
            + REGION_NAME + " " + TEXT + " , "
            + CITY_NAME + " " + TEXT + " , "
            + CREATED_AT + " " + INTEGER + " NOT NULL DEFAULT 0, "
            + UPDATED_AT + " " + INTEGER + " NOT NULL DEFAULT 0);";
    String DROP_TABLE = ContentProviderHelper.dropTable(TABLE_NAME);

    Uri CONTENT_URI = ContentProviderHelper.contentUri(TABLE_NAME);
    String CONTENT_TYPE = ContentProviderHelper.contentType(TABLE_NAME);
    String CONTENT_ITEM_TYPE = ContentProviderHelper.contentItemType(TABLE_NAME);
}
