package com.artjoker.core.socialnetworks;

import android.app.Activity;
import android.content.Intent;

public interface ISocialNetwork {

    public static final String SOC_PREF_NAME = "soc_tokens";
    public int SOCIAL_TYPE_FB = 1;
    public int SOCIAL_TYPE_VK = 2;

    public void create(Activity activity);

    public void resume(Activity activity);

    public void result(Activity activity, int requestCode, int resultCode, Intent data);

    public void destroy(Activity activity);

    public void login(Activity activity);

    public int getSocialNetworkType();

    public void setOnStartLoaderListener(OnStartLoaderListener startLoaderListener);

    public interface OnStartLoaderListener {
        public void onStartLoader(String token, long userID, int socNetworkType);
    }

}
