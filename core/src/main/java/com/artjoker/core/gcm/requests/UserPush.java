package com.artjoker.core.gcm.requests;

import com.artjoker.core.gcm.requests.Push;
import com.artjoker.tool.core.SystemHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class UserPush extends Push {

    @Expose
    @SerializedName("reg_id")
    private final String deviceId;

    @Expose
    @SerializedName("user_id")
    private final long userId;

    public UserPush(long userId, String key) {
        super(key);
        this.deviceId = SystemHelper.getInstance().getUniqueDeviceId();
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public long getUserId() {
        return userId;
    }

}