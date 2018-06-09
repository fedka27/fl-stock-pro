package com.stockroompro.fragments;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.views.ArtJokerSearchView;
import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.stockroompro.R;
import com.stockroompro.adapters.MyAdvertAdapter;
import com.stockroompro.api.models.requests.AllUserAdsRequest;
import com.stockroompro.database.DatabaseProvider;
import com.stockroompro.fragments.base.BaseApplicationPaginationListFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.loaders.SearchLoaderCallback;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.converters.AdvertisementCursorConverter;
import com.stockroompro.utils.ContentProviderConfig;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by user on 18.05.15.
 */
public class MyAdvertisementsFragment extends BaseApplicationPaginationListFragment
        implements SearchView.OnQueryTextListener, View.OnClickListener,
        AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks {
    protected ArtJokerSearchView advertSearchView;
    private MyAdvertAdapter adapter;
    protected ArtJokerTextView noNotificationHint;
    private Bundle myAdvertBundle;
    public static final String DEFAULT_QUERY = "";
    private boolean isSearchEnabled = false;
    private Bundle searchBundle;

    @Override
    public void onResume() {
        super.onResume();
        getParent().setMainTitle(getString(R.string.my_advert_title));
    }

    @Override
    protected void loadPageWithOffset(int offset, int count, int limit) {

    }

    @Override
    protected void initAdapter(AbsListView absListView) {
        adapter = new MyAdvertAdapter(getActivity(), null, AdvertisementCursorConverter.class);
        getAbstractListView().setAdapter(adapter);
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        commonBundleBuilder.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
        commonBundleBuilder.putLong(UID, PersonalData.getInstance(getActivity()).getUserId());
        myAdvertBundle = commonBundleBuilder.build();
        manager.initLoader(LoadersId.DB_ADVERTISEMENT_LOADER_ID, commonBundleBuilder.build(), this);
        manager.initLoader(LoadersId.NETWORK_USER_ADS_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
    }

    @Override
    protected void restartLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        manager.restartLoader(LoadersId.DB_ADVERTISEMENT_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
        manager.restartLoader(LoadersId.NETWORK_USER_ADS_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
    }

    @Override
    protected int getAbstractListId() {
        return R.id.lv_adverts_account;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account_advert_list;
    }

    @Override
    protected void initViews(View view) {
        advertSearchView = (ArtJokerSearchView) view.findViewById(R.id.sv_by_atv_account);
        noNotificationHint = (ArtJokerTextView) view.findViewById(R.id.tv_no_chats);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.SHOW_BUTTON_ADD;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.DB_ADVERTISEMENT_LOADER_ID:
                return new CursorLoader(getActivity(), AdvertisementContract.CONTENT_URI, null,
                        AdvertisementContract.USER_ID + " = ? ",
                        new String[]{String.valueOf(PersonalData.getInstance(getActivity()).getUserId())}, AdvertisementContract.CREATED_AT + " DESC");

            case LoadersId.NETWORK_USER_ADS_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new AllUserAdsRequest(getActivity(), args));

            case LoadersId.DB_MESSAGES_SEARCH_LOADER_ID:
                return new CursorLoader(getActivity(), Uri.withAppendedPath(ContentProviderConfig.BASE_CONTENT_URI, AdvertisementContract.SEARCH_ADVERT_URI),
                        null, null, buildSelectionArgs(args), null);
        }
        return null;
    }

    private String buildSelection() {
        return AdvertisementContract.USER_ID + " = ? ";
    }

    private String[] buildSelectionArgs(Bundle args) {
        return new String[]{args.getString(SearchLoaderCallback.QUERY_KEY), String.valueOf(PersonalData.getInstance(getActivity()).getUserId())};
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {

            case LoadersId.DB_ADVERTISEMENT_LOADER_ID:
                if (getAbstractListView().getAdapter() != null) {
                    Cursor cursor = (Cursor) data;
                    if (!cursor.isClosed())
                        if (cursor.moveToFirst()) {
                            noNotificationHint.setVisibility(View.GONE);
                            DatabaseProvider.printLogs(true);
                            DatabaseProvider.logCursor(cursor);
                            adapter.changeCursor(cursor);
                        } else {
                            noNotificationHint.setVisibility(View.VISIBLE);
                        }
                }
                break;
            case LoadersId.DB_MESSAGES_SEARCH_LOADER_ID:
                if (getAbstractListView().getAdapter() != null) {
                    if (!((Cursor) data).isClosed()) {
                        ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor((Cursor) data);
                    }

                }
                break;
        }
    }

    @Override
    protected void initContent() {
        noNotificationHint.setVisibility(View.GONE);
    }

    @Override
    protected void initListeners(View view) {
        ((ListView) view.findViewById(R.id.lv_adverts_account)).setOnItemClickListener(this);
        getAbstractListView().setOnItemClickListener(this);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        advertSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        advertSearchView.setOnQueryTextListener(this);
    }

    private void startSearchLoader() {
        if (getLoaderManager().getLoader(LoadersId.DB_MESSAGES_SEARCH_LOADER_ID) != null) {
            getLoaderManager().restartLoader(LoadersId.DB_MESSAGES_SEARCH_LOADER_ID, searchBundle, this).forceLoad();
        } else {
            getLoaderManager().initLoader(LoadersId.DB_MESSAGES_SEARCH_LOADER_ID, searchBundle, this).forceLoad();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        commit(AdvertDetailFragment.newInstance(((MyAdvertAdapter) parent.getAdapter()).getItem(position).getAdvertisementId(),
                PersonalData.getInstance(getActivity()).getUserId()), AdvertDetailFragment.class.getCanonicalName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        isSearchEnabled = true;
        searchBundle = new CommonBundleBuilder().putString(SearchLoaderCallback.QUERY_KEY, query).build();
        startSearchLoader();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        isSearchEnabled = true;
        searchBundle = new CommonBundleBuilder().putString(SearchLoaderCallback.QUERY_KEY, newText).build();
        startSearchLoader();
        return true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount - (firstVisibleItem + visibleItemCount) <= 1 && lastTotalItemCount != totalItemCount) {
            lastTotalItemCount = totalItemCount;
            loadPageWithOffset(totalItemCount, visibleItemCount * 2, visibleItemCount * 2 + totalItemCount);
            if (!isSearchEnabled) {
                restartLoader(getLoaderManager(), new CommonBundleBuilder(myAdvertBundle).putIntOffset(totalItemCount)
                        .putIntLimit(totalItemCount + visibleItemCount * 2)
                        .putIntCount(visibleItemCount * 2));
            }
        }
    }
}
