package com.stockroompro.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ToggleButton;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.RequestDescriptorWithLoadFromDB;
import com.artjoker.core.network.RequestProcessor;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.core.views.ArtJokerEditText;
import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.core.views.ExpandableHeightGridView;
import com.artjoker.core.views.ExpandableHeightListView;
import com.artjoker.tool.core.Notification;
import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.core.Ui;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.stockroompro.Launcher;
import com.stockroompro.R;
import com.stockroompro.adapters.AddAdvertPhotoAdapter;
import com.stockroompro.adapters.CategorySpinnerAdapter;
import com.stockroompro.adapters.FiltersCursorTreeAdapter;
import com.stockroompro.adapters.LocationSpinnerAdapter;
import com.stockroompro.api.models.requests.AddAdvertisementPhotos;
import com.stockroompro.api.models.requests.AddAdvertisementRequest;
import com.stockroompro.api.models.requests.CityRequest;
import com.stockroompro.api.models.requests.CountryRequest;
import com.stockroompro.api.models.requests.RegionRequest;
import com.stockroompro.api.models.requests.SubCategoriesRequest;
import com.stockroompro.fragments.base.BaseApplicationListFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalBroadcastRequestProcessor;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.AdvertPhoto;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.Currency;
import com.stockroompro.models.NewAdvert;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.Settings;
import com.stockroompro.models.columns.AdvertisementColumns;
import com.stockroompro.models.columns.CategoryColumns;
import com.stockroompro.models.columns.CityColumns;
import com.stockroompro.models.columns.FiltersColumns;
import com.stockroompro.models.columns.PhotosColumns;
import com.stockroompro.models.columns.RegionColumns;
import com.stockroompro.models.contracts.AdvertFiltersContract;
import com.stockroompro.models.contracts.CategoryContract;
import com.stockroompro.models.contracts.CategoryFiltersContract;
import com.stockroompro.models.contracts.CityContract;
import com.stockroompro.models.contracts.CountryContract;
import com.stockroompro.models.contracts.RegionContract;
import com.stockroompro.models.spinners.Location;
import com.stockroompro.models.spinners.SpinnerItem;
import com.stockroompro.views.AdvertPriceTypeRadioGroup;
import com.stockroompro.views.AdvertStateRadioGroup;
import com.stockroompro.views.AdvertTypeRadioGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import retrofit.mime.TypedFile;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_BARGAIN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CITY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CURRENCY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_DESCRIPTION;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_OPTIONS;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_PHONES;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_PRICE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_PRICE_TYPE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_REGION_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_TITLE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_TYPE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_USED;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_NAME;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CITY_NAME;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DELETED_PHOTOS;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.FILTERS_NAMES;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.PATH;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.REGION_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;

/**
 * Created by bagach.alexandr on 21.04.15.
 */
public class AddAdvertisementFragment extends BaseApplicationListFragment implements CategorySpinnerAdapter.OnCategorySpinnerItemClickListener,
        View.OnClickListener, FiltersCursorTreeAdapter.OnFilterItemSelectListener, LocationSpinnerAdapter.OnLocationSpinnerItemClickListener,
        BaseRequest.UIResponseCallback, AddAdvertPhotoAdapter.OnDeletePhotoListener,
        AdvertPriceTypeRadioGroup.OnPriceTypeSelectedListener, AdvertTypeRadioGroup.OnAdvertTypeSelectedListener, CompoundButton.OnCheckedChangeListener {

    protected static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_PHOTO = 0x3456;
    public static final String ADVERT_CREATED = "ADVERT_CREATED";
    protected static final int CAPTURE_PHOTO_BY_CAMERA = 9999;
    protected static final String PREVIOUS_ELEMENT = "\u2190";

    protected ArtJokerTextView title;
    protected Spinner locationSpinner;
    protected Spinner priceSpinner;
    protected Spinner categorySpinner;
    protected LinearLayout priceSpinnerContainer;
    protected LinearLayout bargainContainer;
    protected ArtJokerTextView priceTitle;
    protected ArtJokerEditText advertTitle;
    protected ArtJokerTextView titleLength;
    protected ArtJokerEditText advertDescription;
    protected ArtJokerEditText advertPrice;
    protected ArtJokerTextView descriptionLength;
    protected ArtJokerTextView filtersDiffLine;
    private ArtJokerTextView licenceText;
    protected AdvertStateRadioGroup isUsedRadioGroup;
    protected AdvertTypeRadioGroup advertType;
    protected AdvertPriceTypeRadioGroup priceType;
    protected CheckBox bargain;
    private ArtJokerButton publish;
    private ArtJokerButton review;
    protected ExpandableHeightListView filtersListView;
    protected ImageButton addPhoto;
    protected ExpandableHeightGridView photosGridView;

    protected FiltersCursorTreeAdapter filterAdapter;
    protected FrameLayout progressBarContainer;
    protected LinearLayout phonesLayout;
    protected ArrayList<String> phonesList = new ArrayList<>();

    protected LocationSpinnerAdapter locationAdapter;
    protected CategorySpinnerAdapter categoryAdapter;
    protected AddAdvertPhotoAdapter photoAdapter;

    /**
     * @param categoryId - id of selected category
     * @param categoryPosition - position of selected category in spinner
     * @param categoryParentId - parentId of selected sub-category
     * @param categoriesId - ArrayList of parent categories for come back in spinner
     * @param filtersId - ArrayList for selected filters
     * @param photosList - ArrayList for selected photos from gallery
     * @param categoriesPath - ArrayList for names of parent categories
     */
    protected long categoryId = 0;
    protected int categoryPosition;
    protected ArrayList<Long> categoriesId = new ArrayList<>();
    protected ArrayList<Integer> filtersId = new ArrayList<>();
    protected ArrayList<String> filtersNames = new ArrayList<>();
    protected ArrayList<String> photosList = new ArrayList<>();
    protected ArrayList<String> categoriesPath = new ArrayList<>();

    protected HashMap<Long, Long> categories = new HashMap<>();

    /**
     * @param photos - HashMap for saving photos before sending to server
     */
    protected HashMap<String, TypedFile> photos;

    /**
     * @param locationType - determines the type of location
     */
    protected ArrayList<Location> locationArrayList = new ArrayList<>(3);
    protected int locationType = Config.TYPE_COUNTRY;
    private boolean userSearchLocationFromPreferences = false;

    private String categoryName;
    private long categoryFromArguments;
    private boolean getCategoryFromArguments;

    protected boolean isLocationFromEditAdvert = false;
    protected boolean isSpinnerItemsLoading = false;
    protected boolean isLocationItemsLoading = false;

    protected long currentTimeForTempPhoto = 0;

    protected ArrayList<ArtJokerTextView> phonesViews;
    private boolean isLastSelectedCategory = false;
    HideProgressBroadcastReceiver receiver = new HideProgressBroadcastReceiver();

    public static AddAdvertisementFragment newInstance(long categoryId, String categoryName) {
        AddAdvertisementFragment fragment = new AddAdvertisementFragment();
        Bundle args = new Bundle();
        args.putLong(CATEGORY_ID, categoryId);
        args.putString(CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        categoryFromArguments = getArguments().getLong(CATEGORY_ID);
        categoryName = getArguments().getString(CATEGORY_NAME);

        if (PersonalData.getInstance(getActivity().getApplicationContext()).getUserPhones() != null) {
            phonesViews = new ArrayList<>(PersonalData.getInstance(getActivity().getApplicationContext()).getUserPhones().size());
        } else {
            phonesViews = new ArrayList<>();
        }

        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putLong(CATEGORY_ID, categoryId);
            getLoaderManager().restartLoader(LoadersId.DB_CATEGORIES_ADD_ADVERT_LOADER_ID, args, this);

            if (PersonalData.getInstance(getActivity()).getUserSearchLocation() != null) {
                ArrayList<String> userSearchLocation = PersonalData.getInstance(getActivity()).getUserSearchLocation();
                for (String item : userSearchLocation) {
                    Log.e("Restore location", item);
                    locationArrayList.add(new Location(item));
                }
                userSearchLocationFromPreferences = true;
            }
            getLoaderManager().restartLoader(LoadersId.NETWORK_COUNTRY_LOADER_ID, args, this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savePhones();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        initializePhones();
    }

    @Override
    public void onPause() {
        super.onPause();
        savePhones();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getLoaderManager().getLoader(LoadersId.DB_CATEGORIES_ADD_ADVERT_LOADER_ID) != null) {
            getLoaderManager().destroyLoader(LoadersId.DB_CATEGORIES_ADD_ADVERT_LOADER_ID);
        }

        if (getLoaderManager().getLoader(LoadersId.NETWORK_COUNTRY_LOADER_ID) != null) {
            getLoaderManager().destroyLoader(LoadersId.NETWORK_COUNTRY_LOADER_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(RequestProcessor.HIDE_ADD_ADVERT_PROGRESS_BAR));
        if (priceType.getPriceType() == Advertisement.PRICE_TYPE_FREE) {
            priceSpinnerContainer.setVisibility(View.GONE);
            priceTitle.setVisibility(View.GONE);
            bargainContainer.setVisibility(View.GONE);
        }

        if (advertType.getAdvertType() == Advertisement.ADVERT_TYPE_BUY) {
            priceType.setVisibility(View.GONE);
            bargainContainer.setVisibility(View.GONE);
        }

        if (filterAdapter != null && filtersId != null) {
            if (filtersId.size() > 0 && filterAdapter != null) {
                restartLoader(LoadersId.DB_ADVERT_FILTERS_LOADER_ID);
            }
        }

        if (categoryAdapter != null) {
            if (categoriesPath.size() == 0 && categoryAdapter.getCategoryPath().size() > 0) {
                categoriesPath = categoryAdapter.getCategoryPath();
            }
        }
    }

    private void savePhones() {
        for (ArtJokerTextView view : phonesViews) {
            phonesList.add(view.getText().toString());
        }
        phonesViews.clear();
    }

    protected void initializePhones() {
        if (phonesLayout.getChildCount() == phonesList.size()) {
            for (int i = 0; i < phonesLayout.getChildCount(); i++) {
                ((ArtJokerTextView) phonesLayout.getChildAt(i).findViewById(R.id.tv_advert_phone)).setText(phonesList.get(i));
            }
        }
        phonesList.clear();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_advert;
    }

    @Override
    protected void initViews(View view) {
        title = (ArtJokerTextView) view.findViewById(R.id.add_adv_title);
        priceSpinner = (Spinner) view.findViewById(R.id.spinner_price);
        locationSpinner = (Spinner) view.findViewById(R.id.spinner_advert_city);
        categorySpinner = (Spinner) view.findViewById(R.id.spinner_advert_category);
        priceSpinnerContainer = (LinearLayout) view.findViewById(R.id.ll_spinner_price_container);
        bargainContainer = (LinearLayout) view.findViewById(R.id.ll_bargain);
        priceTitle = (ArtJokerTextView) view.findViewById(R.id.tv_price_title);
        advertTitle = (ArtJokerEditText) view.findViewById(R.id.et_advert_title);
        phonesLayout = (LinearLayout) view.findViewById(R.id.fl_add_advert_phone_container);
        titleLength = (ArtJokerTextView) view.findViewById(R.id.tv_advert_title_current_number_symb);
        advertDescription = (ArtJokerEditText) view.findViewById(R.id.et_advert_description);
        descriptionLength = (ArtJokerTextView) view.findViewById(R.id.tv_advert_descr_current_number_symb);
        isUsedRadioGroup = (AdvertStateRadioGroup) view.findViewById(R.id.rb_state);
        priceType = (AdvertPriceTypeRadioGroup) view.findViewById(R.id.rb_advert_type);
        advertType = (AdvertTypeRadioGroup) view.findViewById(R.id.add_adv_rb_type);
        bargain = (CheckBox) view.findViewById(R.id.rb_add_advert_bargain);
        publish = (ArtJokerButton) view.findViewById(R.id.button_advert_publish);
        review = (ArtJokerButton) view.findViewById(R.id.button_review);
        filtersListView = (ExpandableHeightListView) view.findViewById(R.id.filters_list_view);
        advertPrice = (ArtJokerEditText) view.findViewById(R.id.et_advert_price);
        addPhoto = (ImageButton) view.findViewById(R.id.button_add_photo);
        photosGridView = (ExpandableHeightGridView) view.findViewById(R.id.add_advert_photo_grid_view);
        licenceText = (ArtJokerTextView) view.findViewById(R.id.tv_add_advert_description);
        progressBarContainer = (FrameLayout) view.findViewById(R.id.add_advert_progress_bar_container);
        filtersDiffLine = (ArtJokerTextView) view.findViewById(R.id.diff_line_3);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }

    @Override
    protected void initListeners(View view) {
        publish.setOnClickListener(this);
        review.setOnClickListener(this);
        addPhoto.setOnClickListener(this);
        priceType.setListener(this);
        advertType.setListener(this);
        licenceText.setMovementMethod(LinkMovementMethod.getInstance());
        advertTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.charAt(0) == ' ') {
                    advertTitle.setText(advertTitle.getText().toString().substring(1));
                } else {
                    titleLength.setText(String.valueOf(advertTitle.getText().length()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        advertDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.charAt(0) == ' ') {
                    advertDescription.setText(advertDescription.getText().toString().substring(1));
                } else {
                    descriptionLength.setText(String.valueOf(advertDescription.getText().length()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void initAdapters() {

        filterAdapter = new FiltersCursorTreeAdapter(null, getActivity(), this);
        photoAdapter = new AddAdvertPhotoAdapter(getActivity(), photosList, this);

        SpinnerAdapter priceAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_price_item,
                Settings.getInstance(getActivity()).getCurrencies());
        priceSpinner.setAdapter(priceAdapter);

        photosGridView.setNumColumns(Config.PHOTOS_GRID_COLUMN_NUMBER);
        photoAdapter.setItems(photosList);
        photosGridView.setAdapter(photoAdapter);

        if (locationAdapter != null) {
            locationSpinner.setAdapter(locationAdapter);
        }

        if (categoryAdapter != null) {
            categorySpinner.setAdapter(categoryAdapter);
        }
    }

    @Override
    protected void initContent() {
        if (!android.text.TextUtils.isEmpty(categoryName) && categoryFromArguments != 0) {
            categoriesPath.add(categoryName);
            getCategoryFromArguments = true;
        }
        addPhoneField(false).setOnCheckedChangeListener(this);
    }

    @Override
    protected void initAdapter(AbsListView absListView) {

    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        if (getCategoryFromArguments) {
            manager.initLoader(LoadersId.DB_ADVERT_FILTERS_LOADER_ID, commonBundleBuilder.putLong(CATEGORY_ID, categoryFromArguments).build(), this);
        } else {
            manager.initLoader(LoadersId.DB_ADVERT_FILTERS_LOADER_ID, commonBundleBuilder.putLong(CATEGORY_ID, categoryId).build(), this);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.DB_CATEGORIES_ADD_ADVERT_LOADER_ID:
                return new RequestDescriptorWithLoadFromDB(getActivity(), CategoryContract.CONTENT_URI, Config.PROJECTION, Config.SELECTION,
                        buildSelectionArgs(args.getLong(CATEGORY_ID, categoryId)), null,
                        new SubCategoriesRequest(getActivity(), args), new LocalBroadcastRequestProcessor(getActivity()));

            case LoadersId.DB_ADVERT_FILTERS_LOADER_ID:
                if (getCategoryFromArguments) {
                    return new CursorLoader(getActivity(), CategoryFiltersContract.CONTENT_URI,
                            Config.CATEGORY_FILTER_PROJECTION, Config.CATEGORY_FILTER_SELECTION, buildSelectionArgs(categoryFromArguments), null);
                } else {
                    return new CursorLoader(getActivity(), CategoryFiltersContract.CONTENT_URI,
                            Config.CATEGORY_FILTER_PROJECTION, Config.CATEGORY_FILTER_SELECTION, buildSelectionArgs(categoryId), null);
                }

            case LoadersId.NETWORK_ADD_ADVERTISEMENT_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new AddAdvertisementRequest(getActivity(), args).setUiCallback(this));

            case LoadersId.ADD_ADVERT_PHOTOS_LOADER_ID:
                try {
                    return new LocalRequestDescriptorLoader(getActivity(), new AddAdvertisementPhotos(getActivity(), args, photos).setUiCallback(this));
                } catch (Exception ex) {
                    Log.e("Timeout", "Timeout");
                }
            case LoadersId.NETWORK_COUNTRY_LOADER_ID:
                isLocationItemsLoading = true;
                return new RequestDescriptorWithLoadFromDB(getActivity(), CountryContract.CONTENT_URI, null, null,
                        null, null, new CountryRequest(getActivity(), args), new LocalBroadcastRequestProcessor(getActivity()));

            case LoadersId.NETWORK_REGION_LOADER_ID:
                isLocationItemsLoading = true;
                return new RequestDescriptorWithLoadFromDB(getActivity(), RegionContract.CONTENT_URI, null, Config.REGION_SELECTION,
                        buildSelectionArgs(args.getLong(COUNTRY_ID)), null, new RegionRequest(getActivity(), args),
                        new LocalBroadcastRequestProcessor(getActivity()));

            case LoadersId.NETWORK_CITY_LOADER_ID:
                isLocationItemsLoading = true;
                return new RequestDescriptorWithLoadFromDB(getActivity(), CityContract.CONTENT_URI, null, Config.CITY_SELECTION,
                        buildSelectionArgs(args.getLong(REGION_ID)), null, new CityRequest(getActivity(), args),
                        new LocalBroadcastRequestProcessor(getActivity()));

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LoadersId.DB_CATEGORIES_ADD_ADVERT_LOADER_ID:
                if (data != null) {
                    SpinnerItem[] items = ((Launcher) getActivity()).createSpinnerItem((Cursor) data, categoriesId, categoryId);
                    if (items.length > 1) {
                        categoryAdapter = new CategorySpinnerAdapter(getActivity().getApplicationContext(),
                                R.layout.advertisement_spinner_item, items, categoriesPath, this);
                        categorySpinner.setAdapter(categoryAdapter);
                    } else {
                        if (categoriesId.size() > 0) {
                            categoriesId.remove(categoriesId.size() - 1);
                            categorySpinner.setSelection(categoryPosition);
                            SystemHelper.getInstance().closeSpinner(categorySpinner);
                            restartLoader(LoadersId.DB_ADVERT_FILTERS_LOADER_ID);
                        }
                        isLastSelectedCategory = true;
                    }
                }
                break;

            case LoadersId.DB_ADVERT_FILTERS_LOADER_ID:
                if (data != null && ((Cursor) data).getCount() > 0) {
                    filterAdapter = new FiltersCursorTreeAdapter((Cursor) data, getActivity(), this);
                    filterAdapter.setSelectedFiltersId(filtersId);
                    filtersListView.setAdapter(filterAdapter);
                    filtersDiffLine.setVisibility(View.VISIBLE);
                } else {
                    filtersDiffLine.setVisibility(View.GONE);
                }
                title.setFocusableInTouchMode(true);
                title.requestFocus();
                break;

            case LoadersId.NETWORK_COUNTRY_LOADER_ID:
            case LoadersId.NETWORK_REGION_LOADER_ID:
            case LoadersId.NETWORK_CITY_LOADER_ID:
                if (data != null) {
                    ArrayList<Long> listOfIds = new ArrayList<>(locationArrayList.size());
                    for (Location location : locationArrayList) {
                        listOfIds.add(location.locationId);
                    }
                    SpinnerItem[] items = ((Launcher) getActivity()).createSpinnerItem((Cursor) data, listOfIds,
                            (locationArrayList.size() > 0 ? locationArrayList.get(locationArrayList.size() - 1).parentId : 0));
                    if (items.length > 1) {
                        locationAdapter = new LocationSpinnerAdapter(getActivity().getApplicationContext(),
                                R.layout.advertisement_spinner_item, items, locationArrayList, this);
                        locationSpinner.setAdapter(locationAdapter);
                        isLocationItemsLoading = false;
                    }
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    public void restartLoader(int loaderId) {
        Bundle args = new Bundle();
        args.putLong(CATEGORY_ID, categoryId);
        if (getLoaderManager().getLoader(loaderId) != null) {
            getLoaderManager().destroyLoader(loaderId);
        }
        getLoaderManager().initLoader(loaderId, args, this);
    }

    private void restartLocationLoader(int loaderId, Bundle args) {
        if (getLoaderManager().getLoader(loaderId) != null) {
            getLoaderManager().getLoader(loaderId).reset();
        }
        getLoaderManager().restartLoader(loaderId, args, this);
    }

    @Override
    public void onCategoryItemClick(int position, long id) {
        if (!isSpinnerItemsLoading) {
            if (categoriesId.size() == 0) {
                categoryAdapter.clearTitle();
                categoriesPath.clear();
                filtersId.clear();
                filtersNames.clear();
                getCategoryFromArguments = false;
            }

            if (position == 0) {
                if (categoriesId.size() > 0) {
                    if (categoriesId.size() == 1) {
                        categoriesId.clear();
                        categoriesPath.clear();
                        categories.clear();
                        categoryId = 0;
                        categoryPosition = 0;
                    } else {
                        categoriesId.remove(categoriesId.size() - 1);
                        categoriesPath.remove(categoriesPath.size() - 1);
                        categories.remove(categoryAdapter.getCategoryParentId(position));
                        categoryId = (categoriesId.size() > 0) ? categoriesId.get(categoriesId.size() - 1) : 0;
                        categoryPosition = position;
                    }
                    restartLoader(LoadersId.DB_CATEGORIES_ADD_ADVERT_LOADER_ID);
                } else {
                    categoryAdapter.clearTitle();
                }
            } else {
                if (categoryId != id) {
                    categoryId = id;
                    categoriesId.add(categoryId);
                    categoryPosition = position;
                    if (categories.size() != categoriesPath.size()) {
                        if (categoriesPath.size() == 0 && categoryAdapter.getCategoryPath().size() > 0) {
                            categoriesPath = categoryAdapter.getCategoryPath();
                        }
                    }
                    if (!categories.containsKey(categoryAdapter.getCategoryParentId(position))) {
                        categories.put(categoryAdapter.getCategoryParentId(position), id);
                        categoriesPath.add(categoryAdapter.getCategoryName(position));
                    } else {
                        categories.remove(categoryAdapter.getCategoryParentId(position));
                        categoriesPath.remove(categoriesPath.size() - 1);
                        categories.put(categoryAdapter.getCategoryParentId(position), id);
                        categoriesPath.add(categoryAdapter.getCategoryName(position));
                        filtersId.clear();
                        filtersNames.clear();
                    }
                    restartLoader(LoadersId.DB_CATEGORIES_ADD_ADVERT_LOADER_ID);
                }
            }
        }
    }

    @Override
    public void onLocationItemClick(int position, long id) {

        if (isLocationFromEditAdvert) {
            locationArrayList.clear();
            isLocationFromEditAdvert = false;
        }

        if (userSearchLocationFromPreferences) {
            locationArrayList.clear();
            userSearchLocationFromPreferences = false;
        }

        if (!isLocationItemsLoading) {
            switch (position) {
                case 0:
                    if (locationArrayList.size() > 0) {
                        switch (locationArrayList.size()) {
                            case 1:
                                locationArrayList.remove(0);
                            case 2:
                                locationArrayList.clear();
                                locationType = Config.TYPE_COUNTRY;
                                restartLocationLoader(LoadersId.NETWORK_COUNTRY_LOADER_ID, null);
                                break;

                            case 3:
                                locationType = Config.TYPE_REGION;
                                locationArrayList.remove(locationArrayList.size() - 1);
                                Bundle regionArgs = new Bundle();
                                regionArgs.putLong(COUNTRY_ID, locationArrayList.get(1).parentId);
                                restartLocationLoader(LoadersId.NETWORK_REGION_LOADER_ID, regionArgs);
                                break;
                        }
                    }
                    break;

                default:
                    switch (locationType) {
                        case Config.TYPE_COUNTRY:
                            Location country = new Location(id, locationAdapter.getLocationName(position), 0, Config.TYPE_COUNTRY);
                            locationType = Config.TYPE_REGION;
                            locationArrayList.add(country);
                            Bundle countryArgs = new Bundle();
                            countryArgs.putLong(COUNTRY_ID, id);
                            restartLocationLoader(LoadersId.NETWORK_REGION_LOADER_ID, countryArgs);
                            break;

                        case Config.TYPE_REGION:
                            if (locationArrayList.size() == 2) {
                                locationArrayList.remove(locationArrayList.size() - 1);
                            }
                            Location region = new Location(id, locationAdapter.getLocationName(position),
                                    locationArrayList.get(0).locationId, Config.TYPE_REGION);

                            locationType = Config.TYPE_CITY;
                            locationArrayList.add(region);
                            Bundle regionArgs = new Bundle();
                            regionArgs.putLong(COUNTRY_ID, locationArrayList.get(0).locationId);
                            regionArgs.putLong(REGION_ID, id);
                            restartLocationLoader(LoadersId.NETWORK_CITY_LOADER_ID, regionArgs);
                            break;

                        case Config.TYPE_CITY:
                            if (locationArrayList.size() == 3) {
                                locationArrayList.remove(locationArrayList.size() - 1);
                            }
                            Location city = new Location(id, locationAdapter.getLocationName(position),
                                    locationArrayList.get(1).locationId, Config.TYPE_CITY);
                            locationArrayList.add(city);
                            locationSpinner.setSelection(position);
                            SystemHelper.getInstance().closeSpinner(locationSpinner);
                            break;
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.button_add_photo:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_PHOTO);
                    } else {
                        doPhotoAction();
                    }
                } else {
                    doPhotoAction();
                }
                break;

            case R.id.button_advert_publish:
                if (isFieldsValid()) {
                    Log.e("!!!", "PUBLISH CLICK");
                    progressBarContainer.setVisibility(View.VISIBLE);
                    progressBarContainer.setOnClickListener(null);
                    Bundle args = createAdvertBundle();
                    getLoaderManager().restartLoader(LoadersId.NETWORK_ADD_ADVERTISEMENT_LOADER_ID, args, this).forceLoad();
                }
                Ui.hideKeyboard(getActivity(), v);
                break;

            case R.id.button_review:
                if (isFieldsValid()) {
                    commit(AdvertisementReviewFragment.newInstance(createAdvertBundle()),
                            AdvertisementReviewFragment.class.getSimpleName(),
                            AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                }
                break;

            case R.id.tv_advert_phone:
                Set<String> phonesList = PersonalData.getInstance(getActivity()).getUserPhones();
                final CharSequence[] phones = new CharSequence[phonesList.size()];
                int i = 0;
                for (String phone : phonesList) {
                    phones[i] = phone;
                    i++;
                }

                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.add_advert_select_phone_title))
                        .setItems(phones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((ArtJokerTextView) v).setText(phones[which]);
                                dialog.dismiss();
                            }
                        }).setPositiveButton(R.string.clear_phones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((ArtJokerTextView) v).setText("");
                        dialog.dismiss();
                    }
                }).show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doPhotoAction();
                } else {
                    Notification.getInstance().show(getActivity().getApplicationContext(), getResources().getString(R.string.error_while_get_external_storage_permossion));
                }
                break;
        }
    }

    protected void doPhotoAction() {
        if (photosList.size() < Settings.getInstance(getActivity()).getAdsPhotoMaxUploadAmount()) {
            CharSequence[] items = new CharSequence[]{getResources().getString(R.string.add_advert_photo_make_photo), getResources().getString(R.string.add_advert_photo_select_from_gallery)};
            new AlertDialog.Builder(getActivity())
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImagePath());
                                    startActivityForResult(cameraIntent, CAPTURE_PHOTO_BY_CAMERA);
                                    break;

                                case 1:
                                    Intent galleryIntent = new Intent(getActivity(), AlbumSelectActivity.class);
                                    galleryIntent.putExtra(Constants.INTENT_EXTRA_LIMIT,
                                            Settings.getInstance(getActivity()).getAdsPhotoMaxUploadAmount() - photosList.size());
/*                                            galleryIntent.putExtra(Constants.INTENT_EXTRA_PHOTO_SIZE,
                                            Settings.getInstance(getActivity()).getAdsPhotoMaxUploadSize() * 1024 * 1024);*/
                                    startActivityForResult(galleryIntent, Constants.GET_PHOTO_FROM_GALLERY);
                                    break;
                            }
                        }
                    }).show();
        } else {
            Notification.getInstance().show(getActivity(),
                    String.format(getResources().getString(R.string.warning_add_max_photo_count_title), Settings.getInstance(getActivity()).getAdsPhotoMaxUploadAmount()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.GET_PHOTO_FROM_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> imagesPath = data.getStringArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                    for (String path : imagesPath) {
                        photosList.add(path);
                    }
                    photosGridView.setNumColumns(Config.PHOTOS_GRID_COLUMN_NUMBER);
                    photoAdapter.setItems(photosList);
                    photosGridView.setAdapter(photoAdapter);
                    photosGridView.setFocusable(true);
                } else {
                    Notification.getInstance().show(getActivity(), R.string.error_while_add_photo);
                }
                break;

            case CAPTURE_PHOTO_BY_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    photosList.add(getImagePath().getPath());
                    photosGridView.setNumColumns(Config.PHOTOS_GRID_COLUMN_NUMBER);
                    photoAdapter.setItems(photosList);
                    photosGridView.setAdapter(photoAdapter);
                    currentTimeForTempPhoto = 0;
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public Uri getImagePath() {
        if (currentTimeForTempPhoto == 0) {
            currentTimeForTempPhoto = System.currentTimeMillis();
        }
        File file = new File(Environment.getExternalStorageDirectory()
                + Config.DCIM_FOLDER, Config.PHOTO_FROM_CAMERA_NAME + "_" + currentTimeForTempPhoto);
        return Uri.fromFile(file);
    }

    protected boolean isFieldsValid() {
        switch (advertType.getAdvertType()) {
            case Advertisement.ADVERT_TYPE_SELL:
                if (locationArrayList.size() == 3) {
                    if ((categoryId != 0 && isLastSelectedCategory) || categoryFromArguments != 0) {
                        switch (priceType.getPriceType()) {
                            case Advertisement.PRICE_TYPE_SELL:
                            case Advertisement.PRICE_TYPE_EXCHANGE:
                                if (advertTitle.isValid() && advertDescription.isValid() && advertPrice.isValid()) {
                                    return true;
                                } else {
                                    Notification.getInstance().show(getActivity(), getResources().getString(R.string.add_advert_check_parameters));
                                    return false;
                                }

                            case Advertisement.PRICE_TYPE_FREE:
                                if (advertTitle.isValid() && advertDescription.isValid()) {
                                    return true;
                                } else {
                                    Notification.getInstance().show(getActivity(), getResources().getString(R.string.add_advert_check_parameters));
                                    return false;
                                }

                            default:
                                Notification.getInstance().show(getActivity(), getResources().getString(R.string.add_advert_empty_price_type));
                                return false;
                        }
                    } else {
                        Notification.getInstance().show(getActivity(), getResources().getString(R.string.add_advert_empty_category));
                        return false;
                    }
                } else {
                    Notification.getInstance().show(getActivity(), getResources().getString(R.string.add_advert_empty_location));
                    return false;
                }

            case Advertisement.ADVERT_TYPE_BUY:
                if (advertTitle.isValid() && advertDescription.isValid() && advertPrice.isValid()) {
                    if (locationArrayList.size() == 3) {
                        if (categoryId != 0 || categoryFromArguments != 0) {
                            return true;
                        } else {
                            Notification.getInstance().show(getActivity(), getResources().getString(R.string.add_advert_empty_category));
                            return false;
                        }
                    } else {
                        Notification.getInstance().show(getActivity(), getResources().getString(R.string.add_advert_empty_location));
                        return false;
                    }

                } else {
                    Notification.getInstance().show(getActivity(), getResources().getString(R.string.add_advert_check_parameters));
                    return false;
                }

            default:
                Notification.getInstance().show(getActivity(), getResources().getString(R.string.add_advert_empty_advert_type));
                return false;
        }
    }

    protected Bundle createAdvertBundle() {
        Bundle args = new Bundle();
        args.putLong(ADVERT_TYPE, advertType.getAdvertType());
        args.putString(ADVERT_TITLE, advertTitle.getText().toString());
        args.putString(ADVERT_DESCRIPTION, advertDescription.getText().toString());
        args.putInt(ADVERT_PRICE_TYPE, priceType.getPriceType());
        args.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());

        // TODO: 13.06.2017
        args.putLong(CATEGORY_ID, (categoryId == 0) ? categoryFromArguments : categoryId);

        if (android.text.TextUtils.isEmpty(advertPrice.getText().toString())) {
            args.putLong(ADVERT_PRICE, 0);
        } else {
            args.putLong(ADVERT_PRICE, Long.valueOf(advertPrice.getText().toString()));
        }

        ArrayList<String> phones = new ArrayList<>(phonesViews.size());
        if (phonesLayout.getChildCount() > 0) {
            for (int i = 0; i < phonesLayout.getChildCount(); i++) {
                ArtJokerTextView view = (ArtJokerTextView) phonesLayout.getChildAt(i).findViewById(R.id.tv_advert_phone);
                if (view != null && !TextUtils.isEmpty(view.getText().toString())) {
                    phones.add(view.getText().toString());
                }
            }
        }

        args.putLong(ADVERT_CURRENCY_ID, ((Currency) priceSpinner.getSelectedItem()).getId());
        putLocationData(args);
        if (phones.size() > 0) {
            args.putStringArrayList(ADVERT_PHONES, phones);
        }
        args.putInt(ADVERT_USED, isUsedRadioGroup.getType());
        args.putInt(ADVERT_BARGAIN, (bargain.isChecked()) ? 1 : 0);
        args.putIntegerArrayList(ADVERT_OPTIONS, filtersId);
        args.putStringArrayList(Config.PHOTOS, photosList);
        args.putStringArrayList(PATH, categoriesPath);
        args.putStringArrayList(FILTERS_NAMES, filtersNames);
        args.putString(CITY_NAME, String.format(getResources().getString(R.string.advert_location_format),
                locationArrayList.get(0).getLocationName(), locationArrayList.get(2).getLocationName()));
        Log.d(AddAdvertisementFragment.class.getSimpleName(), args.toString());
        logArgs(args);
        return args;
    }

    private void logArgs(Bundle args) {
        String tag = "args add adv";
        Log.e(tag, args.toString());
    }

    private void putLocationData(Bundle args) {
        for (Location location : locationArrayList) {
            if (location.getLocationType() == Config.TYPE_COUNTRY) {
                args.putLong(ADVERT_COUNTRY_ID, location.getLocationId());
                Log.e("ADVERT_COUNTRY_ID", location.toString());
            } else if (location.getLocationType() == Config.TYPE_REGION) {
                args.putLong(ADVERT_REGION_ID, location.getLocationId());
                Log.e("ADVERT_REGION_ID", location.toString());
            } else if (location.getLocationType() == Config.TYPE_CITY) {
                args.putLong(ADVERT_CITY_ID, location.getLocationId());
                Log.e("ADVERT_CITY_ID", location.toString());
            }
        }
    }

    /*public void sendPhotos(long adId) {
        if (photosList != null && photosList.size() > 0) {
            photos = new HashMap<>();
            for (int i = 0; i < photosList.size(); i++) {
                File file = new File(photosList.get(i));
                photos.put(String.format(Config.PHOTO_PARAMETER, i), new TypedFile(Config.JPEG_MIME_TYPE, file));
            }
            Bundle args = new Bundle();
            args.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
            args.putStringArrayList(DELETED_PHOTOS, new ArrayList<String>());
            args.putLong(AD_ID, adId);
            getLoaderManager().restartLoader(LoadersId.ADD_ADVERT_PHOTOS_LOADER_ID, args, this).forceLoad();
        }
    }*/
    public void sendPhotos(long adId) {
        if (photosList != null && photosList.size() > 0) {
            photos = new HashMap<>();
            for (int i = 0; i < photosList.size(); i++) {
                File file = new File(photosList.get(i));
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bm2 = BitmapFactory.decodeFile(file.getPath(), options);
                    OutputStream stream = new FileOutputStream(file);
                    bm2.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TypedFile value = new TypedFile(Config.JPEG_MIME_TYPE, file);
                photos.put(String.format(Config.PHOTO_PARAMETER, i), value);
            }
            Bundle args = new Bundle();
            args.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
            args.putStringArrayList(DELETED_PHOTOS, new ArrayList<String>());
            args.putLong(AD_ID, adId);
            getLoaderManager().restartLoader(LoadersId.ADD_ADVERT_PHOTOS_LOADER_ID, args, this).forceLoad();
        }
    }

    @Override
    public void onSelect(View v) {
        if (v instanceof CheckBox) {
            if (((CheckBox) v).isChecked()) {
                filtersId.add((int) v.getTag(R.integer.filter_id_key));
                filtersNames.add((String) v.getTag(R.integer.filter_name_key));
            } else {
                filtersId.remove(v.getTag(R.integer.filter_id_key));
                filtersNames.remove((String) v.getTag(R.integer.filter_name_key));
            }
            filterAdapter.setSelectedFiltersId(filtersId);
        }
    }

    @Override
    public void uiDataResponse(ResponseHolder data) {
        Log.e("!!!", " uiDataResponse");
        if (data.getData() instanceof NewAdvert) {
            if (photosList.size() > 0) {
                sendPhotos(((NewAdvert) data.getData()).getId());
            } else {
                hideAddAdvertProgressBar();
                getActivity().getFragmentManager().popBackStack();
                commit(FragmentAdvertAdded.newInstance(FragmentAdvertAdded.AdvertAction.ADVERT_CREATED), FragmentAdvertAdded.class.getCanonicalName(), null);
            }
        } else if (data.getData() instanceof AdvertPhoto) {
            hideAddAdvertProgressBar();
            getActivity().getFragmentManager().popBackStack();
            commit(FragmentAdvertAdded.newInstance(FragmentAdvertAdded.AdvertAction.ADVERT_CREATED), FragmentAdvertAdded.class.getCanonicalName(), null);
        } else {
            hideAddAdvertProgressBar();
            Notification.getInstance().show(getActivity(), getResources().getString(R.string.add_advert_fail));
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(ADVERT_CREATED));
        }
    }

    public String[] buildSelectionArgs(long id) {
        return new String[]{
                String.valueOf(id),
        };
    }

    @Override
    public void onDeleteClick(int position) {
        photosList.remove(position);
        ((AddAdvertPhotoAdapter) photosGridView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onPriceTypeSelected(View view) {
        switch (view.getId()) {
            case R.id.rb_sell:
                priceType.setPriceType(Advertisement.PRICE_TYPE_SELL);
                priceSpinnerContainer.setVisibility(View.VISIBLE);
                priceTitle.setVisibility(View.VISIBLE);
                bargainContainer.setVisibility(View.VISIBLE);
                break;

            case R.id.rb_exchange:
                priceType.setPriceType(Advertisement.PRICE_TYPE_EXCHANGE);
                priceSpinnerContainer.setVisibility(View.VISIBLE);
                priceTitle.setVisibility(View.VISIBLE);
                bargainContainer.setVisibility(View.VISIBLE);
                break;

            case R.id.rb_free:
                priceType.setPriceType(Advertisement.PRICE_TYPE_FREE);
                priceSpinnerContainer.setVisibility(View.GONE);
                priceTitle.setVisibility(View.GONE);
                bargainContainer.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onAdvertTypeSelected(View v) {
        switch (v.getId()) {
            case R.id.rb_type_sell:
                advertType.setAdvertType(Advertisement.ADVERT_TYPE_SELL);
                priceType.setVisibility(View.VISIBLE);
                bargainContainer.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_type_buy:
                advertType.setAdvertType(Advertisement.ADVERT_TYPE_BUY);
                advertType.setAdvertType(Advertisement.ADVERT_TYPE_BUY);
                priceType.setVisibility(View.GONE);
                bargainContainer.setVisibility(View.GONE);
                break;
        }
    }

    public ToggleButton addPhoneField(boolean isChecked) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.add_phones_advert_layout, null, false);
        ArtJokerTextView phoneField = (ArtJokerTextView) view.findViewById(R.id.tv_advert_phone);
        phoneField.setOnClickListener(this);
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        phonesLayout.addView(view);
        phonesViews.add(phoneField);
        ToggleButton button = (ToggleButton) view.findViewById(R.id.button_add_phone);
        button.setChecked(isChecked);
        return button;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (phonesLayout.getChildCount() < PersonalData.getInstance(getActivity()).getUserPhones().size() - 1) {
                addPhoneField(false).setOnCheckedChangeListener(this);
            } else if (phonesLayout.getChildCount() == PersonalData.getInstance(getActivity()).getUserPhones().size()) {
                buttonView.setChecked(!isChecked);
                Notification.getInstance().show(getActivity(),
                        String.format(getString(R.string.add_advert_cannot_add_more_phones),
                                PersonalData.getInstance(getActivity()).getUserPhones().size()));
            } else {
                addPhoneField(true).setOnCheckedChangeListener(this);
            }
        } else {
            if (phonesLayout.getChildCount() > Config.MIN_PHONE_COUNT) {
                View rlContainerOfPhone = (View) (buttonView.getParent());
                phonesViews.remove(rlContainerOfPhone.findViewById(R.id.tv_advert_phone));
                phonesLayout.removeView(rlContainerOfPhone);
                if (phonesLayout.getChildCount() == Config.MIN_PHONE_COUNT) {
                    ToggleButton button = (ToggleButton) phonesLayout.findViewById(R.id.button_add_phone);
                    button.setOnCheckedChangeListener(null);
                    button.setChecked(false);
                    button.setOnCheckedChangeListener(this);
                }
            }
        }
    }

    public void hideAddAdvertProgressBar() {
        progressBarContainer.setVisibility(View.GONE);
    }

    public interface Config {
        String[] PROJECTION = new String[]{CategoryColumns.ID, CategoryColumns.NAME};
        String SELECTION = CategoryColumns.PARENT_ID + " = ?";
        String[] CATEGORY_FILTER_PROJECTION = new String[]{FiltersColumns._ID, FiltersColumns.ID, FiltersColumns.NAME};
        String[] ADVERT_FILTER_PROJECTION = new String[]{AdvertFiltersContract._ID, AdvertFiltersContract.FILTER_VALUE_ID};
        String ADVERT_FILTER_SELECTION = AdvertFiltersContract.ADVERT_ID + " = ?";
        String CATEGORY_FILTER_SELECTION = FiltersColumns.CATEGORY_ID + " = ?";
        String[] PHOTO_PROJECTION = new String[]{PhotosColumns._ID, PhotosColumns.PHOTO_URL};
        String PHOTO_SELECTION = PhotosColumns.ADVERTISEMENT_ID + " = ?";
        String ADVERT_SELECTION = AdvertisementColumns.ID + " = ?";
        String REGION_SELECTION = RegionColumns.COUNTRY_ID + " = ? ";
        String CITY_SELECTION = CityColumns.REGION_ID + " = ? ";

        String PHOTOS = "photos";
        String DCIM_FOLDER = "/DCIM";
        String PHOTO_PARAMETER = "img[%d]";
        String JPEG_MIME_TYPE = "image/jpeg";
        String PHOTO_FROM_CAMERA_NAME = "temp_photo";
        int PHOTOS_GRID_COLUMN_NUMBER = 3;

        int TYPE_COUNTRY = 0;
        int TYPE_REGION = 1;
        int TYPE_CITY = 2;

        int SIZE_WITH_COUNTRY_ONLY = 1;
        int SIZE_WITH_COUNTRY_AND_REGION = 2;
        int SIZE_WITH_COUNTRY_REGION_CITY = 3;

        int MIN_PHONE_COUNT = 1;
    }

    class HideProgressBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Receive its shit", "Receive");
            hideAddAdvertProgressBar();
            Notification.getInstance().show(getActivity(), getResources().getString(R.string.timeout_request));
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(ADVERT_CREATED));
        }
    }
}