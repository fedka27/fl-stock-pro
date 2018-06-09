package com.stockroompro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stockroompro.models.columns.UserDataColumns;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class UserData implements UserDataColumns {

    @Expose
    private long id;

    @Expose
    @SerializedName(SERVICE_ID)
    private int serviceId;

    @Expose
    @SerializedName(FIRST_NAME)
    private String firstName;

    @Expose
    @SerializedName(LAST_NAME)
    private String lastName;

    @Expose
    private String email;

    @Expose
    @SerializedName(CITY_ID)
    private long cityId;

    @Expose
    private ArrayList<String> phones;

    @Expose
    @SerializedName(REGISTRATION_DATE)
    private long registrationDate;

    @Expose
    @SerializedName(NUMBER_UNREAD_MESSAGES)
    private long numberUnreadMessages;
    @Expose
    @SerializedName(PICTURE_URL)
    private String pictureUrl;

    @Expose
    private float rating;

    @Expose
    private long voices;

    @Expose
    @SerializedName("country_name")
    private String countryName;

    @Expose
    @SerializedName("region_name")
    private String regionName;

    @Expose
    @SerializedName("city_name")
    private String cityName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }

    public long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getVoices() {
        return voices;
    }

    public void setVoices(long voices) {
        this.voices = voices;
    }

    public long getNumberUnreadMessages() {
        return numberUnreadMessages;
    }

    public void setNumberUnreadMessages(long numberUnreadMessages) {
        this.numberUnreadMessages = numberUnreadMessages;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
