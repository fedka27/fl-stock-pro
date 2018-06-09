package com.stockroompro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stockroompro.models.columns.NotificationColumns;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class NotificationItem implements NotificationColumns {

    @Expose
    private long id;

    @Expose
    private ArrayList<Long> ids;

    @Expose
    private String text;

    @Expose
    private int type;

    @Expose
    @SerializedName(USER_ID)
    private long userId;

    @Expose
    @SerializedName(AD_ID)
    private long adId;

    @Expose
    private long date;

    @Expose
    @SerializedName(USER_NAME)
    private String userName;

    @Expose
    @SerializedName(USER_PHOTO)
    private String userPhoto;

    @Expose
    @SerializedName(RATING)
    private long rating;

    @Expose
    @SerializedName(COMMENT_TEXT)
    private String commentText;

    @Expose
    private long readStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getAdId() {
        return adId;
    }

    public void setAdId(long adId) {
        this.adId = adId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public long getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(long readStatus) {
        this.readStatus = readStatus;
    }

    public ArrayList<Long> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Long> ids) {
        this.ids = ids;
    }

    public interface NotificationsTypes {
        int GREETING = 1;
        int COMMENT_FOR_MY_ADVERT = 2;
        int RESPONSE_ON_COMMENT = 3;
        int NEW_MESSAGE = 4;
        int COMMENT_LIKE = 5;
        int COMMENT_DISLIKE = 6;
        int RATING = 7;
        int ADVERT_COMMITED = 8;
        int ADVERT_DISMISSED = 9;
        int SYSTEM_INFO = 10;

    }
}
