package com.stockroompro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stockroompro.models.columns.CommentsColumns;

/**
 * Created by artjoker on 23.04.2015.
 */
public class Comment implements CommentsColumns {
    public static final int USER_CHOICE_IS_LIKE = 1;
    public static final int USER_CHOICE_IS_DISLIKE = 2;
    public static final int USER_DID_NOT_VOTE = 0;

    @Expose
    private long id
            ;
    @Expose
    @SerializedName("ad_id")
    private long advertId;

    @Expose
    @SerializedName("sender_id")
    private long senderId;

    @Expose
    @SerializedName("parent_id")
    private long parentId;

    @Expose
    private long date;

    @Expose
    private String text;

    @Expose
    @SerializedName("sender_name")
    private String senderName;

    @Expose
    @SerializedName("like_count")
    private int likes;

    @Expose
    @SerializedName("dislike_count")
    private int dislikes;

    @Expose
    @SerializedName("user_choice")
    private int userChoice;

    @Expose
    @SerializedName(CREATED_AT)
    private long createdAt;

    @Expose
    @SerializedName(UPDATED_AT)
    private long updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAdvertId() {
        return advertId;
    }

    public void setAdvertId(long advertId) {
        this.advertId = advertId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(int userChoice) {
        this.userChoice = userChoice;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
