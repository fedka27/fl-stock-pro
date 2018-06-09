package com.stockroompro.api.models.requests;

import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.UpdateDatabaseRequest;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.NewAdvert;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.converters.AdvertisementContentValuesConverter;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_BARGAIN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CITY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CURRENCY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_DESCRIPTION;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_OPTIONS;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_PHONES;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_PRICE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_PRICE_TYPE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_REGION_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_TITLE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_TYPE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_USED;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;

/**
 * Created by bagach.alexandr on 27.04.15.
 */
public class AddAdvertisementRequest extends UpdateDatabaseRequest<NewAdvert, AdvertisementContentValuesConverter> implements RequestWithNotifications {

    public AddAdvertisementRequest(Context context, Bundle bundle) {
        super(context, AdvertisementContract.CONTENT_URI, AdvertisementContentValuesConverter.class, bundle);
    }

    @Override
    public ResponseHolder makeRequest(long date) throws Exception {
        return Communicator.getAppServer().addAdvertisement(new RequestParams()
                .addParameter(ADVERT_TYPE, getBundle().getLong(ADVERT_TYPE))
                .addParameter(ADVERT_TITLE, getBundle().getString(ADVERT_TITLE))
                .addParameter(ADVERT_DESCRIPTION, getBundle().getString(ADVERT_DESCRIPTION))
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .addParameter(CATEGORY_ID, getBundle().getLong(CATEGORY_ID))
                .addParameter(ADVERT_PRICE_TYPE, getBundle().getInt(ADVERT_PRICE_TYPE))
                .addParameter(ADVERT_PRICE, getBundle().getLong(ADVERT_PRICE))
                .addParameter(ADVERT_CURRENCY_ID, getBundle().getLong(ADVERT_CURRENCY_ID))
                .addParameter(ADVERT_COUNTRY_ID, getBundle().getLong(ADVERT_COUNTRY_ID))
                .addParameter(ADVERT_REGION_ID, getBundle().getLong(ADVERT_REGION_ID))
                .addParameter(ADVERT_PHONES, getBundle().getStringArrayList(ADVERT_PHONES))
                .addParameter(ADVERT_CITY_ID, getBundle().getLong(ADVERT_CITY_ID))
                .addParameter(ADVERT_USED, getBundle().getInt(ADVERT_USED))
                .addParameter(ADVERT_BARGAIN, getBundle().getInt(ADVERT_BARGAIN))
                .addParameter(ADVERT_OPTIONS, getBundle().getIntegerArrayList(ADVERT_OPTIONS))
                .buildJSON(AppServerSpecs.ADVERTISEMENT_PATH));
    }

    @Override
    public void processResponse(ResponseHolder<NewAdvert> responseHolder) throws Exception {
        AdvertisementContentValuesConverter converter = new AdvertisementContentValuesConverter();
        Advertisement advertisement = new Advertisement();
        advertisement.setId(responseHolder.getData().getId());
        advertisement.setTitle(getBundle().getString(ADVERT_TITLE));
        advertisement.setDescription(getBundle().getString(ADVERT_DESCRIPTION));
        advertisement.setUserId(PersonalData.getInstance(getContext()).getUserId());
        advertisement.setCategoryId(getBundle().getLong(CATEGORY_ID));
        advertisement.setPrice(getBundle().getLong(ADVERT_PRICE));
        advertisement.setPriceType(getBundle().getInt(ADVERT_PRICE_TYPE));
        advertisement.setCountryId(getBundle().getLong(ADVERT_COUNTRY_ID));
        advertisement.setRegionId(getBundle().getLong(ADVERT_REGION_ID));
        advertisement.setCityId(getBundle().getLong(ADVERT_CITY_ID));
        advertisement.setUsed(getBundle().getInt(ADVERT_USED));
        advertisement.setBargain(getBundle().getInt(ADVERT_BARGAIN));
        advertisement.setPhones(getBundle().getStringArrayList(ADVERT_PHONES));
        converter.setObjectModel(advertisement);
        processItem(getContext().getContentResolver(), AdvertisementContract.CONTENT_URI, converter);
    }

    @Override
    public int getRequestName() {
        return R.string.request_user_data;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE | NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING | NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
