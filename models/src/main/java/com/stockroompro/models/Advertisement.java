package com.stockroompro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stockroompro.models.columns.AdvertisementColumns;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class Advertisement implements AdvertisementColumns {

    public static final int PRICE_TYPE_SELL = 1;
    public static final int PRICE_TYPE_EXCHANGE = 2;
    public static final int PRICE_TYPE_FREE = 3;
    public static final int ADVERT_TYPE_SELL = 1;
    public static final int ADVERT_TYPE_BUY = 2;
    public static final int ADVERT_INACTIVE = 0;
    public static final int ADVERT_ACTIVE = 1;
    public static final int ADVERT_UNAPPROVED = 0;
    public static final int ADVERT_APPROVED = 1;
    public static final int ADVERT_EXPIRED = 1;

    @Expose
    private long id;
    @Expose
    private long type;

    @Expose
    private String title;

    @Expose
    private String description;

    @Expose
    @SerializedName(USER_ID)
    private long userId;

    @Expose
    @SerializedName(CATEGORY_ID)
    private long categoryId;

    @Expose
    @SerializedName(CATEGORY_NAME)
    private String categoryName;

    @Expose
    @SerializedName(PRICE_TYPE)
    private int priceType;

    @Expose
    private float price;

    @Expose
    @SerializedName(CURRENCY_ID)
    private int currencyId;

    @Expose
    private ArrayList<String> phones;
    
    @Expose
    @SerializedName(COUNTRY_ID)
    private long countryId;

    @Expose
    @SerializedName(REGION_ID)
    private long regionId;

    @Expose
    @SerializedName(CITY_ID)
    private long cityId;

    @Expose
    @SerializedName(COUNTRY_NAME)
    private String countryName;

    @Expose
    @SerializedName(REGION_NAME)
    private String regionName;

    @Expose
    @SerializedName(CITY_NAME)
    private String cityName;

    @Expose
    private int used;

    @Expose
    private int bargain;

    @Expose
    private int active;

    @Expose
    private int approved;

    @Expose
    private int expired;

    @Expose
    @SerializedName(RENEWAL_DATE)
    private long renewalDate;

    @Expose
    @SerializedName(EXPIRY_DATE)
    private long expiryDate;

    @Expose
    @SerializedName("options")
    private ArrayList<Integer> filters;

    @Expose
    @SerializedName("options_names")
    private ArrayList<String> filtersNames;

    @Expose
    private ArrayList<String> photos;

    private String photoUrl;

    @Expose
    private int favourite;

    public long getId() {
        return id;
    }

    @Expose
    @SerializedName(CREATED_AT)
    private long createdAt;

    @Expose
    @SerializedName(UPDATED_AT)
    private long updatedAt;

    @Expose
    private String path;

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phone) {
        this.phones = phone;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
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

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getBargain() {
        return bargain;
    }

    public void setBargain(int bargain) {
        this.bargain = bargain;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }

    public long getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(long renewalDate) {
        this.renewalDate = renewalDate;
    }

    public ArrayList<Integer> getFilters() {
        return filters;
    }

    public void setFilters(ArrayList<Integer> filters) {
        this.filters = filters;
    }

    public ArrayList<String> getFiltersNames() {
        return filtersNames;
    }

    public void setFiltersNames(ArrayList<String> filtersNames) {
        this.filtersNames = filtersNames;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int isFavourite() {
        return favourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.favourite = isFavourite;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

