package com.stockroompro.fragments;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.stockroompro.Launcher;
import com.stockroompro.R;
import com.stockroompro.adapters.FavouriteAdapter;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.converters.AdvertisementCursorConverter;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by artjoker on 17.04.2015.
 */
public class FavoritesFragment extends AdvertListFragment{
    public static final String BROADCAST_EDIT_MODE_ACTION = "adapter_mode_change";
    public static final String BROADCAST_EDIT_MODE_VALUE = "adapter_mode_change_value";
    public static final String FAVOURITE_REMOVING_ID = "FAVOURITE_REMOVING_ID";
    private FavouriteAdapter adapter;

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Launcher parent = getParent();
        parent.setLocationSpinnerContainer(View.GONE);
        parent.setMainTitle(getString(R.string.favourite_title));
        initLoader(getLoaderManager(), new CommonBundleBuilder());
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(BROADCAST_EDIT_MODE_ACTION));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getAbstractListView().getAdapter() != null) {
                int mode = intent.getIntExtra(BROADCAST_EDIT_MODE_VALUE, FavouritesMode.CLOSE);
                FavouriteAdapter adapter = (FavouriteAdapter) getAbstractListView().getAdapter();
                adapter.setEditMode(mode == FavouritesMode.EDIT);
                getAbstractListView().invalidateViews();
                if (mode == FavouritesMode.DELETE) {
                    for (long addId : adapter.getSelectedId()) {
                        Bundle bundle = new Bundle();
                        bundle.putLong(FAVOURITE_REMOVING_ID, addId);
                        getLoaderManager().restartLoader(LoadersId.ADD_FAVOURITES_LOADER, bundle, FavoritesFragment.this).forceLoad();
                    }
                }
            }
        }
    };

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        manager.restartLoader(LoadersId.FAVOURITES_DB_LOADER, commonBundleBuilder.build(), this).forceLoad();
        manager.initLoader(LoadersId.FAVOURITES_ONLINE_LOADER, commonBundleBuilder.putLong(UID, PersonalData.getInstance(getActivity()).getUserId())
                .putString(TOKEN, PersonalData.getInstance(getActivity()).getToken()).build(), this).forceLoad();
    }

    @Override
    protected void restartLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        manager.restartLoader(LoadersId.FAVOURITES_ONLINE_LOADER, commonBundleBuilder.putLong(UID, PersonalData.getInstance(getActivity()).getUserId())
                .putString(TOKEN, PersonalData.getInstance(getActivity()).getToken()).build(), this).forceLoad();
    }

    @Override
    protected int getAbstractListId() {
        return R.id.lv_favorites_adverts;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favourites;
    }

    @Override
    protected void initListeners(View view) {
        getAbstractListView().setOnItemClickListener(this);
    }

    @Override
    protected void initContent() {
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
    }

    @Override
    protected void initAdapter(AbsListView absListView) {
        adapter = new FavouriteAdapter(getActivity(), null, AdvertisementCursorConverter.class);
        getAbstractListView().setAdapter(adapter);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.SHOW_BUTTON_EDIT;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        isNeedLoad = true;
    }
}
