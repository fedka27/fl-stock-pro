package com.stockroompro.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.artjoker.core.BundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.RequestDescriptorWithLoadFromDB;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.core.views.ArtJokerEditText;
import com.artjoker.tool.core.Notification;
import com.artjoker.tool.core.SystemHelper;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stockroompro.Launcher;
import com.stockroompro.R;
import com.stockroompro.adapters.LocationSpinnerAdapter;
import com.stockroompro.api.models.requests.CityRequest;
import com.stockroompro.api.models.requests.CountryRequest;
import com.stockroompro.api.models.requests.DeleteProfilePhotoRequest;
import com.stockroompro.api.models.requests.EditProfilePhotoRequest;
import com.stockroompro.api.models.requests.EditUserRequest;
import com.stockroompro.api.models.requests.RegionRequest;
import com.stockroompro.fragments.base.BaseApplicationFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalBroadcastRequestProcessor;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.Settings;
import com.stockroompro.models.contracts.CityContract;
import com.stockroompro.models.contracts.CountryContract;
import com.stockroompro.models.contracts.RegionContract;
import com.stockroompro.models.spinners.Location;
import com.stockroompro.models.spinners.SpinnerItem;
import com.stockroompro.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static android.graphics.Typeface.NORMAL;
import static android.graphics.Typeface.create;
import static android.provider.MediaStore.MediaColumns.DATA;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CITY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.EMAIL;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.FILE_PATH;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.FIRST_NAME;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.PHONE_NUMBER;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.REGION_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by user on 09.04.15.
 */

public class EditProfileFragment extends BaseApplicationFragment
        implements TextWatcher, CompoundButton.OnCheckedChangeListener, View.OnClickListener,
        LoaderManager.LoaderCallbacks, BaseRequest.UIResponseCallback, LocationSpinnerAdapter.OnLocationSpinnerItemClickListener {
    public static final String IMAGE_TYPE = "image/*";
    public static final int FIRST_INDEX = 0;
    public static final String ALL = "all";
    public final int RESULT_CODE_PICKER_IMAGES = 999;
    private SimpleDraweeView photoIv;
    private ArtJokerEditText nameEt;

    private ArtJokerEditText emailEt;
    private ArtJokerButton saveButton;
    private Spinner locationSpinner;
    private ArtJokerButton deleteProfilePhoto;
    private ArtJokerButton editProfilePhoto;
    public static final int MAX_PHONES_COUNT = 3;
    public static final int MIN_PHONE_COUNT = 1;
    private LinearLayout phonesLayout;
    private ArrayList<ArtJokerEditText> phonesViews = new ArrayList<ArtJokerEditText>(MIN_PHONE_COUNT);
    private ArrayList<String> phones;
    private int i = 1000;

    private boolean userLocationFromPreferences = false;
    protected boolean isLocationItemsLoading = false;
    protected LocationSpinnerAdapter locationAdapter;
    protected ArrayList<Location> locationArrayList = new ArrayList<>(3);
    protected int locationType = AddAdvertisementFragment.Config.TYPE_COUNTRY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        phones = new ArrayList<String>(MIN_PHONE_COUNT);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_profile;
    }

    @Override
    protected void initListeners(View view) {
        deleteProfilePhoto.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        editProfilePhoto.setOnClickListener(this);
        nameEt.addTextChangedListener(this);
        emailEt.addTextChangedListener(this);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }

    @Override
    protected void initViews(View view) {
        photoIv = (SimpleDraweeView) view.findViewById(R.id.iv_edit_profile_image);
        deleteProfilePhoto = (ArtJokerButton) view.findViewById(R.id.button_delete_profile_photo);
        nameEt = (ArtJokerEditText) view.findViewById(R.id.et_edit_profile_name);
        emailEt = (ArtJokerEditText) view.findViewById(R.id.et_edit_profile_email);
        locationSpinner = (Spinner) view.findViewById(R.id.fragment_edit_profile_location_spinner);
        saveButton = (ArtJokerButton) view.findViewById(R.id.button_save_profile_changes);
        editProfilePhoto = (ArtJokerButton) view.findViewById(R.id.button_edit_profile_photo);
        emailEt.setValidateEmail(true);
        photoIv.setImageURI(PersonalData.getInstance(getActivity()).getUserPicture());
        saveButton.setEnabled(false);
        phonesLayout = (LinearLayout) view.findViewById(R.id.phonesLayout);
        initPhones(view);
    }

    @Override
    protected void initContent() {
        emailEt.setText(PersonalData.getInstance(getActivity()).getUserEmail());
        nameEt.setText(PersonalData.getInstance(getActivity()).getUserFirstName());
        initLocationSpinner();
        getLoaderManager().initLoader(LoadersId.NETWORK_COUNTRY_LOADER_ID, null, this);
    }

    @Override
    protected void initAdapters() {
        locationAdapter = new LocationSpinnerAdapter(getActivity().getApplicationContext(),
                R.layout.advertisement_spinner_item, new SpinnerItem[]{}, locationArrayList, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        phones.clear();
        for (int i = 0; i < phonesLayout.getChildCount(); i++) {
            phones.add(((ArtJokerEditText) phonesLayout.getChildAt(i).findViewById(R.id.et_reg_phone_1)).getText().toString());
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        for (int i = 0; i < phonesLayout.getChildCount(); i++) {
            if (phones.size() >= phonesLayout.getChildCount())
                ((ArtJokerEditText) phonesLayout.getChildAt(i).findViewById(R.id.et_reg_phone_1)).setText(phones.get(i));
        }
    }

    private void initPhones(View view) {
        String[] phone = PersonalData.getInstance(getActivity()).getUserPhones().toArray(new String[]{});
        if (phone.length > 0) {

            for (int i = 0; i < phone.length; i++) {
                if (phone.length == MIN_PHONE_COUNT) {
                    addPhoneField(false).setOnCheckedChangeListener(this);
                } else if (phone.length == MIN_PHONE_COUNT + 1) {
                    if (i == MIN_PHONE_COUNT) {
                        addPhoneField(false).setOnCheckedChangeListener(this);
                    } else {
                        addPhoneField(true).setOnCheckedChangeListener(this);
                    }
                } else {
                    addPhoneField(true).setOnCheckedChangeListener(this);
                }
                phonesViews.get(i).setText(phone[i]);
            }
        } else {
            addPhoneField(false).setOnCheckedChangeListener(this); // if no phones  show empty field with +
        }
    }

    private void initLocationSpinner() {
        if (PersonalData.getInstance(getActivity()).getUserCountryId() != 0) {
            Location country = new Location(PersonalData.getInstance(getActivity()).getUserCountryId(),
                    PersonalData.getInstance(getActivity()).getUserCountryName(),
                    0, AddAdvertisementFragment.Config.TYPE_COUNTRY);
            locationArrayList.add(country);
        }
        if (PersonalData.getInstance(getActivity()).getUserRegionId() != 0) {
            Location region = new Location(PersonalData.getInstance(getActivity()).getUserRegionId(),
                    PersonalData.getInstance(getActivity()).getUserRegionName(),
                    0, AddAdvertisementFragment.Config.TYPE_REGION);
            locationArrayList.add(region);
        }
        if (PersonalData.getInstance(getActivity()).getUserCityId() != 0) {
            Location city = new Location(PersonalData.getInstance(getActivity()).getUserCityId(),
                    PersonalData.getInstance(getActivity()).getUserCityName(),
                    0, AddAdvertisementFragment.Config.TYPE_CITY);
            locationArrayList.add(city);
        }
        if (locationArrayList.size() > 0) {
            userLocationFromPreferences = true;
        }
    }

    @SuppressWarnings(ALL)
    private ToggleButton addPhoneField(boolean isChecked) {
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.add_phone_layout, null, false);
        v.setId(i++);
        v.setSaveEnabled(true);
        phonesLayout.addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ArtJokerEditText phoneField = (ArtJokerEditText) v.findViewById(R.id.et_reg_phone_1);
        phoneField.setValidatePhone(true);
        phoneField.setTypeface(create(getString(R.string.sans_serif_light), NORMAL));
        phonesViews.add(phoneField);
        ToggleButton button = ((ToggleButton) v.findViewById(R.id.button_likes));
        button.setChecked(isChecked);
        return button;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (phonesLayout.getChildCount() < MAX_PHONES_COUNT - 1) {
                addPhoneField(false).setOnCheckedChangeListener(this);
            } else {
                addPhoneField(true).setOnCheckedChangeListener(this);
            }
        } else {
            if (phonesLayout.getChildCount() > MIN_PHONE_COUNT) {
                View rlContainerOfPhone = (View) (buttonView.getParent().getParent());
                phonesViews.remove((ArtJokerEditText) rlContainerOfPhone.findViewById(R.id.et_reg_phone_1));
                phonesLayout.removeView(rlContainerOfPhone);
                if (phonesLayout.getChildCount() == MIN_PHONE_COUNT || phonesLayout.getChildCount() < MAX_PHONES_COUNT) {
                    View container = phonesLayout.getChildAt(phonesLayout.getChildCount() - 1);
                    ToggleButton button = (ToggleButton) container.findViewById(R.id.button_likes);
                    button.setOnCheckedChangeListener(null);
                    button.setChecked(false);
                    button.setOnCheckedChangeListener(this);
                }
            }
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.NETWORK_EDIT_PROFILE_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new EditUserRequest(getActivity(), args).setUiCallback(this));

            case LoadersId.NETWORK_DELETE_PROFILE_PHOTO_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new DeleteProfilePhotoRequest(getActivity(), args));

            case LoadersId.NETWORK_EDIT_PROFILE_PHOTO_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new EditProfilePhotoRequest(getActivity(), args));

            case LoadersId.NETWORK_COUNTRY_LOADER_ID:
                isLocationItemsLoading = true;
                return new RequestDescriptorWithLoadFromDB(getActivity(), CountryContract.CONTENT_URI, null, null,
                        null, null, new CountryRequest(getActivity(), args), new LocalBroadcastRequestProcessor(getActivity()));

            case LoadersId.NETWORK_REGION_LOADER_ID:
                isLocationItemsLoading = true;
                return new RequestDescriptorWithLoadFromDB(getActivity(), RegionContract.CONTENT_URI, null, AddAdvertisementFragment.Config.REGION_SELECTION,
                        buildSelectionArgs(args.getLong(COUNTRY_ID)), null, new RegionRequest(getActivity(), args),
                        new LocalBroadcastRequestProcessor(getActivity()));

            case LoadersId.NETWORK_CITY_LOADER_ID:
                isLocationItemsLoading = true;
                return new RequestDescriptorWithLoadFromDB(getActivity(), CityContract.CONTENT_URI, null, AddAdvertisementFragment.Config.CITY_SELECTION,
                        buildSelectionArgs(args.getLong(REGION_ID)), null, new CityRequest(getActivity(), args),
                        new LocalBroadcastRequestProcessor(getActivity()));
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LoadersId.NETWORK_DELETE_PROFILE_PHOTO_ID:
                photoIv.setImageDrawable(null);
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
                        locationAdapter = new LocationSpinnerAdapter(getActivity(),
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

    public String[] buildSelectionArgs(long id) {
        return new String[]{
                String.valueOf(id),
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save_profile_changes:
                editProfile();
                hideKeyBoard();
                break;
            case R.id.button_delete_profile_photo:
                deleteProfilePhoto();
                break;
            case R.id.button_edit_profile_photo:
                editProfilePhoto();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_CODE_PICKER_IMAGES:
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = data.getData();
                    try {
                        File file = FileUtils.getFileFrom(getActivity(), imageUri);

                        Bitmap picture = decodeFile(imageUri);
                        if (picture != null) {
                            photoIv.setImageBitmap(picture);
                        }

                        if (file == null || TextUtils.isEmpty(file.getPath())) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle(R.string.rate_dialog_error_title)
                                    .setMessage(R.string.no_file_error)
                                    .create()
                                    .show();
                        } else if (file.length() > Settings.getInstance(getActivity()).getAdsPhotoMaxUploadSize() * 1024 * 1024) {
                            Notification.getInstance().show(getActivity(), String.format(getActivity().getString(R.string.cannot_load_photo_large_size),
                                    Settings.getInstance(getActivity()).getAdsPhotoMaxUploadSize()));
                        } else {
                            Bundle bundle = new BundleBuilder()
                                    .putString(FILE_PATH, file.getPath())
                                    .putString(TOKEN, PersonalData.getInstance(getActivity()).getToken())
                                    .putLong(UID, PersonalData.getInstance(getActivity()).getUserId())
                                    .build();

                            if (getLoaderManager().getLoader(LoadersId.NETWORK_EDIT_PROFILE_PHOTO_ID) != null)
                                getLoaderManager().restartLoader(LoadersId.NETWORK_EDIT_PROFILE_PHOTO_ID, bundle, this).forceLoad();
                            else
                                getLoaderManager().initLoader(LoadersId.NETWORK_EDIT_PROFILE_PHOTO_ID, bundle, this).forceLoad();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Notification.getInstance().show(getActivity(), getActivity().getString(R.string.cannot_load_photo));
                    }
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void editProfilePhoto() {
        Intent intent = new Intent();
        intent.setType(IMAGE_TYPE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.chooser_text)), RESULT_CODE_PICKER_IMAGES);
    }

    private void deleteProfilePhoto() {
        Bundle bundle = new BundleBuilder()
                .putLong(UID, PersonalData.getInstance(getActivity()).getUserId())
                .putString(TOKEN, PersonalData.getInstance(getActivity()).getToken())
                .build();
        if (getLoaderManager().getLoader(LoadersId.NETWORK_DELETE_PROFILE_PHOTO_ID) != null)
            getLoaderManager().restartLoader(LoadersId.NETWORK_DELETE_PROFILE_PHOTO_ID, bundle, this).forceLoad();
        else
            getLoaderManager().initLoader(LoadersId.NETWORK_DELETE_PROFILE_PHOTO_ID, bundle, this).forceLoad();
    }


    private void editProfile() {
        String name = "";
        if (nameEt.getText().toString().isEmpty())
            name = PersonalData.getInstance(getActivity()).getUserFirstName();
        else
            name = nameEt.getText().toString();


        String email = "";
        if (emailEt.getText().toString().isEmpty() || !emailEt.isValid())
            email = PersonalData.getInstance(getActivity()).getUserEmail();
        else
            email = emailEt.getText().toString();

        Bundle bundle = new BundleBuilder()
                .putLong(UID, PersonalData.getInstance(getActivity()).getUserId())
                .putString(TOKEN, PersonalData.getInstance(getActivity()).getToken())
                .putString(EMAIL, email)
                .putString(FIRST_NAME, name)
                .build();
        bundle.putStringArray(PHONE_NUMBER, getAllPhones().toArray(new String[]{}));

        switch (locationArrayList.size()) {
            case 1:
                bundle.putLong(COUNTRY_ID, locationArrayList.get(0).locationId);
                break;

            case 2:
                bundle.putLong(COUNTRY_ID, locationArrayList.get(0).locationId);
                bundle.putLong(REGION_ID, locationArrayList.get(1).locationId);
                break;

            case 3:
                bundle.putLong(COUNTRY_ID, locationArrayList.get(0).locationId);
                bundle.putLong(REGION_ID, locationArrayList.get(1).locationId);
                bundle.putLong(ADVERT_CITY_ID, locationArrayList.get(2).locationId);
                break;
        }

        getLoaderManager().restartLoader(LoadersId.NETWORK_EDIT_PROFILE_LOADER_ID, bundle, this).forceLoad();

    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst())
            try {
                int column_index = cursor.getColumnIndexOrThrow(DATA);
                return cursor.getString(column_index);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        return "";
    }

    private ArrayList<String> getAllPhones() {
        ArrayList<String> allPhones = new ArrayList<String>(MAX_PHONES_COUNT);
        for (int i = FIRST_INDEX; i < phonesViews.size(); i++) {
            allPhones.add(phonesViews.get(i).getText().toString());
        }

        return allPhones;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (nameEt.isValid() && emailEt.isValid()) {
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }

    }

    private Bitmap decodeFile(Uri uri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);


            final int REQUIRED_SIZE = 70;

            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    options.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    @Override
    public void uiDataResponse(ResponseHolder data) {
        if (data.getStatusCode() == 0) {
            Notification.getInstance().show(getActivity().getApplicationContext(),
                    getActivity().getApplicationContext().getString(R.string.change_personal_data_done));
            PersonalData.getInstance(getActivity()).clearUserLocation();
            switch (locationArrayList.size()) {
                case 1:
                    PersonalData.getInstance(getActivity()).setUserCountryId(locationArrayList.get(0).locationId);
                    PersonalData.getInstance(getActivity()).setUserCountryName(locationArrayList.get(0).locationName);
                    break;

                case 2:
                    PersonalData.getInstance(getActivity()).setUserCountryId(locationArrayList.get(0).locationId);
                    PersonalData.getInstance(getActivity()).setUserCountryName(locationArrayList.get(0).locationName);
                    PersonalData.getInstance(getActivity()).setUserRegionId(locationArrayList.get(1).locationId);
                    PersonalData.getInstance(getActivity()).setUserRegionName(locationArrayList.get(1).locationName);
                    break;

                case 3:
                    PersonalData.getInstance(getActivity()).setUserCountryId(locationArrayList.get(0).locationId);
                    PersonalData.getInstance(getActivity()).setUserCountryName(locationArrayList.get(0).locationName);
                    PersonalData.getInstance(getActivity()).setUserRegionId(locationArrayList.get(1).locationId);
                    PersonalData.getInstance(getActivity()).setUserRegionName(locationArrayList.get(1).locationName);
                    PersonalData.getInstance(getActivity()).setUserCityId(locationArrayList.get(2).locationId);
                    PersonalData.getInstance(getActivity()).setUserCityName(locationArrayList.get(2).locationName);
                    break;
            }
        } else {
            Notification.getInstance().show(getActivity().getApplicationContext(),
                    getActivity().getApplicationContext().getString(R.string.change_personal_data_fail));
        }
    }

    @Override
    public void onLocationItemClick(int position, long id) {

        if (userLocationFromPreferences) {
            locationArrayList.clear();
            userLocationFromPreferences = false;
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
                                locationType = AddAdvertisementFragment.Config.TYPE_COUNTRY;
                                initLocationLoader(LoadersId.NETWORK_COUNTRY_LOADER_ID, null);
                                break;

                            case 3:
                                locationType = AddAdvertisementFragment.Config.TYPE_REGION;
                                locationArrayList.remove(locationArrayList.size() - 1);
                                Bundle regionArgs = new Bundle();
                                regionArgs.putLong(COUNTRY_ID, locationArrayList.get(0).parentId);
                                initLocationLoader(LoadersId.NETWORK_REGION_LOADER_ID, regionArgs);
                                break;
                        }
                    }
                    break;

                default:
                    switch (locationType) {
                        case AddAdvertisementFragment.Config.TYPE_COUNTRY:
                            Location country = new Location(id, locationAdapter.getLocationName(position), 0, AddAdvertisementFragment.Config.TYPE_COUNTRY);
                            locationType = AddAdvertisementFragment.Config.TYPE_REGION;
                            locationArrayList.add(country);
                            Bundle countryArgs = new Bundle();
                            countryArgs.putLong(COUNTRY_ID, id);
                            restartLocationLoader(LoadersId.NETWORK_REGION_LOADER_ID, countryArgs);
                            break;

                        case AddAdvertisementFragment.Config.TYPE_REGION:
                            if (locationArrayList.size() == 2) {
                                locationArrayList.remove(locationArrayList.size() - 1);
                            }
                            Location region = new Location(id, locationAdapter.getLocationName(position),
                                    locationArrayList.get(0).locationId, AddAdvertisementFragment.Config.TYPE_REGION);

                            locationType = AddAdvertisementFragment.Config.TYPE_CITY;
                            locationArrayList.add(region);
                            Bundle regionArgs = new Bundle();
                            regionArgs.putLong(COUNTRY_ID, locationArrayList.get(0).locationId);
                            regionArgs.putLong(REGION_ID, id);
                            restartLocationLoader(LoadersId.NETWORK_CITY_LOADER_ID, regionArgs);
                            break;

                        case AddAdvertisementFragment.Config.TYPE_CITY:
                            if (locationArrayList.size() == 3) {
                                locationArrayList.remove(locationArrayList.size() - 1);
                            }
                            Location city = new Location(id, locationAdapter.getLocationName(position),
                                    locationArrayList.get(1).locationId, AddAdvertisementFragment.Config.TYPE_CITY);
                            locationArrayList.add(city);
                            locationSpinner.setSelection(position);
                            SystemHelper.getInstance().closeSpinner(locationSpinner);
                            break;
                    }
                    break;
            }
        }
    }

    private void initLocationLoader(int loaderId, Bundle args) {
        getLoaderManager().initLoader(loaderId, args, this);
    }

    private void restartLocationLoader(int loaderId, Bundle args) {
        if (getLoaderManager().getLoader(loaderId) != null) {
            getLoaderManager().getLoader(loaderId).reset();
        }
        getLoaderManager().restartLoader(loaderId, args, this);
    }
}
