package com.stockroompro.socialnetworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.artjoker.core.socialnetworks.ISocialNetwork;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;


public class VKNetwork implements ISocialNetwork {
    private static final String APP_ID = "6004532"; //old 5742873
    public static final String TOKEN_KEY = "VK_ACCESS_TOKEN";
    private static String[] myScope = new String[]{VKScope.FRIENDS,
            VKScope.WALL, VKScope.PHOTOS};
    private Context context;
    private OnStartLoaderListener startLoaderListener;

    @Override
    public void create(Activity activity) {
        this.context = activity;
        VKUIHelper.onCreate(activity);
        VKSdk.initialize(sdkListener, APP_ID, VKAccessToken.tokenFromSharedPreferences(activity, TOKEN_KEY));
    }

    private VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();//fd
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(myScope);
        }

        @Override
        public void onAccessDenied(VKError authorizationError) {
            new AlertDialog.Builder(context).setMessage(
                    authorizationError.errorMessage).show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            newToken.saveTokenToSharedPreferences(context, TOKEN_KEY);
            if (startLoaderListener != null) {
                startLoaderListener.onStartLoader(newToken.accessToken, Integer.parseInt(newToken.userId), SOCIAL_TYPE_VK);
            }
            SharedPreferences preferences = context.getSharedPreferences(SOC_PREF_NAME, Context.MODE_PRIVATE);
            preferences.edit().putString(TOKEN_KEY, newToken.accessToken).apply();
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {

        }
    };

    public static String getSavedToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SOC_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(TOKEN_KEY, null);
    }

    @Override
    public void destroy(Activity activity) {
        VKUIHelper.onDestroy(activity);
        startLoaderListener = null;
    }

    @Override
    public void login(Activity activity) {
        VKSdk.authorize(myScope, true, false);
    }

    @Override
    public int getSocialNetworkType() {
        return SOCIAL_TYPE_VK;
    }

    @Override
    public void setOnStartLoaderListener(ISocialNetwork.OnStartLoaderListener startLoaderListener) {
        this.startLoaderListener = startLoaderListener;
    }

    @Override
    public void resume(Activity activity) {
        VKUIHelper.onResume(activity);
    }

    @Override
    public void result(Activity activity, int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(activity, requestCode, resultCode, data);
    }


}
