package com.artjoker.core.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.artjoker.tool.fragments.collections.AnimationCollection;

public interface OnInteractionListener {

    void onSetPrimary(Fragment fragment, String backStackTag);

    void onCommit(Fragment fragment, String backStackTag, AnimationCollection animation);

    void onCommit(Fragment fragment, AnimationCollection animation);

    void onMatchCommit(Fragment fragment, String backStackTag, AnimationCollection animation);

    void onPopBack(int type, String backStackTag);

    void onEvent(int type, Bundle data);

}