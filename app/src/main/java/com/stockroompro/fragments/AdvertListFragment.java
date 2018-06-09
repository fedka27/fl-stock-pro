package com.stockroompro.fragments;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.network.RequestDescriptorLoader;
import com.artjoker.core.views.ArtJokerSearchView;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.stockroompro.Launcher;
import com.stockroompro.R;
import com.stockroompro.StockRoomBundleBuilderArgs;
import com.stockroompro.adapters.AdvertAdapter;
import com.stockroompro.adapters.AdvertSuggestionAdapter;
import com.stockroompro.api.models.requests.AddOrRemoveFavouritesRequest;
import com.stockroompro.api.models.requests.AdvertisementRequest;
import com.stockroompro.api.models.requests.FavouritesRequest;
import com.stockroompro.api.models.requests.FiltersRequest;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.api.models.requests.SearchAdvertisementRequest;
import com.stockroompro.fragments.FiltersFragment.OnFiltersChangeListener;
import com.stockroompro.fragments.base.BaseApplicationPaginationListFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.loaders.SearchLoaderCallback;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.Settings;
import com.stockroompro.models.columns.AdvertFiltersColumns;
import com.stockroompro.models.columns.AdvertisementColumns;
import com.stockroompro.models.contracts.AdvertFiltersContract;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.converters.AdvertisementCursorConverter;
import com.stockroompro.utils.AddUserLocationIntoBundle;

import java.util.ArrayList;

import static android.support.v4.view.GravityCompat.END;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CITY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.QUERY;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.REGION_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;


/**
 * Created by bagach.alexandr on 31.03.15.
 */
public class AdvertListFragment extends BaseApplicationPaginationListFragment implements AdapterView.OnItemClickListener,
        View.OnClickListener, SearchView.OnQueryTextListener, DrawerLayout.DrawerListener, OnFiltersChangeListener {

    private static final String CATEGORY_ID = "category_id";
    private static final String CATEGORY_NAME_KEY = "category_name_key";
    public long categoryId = 0;

    private ListView advertsListView;
    private ImageView changeListType;
    private ArtJokerSearchView advertSearchView;
    private AdvertAdapter advertAdapter;
    private AdvertSuggestionAdapter advertSuggestionAdapter;
    private DrawerLayout filtersDrawer;
    public String categoryName;
    private FiltersFragment filters;
    private Bundle filtersValuesBundle;
    private View empty;
    private boolean isSearchEnable = false;
    private TextView searchText;

    private boolean isLoading = false;
    protected boolean isNeedLoad = true;
    private Cursor lastCursor;

    public static AdvertListFragment newInstance(long categoryId, String name) {
        AdvertListFragment fragment = new AdvertListFragment();
        Bundle args = new Bundle();
        args.putLong(CATEGORY_ID, categoryId);
        args.putString(CATEGORY_NAME_KEY, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        categoryId = getArguments().getLong(CATEGORY_ID);
        categoryName = getArguments().getString(CATEGORY_NAME_KEY);
        advertAdapter = new AdvertAdapter(getActivity(), null, AdvertisementCursorConverter.class);
        advertSuggestionAdapter = new AdvertSuggestionAdapter(getActivity(), null, true);
        filters = FiltersFragment.newInstance(categoryId, categoryName);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("onActivityCreated", "onActivityCreated");
//        setFirstLaunch(false);
    }

    @Override
    public void onResume() {
        if (filtersDrawer != null) {
            if (!filters.isAdded()) {
                if (getActivity().findViewById(R.id.filters_container) != null) {
                    Log.e("filters_container", getActivity().findViewById(R.id.filters_container).toString());
                    // TODO: 13.06.2017 fix bug, but create many other
                    getChildFragmentManager().beginTransaction().replace(R.id.filters_container, filters).commit();
                    filters.setOnFiltersChangeListener(this);
                }
            }
            if (getActivity() != null) {
                ((Launcher) getActivity()).setFiltersSelected(filtersDrawer.isDrawerVisible(END));
            }
        }

        if (advertSearchView != null) {
            advertSearchView.setOnQueryTextListener(this);
        }

        ((Launcher) getActivity()).setLocationSpinnerContainer(View.VISIBLE);
        super.onResume();

        getParent().setMainTitle(categoryName);
    }

    @Override
    public void onPause() {
        if (filtersDrawer != null && !filters.isDetached()) {
            filters.setOnFiltersChangeListener(null);
            getChildFragmentManager().beginTransaction().remove(filters).commit();
        }
//        destroyLoaders();
        super.onPause();
    }

    private void destroyLoaders() {
        getLoaderManager().destroyLoader(LoadersId.NETWORK_ADERTS_LOADER_BY_FILTERS_ID);
        getLoaderManager().destroyLoader(LoadersId.DB_ADVERTISEMENT_LOADER_PAGINATION_ID);
        getLoaderManager().destroyLoader(LoadersId.NETWORK_ADVERTISEMENT_LOADER_ID);
        getLoaderManager().destroyLoader(LoadersId.ADD_FAVOURITES_LOADER);
        getLoaderManager().destroyLoader(LoadersId.FAVOURITES_DB_LOADER);
        getLoaderManager().destroyLoader(LoadersId.FAVOURITES_ONLINE_LOADER);
        getLoaderManager().destroyLoader(LoadersId.NETWORK_FILTERS_LOADER_ID);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_adverts_list_with_filters;
    }

    @Override
    protected int getAbstractListId() {
        return R.id.lv_adverts;
    }

    @Override
    protected void initViews(View view) {
        advertsListView = (ListView) view.findViewById(R.id.lv_adverts);
        changeListType = (ImageView) view.findViewById(R.id.button_filters);
        advertSearchView = (ArtJokerSearchView) view.findViewById(R.id.sv_by_atv);
        filtersDrawer = ((DrawerLayout) view.findViewById(R.id.drawer_filters));
        searchText = (TextView) view.findViewById(R.id.fragment_adverts_search_text);
        empty = view.findViewById(android.R.id.empty);
        if (filtersDrawer != null) {
            filtersDrawer.setDrawerListener(this);
        }
    }

    @Override
    protected void loadPageWithOffset(int offset, int count, int limit) {

    }

    @Override
    protected void initAdapter(AbsListView absListView) {
        getAbstractListView().setAdapter(advertAdapter);
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void initContent() {

        String searchText = String.format(getResources().getString(R.string.search_by_adverts), categoryName);
        advertSearchView.setQueryHint(searchText);
    }

    @Override
    protected void initListeners(View view) {
        getAbstractListView().setOnItemClickListener(this);
        changeListType.setOnClickListener(this);
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {

        if (!isSearchEnable) {
            if (PersonalData.getInstance(getActivity()).getUserSearchLocation() != null) {
                commonBundleBuilder = new CommonBundleBuilder(addLocationIntoBundle(commonBundleBuilder.build()));
            }

            if (filtersValuesBundle != null) {
                commonBundleBuilder.build().putAll(filtersValuesBundle);
            }
            if (isNeedLoad) {
                Log.e("!!!", "initLoader");
                manager.restartLoader(LoadersId.NETWORK_ADVERTISEMENT_LOADER_ID,
                        commonBundleBuilder.putLong(CATEGORY_ID, categoryId).build(),
                        this).forceLoad();
            } else {
                ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor(lastCursor);
                //getLoaderManager().destroyLoader(LoadersId.NETWORK_ADERTS_LOADER_BY_FILTERS_ID);
                //   destroyLoaders();
                //  isNeedLoad = true;
            }

//          manager.restartLoader(LoadersId.DB_ADVERTISEMENT_LOADER_ID, commonBundleBuilder
//                    .putLong(CATEGORY_ID, categoryId)
//                    .putBoolean(StockRoomBundleBuilderArgs.ORDER_BY_LAST_ADDED, true)
//                    .build(), this).forceLoad();
        }
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.SHOW_BUTTON_ADD | HeaderIconsPolicy.SHOW_BUTTON_FILTERS;
    }

    @Override
    protected void restartLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        Log.e("!!!", "restartLoader: ");
        if (PersonalData.getInstance(getActivity()).getUserSearchLocation() != null) {
            commonBundleBuilder = new CommonBundleBuilder(addLocationIntoBundle(commonBundleBuilder.build()));
        }
        int networkAdertsLoaderByFiltersId;
        if (filtersValuesBundle != null) {
            commonBundleBuilder.build().putAll(filtersValuesBundle);
            networkAdertsLoaderByFiltersId = LoadersId.NETWORK_ADERTS_LOADER_BY_FILTERS_ID;
        } else {
            networkAdertsLoaderByFiltersId = LoadersId.NETWORK_ADERTS_LOADER_BY_PAGINATION_ID;
        }
//        manager.restartLoader(LoadersId.DB_ADVERTISEMENT_LOADER_ID, commonBundleBuilder.putLong(CATEGORY_ID, categoryId).build(), this);
        manager.restartLoader(networkAdertsLoaderByFiltersId, commonBundleBuilder.putLong(CATEGORY_ID, categoryId).build(), this).forceLoad();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount - (firstVisibleItem + visibleItemCount) <= 1 && lastTotalItemCount != totalItemCount) {
            lastTotalItemCount = totalItemCount;
            loadPageWithOffset(totalItemCount, visibleItemCount * 2, visibleItemCount * 2 + totalItemCount);
            if (!isSearchEnable && !isLoading && filtersValuesBundle == null) {
                restartLoader(getLoaderManager(), new CommonBundleBuilder(filtersValuesBundle).putIntOffset(totalItemCount)
                        .putIntLimit(totalItemCount + visibleItemCount * 2)
                        .putIntCount(visibleItemCount * 2));
            }
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.NETWORK_ADERTS_LOADER_BY_FILTERS_ID:
                Log.e("!!!", "onCreateLoader: NETWORK_ADERTS_LOADER_BY_FILTERS_ID");
                isLoading = true;
                searchText.setVisibility(View.VISIBLE);
                return new LocalRequestDescriptorLoader(getActivity(),
                        new SearchAdvertisementRequest(getActivity(), args));
            case LoadersId.NETWORK_ADERTS_LOADER_BY_PAGINATION_ID:
                Log.e("!!!", "onCreateLoader: NETWORK_ADERTS_LOADER_BY_PAGINATION_ID");
                isLoading = true;
                return new LocalRequestDescriptorLoader(getActivity(),
                        new SearchAdvertisementRequest(getActivity(), args));

            case LoadersId.DB_ADVERTISEMENT_LOADER_ID:
                Log.e("!!!", "onCreateLoader: DB_ADVERTISEMENT_LOADER_ID");
                searchText.setVisibility(View.VISIBLE);
            case LoadersId.DB_ADVERTISEMENT_LOADER_PAGINATION_ID:
                Log.e("!!!", "onCreateLoader: DB_ADVERTISEMENT_LOADER_PAGINATION_ID");
                isLoading = true;
                addLocationWhileFilteringIntoBundle(args);
                return new CursorLoader(getActivity(),
                        AdvertisementContract.CONTENT_URI,
                        new String[]{"*",
                                Settings.getInstance(getActivity()).getIndependentProjectionPriceField() + " as " + AdvertisementContract.INDEPENDENT_PRICE},
                        buildSelection(args),
                        buildSelectionArgs(args),
                        getAppendLimitSQLString(args, getOrder(args)));

            case LoadersId.NETWORK_ADVERTISEMENT_LOADER_ID:
                Log.e("!!!", "onCreateLoader: NETWORK_ADVERTISEMENT_LOADER_ID");
                isLoading = true;
                return new LocalRequestDescriptorLoader(getActivity(), new AdvertisementRequest(getActivity(), args));

            case LoadersId.NETWORK_FILTERS_LOADER_ID:
                Log.e("!!!", "onCreateLoader: NETWORK_FILTERS_LOADER_ID");
                return new LocalRequestDescriptorLoader(getActivity(), new FiltersRequest(getActivity(), args));

            case LoadersId.FAVOURITES_ONLINE_LOADER:
                Log.e("!!!", "onCreateLoader: FAVOURITES_ONLINE_LOADER");
                return new LocalRequestDescriptorLoader(getActivity(), new FavouritesRequest(getActivity(), args));

            case LoadersId.FAVOURITES_DB_LOADER:
                Log.e("!!!", "onCreateLoader: FAVOURITES_DB_LOADER");
                return new CursorLoader(getActivity().getApplicationContext(), AdvertisementContract.CONTENT_URI, null,
                        AdvertisementColumns.FAVOURITE + " = 1", null, null);

            case LoadersId.ADD_FAVOURITES_LOADER:
                Log.e("!!!", "onCreateLoader: ADD_FAVOURITES_LOADER");
                Bundle bundle = new Bundle();
                bundle.putLong(AD_ID, args.getLong(FavoritesFragment.FAVOURITE_REMOVING_ID));
                bundle.putLong(UID, PersonalData.getInstance(getActivity()).getUserId());
                bundle.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
                AddOrRemoveFavouritesRequest addToFavouritesRequest = new AddOrRemoveFavouritesRequest(getActivity().getApplicationContext(), bundle);
                return new RequestDescriptorLoader(getActivity().getApplicationContext(), addToFavouritesRequest);
        }
        return null;
    }

    public static Cursor cloneCursor(Cursor oldCursor) {

        if (oldCursor == null) {

            return null;

        } else {

            /**
             * Remember the cursor position
             */
            int originalCursorPosition = oldCursor.getPosition();

            String[] projection = oldCursor.getColumnNames();
            MatrixCursor newCursor = new MatrixCursor(projection);

            int numColumns = oldCursor.getColumnCount();

            while (oldCursor.moveToNext()) {

                /**
                 * Create the new row object
                 */
                Object[] newRow = new Object[numColumns];

                /**
                 * Populate each column in the new row
                 */
                for (int columnIndex = 0; columnIndex < numColumns; columnIndex++) {

                    /**
                     * Detect the field type
                     */
                    int fieldType = oldCursor.getType(columnIndex);

                    /**
                     * Use the field type to populate the row correctly
                     */
                    if (fieldType == Cursor.FIELD_TYPE_BLOB) {
                        newRow[columnIndex] = oldCursor.getBlob(columnIndex);
                    } else if (fieldType == Cursor.FIELD_TYPE_FLOAT) {
                        newRow[columnIndex] = oldCursor.getDouble(columnIndex);
                    } else if (fieldType == Cursor.FIELD_TYPE_INTEGER) {
                        newRow[columnIndex] = oldCursor.getLong(columnIndex);
                    } else if (fieldType == Cursor.FIELD_TYPE_STRING) {
                        newRow[columnIndex] = oldCursor.getString(columnIndex);
                    } else if (fieldType == Cursor.FIELD_TYPE_NULL) {
                        newRow[columnIndex] = null;
                    } else {
                        throw new RuntimeException("Unknown fieldType (" + fieldType + ") for column" + columnIndex);
                    }

                }

                /**
                 * Add the new row to the new cursor
                 */
                newCursor.addRow(newRow);

            }

            /**
             * Move both cursors to the position that oldCursor was in before this method was called
             */
            oldCursor.moveToPosition(originalCursorPosition);
            newCursor.moveToPosition(originalCursorPosition);

            /**
             * Return the cloned cursor
             */
            return newCursor;

        }

    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        int id = -1;

        if (isNeedLoad) {
            int loaderId = loader.getId();
            Log.e("!!!", "onLoadFinished " + loaderId);
            switch (loaderId) {
                case LoadersId.DB_ADVERTISEMENT_LOADER_ID:
                case LoadersId.DB_ADVERTISEMENT_LOADER_PAGINATION_ID:
                case LoadersId.FAVOURITES_DB_LOADER:
                    Log.e("!!!", "onLoadFinished  = " + loader.getId());
                    if (getAbstractListView() != null) {
                        ListAdapter adapter = getAbstractListView().getAdapter();
                        if (adapter != null && (adapter instanceof AdvertAdapter ||
                                adapter instanceof AdvertSuggestionAdapter)) {

                            Cursor cursor = (Cursor) data;
                            if (!cursor.isClosed()) {
                                Log.e("!!!", "cursor size = " + ((Cursor) data).getCount());
                                lastCursor = cloneCursor(cursor);
                                ((CursorAdapter) adapter).changeCursor(cursor);
                            }
                            searchText.setVisibility(View.GONE);
                            if (data != null && ((Cursor) data).getCount() > 0) {
                                empty.setVisibility(View.GONE);
                            } else {
                                empty.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    isLoading = false;
                    break;

                case LoadersId.NETWORK_ADERTS_LOADER_BY_PAGINATION_ID:
                    id = LoadersId.DB_ADVERTISEMENT_LOADER_PAGINATION_ID;
                case LoadersId.NETWORK_ADVERTISEMENT_LOADER_ID:
                case LoadersId.NETWORK_ADERTS_LOADER_BY_FILTERS_ID:
                    CommonBundleBuilder commonBundleBuilder = new CommonBundleBuilder();

                    if (PersonalData.getInstance(getActivity()).getUserSearchLocation() != null) {
                        commonBundleBuilder = new CommonBundleBuilder(addLocationIntoBundle(commonBundleBuilder.build()));
                    }

                    if (filtersValuesBundle != null) {
                        commonBundleBuilder.build().putAll(filtersValuesBundle);
                    }
                    if (id == -1) {
                        id = LoadersId.DB_ADVERTISEMENT_LOADER_ID;
                    }
                    getLoaderManager().restartLoader(id, commonBundleBuilder
                            .putLong(CATEGORY_ID, categoryId)
                            .putBoolean(StockRoomBundleBuilderArgs.ORDER_BY_LAST_ADDED, true)
                            .build(), this);
                    id = -1;
                    break;
            }
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

        isLoading = false;
        int id = loader.getId();

        if (isNeedLoad) {
            Log.e("!!!", "onLoaderReset: " + id);
            switch (id) {
                case LoadersId.DB_ADVERTISEMENT_LOADER_ID:
                case LoadersId.DB_ADVERTISEMENT_LOADER_PAGINATION_ID:
                case LoadersId.FAVOURITES_DB_LOADER:
                    if (advertsListView != null) {
                        if (advertsListView.getAdapter() != null) {

                            ((CursorAdapter) advertsListView.getAdapter()).changeCursor(null);

                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (getAbstractListView().getAdapter() instanceof AdvertSuggestionAdapter) {
            Cursor cursor = advertSuggestionAdapter.getCursor();
            if (cursor.moveToPosition(position)) {
                commit(AdvertDetailFragment.newInstance(cursor.getLong(cursor.getColumnIndexOrThrow(AdvertisementColumns.ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(AdvertisementColumns.USER_ID))),
                        AdvertDetailFragment.class.getSimpleName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
            }
        } else {
            commit(AdvertDetailFragment.newInstance(((AdvertAdapter) parent.getAdapter()).getItem(position).getAdvertisementId(),
                    ((AdvertAdapter) parent.getAdapter()).getItem(position).getAdvertisementId()),
                    AdvertDetailFragment.class.getSimpleName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
        }
        isNeedLoad = false;
    }

    private String[] buildSelectionArgs(Bundle args) {
        ArrayList<String> list = new ArrayList<>();
        list.add(String.valueOf(args.getLong(CATEGORY_ID)));

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_NEW, false)) {
            list.add(String.valueOf(AdvertisementContract.TYPE_NEW));
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_USED, false)) {
            list.add(String.valueOf(AdvertisementContract.TYPE_USED));
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_BUY, false)) {
            list.add(String.valueOf(Advertisement.ADVERT_TYPE_BUY));
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_SELL, false)) {
            list.add(String.valueOf(Advertisement.ADVERT_TYPE_SELL));
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_EXCHANGE, false)) {
            list.add(String.valueOf(Advertisement.PRICE_TYPE_EXCHANGE));
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_FREE, false)) {
            list.add(String.valueOf(Advertisement.PRICE_TYPE_FREE));
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_SALE, false)) {
            list.add(String.valueOf(Advertisement.PRICE_TYPE_SELL));
        }

        if (args.getString(QUERY, null) != null) {
            list.add(String.valueOf("%" + args.getString(QUERY) + "%"));
        }

        if (args.getLong(COUNTRY_ID, -1l) != -1l) {
            list.add(String.valueOf(args.getLong(COUNTRY_ID)));
        }

        if (args.getLong(REGION_ID, -1l) != -1l) {
            list.add(String.valueOf(args.getLong(REGION_ID)));
        }

        if (args.getLong(ADVERT_CITY_ID, -1l) != -1l) {
            list.add(String.valueOf(args.getLong(ADVERT_CITY_ID)));
        }

        return list.toArray(new String[]{});
    }

    private String buildSelection(Bundle args) {
        StringBuilder appendingSQL = new StringBuilder();
        appendingSQL.append(AdvertisementColumns.CATEGORY_ID);
        appendingSQL.append("=?");

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_NEW, false)) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementColumns.USED);
            appendingSQL.append("=?");
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_USED, false)) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementColumns.USED);
            appendingSQL.append("=?");
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_BUY, false)) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementColumns.TYPE);
            appendingSQL.append("=?");
        }

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_SELL, false)) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementColumns.TYPE);
            appendingSQL.append("=?");
        }

        StringBuilder sqlExchangeSaleFree = new StringBuilder();

        if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_EXCHANGE, false)
                || args.getBoolean(StockRoomBundleBuilderArgs.FILTER_FREE, false)
                || args.getBoolean(StockRoomBundleBuilderArgs.FILTER_SALE, false)) {
            if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_EXCHANGE, false)) {
                sqlExchangeSaleFree.append(" AND ( ");
                sqlExchangeSaleFree.append(AdvertisementColumns.PRICE_TYPE);
                sqlExchangeSaleFree.append("=?");
            }

            if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_FREE, false)) {
                if (sqlExchangeSaleFree.length() == 0) {
                    sqlExchangeSaleFree.append(" AND ( ");
                } else {
                    sqlExchangeSaleFree.append(" OR ");
                }
                sqlExchangeSaleFree.append(AdvertisementColumns.PRICE_TYPE);
                sqlExchangeSaleFree.append("=?");
            }

            if (args.getBoolean(StockRoomBundleBuilderArgs.FILTER_SALE, false)) {
                if (sqlExchangeSaleFree.length() == 0) {
                    sqlExchangeSaleFree.append(" AND ( ");
                } else {
                    sqlExchangeSaleFree.append(" OR ");
                }
                sqlExchangeSaleFree.append(AdvertisementColumns.PRICE_TYPE);
                sqlExchangeSaleFree.append("=?");
            }
            sqlExchangeSaleFree.append(" )");
            appendingSQL.append(sqlExchangeSaleFree);
        }

        if (!args.getBoolean(StockRoomBundleBuilderArgs.FILTER_FREE, false)) {
            if (args.getDouble(StockRoomBundleBuilderArgs.PRICE_LOW, -1d) != -1d) {
                appendingSQL.append(" AND ");
                appendingSQL.append(AdvertisementContract.INDEPENDENT_PRICE);
                appendingSQL.append(">=");
                appendingSQL.append(args.getDouble(StockRoomBundleBuilderArgs.PRICE_LOW, -1d));
            }

            if (args.getDouble(StockRoomBundleBuilderArgs.PRICE_HIGH, -1d) != -1d) {
                appendingSQL.append(" AND ");
                appendingSQL.append(AdvertisementContract.INDEPENDENT_PRICE);
                appendingSQL.append("<=");
                appendingSQL.append(args.getDouble(StockRoomBundleBuilderArgs.PRICE_HIGH, -1d));
            }
        }

        if (args.getString(QUERY, null) != null) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementContract.TITLE);
            appendingSQL.append(" LIKE ? ");
            appendingSQL.append(" OR ");
            appendingSQL.append(AdvertisementContract.DESCRIPTION);
            appendingSQL.append(" LIKE ?");
        }

        if (args.getLong(COUNTRY_ID, -1L) != -1L) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementContract.COUNTRY_ID);
            appendingSQL.append(" = ? ");
        }

        if (args.getLong(REGION_ID, -1L) != -1L) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementContract.REGION_ID);
            appendingSQL.append(" = ? ");
        }

        if (args.getLong(ADVERT_CITY_ID, -1L) != -1L) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementContract.CITY_ID);
            appendingSQL.append(" = ? ");
        }

        ArrayList<Integer> moreFilters = args.getIntegerArrayList(StockRoomBundleBuilderArgs.FILTER_MORE_FIELDS);
        if (moreFilters != null && moreFilters.size() > 0) {
            appendingSQL.append(" AND ");
            appendingSQL.append(AdvertisementColumns.ID);
            appendingSQL.append(" in (SELECT ");
            appendingSQL.append(AdvertFiltersColumns.ADVERT_ID);
            appendingSQL.append(" from ");
            appendingSQL.append(AdvertFiltersContract.TABLE_NAME);
            appendingSQL.append(" where ");
            appendingSQL.append(AdvertFiltersContract.FILTER_VALUE_ID);
            appendingSQL.append(" in (");
            boolean first = true;
            for (int id : moreFilters) {
                if (!first) {
                    appendingSQL.append(", ");
                } else {
                    first = false;
                }
                appendingSQL.append(id);
            }
            appendingSQL.append(")");

            appendingSQL.append(" GROUP BY ");
            appendingSQL.append(AdvertFiltersColumns.ADVERT_ID);
            appendingSQL.append(" HAVING count(*) = ");
            appendingSQL.append(moreFilters.size());
            appendingSQL.append(")");
        }

        return appendingSQL.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_filters:
                if (advertAdapter.getAdapterType() == AdvertAdapter.TYPE_SMALL) {
                    advertAdapter.setAdapterType(AdvertAdapter.TYPE_BIG);
                } else {
                    advertAdapter.setAdapterType(AdvertAdapter.TYPE_SMALL);
                }
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        startSearchLoader(query);
        return true;
    }

    private void startSearchLoader(String query) {
        if (isNeedLoad) {
            advertsListView.setAdapter(advertSuggestionAdapter);
            isSearchEnable = true;
            Bundle args = new Bundle();

            if (PersonalData.getInstance(getActivity()).getUserSearchLocation() != null) {
                args = AddUserLocationIntoBundle.getInstance().addLocation(args);
            }

            args.putString(SearchLoaderCallback.QUERY_KEY, query);
            args.putLong(SearchLoaderCallback.CATEGORY_ID_KEY, categoryId);
            getLoaderManager().restartLoader(LoadersId.SEARCH_LOADER, args, new SearchLoaderCallback(
                    advertSuggestionAdapter, getActivity()));
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        startSearchLoader(newText);
        return true;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        if (getActivity() != null) {
            ((Launcher) getActivity()).setFiltersSelected(filtersDrawer.isDrawerVisible(END));
            AutoCompleteTextView filterCity = (AutoCompleteTextView) drawerView.findViewById(R.id.filter_city_autocomplete);
            if (filterCity != null && filtersDrawer.isDrawerVisible(END)) {
                InputMethodManager in = (InputMethodManager) drawerView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(filterCity.getWindowToken(), 0);
            }
        }
    }


    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    private Bundle addLocationWhileFilteringIntoBundle(Bundle commonBundleBuilder) {
        return AddUserLocationIntoBundle.getInstance().addLocationWithDef(commonBundleBuilder);
    }

    private Bundle addLocationIntoBundle(Bundle commonBundleBuilder) {
        return AddUserLocationIntoBundle.getInstance().addLocation(commonBundleBuilder);
    }

    @Override
    public void onFiltersChanged(Bundle bundle) {
        isNeedLoad = true;
        Log.e(FiltersFragment.TAG, "onFiltersChanged: ");
        bundle.putLong(CATEGORY_ID, categoryId);
        if (isSearchEnable) {
            bundle.putString(RequestParams.ParamNames.QUERY, advertSearchView.getQuery().toString());
        }
        filtersValuesBundle = bundle;
        restartLoader(getLoaderManager(), new CommonBundleBuilder());
//        getLoaderManager().restartLoader(LoadersId.NETWORK_ADERTS_LOADER_BY_FILTERS_ID, bundle, this).forceLoad();
//        getLoaderManager().restartLoader(LoadersId.DB_ADVERTISEMENT_LOADER_ID, bundle, this).forceLoad();
    }

    public String getOrder(Bundle bundle) {
        StringBuilder appendingSQL = new StringBuilder();
        if (bundle != null) {
            if (bundle.getBoolean(StockRoomBundleBuilderArgs.ORDER_BY_PRICE_ASC)) {
                appendingSQL.append(AdvertisementContract.INDEPENDENT_PRICE);
                appendingSQL.append(" ASC");
            } else if (bundle.getBoolean(StockRoomBundleBuilderArgs.ORDER_BY_PRICE_DESC)) {
                appendingSQL.append(AdvertisementContract.INDEPENDENT_PRICE);
                appendingSQL.append(" DESC");
            } else if (bundle.getBoolean(StockRoomBundleBuilderArgs.ORDER_BY_RATING)) {
                appendingSQL.append(AdvertisementContract.CREATED_AT);
                appendingSQL.append(" DESC, ");
                appendingSQL.append(AdvertisementContract.PRICE);
                appendingSQL.append(" ASC");
            } else if (bundle.getBoolean(StockRoomBundleBuilderArgs.ORDER_BY_LAST_ADDED)) {
                appendingSQL.append(AdvertisementContract.CREATED_AT);
                appendingSQL.append(" DESC");
            } else {
                appendingSQL.append(AdvertisementContract.CREATED_AT);
                appendingSQL.append(" DESC");
            }
        } else {
            appendingSQL.append(AdvertisementContract.CREATED_AT);
            appendingSQL.append(" DESC");
        }
        return appendingSQL.toString();
    }

    public void updateList() {
        if (!isLoading) {
            restartLoader(getLoaderManager(), new CommonBundleBuilder());
        }
    }
}
