package com.stockroompro.fragments;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SearchView;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.views.ArtJokerSearchView;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.stockroompro.R;
import com.stockroompro.adapters.CategoryAdapter;
import com.stockroompro.api.models.requests.CategoriesRequest;
import com.stockroompro.api.models.requests.SubCategoriesRequest;
import com.stockroompro.fragments.base.BaseApplicationPaginationListFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalBroadcastRequestProcessor;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.loaders.SearchLoaderCallback;
import com.stockroompro.models.columns.CategoryColumns;
import com.stockroompro.models.contracts.CategoryContract;
import com.stockroompro.models.converters.CategoryCursorConverter;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;

/**
 * Created by alexsergienko on 18.03.15.
 */
public class CategoryFragment extends BaseApplicationPaginationListFragment
        implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, View.OnClickListener {

    public long categoryId = 0;
    protected int searchType = SearchLoaderCallback.SEARCH_BY_ADVERT;
    private static final String ALL_CATEGORIES_KEY = "all_categories_key";
    protected ArtJokerSearchView categorySearchView;
    protected CheckSubCategoryAvailability checkSubCategoryAvailability;
    private ImageView btnChangeSearchType;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_categories;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSubCategoryAvailability = new CheckSubCategoryAvailability(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getParent().setMainTitle(getString(R.string.main_name));
    }

    @Override
    protected void initViews(View view) {
        categorySearchView = (ArtJokerSearchView) view.findViewById(getSearchViewId());
        btnChangeSearchType = (ImageView) view.findViewById(R.id.button_change_search_type);
    }

    protected int getSearchViewId() {
        return R.id.category_search_view;
    }

    @Override
    protected int getAbstractListId() {
        return R.id.categories_grid_view;
    }

    @Override
    protected void initAdapter(AbsListView absListView) {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int columns = getResources().getInteger(R.integer.columns_in_categories);
        int size = (dm.widthPixels - getResources().getDimensionPixelSize(R.dimen.category_spacing) * (columns - 1)) / columns;
        getAbstractListView().setAdapter(new CategoryAdapter(getActivity(), null, CategoryCursorConverter.class, size));
    }

    @Override
    protected void initListeners(View view) {
        btnChangeSearchType.setOnClickListener(this);
        getAbstractListView().setOnItemClickListener(this);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        categorySearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        categorySearchView.setOnQueryTextListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (checkSubCategoryAvailability.getStatus() == AsyncTask.Status.FINISHED) {
            checkSubCategoryAvailability = null;
            checkSubCategoryAvailability = new CheckSubCategoryAvailability(getActivity());
        }

        if (checkSubCategoryAvailability.getStatus() == AsyncTask.Status.PENDING || checkSubCategoryAvailability.getStatus() == AsyncTask.Status.FINISHED) {
            checkSubCategoryAvailability.setCategoryId(((CategoryAdapter) parent.getAdapter()).getItem(position).getCategoryId());
            checkSubCategoryAvailability.setCategoryFragment(CategoryFragment.this);
            checkSubCategoryAvailability.execute();
        }
    }

    @Override
    protected void loadPageWithOffset(int offset, int count, int limit) {
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        manager.initLoader(LoadersId.DB_CATEGORIES_LOADER_ID, commonBundleBuilder.putLong(CATEGORY_ID, categoryId).build(), this);
        manager.initLoader(LoadersId.NETWORK_CATEGORIES_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.SHOW_BUTTON_ADD;
    }

    @Override
    protected void restartLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        manager.restartLoader(LoadersId.DB_CATEGORIES_LOADER_ID, commonBundleBuilder.putLong(CATEGORY_ID, categoryId).build(), this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.DB_CATEGORIES_LOADER_ID:
                return new CursorLoader(getActivity(), CategoryContract.CONTENT_URI, null, Config.SELECTION,
                        buildSelectionArgs(args.getLong(ALL_CATEGORIES_KEY, categoryId)),
                        getAppendLimitSQLString(args, CategoryContract._ID + ", " + CategoryContract.NAME));

            case LoadersId.NETWORK_CATEGORIES_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new CategoriesRequest(getActivity(), args));

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LoadersId.DB_CATEGORIES_LOADER_ID:
                if (getAbstractListView().getAdapter() != null) {
                    ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor((Cursor) data);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        switch (loader.getId()) {
            case LoadersId.DB_CATEGORIES_LOADER_ID:
                ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor(null);
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() > 0) {
            commit(SearchResultsFragment.newInstance(query, categoryId, searchType),
                    SearchResultsFragment.class.getCanonicalName(), null);
            categorySearchView.setQuery("", false);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 3) {
            commit(SearchResultsFragment.newInstance(newText, categoryId, searchType),
                    SearchResultsFragment.class.getCanonicalName(),
                    AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
            categorySearchView.setQuery("", false);
        }
        return true;
    }

    public String[] buildSelectionArgs(long id) {
        return new String[]{
                String.valueOf(id),
        };
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
                                        categorySearchView.setQueryHint(getResources().getString(R.string.search_by_adverts));
                                        break;

                                    case 1:
                                        searchType = SearchLoaderCallback.SEARCH_BY_USERS;
                                        categorySearchView.setQueryHint(getResources().getString(R.string.search_by_users));
                                        break;
                                }
                                if (categorySearchView.getSuggestionsAdapter() != null) {
                                    categorySearchView.getSuggestionsAdapter().changeCursor(null);
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

    private interface Config {
        String SELECTION = CategoryColumns.PARENT_ID + "=?";
        String NAME_SELECTION = CategoryColumns.ID + "=?";

    }

    @Override
    public void onDetach() {
        super.onDetach();
        checkSubCategoryAvailability.setCategoryFragment(null);
        if (checkSubCategoryAvailability != null && !checkSubCategoryAvailability.isCancelled()) {
            checkSubCategoryAvailability.cancel(true);
        }
    }

    /**
     * asyncTask for checking availability of subcategories
     */
    static class CheckSubCategoryAvailability extends AsyncTask<Void, Void, Cursor> {

        private long categoryId;
        private String categoryName;
        private Context context;
        private CategoryFragment categoryFragment;

        public CheckSubCategoryAvailability(Context context) {
            this.context = context;
        }

        public void setCategoryFragment(CategoryFragment categoryFragment) {
            this.categoryFragment = categoryFragment;
        }

        public void setCategoryId(long categoryId) {
            this.categoryId = categoryId;
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            Bundle args = new Bundle();
            args.putLong(CATEGORY_ID, categoryId);
            LocalBroadcastRequestProcessor localBroadcastRequestProcessor = new LocalBroadcastRequestProcessor(context);

            Cursor c = context.getContentResolver().query(CategoryContract.CONTENT_URI, null, Config.NAME_SELECTION,
                    new String[]{String.valueOf(categoryId)}, null);
            if (c != null && c.moveToFirst()) {
                categoryName = c.getString(c.getColumnIndexOrThrow(CategoryContract.NAME));
                c.close();
            } else {
                categoryName = context.getString(R.string.not_name);
            }
            localBroadcastRequestProcessor.process(new SubCategoriesRequest(context, args));

            return context.getContentResolver().query(CategoryContract.CONTENT_URI, null, Config.SELECTION,
                    new String[]{String.valueOf(categoryId)}, null);
        }

        @Override
        protected void onCancelled(Cursor cursor) {
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            try {
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    categoryFragment.commit(SubCategoryFragment.newInstance(categoryId, categoryName),
                            SubCategoryFragment.class.getCanonicalName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                } else {
                    categoryFragment.commit(AdvertListFragment.newInstance(categoryId, categoryName), AdvertListFragment.class.getCanonicalName(),
                            null);
                }
            } finally {
                cursor.close();
            }
        }
    }
}
