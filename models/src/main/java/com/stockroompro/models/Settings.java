package com.stockroompro.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.stockroompro.models.contracts.AdvertisementContract;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 24.03.15.
 */
public final class Settings {

    public interface PreferencesConfig {
        String PREFERENCES_NAME = "settings_preferences";
        String ADS_RELEVANCE_PERIOD = "ads_relevance_period";
        String ADS_PHOTO_MAX_UPLOAD_AMOUNT = "ads_photo_max_upload_amount";
        String ADS_PHOTO_MAX_UPLOAD_SIZE = "ads_photo_max_upload_size";
        String CURRENCIES = "currencies";
    }

    @Expose
    @SerializedName(PreferencesConfig.ADS_RELEVANCE_PERIOD)
    /**Amount of days*/
    private int adsRelevancePeriod;
    @Expose
    @SerializedName(PreferencesConfig.ADS_PHOTO_MAX_UPLOAD_AMOUNT)
    private int adsPhotoMaxUploadAmount;
    @Expose
    @SerializedName(PreferencesConfig.ADS_PHOTO_MAX_UPLOAD_SIZE)
    /**Size in Mb*/
    private int adsPhotoMaxUploadSize;
    @Expose
    private ArrayList<Currency> currencies;

    private static final int DEFAULT_DISPLAY_CURRENCY_ID = 1;
    private static final int DEFAULT_ADS_RELEVANCE_PERIOD = 30;
    private static final int DEFAULT_ADS_PHOTO_MAX_UPLOAD_AMOUNT = 10;
    private static final int DEFAULT_ADS_PHOTO_MAX_UPLOAD_SIZE = 2;
    private static final String DEFAULT_CURRENCIES = "[" +
            "{" +
            "\"id\":2," +
            "\"name\":\"RUB\"," +
            "\"ratio_by_default\":0.40" +
            "}," +
            "{" +
            "\"id\":1, " +
            "\"name\": \"UAH\"," +
            "\"ratio_by_default\":1" +
            "}," +
            "{" +
            "\"id\":4," +
            "\"name\":\"EUR\"," +
            "\"ratio_by_default\":25.0" +
            "}," +
            "{" +
            "\"id\":3," +
            "\"name\":\"USD\"," +
            "\"ratio_by_default\":22.2" +
            "}," +
            "{" +
            "\"id\":5," +
            "\"name\":\"BYR\"," +
            "\"ratio_by_default\":0.0057" +
            "}," +
            "{" +
            "\"id\":6," +
            "\"name\":\"KZT\"," +
            "\"ratio_by_default\":0.1183" +
            "}" +
            "]";
    private static Settings instance;

    public static Settings getInstance(Context context) {
        if (instance == null) {
            instance = new Settings();
            instance.read(context);
        }
        return instance;
    }

    public void save(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PreferencesConfig.PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferences.edit()
                .putInt(PreferencesConfig.ADS_RELEVANCE_PERIOD, adsRelevancePeriod)
                .putInt(PreferencesConfig.ADS_PHOTO_MAX_UPLOAD_SIZE, adsPhotoMaxUploadSize)
                .putInt(PreferencesConfig.ADS_PHOTO_MAX_UPLOAD_AMOUNT, adsPhotoMaxUploadAmount)
                .putString(PreferencesConfig.CURRENCIES, new Gson().toJson(currencies)).apply();
    }

    public Settings read(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PreferencesConfig.PREFERENCES_NAME, Context.MODE_PRIVATE);
        adsRelevancePeriod = preferences.getInt(PreferencesConfig.ADS_RELEVANCE_PERIOD, DEFAULT_ADS_RELEVANCE_PERIOD);
        adsPhotoMaxUploadAmount = preferences.getInt(PreferencesConfig.ADS_PHOTO_MAX_UPLOAD_AMOUNT, DEFAULT_ADS_PHOTO_MAX_UPLOAD_AMOUNT);
        adsPhotoMaxUploadSize = preferences.getInt(PreferencesConfig.ADS_PHOTO_MAX_UPLOAD_SIZE, DEFAULT_ADS_PHOTO_MAX_UPLOAD_SIZE);
        try {
            currencies = new Gson().fromJson(preferences.getString(PreferencesConfig.CURRENCIES, DEFAULT_CURRENCIES), new TypeToken<ArrayList<Currency>>() {
            }.getType());
        } catch (Exception e) {
            currencies = new Gson().fromJson(DEFAULT_CURRENCIES, new TypeToken<ArrayList<Currency>>() {
            }.getType());
        }
        return this;
    }

    public Currency getCurrencyById(int currencyId) {

        for (Currency currency : getCurrencies()) {
            if (currency.getId() == currencyId) {
                return currency;
            }
        }
        return new Currency(currencyId, getCurrencies().get(0).getName(), 1.0);
    }

    public int getPriceInDefaultCurrency(double price, int priceCurrencyId) {
        return getPrice(price, priceCurrencyId, DEFAULT_DISPLAY_CURRENCY_ID);
    }

    public int getPrice(double price, int priceCurrencyId, int displayCurrencyId) {
        Currency priceCurrency = getCurrencyById(priceCurrencyId);
        Currency displayCurrency = getCurrencyById(displayCurrencyId);
        return (int) (Math.floor(price * 100 * priceCurrency.getRatioByDefault() / displayCurrency.getRatioByDefault())) / 100;
    }

    /**
     * Projection for getting price for make ordering by price independent on currency
     */
    public String getIndependentProjectionPriceField() {
        Currency displayCurrency = getCurrencyById(DEFAULT_DISPLAY_CURRENCY_ID);
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(AdvertisementContract.PRICE);
        builder.append("*(CASE (");
        builder.append(AdvertisementContract.CURRENCY_ID);
        builder.append(")");
        for (Currency currency : currencies) {
            builder.append(" WHEN ");
            builder.append(currency.getId());
            builder.append(" THEN ");
            builder.append(currency.getRatioByDefault() / displayCurrency.getRatioByDefault());
        }
        builder.append(" ELSE 1.0 ");
        builder.append("END)");
        builder.append(")");
        return builder.toString();
    }

    public int getAdsRelevancePeriod() {
        return adsRelevancePeriod;
    }

    public void setAdsRelevancePeriod(int adsRelevancePeriod) {
        this.adsRelevancePeriod = adsRelevancePeriod;
    }

    public int getAdsPhotoMaxUploadAmount() {
        return adsPhotoMaxUploadAmount;
    }

    public void setAdsPhotoMaxUploadAmount(int adsPhotoMaxUploadAmount) {
        this.adsPhotoMaxUploadAmount = adsPhotoMaxUploadAmount;
    }

    public int getAdsPhotoMaxUploadSize() {
        return adsPhotoMaxUploadSize;
    }

    public void setAdsPhotoMaxUploadSize(int adsPhotoMaxUploadSize) {
        this.adsPhotoMaxUploadSize = adsPhotoMaxUploadSize;
    }

    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ArrayList<Currency> currencies) {
        this.currencies = currencies;
    }
}
