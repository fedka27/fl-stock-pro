package com.artjoker.core.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.artjoker.core.socialnetworks.ISocialNetwork;
import com.artjoker.core.socialnetworks.SocialManager;


public abstract class SocialActivity extends Activity {
    protected SocialManager socialManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socialManager = new SocialManager();
        initSocialNetworks();
        socialManager.create(this);
    }

    protected abstract void initSocialNetworks();

    @Override
    protected void onResume() {
        super.onResume();
        if(socialManager!=null)
        socialManager.resume(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(socialManager!=null)
            socialManager.result(this,requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(socialManager!=null)
        socialManager.destroy(this);
    }
    public void login(int socialNetworkType){
        if(socialManager!=null)
            socialManager.login(this,socialNetworkType);
    }
    public void setCallbacks(ISocialNetwork.OnStartLoaderListener onStartLoaderListener){
        socialManager.setCallbacks(onStartLoaderListener);
    }

}
