package com.stockroompro.api.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stockroompro.models.NotificationItem;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class Notification {

    @Expose
    @SerializedName("date")
    private ArrayList<NotificationItem> notificationItemArrayList;

    public ArrayList<NotificationItem> getChargesAndPayments() {
        return notificationItemArrayList;
    }

    public void setChargesAndPayments(ArrayList<NotificationItem> notificationItemArrayList) {
        this.notificationItemArrayList = notificationItemArrayList;
    }
}
