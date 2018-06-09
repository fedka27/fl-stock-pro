package com.artjoker.core.fragments;

import android.app.LoaderManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.artjoker.core.CommonBundleBuilder;

/**
 * Created by alexsergienko on 18.03.15.
 */
public abstract class AbstractBasicList extends AbstractBasic implements LoaderManager.LoaderCallbacks {

    private AbsListView abstractListView;

    public AbsListView getAbstractListView() {
        return abstractListView;
    }

    private boolean isFirstLaunch = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = getViewFromLayout(inflater, container);
        initAbsListViews(view);
        initViews(view);
        initAdapter(abstractListView);
        initAdapters();
        initListeners(view);
        initTypefaces(view);
        return view;
    }

    @IdRes
    protected int getAbstractListId() {
        return android.R.id.list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLoader(getLoaderManager(), new CommonBundleBuilder());

    }

    protected void initAbsListViews(View view) {
        abstractListView = (AbsListView) view.findViewById(getAbstractListId());
    }

    /**
     * Use this method for setup adapter. Do not use for creation adapter, use onCreate instead for create instance of adapter.
     */
    protected abstract void initAdapter(AbsListView absListView);

    protected abstract void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder);

    public boolean isFirstLaunch() {
        return isFirstLaunch;
    }

    public void setFirstLaunch(boolean firstLaunch) {
        isFirstLaunch = firstLaunch;
    }
}
