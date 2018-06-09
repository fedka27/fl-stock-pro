package com.stockroompro.fragments;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.fragments.AbstractBasicListPagination;
import com.artjoker.core.views.ArtJokerSearchView;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.stockroompro.Launcher;
import com.stockroompro.R;
import com.stockroompro.adapters.AdvertSuggestionAdapter;
import com.stockroompro.adapters.UsersSuggestionAdapter;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.fragments.base.BaseApplicationFragment;
import com.stockroompro.fragments.base.BaseApplicationPaginationListFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.SearchLoaderCallback;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.columns.AdvertisementColumns;
import com.stockroompro.models.columns.UserDataColumns;
import com.stockroompro.models.spinners.Location;
import com.stockroompro.utils.AddUserLocationIntoBundle;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Alexandr.Bagach on 28.09.2015.
 */
public class SearchResultsFragment extends BaseApplicationPaginationListFragment
        implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private AdvertSuggestionAdapter advertSuggestionAdapter;
    private UsersSuggestionAdapter userSuggestionAdapter;
    private ListView searchResultsListView;
    private ArtJokerSearchView categorySearchView;
    private ImageView btnChangeSearchType;
    private int searchType;

    public static SearchResultsFragment newInstance(String searchText, long categoryId, int searchType) {
        Bundle args = new Bundle();
        args.putString(SearchLoaderCallback.QUERY_KEY, searchText);
        args.putLong(SearchLoaderCallback.CATEGORY_ID_KEY, categoryId);
        args.putInt(SearchLoaderCallback.SEARCH_TYPE_ID_KEY, searchType);
        SearchResultsFragment fragment = new SearchResultsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_results;
    }

    @Override
    protected void initViews(View view) {
        categorySearchView = (ArtJokerSearchView) view.findViewById(R.id.fragment_search_results_search_view);
        searchResultsListView = (ListView) view.findViewById(getAbstractListId());
        btnChangeSearchType = (ImageView) view.findViewById(R.id.fragment_search_results_change_search_type);
        categorySearchView.setQuery(getArguments().getString(SearchLoaderCallback.QUERY_KEY), true);
        categorySearchView.setIconified(false);
    }

    @Override
    protected void initAdapter(AbsListView absListView) {
        userSuggestionAdapter = new UsersSuggestionAdapter(getActivity(), null, true);
        advertSuggestionAdapter = new AdvertSuggestionAdapter(getActivity(), null, true);
    }

    @Override
    protected int getAbstractListId() {
        return R.id.fragment_search_results_listview;
    }

    @Override
    protected void initListeners(View view) {
        btnChangeSearchType.setOnClickListener(this);
        categorySearchView.setOnQueryTextListener(this);
        searchResultsListView.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Launcher) getActivity()).setLocationSpinnerContainer(View.VISIBLE);
        searchType = getArguments().getInt(SearchLoaderCallback.SEARCH_TYPE_ID_KEY);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Launcher.LOCATION_CHANGED));
        startSearch(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() > 0) {
            startSearch(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {
            startSearch(newText);
        }
        return true;
    }

    public void startSearch() {
        String query = getArguments().getString(SearchLoaderCallback.QUERY_KEY);
        startSearch(query);
    }

    private void startSearch(String query) {
        Bundle args = new Bundle();

        if (PersonalData.getInstance(getActivity()).getUserSearchLocation() != null) {
            args = AddUserLocationIntoBundle.getInstance().addLocation(args);
        }

        if (query != null && !TextUtils.isEmpty(query)) {
            args.putString(SearchLoaderCallback.QUERY_KEY, query);
        } else {
            args.putString(SearchLoaderCallback.QUERY_KEY, getArguments().getString(SearchLoaderCallback.QUERY_KEY));
        }

        switch (searchType) {
            case SearchLoaderCallback.SEARCH_BY_ADVERT:
                searchResultsListView.setAdapter(advertSuggestionAdapter);
                args.putLong(SearchLoaderCallback.CATEGORY_ID_KEY, getArguments().getLong(SearchLoaderCallback.CATEGORY_ID_KEY));
                getLoaderManager().restartLoader(LoadersId.SEARCH_LOADER, args, new SearchLoaderCallback(
                        advertSuggestionAdapter, getActivity()));
                break;

            case SearchLoaderCallback.SEARCH_BY_USERS:
                searchResultsListView.setAdapter(userSuggestionAdapter);
                args.putBoolean(SearchLoaderCallback.SEARCH_BY_USERS_KEY, true);
                getLoaderManager().restartLoader(LoadersId.SEARCH_LOADER, args, new SearchLoaderCallback(
                        userSuggestionAdapter, getActivity()));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (searchResultsListView.getAdapter() instanceof AdvertSuggestionAdapter) {
            Cursor cursor = advertSuggestionAdapter.getCursor();
            if (cursor.moveToPosition(position)) {
                commit(AdvertDetailFragment.newInstance(cursor.getLong(cursor.getColumnIndexOrThrow(AdvertisementColumns.ID)),
                                cursor.getLong(cursor.getColumnIndexOrThrow(AdvertisementColumns.USER_ID))),
                        AdvertDetailFragment.class.getSimpleName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
            }
        } else if (searchResultsListView.getAdapter() instanceof UsersSuggestionAdapter) {
            Cursor cursor = userSuggestionAdapter.getCursor();
            if (cursor.moveToPosition(position)) {
                commit(SellerProfileFragment.newInstance(cursor.getLong(cursor.getColumnIndexOrThrow(UserDataColumns.ID))),
                        AdvertDetailFragment.class.getSimpleName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_search_results_change_search_type:
                CharSequence[] items = new CharSequence[] {getResources().getString(R.string.search_by_adverts), getResources().getString(R.string.search_by_users)};
                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.select_search_type))
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        searchType = SearchLoaderCallback.SEARCH_BY_ADVERT;
                                        categorySearchView.setQueryHint(getResources().getString(R.string.search_by_adverts));
                                        break;

                                    case 1:
                                        searchType = SearchLoaderCallback.SEARCH_BY_USERS;
                                        categorySearchView.setQueryHint(getResources().getString(R.string.search_by_users));
                                        break;
                                }
                                if (searchResultsListView.getAdapter() != null) {
                                    ((CursorAdapter) searchResultsListView.getAdapter()).changeCursor(null);
                                }
                                dialog.dismiss();
                            }
                        }).setPositiveButton(R.string.close_alert, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((CursorAdapter)searchResultsListView.getAdapter()).changeCursor(null);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }


    @Override
    protected void restartLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {

    }

    @Override
    protected void loadPageWithOffset(int offset, int count, int limit) {

    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Launcher.LOCATION_CHANGED)) {
                if (categorySearchView.getQuery().toString().length() > 0) {
                    startSearch(categorySearchView.getQuery().toString());
                }
            }
        }
    };
}
