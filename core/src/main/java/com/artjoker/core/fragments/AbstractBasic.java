package com.artjoker.core.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;

import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.fragments.collections.AnimationCollection;

public abstract class AbstractBasic extends Fragment {
    private OnInteractionListener interactionListener;
    protected View rootView;


    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            interactionListener = (OnInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + OnInteractionListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            // this is the first time onCreateView has been called
            rootView = getViewFromLayout(inflater, container);
            initViews(rootView);
            initAdapters();
            initListeners(rootView);
            initTypefaces(rootView);
        } else {
            // remove previous parent
            ViewParent parent = rootView.getParent();
            if (parent != null)
                ((ViewGroup) parent).removeView(rootView);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initContent();
    }

    /**
     * Always call super in all overridden methods.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    protected View getViewFromLayout(final LayoutInflater inflater, final ViewGroup container) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    protected abstract int getLayoutId();

    protected abstract void initViews(View view);

    protected void initAdapters() {

    }

    protected void initListeners(View view) {

    }

    protected void initTypefaces(View view) {

    }

    protected void initContent() {

    }

    protected void setPrimary(final Fragment fragment, final String backStackTag) {
        if (interactionListenerExist()) {
            interactionListener.onSetPrimary(fragment, backStackTag);
        }
    }

    protected void commit(final Fragment fragment, final String backStackTag, final AnimationCollection animation) {
        if (interactionListenerExist()) {
            interactionListener.onCommit(fragment, backStackTag, animation);
        }
    }

    protected void commit(final Fragment fragment, final AnimationCollection animation) {
        if (interactionListenerExist()) {
            interactionListener.onCommit(fragment, animation);
        }
    }

    protected void matchCommit(final Fragment fragment, final String backStackTag, final AnimationCollection animation) {
        if (interactionListenerExist()) {
            interactionListener.onMatchCommit(fragment, backStackTag, animation);
        }
    }

    protected void popBack() {
        popBackByType(PopBackType.SIMPLE, null);
    }

    protected void popBackToTag(final String backStackTag) {
        popBackByType(PopBackType.TO_TAG, backStackTag);
    }

    protected void sendEvent(final int type) {
        sendEvent(type, null);
    }

    protected void sendEvent(final int type, final Bundle data) {
        if (interactionListenerExist()) {
            interactionListener.onEvent(type, data);
        }
    }

    private void popBackByType(final int type, final String backStackTag) {
        if (interactionListenerExist()) {
            interactionListener.onPopBack(type, backStackTag);
        }
    }

    private boolean interactionListenerExist() {
        return SystemHelper.getInstance().exist(interactionListener);
    }

    public interface PopBackType {
        int SIMPLE = 0x9901;
        int TO_TAG = 0x9902;
        int IMMEDIATE = 0x9903;
    }

    public void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected abstract int getHeaderIconsPolicy();
}