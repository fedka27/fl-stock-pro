package com.stockroompro.api.models.responses.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class UserData {

    @Expose
    private long id;

    @Expose
    @SerializedName("service_id")
    private int serviceId;

    @Expose
    @SerializedName("first_name")
    private String firstName;

    @Expose
    @SerializedName("last_name")
    private String lastName;

    @Expose
    private String email;

    @Expose
    @SerializedName("city_id")
    private long cityId;

    @Expose
    private ArrayList<String> phones;

    @Expose
    @SerializedName("reg_date")
    private long registrationDate;

    @Expose
    @SerializedName("unr_msgs_amnt")
    private long numberUnreadMessages;

    @Expose
    @SerializedName("unr_ntcs_amnt")
    private long numberUnreadNotifications;

    @Expose
    @SerializedName("pic")
    private String pictureUrl;

    @Expose
    private float rating;

    @Expose
    private long voices;

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

    public long getNumberUnreadNotifications() {
        return numberUnreadNotifications;
    }

    public void setNumberUnreadNotifications(long numberUnreadNotifications) {
        this.numberUnreadNotifications = numberUnreadNotifications;
    }
}
