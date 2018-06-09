package com.stockroompro.api.models.requests;

import android.content.Context;
import android.util.Log;

import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.models.Settings;

import java.util.Locale;


public class SettingsRequest extends BaseRequest<Settings> implements RequestWithNotifications {
    public SettingsRequest(Context context) {
        super(context);
    }

    @Override
    public ResponseHolder<Settings> makeRequest() throws Exception {
        Log.e("!!!", "SettingsRequest makeRequest");
        return Communicator.getAppServer().getSettings(new RequestParams()
                .addParameter(RequestParams.ParamNames.LOCALE, Locale.getDefault()
                        .getLanguage()).buildJSON(AppServerSpecs.SETTINGS_PATH));
    }

    @Override
    public void processResponse(ResponseHolder<Settings> responseHolder) throws Exception {
        if (responseHolder.getData() != null) {
            Log.e("!!!", responseHolder.getData().getCurrencies().toString());
            Settings settings = Settings.getInstance(getContext());
            settings.setAdsRelevancePeriod(responseHolder.getData().getAdsRelevancePeriod());
            settings.setAdsPhotoMaxUploadAmount(responseHolder.getData().getAdsPhotoMaxUploadAmount());
            settings.setAdsPhotoMaxUploadSize(responseHolder.getData().getAdsPhotoMaxUploadSize());
            settings.setCurrencies(responseHolder.getData().getCurrencies());
            settings.save(getContext());
        }
    }

    @Override
    public int getRequestName() {
        return R.string.request_settings;
    }

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.DO_NOT_NOTIFY_ALL;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
