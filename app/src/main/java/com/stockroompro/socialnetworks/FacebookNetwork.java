package com.stockroompro.socialnetworks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.artjoker.core.socialnetworks.ISocialNetwork;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;


public class FacebookNetwork implements FacebookCallback<LoginResult>,ISocialNetwork {
    public static final String  EMAIL = "email";
    public static final String TOKEN_KEY = "FB_ACCESS_TOKEN";
    private CallbackManager callbackManager;
    private OnStartLoaderListener startLoaderListener;
    private Context context;

    public void create(Activity activity){
        context = activity;
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,this);
    }

    @Override
    public void resume(Activity activity) {

    }

    @Override
    public void result(Activity activity,int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void destroy(Activity activity) {
        startLoaderListener = null;
    }

    @Override
    public void login(Activity activity) {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(EMAIL));
    }

    @Override
    public int getSocialNetworkType() {
    return SOCIAL_TYPE_FB;
    }

    @Override
    public void setOnStartLoaderListener(OnStartLoaderListener startLoaderListener) {
        this.startLoaderListener = startLoaderListener;
    }


    @Override
    public void onSuccess(LoginResult loginResult) {

        if (startLoaderListener != null) {
            startLoaderListener.onStartLoader(AccessToken.getCurrentAccessToken().getToken(),Long.parseLong(AccessToken.getCurrentAccessToken().getUserId()), SOCIAL_TYPE_FB);
        }
        SharedPreferences preferences = context.getSharedPreferences(SOC_PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(TOKEN_KEY, AccessToken.getCurrentAccessToken().getToken()).apply();

    }
    public static String getSavedToken(Context context){
        SharedPreferences preferences = context.getSharedPreferences(SOC_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(TOKEN_KEY,null);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException e) {

    }

}
