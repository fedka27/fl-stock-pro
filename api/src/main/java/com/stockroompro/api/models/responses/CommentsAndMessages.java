package com.stockroompro.api.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stockroompro.models.columns.CommentsColumns;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class CommentsAndMessages implements CommentsColumns {

    @Expose
    private long id;

    @Expose
    @SerializedName(ADVERTISEMENT_ID)
    private long advertisementId;

    @Expose
    @SerializedName(SENDER_ID)
    private long senderId;

    @Expose
    @SerializedName(RECEIVER_ID)
    private long receiverId;

    @Expose
    @SerializedName(PARENT_ID)
    private long parentId;

    @Expose
    @SerializedName("new")
    private int newComment;

    @Expose
    private String text;

    @Expose
    private long date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(long advertisementId) {
        this.advertisementId = advertisementId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
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

    public int getNewComment() {
        return newComment;
    }

    public void setNewComment(int newComment) {
        this.newComment = newComment;
    }
}
