package com.stockroompro.fragments;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.artjoker.core.BundleBuilder;
import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.stockroompro.Launcher;
import com.stockroompro.R;
import com.stockroompro.StockRoomBundleBuilderArgs;
import com.stockroompro.api.models.requests.CategoryFiltersRequest;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.database.DatabaseProvider;
import com.stockroompro.fragments.base.BaseApplicationFragment;
import com.stockroompro.loaders.FilterPriceRangeLoader;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.columns.FiltersColumns;
import com.stockroompro.models.contracts.CategoryFiltersContract;
import com.stockroompro.models.contracts.FilterValuesContract;
import com.stockroompro.models.filters.FilterPriceRange;
import com.stockroompro.utils.AddUserLocationIntoBundle;
import com.stockroompro.views.ExpandableLinearLayout;
import com.stockroompro.views.FilterHeaderView;
import com.yahoo.mobile.client.android.util.RangeSeekBar;

import java.util.ArrayList;


/**
 * Created by alexsergienko on 18.05.15.
 */
public class FiltersFragment extends BaseApplicationFragment implements LoaderManager.LoaderCallbacks<Object>,
        View.OnClickListener, RangeSeekBar.OnRangeSeekBarChangeListener<Double>, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {

    public static final String TAG = "filters";
    public static final String CATEGORY_NAME_ID = "CATEGORY_NAME_ID";
    private long categoryId = 0;
    private String categoryName = "AutoPlanerPro";
    private FilterHeaderView titlesSortList;
    private FilterHeaderView titlesNewUsed;
    private FilterHeaderView titlesBuySell;
    private FilterHeaderView titlesSaleExchangeFree;
    private FilterHeaderView titlesMoreFilters;
    private ExpandableLinearLayout expandableSortList;
    private ExpandableLinearLayout expandableNewUsed;
    private ExpandableLinearLayout expandableCity;
    private ExpandableLinearLayout expandableBuySell;
    private ExpandableLinearLayout expandableSaleExchangeFree;
    private ExpandableLinearLayout expandableMoreFilters;
    private RangeSeekBar<Double> rangeOfPricesView;
    private View rangeOfPricesViewContainer;
    private RadioGroup sortGroup;
    private CompoundButton orderByLastAdded;
    private CompoundButton orderByPriceAsc;
    private CompoundButton orderByPriceDesc;
    private CompoundButton filterNew;
    private CompoundButton filterUsed;
    private CompoundButton filterBuy;
    private AutoCompleteTextView filterCityAutocomplete;
    /**
     * Type of advertisement
     */
    private CompoundButton filterSell;

    /**
     * Type of prices
     */
    private CompoundButton filterSale;
    private CompoundButton filterFree;
    private CompoundButton filterExchange;
    private View clearFilters;
    private OnFiltersChangeListener onFiltersChangeListener;
    private boolean isRangeSetupped;
    private static final String FILTER_NAME = "filter_name";
    private static final String FILTER_ID = "filter_id";
//    private SimpleCursorAdapter cityAdapter;

    public static FiltersFragment newInstance(long categoryId, String categoryName) {
        FiltersFragment fragment = new FiltersFragment();
        Bundle args = new Bundle();
        args.putLong(FilterValuesContract.CATEGORY_ID, categoryId);
        args.putString(CATEGORY_NAME_ID, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getLong(FilterValuesContract.CATEGORY_ID);
            categoryName = getArguments().getString(CATEGORY_NAME_ID);
        }
        /*cityAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, null, new String[]{CityColumns.NAME}, new int[]{android.R.id.text1}, 0);
        cityAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence str) {
                return getActivity().getContentResolver().query(Uri.withAppendedPath(ContentProviderConfig.BASE_CONTENT_URI, CityContract.SEARCH_CITY_URI),
                        null,
                        null,
                        new String[]{str.toString(), String.valueOf(categoryId)},
                        null);
            }
        });

        cityAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cur) {
                int index = cur.getColumnIndex(CityColumns.NAME);
                return cur.getString(index);
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        Launcher parent = getParent();
        parent.setLocationSpinnerContainer(View.VISIBLE);
        parent.setMainTitle(categoryName);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = new CommonBundleBuilder().putLong(FilterValuesContract.CATEGORY_ID, categoryId).build();
        getLoaderManager().initLoader(LoadersId.DB_FILTER_PRICE_RANGES, bundle, this).forceLoad();
        getLoaderManager().initLoader(LoadersId.DB_FILTERS_BY_CATEGORY, bundle, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.filters;
    }

    @Override
    protected void initAdapters() {
//        filterCityAutocomplete.setAdapter(cityAdapter);
    }

    @Override
    protected void initViews(View view) {
        clearFilters = view.findViewById(R.id.button_reset_filters);
        expandableSortList = (ExpandableLinearLayout) view.findViewById(R.id.filter_expandable_sort);
        expandableCity = (ExpandableLinearLayout) view.findViewById(R.id.filter_expandable_city);
        expandableBuySell = (ExpandableLinearLayout) view.findViewById(R.id.filter_expandable_buy_sell);
        expandableNewUsed = (ExpandableLinearLayout) view.findViewById(R.id.filter_expandable_new_used);
        expandableSaleExchangeFree = (ExpandableLinearLayout) view.findViewById(R.id.filter_expandable_sale_exchange_free);
        expandableMoreFilters = (ExpandableLinearLayout) view.findViewById(R.id.filter_expandable_more_filters);
        titlesSortList = (FilterHeaderView) view.findViewById(R.id.filter_button_sort);
        titlesSortList.setExpandableLinearLayout(expandableSortList);
        titlesBuySell = (FilterHeaderView) view.findViewById(R.id.filter_button_buy_sell);
        titlesBuySell.setExpandableLinearLayout(expandableBuySell);
        titlesNewUsed = (FilterHeaderView) view.findViewById(R.id.filter_button_new_used);
        titlesNewUsed.setExpandableLinearLayout(expandableNewUsed);
        titlesSaleExchangeFree = (FilterHeaderView) view.findViewById(R.id.filter_button_sale_exchange_free);
        titlesSaleExchangeFree.setExpandableLinearLayout(expandableSaleExchangeFree);
        titlesMoreFilters = (FilterHeaderView) view.findViewById(R.id.fragment_button_more_filters);
        titlesMoreFilters.setExpandableLinearLayout(expandableMoreFilters);
        rangeOfPricesView = (RangeSeekBar) view.findViewById(R.id.range_seek_bar_price_filter);
        rangeOfPricesViewContainer = view.findViewById(R.id.rl_price_filter);
        sortGroup = (RadioGroup) view.findViewById(R.id.filters_sort_group);
        orderByLastAdded = (CompoundButton) view.findViewById(R.id.filter_order_by_last_added);
        orderByPriceAsc = (CompoundButton) view.findViewById(R.id.filter_order_by_price_asc);
        orderByPriceDesc = (CompoundButton) view.findViewById(R.id.filter_order_by_price_desc);
        filterCityAutocomplete = (AutoCompleteTextView) view.findViewById(R.id.filter_city_autocomplete);
        filterBuy = (CompoundButton) view.findViewById(R.id.cb_filter_buy);
        filterSell = (CompoundButton) view.findViewById(R.id.cb_filter_sell);
        filterNew = (CompoundButton) view.findViewById(R.id.cb_filter_new);
        filterUsed = (CompoundButton) view.findViewById(R.id.cb_filter_used);
        filterSale = (CompoundButton) view.findViewById(R.id.cb_filter_sale);
        filterExchange = (CompoundButton) view.findViewById(R.id.cb_filter_exchange);
        filterFree = (CompoundButton) view.findViewById(R.id.cb_filter_free);
        isRangeSetupped = false;
        titlesSortList.setSubtitleText(getString(R.string.filter_text_last_added));

    }

    @Override
    protected void initListeners(View view) {
        view.setOnClickListener(this);
        clearFilters.setOnClickListener(this);
        sortGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.filter_order_by_last_added:
                        titlesSortList.setSubtitleText(getString(R.string.filter_text_last_added));
                        break;
                    case R.id.filter_order_by_price_asc:
                        titlesSortList.setSubtitleText(getString(R.string.filter_text_by_price_asc));
                        break;
                    case R.id.filter_order_by_price_desc:
                        titlesSortList.setSubtitleText(getString(R.string.filter_text_by_price_desc));
                        break;
                }
                filtersChanged();
            }
        });
        rangeOfPricesView.setOnRangeSeekBarChangeListener(this);
        filterBuy.setOnCheckedChangeListener(this);
        filterSell.setOnCheckedChangeListener(this);
        filterNew.setOnCheckedChangeListener(this);
        filterUsed.setOnCheckedChangeListener(this);
        filterSale.setOnCheckedChangeListener(this);
        filterExchange.setOnCheckedChangeListener(this);
        filterFree.setOnCheckedChangeListener(this);

        filterCityAutocomplete.setOnItemClickListener(this);
        filterCityAutocomplete.setOnClickListener(this);
    }

    private void refreshTitles(FilterHeaderView headerView, ExpandableLinearLayout layout) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < layout.getChildCount(); i++) {
            if (layout.getChildAt(i) instanceof CompoundButton) {
                if (((CompoundButton) layout.getChildAt(i)).isChecked()) {
                    if (str.length() > 0) {
                        str.append(", ");
                    }
                    str.append(((CompoundButton) layout.getChildAt(i)).getText());
                }
            }
        }
        headerView.setSubtitleText(str.toString());
    }

    private void filtersChanged() {
        Log.e(TAG, "filtersChanged: ");
        DatabaseProvider.printLogs(true);
        refreshTitles(titlesMoreFilters, expandableMoreFilters);
        refreshTitles(titlesBuySell, expandableBuySell);
        refreshTitles(titlesNewUsed, expandableNewUsed);
        refreshTitles(titlesSaleExchangeFree, expandableSaleExchangeFree);
        if (onFiltersChangeListener != null) {
            BundleBuilder bundleBuilder = new CommonBundleBuilder()
                    .putBoolean(StockRoomBundleBuilderArgs.ORDER_BY_LAST_ADDED, orderByLastAdded.isChecked())
                    .putBoolean(StockRoomBundleBuilderArgs.ORDER_BY_PRICE_ASC, orderByPriceAsc.isChecked())
                    .putBoolean(StockRoomBundleBuilderArgs.ORDER_BY_PRICE_DESC, orderByPriceDesc.isChecked())
                    .putBoolean(StockRoomBundleBuilderArgs.FILTER_NEW, filterNew.isChecked() && !filterUsed.isChecked())
                    .putBoolean(StockRoomBundleBuilderArgs.FILTER_USED, filterUsed.isChecked() && !filterNew.isChecked())
                    .putBoolean(StockRoomBundleBuilderArgs.FILTER_BUY, filterBuy.isChecked() && !filterSell.isChecked())
                    .putBoolean(StockRoomBundleBuilderArgs.FILTER_SELL, filterSell.isChecked() && !filterBuy.isChecked())
                    .putBoolean(StockRoomBundleBuilderArgs.FILTER_SALE, filterSale.isChecked() && (!filterFree.isChecked() || !filterExchange.isChecked()))
                    .putBoolean(StockRoomBundleBuilderArgs.FILTER_EXCHANGE, filterExchange.isChecked() && (!filterSale.isChecked() || !filterFree.isChecked()))
                    .putBoolean(StockRoomBundleBuilderArgs.FILTER_FREE, filterFree.isChecked() && (!filterExchange.isChecked() || !filterSale.isChecked()));
            if (isRangeSetupped) {
                bundleBuilder.putDouble(StockRoomBundleBuilderArgs.PRICE_LOW, rangeOfPricesView.getSelectedMinValue());
                bundleBuilder.putDouble(StockRoomBundleBuilderArgs.PRICE_HIGH, rangeOfPricesView.getSelectedMaxValue());
            }
            if (PersonalData.getInstance(getActivity()).getUserSearchLocation() != null) {
                bundleBuilder = new BundleBuilder(AddUserLocationIntoBundle.getInstance().addLocation(bundleBuilder.build()));
            }

            ArrayList<Integer> moreFilters = filterValuesIds(expandableMoreFilters);
            if (moreFilters.size() > 0) {
                bundleBuilder.putIntegerArrayList(StockRoomBundleBuilderArgs.FILTER_MORE_FIELDS, moreFilters);
            }
            if (isResumed()) {
                onFiltersChangeListener.onFiltersChanged(bundleBuilder.build());
            }
        }
    }

    private ArrayList<Integer> filterValuesIds(ExpandableLinearLayout layout) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++) {
            if (layout.getChildAt(i) instanceof CompoundButton) {
                if (((CompoundButton) layout.getChildAt(i)).isChecked()) {
                    list.add((Integer) layout.getChildAt(i).getTag());
                }
            }
        }
        return list;
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.SHOW_BUTTON_ADD | HeaderIconsPolicy.SHOW_BUTTON_FILTERS;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.NETWORK_FILTERS_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(),
                        new CategoryFiltersRequest(getActivity(), args));
            case LoadersId.DB_FILTERS_BY_CATEGORY:
                return new CursorLoader(getActivity(),
                        FilterValuesContract.CONTENT_URI,
                        new String[]{"*", String.format("(select %s from %s where %s=%s) as %s",
                                CategoryFiltersContract.NAME,
                                CategoryFiltersContract.TABLE_NAME,
                                CategoryFiltersContract.ID,
                                FilterValuesContract.FILTER_ID,
                                FILTER_NAME),
                                String.format("(select %s from %s where %s=%s) as %s",
                                        CategoryFiltersContract.FILTER_ID,
                                        CategoryFiltersContract.TABLE_NAME,
                                        CategoryFiltersContract.CATEGORY_ID,
                                        args.getLong(FilterValuesContract.CATEGORY_ID),
                                        FILTER_ID)},
                        String.format("%s in (select %s from %s where %s=%d)",
                                FilterValuesContract.FILTER_ID,
                                CategoryFiltersContract.ID,
                                CategoryFiltersContract.TABLE_NAME,
                                CategoryFiltersContract.CATEGORY_ID,
                                args.getLong(FilterValuesContract.CATEGORY_ID)),
                        null,
                        null);

            case LoadersId.DB_FILTER_PRICE_RANGES:
                return new FilterPriceRangeLoader(getActivity(), args);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (data != null) {
            switch (loader.getId()) {
                case LoadersId.DB_FILTERS_BY_CATEGORY:
                    Cursor cursor = ((Cursor) data);
                    setupMoreFilters(cursor);
                    break;

                case LoadersId.DB_FILTER_PRICE_RANGES:
                    FilterPriceRange filterPriceRange = (FilterPriceRange) data;
                    if (rangeOfPricesView.getAbsoluteMinValue() == null || rangeOfPricesView.getAbsoluteMaxValue() == null ||
                            rangeOfPricesView.getAbsoluteMinValue() != filterPriceRange.getMinValue() ||
                            rangeOfPricesView.getAbsoluteMaxValue() != filterPriceRange.getMaxValue()) {
                        rangeOfPricesView.setRangeValues(filterPriceRange.getMinValue(), filterPriceRange.getMaxValue());
                        if (filterPriceRange.getMaxValue() == filterPriceRange.getMinValue()) {
                            rangeOfPricesViewContainer.setVisibility(View.GONE);
                        } else {
                            isRangeSetupped = true;
                            rangeOfPricesViewContainer.setVisibility(View.VISIBLE);
                        }
                    }
                    break;

                case LoadersId.NETWORK_FILTERS_LOADER_ID:
                    break;
            }
        }
    }

    private void setupMoreFilters(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            if (cursor.getCount() != 0) {
                if (expandableMoreFilters.getTag() == null) {
                    expandableMoreFilters.removeAllViews();
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    int i = 1000;//Any number for setup as view id.
                    long parentId = 0;
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        if (parentId != cursor.getLong(cursor.getColumnIndexOrThrow(FILTER_ID))) {
                            parentId = cursor.getLong(cursor.getColumnIndexOrThrow(FILTER_ID));
                            TextView textView = (TextView) layoutInflater.inflate(R.layout.filters_expanbale_item_header, null, false);
                            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.add_advert_radio_button_margin)));
                            textView.setText(cursor.getString(cursor.getColumnIndexOrThrow(FILTER_NAME)));
                            textView.setId(i++);
                            expandableMoreFilters.addView(textView);
                        }

                        CompoundButton text = (CompoundButton) layoutInflater.inflate(R.layout.filters_expanable_item, null, false);
                        text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.add_advert_radio_button_margin)));
                        text.setText(cursor.getString(cursor.getColumnIndexOrThrow(FiltersColumns.NAME)));
                        text.setId(i++);
                        text.setTag(cursor.getInt(cursor.getColumnIndexOrThrow(FiltersColumns.ID)));
                        text.setOnCheckedChangeListener(this);
                        expandableMoreFilters.addView(text);
                    }
                    expandableMoreFilters.setTag(expandableMoreFilters.getChildCount());

                    if (expandableMoreFilters.getChildCount() > 0) {
                        titlesMoreFilters.setVisibility(View.VISIBLE);

                        if (titlesMoreFilters.isChecked()) {
                            expandableMoreFilters.expand();
                        }

                    } else {
                        titlesMoreFilters.setVisibility(View.GONE);
                        expandableMoreFilters.setVisibility(View.GONE);
                    }
                }
            } else {
                titlesMoreFilters.setVisibility(View.GONE);
                expandableMoreFilters.collapse();

                if (getLoaderManager().getLoader(LoadersId.NETWORK_FILTERS_LOADER_ID) != null) {
                    getLoaderManager().getLoader(LoadersId.NETWORK_FILTERS_LOADER_ID).reset();
                }

                getLoaderManager().restartLoader(LoadersId.NETWORK_FILTERS_LOADER_ID,
                        new CommonBundleBuilder().putLong(RequestParams.ParamNames.CATEGORY_ID, categoryId).build(), this).forceLoad();
            }
        }
        if (expandableMoreFilters.getChildCount() > 0) {
            titlesMoreFilters.setVisibility(View.VISIBLE);
            if (titlesMoreFilters.isChecked()) {
                expandableMoreFilters.expand();
            }
        } else {
            titlesMoreFilters.setVisibility(View.GONE);
            expandableMoreFilters.setVisibility(View.GONE);
        }
    }

    private void clearFilters() {
        clearFiltersInGroup(expandableBuySell);
        clearFiltersInGroup(expandableMoreFilters);
        clearFiltersInGroup(expandableSaleExchangeFree);
        clearFiltersInGroup(expandableNewUsed);
        clearSorting();
        rangeOfPricesView.resetSelectedValues();
    }

    private void clearSorting() {
        orderByLastAdded.setChecked(true); //Default sort position
    }

    private void clearFiltersInGroup(ExpandableLinearLayout layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            if (layout.getChildAt(i) instanceof CompoundButton) {
                if (((CompoundButton) layout.getChildAt(i)).isChecked()) {
                    ((CompoundButton) layout.getChildAt(i)).setChecked(false);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        switch (loader.getId()) {
            case LoadersId.NETWORK_FILTERS_LOADER_ID:
                break;

            case LoadersId.DB_FILTERS_BY_CATEGORY:
                break;

            case LoadersId.DB_FILTER_PRICE_RANGES:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_reset_filters:
                clearFilters();
                filtersChanged();
                break;

            case R.id.filter_city_autocomplete:
                filtersChanged();
                break;
        }
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Double minValue, Double maxValue) {
        filtersChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        filtersChanged();
    }

    public void setOnFiltersChangeListener(OnFiltersChangeListener onFiltersChangeListener) {
        this.onFiltersChangeListener = onFiltersChangeListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = ((SimpleCursorAdapter) filterCityAutocomplete.getAdapter()).getCursor();
        if (cursor != null && !cursor.isClosed() && cursor.moveToPosition(position)) {
            filtersChanged();
            InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(filterCityAutocomplete.getWindowToken(), 0);
        }
    }

    public interface OnFiltersChangeListener {
        void onFiltersChanged(Bundle bundle);
    }
}
