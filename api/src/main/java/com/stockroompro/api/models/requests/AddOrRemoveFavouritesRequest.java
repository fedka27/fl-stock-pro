package com.stockroompro.api.models.requests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.StatusCode;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.contracts.AdvertisementContract;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by bagach.alexandr on 03.04.15.
 */
public class AddOrRemoveFavouritesRequest extends BaseRequest implements RequestWithNotifications {
    private static final int ADDED = 1;
    private static final int NOT_ADDED = 0;
    private boolean isAlreadyAdded;

    public AddOrRemoveFavouritesRequest(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    public ResponseHolder makeRequest() throws Exception {
        long addId = getBundle().getLong(AD_ID);
        long userId = getBundle().getLong(UID);
        isAlreadyAdded = false;
        Cursor cursor = getContext().getContentResolver().query(AdvertisementContract.CONTENT_URI,
                new String[]{AdvertisementContract.FAVOURITE}, AdvertisementContract.ID + " = " + addId, null, null);
        if (cursor.moveToFirst()) {
            isAlreadyAdded = cursor.getInt(0) == ADDED;
        }
        cursor.close();
        if (isAlreadyAdded) {
            return Communicator.getAppServer().removeFavourite(userId, addId, new RequestParams()
                    .addParameter(TOKEN, getBundle().getString(TOKEN))
                    .buildJSONWithPathParams(AppServerSpecs.FAVOURITES_REMOVING_PATH,
                            AppServerSpecs.USER_ID, userId, AppServerSpecs.ADD_ID, addId));
        } else {
            return Communicator.getAppServer().addFavourite(userId, new RequestParams()
                    .addParameter(AD_ID, addId)
                    .addParameter(TOKEN, getBundle().getString(TOKEN))
                    .buildJSONWithPathParams(AppServerSpecs.FAVOURITES_ADDING_PATH, AppServerSpecs.USER_ID, userId));
        }
    }

    @Override
    public void processResponse(ResponseHolder responseHolder) throws Exception {
        if (responseHolder.getStatusCode() == StatusCode.RESPONSE_SUCCESS) {
            long addId = getBundle().getLong(AD_ID);
            ContentValues contentValues = new ContentValues();
            contentValues.put(AdvertisementContract.FAVOURITE, isAlreadyAdded ? NOT_ADDED : ADDED);
            getContext().getContentResolver().update(AdvertisementContract.CONTENT_URI, contentValues,
                    AdvertisementContract.ID + " = ?", new String[]{String.valueOf(addId)});
        }
    }

    @Override
    public int getRequestName() {
        return R.string.favourites_add_remove;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE | NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING | NotificationsPolicy.NOTIFY_SUCCESS | NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }

    @Override
    public boolean isValid() {
        return PersonalData.getInstance(getContext()).getToken() != null;
    }
}
