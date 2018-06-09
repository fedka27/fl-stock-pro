package com.stockroompro.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by bagach.alexandr on 24.03.15.
 */
public final class PersonalData {

    public interface PreferencesConfig {
        String PREFERENCES_NAME = "user_data_preferences";
        String USER_ID_KEY = "user_id";
        String USER_FIRST_NAME_KEY = "use_first_name_id";
        String USER_LAST_NAME_KEY = "user_last_name_id";
        String USER_EMAIL_KEY = "user_email_id";
        String USER_COUNTRY_ID_KEY = "user_country_id";
        String USER_COUNTRY_NAME_KEY = "user_country_name";
        String USER_REGION_ID_KEY = "user_region_id";
        String USER_REGION_NAME_KEY = "user_region_name";
        String USER_CITY_ID_KEY = "user_city_id";
        String USER_CITY_NAME_KEY = "user_city_name";
        String USER_PHONES_KEY = "user_phones_id";
        String USER_REGISTRATION_DATE_KEY = "user_registration_date_id";
        String USER_PICTURE_KEY = "user_picture_id";
        String USER_RATING_KEY = "user_rating_id";
        String USER_VOICES_KEY = "user_voices_id";
        String USER_TOKEN_KEY = "user_token";
        String USER_SERVICE_TOKEN_KEY = "user_service_token";
        String USER_SERVICE_ID_KEY = "service_id";
        String UNREAD_MESSAGES_KEY = "unread_messages";
        String UNREAD_NOTIFICATIONS_KEY = "unr_ntcs_amnt";
        String REFRESH_TOKEN_KEY = "refresh_token";
        String USER_SEARCH_LOCATION_KEY = "user_search_location";
    }

    private SharedPreferences preferences;
    private static PersonalData instance;

    public static PersonalData getInstance(Context context) {
        if (instance == null) {
            instance = new PersonalData(context);
        }
        return instance;
    }

    private PersonalData(Context context) {
        preferences = context.getSharedPreferences(PreferencesConfig.PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setUserToken(String token) {
        preferences.edit().putString(PreferencesConfig.USER_TOKEN_KEY, token).apply();
    }

    public void setRefreshToken(String token) {
        preferences.edit().putString(PreferencesConfig.REFRESH_TOKEN_KEY, token).apply();
    }

    public void setUserServiceToken(String token) {
        preferences.edit().putString(PreferencesConfig.USER_SERVICE_TOKEN_KEY, token).apply();
    }

    public void setUserServiceId(long serviceId) {
        preferences.edit().putLong(PreferencesConfig.USER_SERVICE_ID_KEY, serviceId).apply();
    }

    public void setCountUnreadMessages(long number) {
        preferences.edit().putLong(PreferencesConfig.UNREAD_MESSAGES_KEY, number).apply();
    }

    public void setUserId(long userId) {
        preferences.edit().putLong(PreferencesConfig.USER_ID_KEY, userId).apply();
    }

    public void setUnreadNotificationsCount(long count) {
        preferences.edit().putLong(PreferencesConfig.UNREAD_NOTIFICATIONS_KEY, count).apply();
    }

    public void setUserFirstName(String firstName) {
        preferences.edit().putString(PreferencesConfig.USER_FIRST_NAME_KEY, firstName).apply();
    }

    public void setUserLastName(String lastName) {
        preferences.edit().putString(PreferencesConfig.USER_LAST_NAME_KEY, lastName).apply();
    }

    public void setUserEmail(String email) {
        preferences.edit().putString(PreferencesConfig.USER_EMAIL_KEY, email).apply();
    }

    public void setUserCountryId(long countryId) {
        preferences.edit().putLong(PreferencesConfig.USER_COUNTRY_ID_KEY, countryId).apply();
    }

    public void setUserCountryName(String countryName) {
        preferences.edit().putString(PreferencesConfig.USER_COUNTRY_NAME_KEY, countryName).apply();
    }

    public void setUserRegionId(long regionId) {
        preferences.edit().putLong(PreferencesConfig.USER_REGION_ID_KEY, regionId).apply();
    }

    public void setUserRegionName(String regionName) {
        preferences.edit().putString(PreferencesConfig.USER_REGION_NAME_KEY, regionName).apply();
    }

    public void setUserCityId(long cityId) {
        preferences.edit().putLong(PreferencesConfig.USER_CITY_ID_KEY, cityId).apply();
    }

    public void setUserCityName(String cityName) {
        preferences.edit().putString(PreferencesConfig.USER_CITY_NAME_KEY, cityName).apply();
    }

    public void setUserPhones(Set<String> phones) {
        preferences.edit().putStringSet(PreferencesConfig.USER_PHONES_KEY, phones).apply();
    }

    public void setUserRegistrationDate(long date) {
        preferences.edit().putLong(PreferencesConfig.USER_REGISTRATION_DATE_KEY, date).apply();
    }

    public void setUserPicture(String pictureUrl) {
        preferences.edit().putString(PreferencesConfig.USER_PICTURE_KEY, pictureUrl).apply();
    }

    public void setUserRating(float rating) {
        preferences.edit().putFloat(PreferencesConfig.USER_RATING_KEY, rating).apply();
    }

    public void setUserVoices(long voices) {
        preferences.edit().putLong(PreferencesConfig.USER_VOICES_KEY, voices).apply();
    }

    public void setUserSearchLocation(ArrayList<String> arrayList) {
        Set<String> stringSet = new LinkedHashSet<>(arrayList.size());
        for (int i = 0, size = arrayList.size(); i < size; i++) {
            stringSet.add(arrayList.get(i));
        }
        preferences.edit().putStringSet(PreferencesConfig.USER_SEARCH_LOCATION_KEY, stringSet).apply();
    }

    public long getUserId() {
        return preferences.getLong(PreferencesConfig.USER_ID_KEY, 0);
    }

    public String getUserFirstName() {
        return preferences.getString(PreferencesConfig.USER_FIRST_NAME_KEY, "");
    }

    public String getUserLastName() {
        return preferences.getString(PreferencesConfig.USER_LAST_NAME_KEY, "");
    }

    public String getUserEmail() {
        return preferences.getString(PreferencesConfig.USER_EMAIL_KEY, null);
    }

    public long getUserCountryId() {
        return preferences.getLong(PreferencesConfig.USER_COUNTRY_ID_KEY, 0);
    }

    public String getUserCountryName() {
        return preferences.getString(PreferencesConfig.USER_COUNTRY_NAME_KEY, null);
    }

    public long getUserRegionId() {
        return preferences.getLong(PreferencesConfig.USER_REGION_ID_KEY, 0);
    }

    public String getUserRegionName() {
        return preferences.getString(PreferencesConfig.USER_REGION_NAME_KEY, null);
    }

    public long getUserCityId() {
        return preferences.getLong(PreferencesConfig.USER_CITY_ID_KEY, 0);
    }

    public String getUserCityName() {
        return preferences.getString(PreferencesConfig.USER_CITY_NAME_KEY, null);
    }

    public Set<String> getUserPhones() {
        return preferences.getStringSet(PreferencesConfig.USER_PHONES_KEY, null);
    }

    public long getUserRegistrationDate() {
        return preferences.getLong(PreferencesConfig.USER_REGISTRATION_DATE_KEY, 0);
    }

    public long getUserUnreadMessagesCount() {
        return preferences.getLong(PreferencesConfig.UNREAD_MESSAGES_KEY, 0);
    }

    public long getUserUnreadNotificationsCount() {
        return preferences.getLong(PreferencesConfig.UNREAD_NOTIFICATIONS_KEY, 0);
    }

    public String getUserPicture() {
        return preferences.getString(PreferencesConfig.USER_PICTURE_KEY, null);
    }

    public String getUserToken() {
        return preferences.getString(PreferencesConfig.USER_TOKEN_KEY, null);
    }

    public String getRefreshToken() {
        return preferences.getString(PreferencesConfig.REFRESH_TOKEN_KEY, null);
    }

    public String getUserServiceToken() {
        return preferences.getString(PreferencesConfig.USER_SERVICE_TOKEN_KEY, null);
    }

    public long getUserServiceId() {
        return preferences.getLong(PreferencesConfig.USER_SERVICE_ID_KEY, 1);
    }

    public float getUserRating() {
        return preferences.getFloat(PreferencesConfig.USER_RATING_KEY, 0);
    }

    public long getUserVoices() {
        return preferences.getLong(PreferencesConfig.USER_VOICES_KEY, 0);
    }

    public ArrayList<String> getUserSearchLocation() {
        Set<String> strings = preferences.getStringSet(PreferencesConfig.USER_SEARCH_LOCATION_KEY, null);
        ArrayList<String> arrayList = new ArrayList<>();
        if (strings != null) {
            for (String string : strings) {
                arrayList.add(string);
            }
        }
        return arrayList;
    }

    public void removeUserSearchLocation() {
        preferences.edit().remove(PreferencesConfig.USER_SEARCH_LOCATION_KEY).apply();
    }

    public void clearUserLocation(){
        preferences.edit().remove(PreferencesConfig.USER_COUNTRY_ID_KEY).apply();
        preferences.edit().remove(PreferencesConfig.USER_COUNTRY_NAME_KEY).apply();
        preferences.edit().remove(PreferencesConfig.USER_REGION_ID_KEY).apply();
        preferences.edit().remove(PreferencesConfig.USER_REGION_NAME_KEY).apply();
        preferences.edit().remove(PreferencesConfig.USER_CITY_ID_KEY).apply();
        preferences.edit().remove(PreferencesConfig.USER_CITY_NAME_KEY).apply();
    }

    public String getToken() {
        String token = instance.getUserToken();
        if (token == null)
            token = instance.getUserServiceToken();
        return token;
    }
}
