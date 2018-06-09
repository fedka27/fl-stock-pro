package com.stockroompro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 10.04.15.
 */
public class Message {

    @Expose
    private long id;

    @Expose
    @SerializedName("status")
    private int sendingStatus;

    @Expose
    private String title;

    @Expose
    @SerializedName("img")
    private String image;

    @Expose
    @SerializedName("sender_id")
    private long senderId;

    @Expose
    @SerializedName("ad_id")
    private long adId;

    @Expose
    @SerializedName("recipient_id")
    private long recipientId;

    @Expose
    @SerializedName("receiver_id")
    private long receiverId;

    @Expose
    @SerializedName("updated_at")
    private long updatedAt;

    @Expose
    @SerializedName("created_at")
    private long createdAt;

    @Expose
    @SerializedName("parent_id")
    private long parentId;

    @Expose
    @SerializedName("user_id")
    private long userId;

    @Expose
    @SerializedName("interlocator_id")
    private long interlocatorId;

    @Expose
    @SerializedName("interlocator_name")
    private String interlocatorName;

    @Expose
    @SerializedName("new")
    private long newCount;

    @Expose
    @SerializedName("user_name")
    private String userName;

    @Expose
    @SerializedName("temp_id_only_for_unsended")
    private String tempId;

    @Expose
    private String type;

    @Expose
    private String senderName;

    @Expose
    @SerializedName("currency_id")
    private int currencyId;

    @Expose
    private String text;

    @Expose
    private long date;

    @Expose
    private String price;

    @Expose
    private int unsend;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getAdId() {
        return adId;
    }

    public void setAdId(long adId) {
        this.adId = adId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getNewCount() {
        return newCount;
    }

    public void setNewCount(long newCount) {
        this.newCount = newCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSendingStatus() {
        return sendingStatus;
    }

    public void setSendingStatus(int sendingStatus) {
        this.sendingStatus = sendingStatus;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currency) {
        this.currencyId = currency;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getUnsend() {
        return unsend;
    }

    public void setUnsend(int unsend) {
        this.unsend = unsend;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
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

    public long getInterlocatorId() {
        return interlocatorId;
    }

    public void setInterlocatorId(long interlocatorId) {
        this.interlocatorId = interlocatorId;
    }

    public String getInterlocatorName() {
        return interlocatorName;
    }

    public void setInterlocatorName(String interlocatorName) {
        this.interlocatorName = interlocatorName;
    }
}
