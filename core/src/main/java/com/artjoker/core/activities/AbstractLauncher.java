package com.artjoker.core.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.artjoker.core.fragments.OnInteractionListener;
import com.artjoker.tool.fragments.CompositeFragmentManager;
import com.artjoker.tool.fragments.collections.AnimationCollection;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.artjoker.core.fragments.AbstractBasic.PopBackType;


public abstract class AbstractLauncher extends SocialActivity implements OnInteractionListener, CompositeFragmentManager.Callback {
    protected CompositeFragmentManager fragmentManager;
    private ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDependencies();
        setContentView(getContentViewLayoutResId());
        initViews();
        initListeners();
        initContent();
        fragmentManager = new CompositeFragmentManager(getFragmentManager(), getContentViewContainerId());
        fragmentManager.setCallback(this);
        if (savedInstanceState == null) {
            initState();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragmentManager.getCurrent().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public final void onSetPrimary(Fragment fragment, String backStackTag) {
        if (backStackTag != null) {
            fragmentManager.setPrimary(fragment, backStackTag);
        } else {
            fragmentManager.setPrimary(fragment);
        }
    }

    // @Override
    public final void onCommit(Fragment fragment, String backStackTag, AnimationCollection animation) {
        fragmentManager.commit(fragment, backStackTag, animation);
    }

    // @Override
    public final void onCommit(Fragment fragment, AnimationCollection animation) {
        fragmentManager.commit(fragment, animation);
    }

    // @Override
    public final void onMatchCommit(Fragment fragment, String backStackTag, AnimationCollection animation) {
        if (fragment != null && !fragmentManager.matchCurrent(fragment)) {
            fragmentManager.commit(fragment, backStackTag, animation);
        }
    }

    @Override
    public final void onPopBack(int event, String backStackTag) {
        switch (event) {
            case PopBackType.SIMPLE:
                fragmentManager.popBack();
                break;
            case PopBackType.TO_TAG:
                fragmentManager.popBack(backStackTag);
                break;
            case PopBackType.IMMEDIATE:
                fragmentManager.immediatePopBack();
                break;
        }
    }

    protected final void onEvent(int type) {
        onEvent(type, null);
    }

    protected final void showContainer() {
        container.setVisibility(VISIBLE);
    }

    protected final void hideContainer() {
        container.setVisibility(GONE);
    }

    protected abstract void initDependencies();

    protected void initViews() {
        container = (ViewGroup) findViewById(getContentViewContainerId());
    }

    protected void initListeners() {

    }

    protected void initContent() {

    }

    protected void initState() {
        commitInitFragment(getInitFragment());
    }

    protected final void commitInitFragment(Fragment fragment) {
        fragmentManager.initCommit(fragment, null);
    }

    protected abstract int getContentViewLayoutResId();

    protected abstract int getContentViewContainerId();

    protected abstract Fragment getInitFragment();

    protected void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}