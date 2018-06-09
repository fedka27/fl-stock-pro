package com.artjoker.core.socialnetworks;

import android.app.Activity;
import android.content.Intent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class SocialManager {
    private List<ISocialNetwork> socialNetworks = new LinkedList<ISocialNetwork>();
    private Iterator<ISocialNetwork> iterator;

    public void addSocialNetwork(ISocialNetwork network) {
        socialNetworks.add(network);
    }


    public void create(Activity activity) {
        iterator = socialNetworks.iterator();
        while (iterator.hasNext())
            iterator.next().create(activity);
    }


    public void resume(Activity activity) {
        iterator = socialNetworks.iterator();
        while (iterator.hasNext())
            iterator.next().resume(activity);

    }


    public void result(Activity activity, int requestCode, int resultCode, Intent data) {
        iterator = socialNetworks.iterator();
        while (iterator.hasNext())
            iterator.next().result(activity, requestCode, resultCode, data);
    }


    public void destroy(Activity activity) {
        iterator = socialNetworks.iterator();
        while (iterator.hasNext())
            iterator.next().destroy(activity);
    }


    public void login(Activity activity, int socialType) {
        iterator = socialNetworks.iterator();
        while (iterator.hasNext()) {
            ISocialNetwork network = iterator.next();
            if (network.getSocialNetworkType() == socialType)
                network.login(activity);
        }

    }

    public void setCallbacks(ISocialNetwork.OnStartLoaderListener onStartLoaderListener) {
        iterator = socialNetworks.iterator();
        while (iterator.hasNext())
            iterator.next().setOnStartLoaderListener(onStartLoaderListener);
    }
}


