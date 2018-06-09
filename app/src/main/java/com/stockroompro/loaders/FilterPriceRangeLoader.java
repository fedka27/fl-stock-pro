package com.stockroompro.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.artjoker.core.network.ResponseHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.api.models.responses.RangeOfPrices;
import com.stockroompro.models.Settings;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.contracts.FilterValuesContract;
import com.stockroompro.models.converters.AdvertisementCursorConverter;
import com.stockroompro.models.filters.FilterPriceRange;

import java.net.UnknownHostException;

public class FilterPriceRangeLoader extends AsyncTaskLoader<FilterPriceRange> {
    private static final String TAG = FilterPriceRangeLoader.class.getSimpleName();
    private final Bundle bundle;

    public FilterPriceRangeLoader(Context context, Bundle bundle) {
        super(context);
        this.bundle = bundle;
    }

    @Override
    public FilterPriceRange loadInBackground() {
        FilterPriceRange filterPriceRange = new FilterPriceRange();
        try {
            ResponseHolder<RangeOfPrices> response = Communicator.getAppServer().getAdvertsPricesRange(
                    new RequestParams()
                            .addParameter(RequestParams.ParamNames.ONLY_PRICE, true)
                            .addParameter(RequestParams.ParamNames.CATEGORY_ID, bundle.getLong(FilterValuesContract.CATEGORY_ID, 0))
                            .buildJSON(AppServerSpecs.ADVERTISEMENT_PATH));
            if (response != null && response.getData() != null && response.getData().getPriceMax() != 0) {
                filterPriceRange.setMinValue(0);
                filterPriceRange.setMaxValue(Settings.getInstance(getContext()).getPriceInDefaultCurrency(response.getData().getPriceMax(), response.getData().getCurrencyId()));
            } else {
                Cursor c = getContext().getContentResolver().query(AdvertisementContract.CONTENT_URI, null,
                        AdvertisementContract.CATEGORY_ID + " = ?",
                        new String[]{String.valueOf(bundle.getLong(FilterValuesContract.CATEGORY_ID, 0))}, null);
                if (c != null) {
                    AdvertisementCursorConverter converter = new AdvertisementCursorConverter();
                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                        converter.setCursor(c);
                        double price = Settings.getInstance(getContext()).getPriceInDefaultCurrency(converter.getObject().getPrice(), converter.getObject().getCurrencyId());
                        if (filterPriceRange.getMinValue() > price/* || (price > 0 && filterPriceRange.getMinValue() == 0)*/) {
                            filterPriceRange.setMinValue(price);
                        }
                        if (filterPriceRange.getMaxValue() < price) {
                            filterPriceRange.setMaxValue(price);
                        }
                    }
                    c.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Cannot process response ", e);
        }
        return filterPriceRange;
    }

}
