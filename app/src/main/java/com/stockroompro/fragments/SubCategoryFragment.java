package com.stockroompro.fragments;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.SearchView;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.stockroompro.R;
import com.stockroompro.adapters.SubCategoryAdapter;
import com.stockroompro.api.models.requests.SubCategoriesRequest;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.loaders.SearchLoaderCallback;
import com.stockroompro.models.columns.CategoryColumns;
import com.stockroompro.models.contracts.CategoryContract;
import com.stockroompro.models.converters.SubCategoryCursorConverter;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_NAME;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class SubCategoryFragment extends CategoryFragment
        implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, View.OnClickListener {

    public String categoryName;
    private SubCategoryAdapter adapter;

    public static SubCategoryFragment newInstance(long categoryId, String categoryName) {
        SubCategoryFragment fragment = new SubCategoryFragment();
        Bundle args = new Bundle();
        args.putLong(CATEGORY_ID, categoryId);
        args.putString(CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId = getArguments().getLong(CATEGORY_ID);
        categoryName = getArguments().getString(CATEGORY_NAME);
        adapter = new SubCategoryAdapter(getActivity(), null, SubCategoryCursorConverter.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        getParent().setMainTitle(categoryName);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_subcategories;
    }

    @Override
    protected int getAbstractListId() {
        return R.id.subcategories_listView;
    }

    @Override
    protected int getSearchViewId() {
        return R.id.sv_subcategory;
    }

    @Override
    protected void initContent() {
        String searchText = String.format(getResources().getString(R.string.search_by_adverts), categoryName);
        categorySearchView.setQueryHint(searchText);
    }

    @Override
    protected void loadPageWithOffset(int offset, int count, int limit) {

    }

    @Override
    protected void initAdapter(AbsListView absListView) {
        super.initAdapter(absListView);
        absListView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (checkSubCategoryAvailability.getStatus() == AsyncTask.Status.FINISHED) {
            checkSubCategoryAvailability = null;
            checkSubCategoryAvailability = new CheckSubCategoryAvailability(getActivity());
        }

        if (checkSubCategoryAvailability.getStatus() == AsyncTask.Status.PENDING || checkSubCategoryAvailability.getStatus() == AsyncTask.Status.FINISHED) {
            checkSubCategoryAvailability.setCategoryId(((SubCategoryAdapter) parent.getAdapter()).getItem(position).getCategoryId());
            checkSubCategoryAvailability.setCategoryFragment(SubCategoryFragment.this);
            checkSubCategoryAvailability.execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_change_search_type:
                CharSequence[] items = new CharSequence[] {getResources().getString(R.string.search_by_adverts), getResources().getString(R.string.search_by_users)};
                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.select_search_type))
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        searchType = SearchLoaderCallback.SEARCH_BY_ADVERT;
                                        String searchText = String.format(getResources().getString(R.string.searchText), categoryName);
                                        categorySearchView.setQueryHint(searchText);
                                        break;

                                    case 1:
                                        searchType = SearchLoaderCallback.SEARCH_BY_USERS;
                                        categorySearchView.setQueryHint(getResources().getString(R.string.search_by_users));
                                        break;
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
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        manager.initLoader(LoadersId.DB_SUB_CATEGORIES_LOADER_ID, commonBundleBuilder.putLong(CATEGORY_ID, categoryId).build(), this);
        manager.initLoader(LoadersId.NETWORK_SUB_CATEGORIES_LOADER_ID, commonBundleBuilder.putLong(CATEGORY_ID, categoryId).build(), this).forceLoad();
    }

    @Override
    protected void restartLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        manager.restartLoader(LoadersId.DB_SUB_CATEGORIES_LOADER_ID, commonBundleBuilder.putLong(CATEGORY_ID, categoryId).build(), this);
        manager.restartLoader(LoadersId.NETWORK_SUB_CATEGORIES_LOADER_ID, commonBundleBuilder.putLong(CATEGORY_ID, categoryId).build(), this).forceLoad();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.DB_SUB_CATEGORIES_LOADER_ID:
                return new CursorLoader(getActivity(), CategoryContract.CONTENT_URI, null, Config.SELECTION,
                        buildSelectionArgs(args.getLong(CATEGORY_ID)),
                        getAppendLimitSQLString(args, CategoryContract._ID + ", " + CategoryContract.NAME));

            case LoadersId.NETWORK_SUB_CATEGORIES_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new SubCategoriesRequest(getActivity(), args));

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        switch (loader.getId()) {

            case LoadersId.DB_SUB_CATEGORIES_LOADER_ID:
                if (getAbstractListView().getAdapter() != null) {
                    ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor((Cursor) data);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        switch (loader.getId()) {
            case LoadersId.DB_SUB_CATEGORIES_LOADER_ID:
                ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor(null);
                break;
        }
    }

    private interface Config {
        String SELECTION = CategoryColumns.PARENT_ID + "=?";
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }
}
