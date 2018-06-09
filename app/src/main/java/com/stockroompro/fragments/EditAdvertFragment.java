package com.stockroompro.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.tool.core.Notification;
import com.artjoker.tool.core.TextUtils;
import com.artjoker.tool.core.Ui;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.darsh.multipleimageselect.helpers.Constants;
import com.stockroompro.R;
import com.stockroompro.adapters.AddAdvertPhotoAdapter;
import com.stockroompro.adapters.FiltersCursorTreeAdapter;
import com.stockroompro.api.models.requests.EditAdvertisementRequest;
import com.stockroompro.database.DatabaseProvider;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.NewAdvert;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.Settings;
import com.stockroompro.models.columns.AdvertisementColumns;
import com.stockroompro.models.columns.PhotosColumns;
import com.stockroompro.models.contracts.AdvertFiltersContract;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.contracts.CategoryFiltersContract;
import com.stockroompro.models.contracts.PhotosContract;
import com.stockroompro.models.converters.AdvertisementCursorConverter;
import com.stockroompro.models.spinners.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.mime.TypedFile;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DELETED_PHOTOS;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;

/**
 * Created by bagach.alexandr on 18.05.15.
 */
public class EditAdvertFragment extends AddAdvertisementFragment {

    public static final int ACTIVE_STATE = 1;
    private ToggleButton advertActive;
    private Button saveAdvert;
    private List<String> phones = new ArrayList<>();

    private long advertId;
    private ArrayList<String> newAddedPhotos = new ArrayList<>();
    private ArrayList<String> deletedPhotos = new ArrayList<>();

    public static EditAdvertFragment newInstance(long id) {
        EditAdvertFragment fragment = new EditAdvertFragment();
        Bundle args = new Bundle();
        args.putLong(AD_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        advertId = getArguments().getLong(AD_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        getParent().setMainTitle(getString(R.string.advert_text_title));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_advert;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putLong(AD_ID, advertId);
            getLoaderManager().restartLoader(LoadersId.ADVERT_PHOTOS_DB_LOADER, bundle, this);
            getLoaderManager().restartLoader(LoadersId.DB_ADVERTISEMENT_LOADER_ID, bundle, this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        phones = phonesList;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        removeLastPhonesLayoutElement();
    }

    private void removeLastPhonesLayoutElement() {
        if (phones.size() > 0) {
            if (phonesLayout.getChildCount() > phones.size()) {
                phonesLayout.removeView(phonesLayout.getChildAt(phonesLayout.getChildCount() - 1));
                phonesViews.remove(phonesViews.size() - 1);
            }
        }
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        advertActive = (ToggleButton) view.findViewById(R.id.tb_active_advert_edit_advert);
        saveAdvert = (ArtJokerButton) view.findViewById(R.id.button_edit_advert_publish);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return NotificationsPolicy.DO_NOT_NOTIFY_ALL;
    }


    @Override
    protected void initListeners(View view) {
        priceType.setListener(this);
        saveAdvert.setOnClickListener(this);
        addPhoto.setOnClickListener(this);
        advertType.setListener(this);

        advertTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                titleLength.setText(String.valueOf(advertTitle.getText().length()));
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
                descriptionLength.setText(String.valueOf(advertDescription.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void initAdapters() {
        super.initAdapters();
        photosGridView.setNumColumns(Config.PHOTOS_GRID_COLUMN_NUMBER);
        photosGridView.setAdapter(photoAdapter);
        filterAdapter = new FiltersCursorTreeAdapter(null, getActivity(), this);
        filtersListView.setAdapter(filterAdapter);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.ADVERT_PHOTOS_DB_LOADER:
                return new CursorLoader(getActivity(), PhotosContract.CONTENT_URI, Config.PHOTO_PROJECTION,
                        Config.PHOTO_SELECTION, buildSelectionArgs(advertId), null);

            case LoadersId.DB_ADVERTISEMENT_LOADER_ID:
                return new CursorLoader(getActivity(), AdvertisementContract.CONTENT_URI, null,
                        Config.ADVERT_SELECTION, buildSelectionArgs(advertId), null);

            case LoadersId.NETWORK_EDIT_ADVERTISEMENT_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new EditAdvertisementRequest(getActivity(), args).setUiCallback(this));

            case LoadersId.DB_ADVERT_FILTERS_LOADER_ID:
                return new CursorLoader(getActivity(), CategoryFiltersContract.CONTENT_URI,
                        Config.CATEGORY_FILTER_PROJECTION, Config.CATEGORY_FILTER_SELECTION, buildSelectionArgs(categoryId), null);

            case LoadersId.EDIT_ADVERT_FILTERS:
                return new CursorLoader(getActivity(), AdvertFiltersContract.CONTENT_URI,
                        Config.ADVERT_FILTER_PROJECTION, Config.ADVERT_FILTER_SELECTION, buildSelectionArgs(advertId), null);

            default:
                return super.onCreateLoader(id, args);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LoadersId.ADVERT_PHOTOS_DB_LOADER:
                if (data != null) {
                    Cursor cursor = (Cursor) data;
                    if (cursor.moveToFirst()) {
                        do {
                            photosList.add(cursor.getString(cursor.getColumnIndexOrThrow(PhotosColumns.PHOTO_URL)));
                        } while (cursor.moveToNext());
                        photoAdapter.setItems(photosList);
                        photoAdapter.notifyDataSetChanged();
                    }
                }
                break;

            case LoadersId.DB_ADVERTISEMENT_LOADER_ID:
                if (data != null) {
                    Cursor c = (Cursor) data;
                    if (c.moveToFirst()) {
                        AdvertisementCursorConverter converter = new AdvertisementCursorConverter();
                        converter.setCursor(c);
                        initData(converter);
                    }
                }
                break;

            case LoadersId.DB_ADVERT_FILTERS_LOADER_ID:
                if (data != null && ((Cursor) data).getCount() > 0) {
                    DatabaseProvider.printLogs(true);
                    DatabaseProvider.logCursor((Cursor) data);
                    filterAdapter.changeCursor((Cursor) data);
                    getLoaderManager().restartLoader(LoadersId.EDIT_ADVERT_FILTERS, null, this);
                }
                break;

            case LoadersId.EDIT_ADVERT_FILTERS:
                if (data != null && ((Cursor) data).getCount() > 0) {
                    Cursor c = (Cursor) data;
                    DatabaseProvider.printLogs(true);
                    DatabaseProvider.logCursor(c);
                    filtersId = new ArrayList<>(c.getCount());
                    if (c.moveToFirst()) {
                        do {
                            filtersId.add(c.getInt(c.getColumnIndexOrThrow(AdvertFiltersContract.FILTER_VALUE_ID)));
                        } while (c.moveToNext());
                    }

                    filterAdapter.setSelectedFiltersId(filtersId);
                }
                title.setFocusableInTouchMode(true);
                title.requestFocus();
                break;

            default:
                super.onLoadFinished(loader, data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        super.onLoaderReset(loader);
    }

    private void initData(AdvertisementCursorConverter converter) {
        if (converter != null) {
            advertActive.setChecked(converter.getObject().getActive() == ACTIVE_STATE);
            advertTitle.setText(converter.getObject().getTitle());
            advertDescription.setText(converter.getObject().getDescription());
            isUsedRadioGroup.setSelected(converter.getObject().getUsed());
            priceType.setButtons(converter.getObject().getPriceType());
            priceType.setPriceType(converter.getObject().getPriceType());
            advertPrice.setText(TextUtils.getInstance().getStringFromDoubleValue(converter.getObject().getPrice()));

            String currencyName = Settings.getInstance(getActivity()).getCurrencyById(converter.getObject().getCurrencyId()).getName();
            if (!android.text.TextUtils.isEmpty(currencyName)) {
                setPriceSpinnerSelection(currencyName);
            }

            advertType.setAdvertType((int) converter.getObject().getType());
            locationArrayList.add(new Location(converter.getObject().getCountryId(),
                    converter.getObject().getCountryName(), 0, Config.TYPE_COUNTRY));

            locationArrayList.add(new Location(converter.getObject().getRegionId(),
                    converter.getObject().getRegionName(), converter.getObject().getCountryId(), Config.TYPE_REGION));

            locationArrayList.add(new Location(converter.getObject().getCityId(),
                    converter.getObject().getCityName(), converter.getObject().getRegionId(), Config.TYPE_CITY));

            categoriesPath.add(converter.getObject().getCategoryName());
            categoryId = converter.getObject().getCategoryId();
            advertActive.setChecked(converter.getObject().getActive() == 1);
            initPhones(converter.getObject().getPhones());
            isLocationFromEditAdvert = true;

            filtersId = converter.getObject().getFilters();
            if (filtersId == null) {
                filtersId = new ArrayList<>();
            }
            Bundle bundle = new Bundle();
            bundle.putLong(CATEGORY_ID, categoryId);
            getLoaderManager().restartLoader(LoadersId.DB_ADVERT_FILTERS_LOADER_ID, bundle, this);
        }
    }

    private void setPriceSpinnerSelection(String myString) {
        int index = 0;
        for (int i = 0; i < priceSpinner.getCount(); i++) {
            if (priceSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        priceSpinner.setSelection(index);
    }

    private void initPhones(ArrayList<String> advertPhonesList) {
        phones = advertPhonesList;
        if (advertPhonesList.size() > 0) {
            for (int i = 0; i < advertPhonesList.size(); i++) {
                addPhoneField(true).setOnCheckedChangeListener(this);
                ((ArtJokerTextView) phonesLayout.getChildAt(i).findViewById(R.id.tv_advert_phone)).setText(advertPhonesList.get(i));
            }
        } else {
            addPhoneField(true).setOnCheckedChangeListener(this);
        }
        removeLastPhonesLayoutElement();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_edit_advert_publish:
                if (isFieldsValid()) {
                    progressBarContainer.setVisibility(View.VISIBLE);
                    progressBarContainer.setOnClickListener(null);
                    Bundle args = createAdvertBundle();
                    args.putLong(AD_ID, advertId);
                    args.putInt(AdvertisementColumns.ACTIVE, advertActive.isChecked() ? 1 : 0);
                    getLoaderManager().restartLoader(LoadersId.NETWORK_EDIT_ADVERTISEMENT_LOADER_ID, args, this).forceLoad();
                }
                Ui.hideKeyboard(getActivity(), v);
                break;

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

            default:
                super.onClick(v);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.GET_PHOTO_FROM_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> imagesPath = data.getStringArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                    for (String path : imagesPath) {
                        newAddedPhotos.add(path);
                        photosList.add(path);
                    }
                    photosGridView.setNumColumns(Config.PHOTOS_GRID_COLUMN_NUMBER);
                    photoAdapter.setItems(photosList);
                    photosGridView.setAdapter(photoAdapter);
                }
                break;

            case CAPTURE_PHOTO_BY_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    photosList.add(getImagePath().getPath());
                    newAddedPhotos.add(getImagePath().getPath());
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

    @Override
    public void onDeleteClick(int position) {
        if (newAddedPhotos.contains(Uri.parse(((AddAdvertPhotoAdapter) photosGridView.getAdapter()).getItemName(position)).getPath())) {
            newAddedPhotos.remove(Uri.parse(((AddAdvertPhotoAdapter) photosGridView.getAdapter()).getItemName(position)).getPath());
        } else {
            deletedPhotos.add(Uri.parse(((AddAdvertPhotoAdapter) photosGridView.getAdapter()).getItemName(position)).getLastPathSegment());
        }
        photosList.remove(position);
        ((AddAdvertPhotoAdapter) photosGridView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void uiDataResponse(ResponseHolder data) {
        if (data.getData() instanceof NewAdvert) {
            if (newAddedPhotos.size() > 0 || deletedPhotos.size() > 0) {
                sendPhotos(((NewAdvert) data.getData()).getId());
            } else {
                hideAddAdvertProgressBar();
                commit(FragmentAdvertAdded.newInstance(FragmentAdvertAdded.AdvertAction.ADVERT_EDITED),
                        FragmentAdvertAdded.class.getCanonicalName(), null);
            }
        } else if (data.getStatusCode() == 0) {
            hideAddAdvertProgressBar();
            commit(FragmentAdvertAdded.newInstance(FragmentAdvertAdded.AdvertAction.ADVERT_EDITED),
                    FragmentAdvertAdded.class.getCanonicalName(),
                    AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
        } else {
            hideAddAdvertProgressBar();
            Notification.getInstance().show(getActivity(), getResources().getString(R.string.add_advert_fail));
        }
    }

    @Override
    public void sendPhotos(long adId) {
        Bundle args = new Bundle();
        args.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
        args.putLong(AD_ID, adId);
        if (newAddedPhotos != null && newAddedPhotos.size() > 0) {
            photos = new HashMap<>();
            for (int i = 0; i < newAddedPhotos.size(); i++) {
                File file = new File(newAddedPhotos.get(i));
                photos.put(String.format(Config.PHOTO_PARAMETER, i), new TypedFile(Config.JPEG_MIME_TYPE, file));
            }
        }

        if (deletedPhotos != null && deletedPhotos.size() > 0) {
            args.putStringArrayList(DELETED_PHOTOS, deletedPhotos);
        }
        getLoaderManager().restartLoader(LoadersId.ADD_ADVERT_PHOTOS_LOADER_ID, args, this).forceLoad();
    }
}
