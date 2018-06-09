package com.stockroompro.fragments;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
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
import com.artjoker.tool.core.SystemHelper;
import com.stockroompro.Launcher;
import com.stockroompro.R;
import com.stockroompro.adapters.LocationSpinnerAdapter;
import com.stockroompro.api.models.requests.CityRequest;
import com.stockroompro.api.models.requests.CountryRequest;
import com.stockroompro.api.models.requests.RegionRequest;
import com.stockroompro.api.models.requests.RegistrationRequest;
import com.stockroompro.fragments.base.BaseApplicationFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalBroadcastRequestProcessor;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.contracts.CityContract;
import com.stockroompro.models.contracts.CountryContract;
import com.stockroompro.models.contracts.RegionContract;
import com.stockroompro.models.spinners.Location;
import com.stockroompro.models.spinners.SpinnerItem;

import java.util.ArrayList;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CITY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.REGION_ID;

public class RegistrationFragment extends BaseApplicationFragment
        implements CompoundButton.OnCheckedChangeListener, BaseRequest.UIResponseCallback,
        View.OnClickListener, LoaderManager.LoaderCallbacks, LocationSpinnerAdapter.OnLocationSpinnerItemClickListener {

    public static final int MAX_PHONES_COUNT = 3;
    public static final int MIN_PHONE_COUNT = 1;
    private ArtJokerEditText etPassword,
            etRepeatPassword,
            etEmail,
            etFirstName;
    private ArtJokerButton buttonRegistration;
    private LinearLayout phonesLayout;
    private ArrayList<ArtJokerEditText> phonesViews = new ArrayList<>(MIN_PHONE_COUNT);

    private Spinner locationSpinner;
    protected boolean isLocationItemsLoading = false;
    protected LocationSpinnerAdapter locationAdapter;
    protected ArrayList<Location> locationArrayList = new ArrayList<>(3);
    protected int locationType = AddAdvertisementFragment.Config.TYPE_COUNTRY;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_registration;
    }

    @Override
    protected void initViews(View view) {
        etEmail = (ArtJokerEditText) view.findViewById(R.id.et_reg_email);
        etEmail.setValidateEmail(true);
        etPassword = (ArtJokerEditText) view.findViewById(R.id.et_reg_password);
        etRepeatPassword = (ArtJokerEditText) view.findViewById(R.id.et_reg_repeat_password);
        etFirstName = (ArtJokerEditText) view.findViewById(R.id.et_reg_login);
        locationSpinner = (Spinner) view.findViewById(R.id.fragment_registration_location_spinner);
        buttonRegistration = (ArtJokerButton) view.findViewById(R.id.button_registrtion);
        phonesLayout = (LinearLayout) view.findViewById(R.id.phonesLayout);
        initPhones(view);
    }

    private void initPhones(View view) {
        addPhoneField(false).setOnCheckedChangeListener(this);
    }

    @Override
    protected void initListeners(View view) {
        buttonRegistration.setOnClickListener(this);
    }

    @Override
    protected void initAdapters() {
        locationAdapter = new LocationSpinnerAdapter(getActivity().getApplicationContext(),
                R.layout.advertisement_spinner_item, new SpinnerItem[]{}, locationArrayList, this);
    }

    @Override
    protected void initContent() {
        getLoaderManager().initLoader(LoadersId.NETWORK_COUNTRY_LOADER_ID, null, this);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.NETWORK_REGISTRATION_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(),
                        new RegistrationRequest(getActivity(), args).setUiCallback(this));

            case LoadersId.NETWORK_COUNTRY_LOADER_ID:
                isLocationItemsLoading = true;
                return new RequestDescriptorWithLoadFromDB(getActivity(), CountryContract.CONTENT_URI,
                        null, null, null, null, new CountryRequest(getActivity(), args),
                        new LocalBroadcastRequestProcessor(getActivity()));

            case LoadersId.NETWORK_REGION_LOADER_ID:
                isLocationItemsLoading = true;
                return new RequestDescriptorWithLoadFromDB(getActivity(), RegionContract.CONTENT_URI,
                        null, AddAdvertisementFragment.Config.REGION_SELECTION, buildSelectionArgs(args.getLong(COUNTRY_ID)),
                        null, new RegionRequest(getActivity(), args), new LocalBroadcastRequestProcessor(getActivity()));

            case LoadersId.NETWORK_CITY_LOADER_ID:
                isLocationItemsLoading = true;
                return new RequestDescriptorWithLoadFromDB(getActivity(), CityContract.CONTENT_URI, null,
                        AddAdvertisementFragment.Config.CITY_SELECTION, buildSelectionArgs(args.getLong(REGION_ID)), null,
                        new CityRequest(getActivity(), args), new LocalBroadcastRequestProcessor(getActivity()));
        }
        return null;
    }


    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
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
            case R.id.button_registrtion:
                registration();
                break;
        }
    }

    private void registration() {
        if (isValidFields()) {
            Bundle registrationBundle = new BundleBuilder()
                    .putString(ParamNames.PASSWORD, etPassword.getText().toString())
                    .putString(ParamNames.EMAIL, etEmail.getText().toString())
                    .putString(ParamNames.FIRST_NAME, etFirstName.getText().toString())
                    .putString(ParamNames.LAST_NAME, "")
                    .build();
            registrationBundle.putStringArrayList(ParamNames.PHONE_NUMBER, getAllPhones());

            switch (locationArrayList.size()) {
                case 1:
                    registrationBundle.putLong(COUNTRY_ID, locationArrayList.get(0).locationId);
                    break;

                case 2:
                    registrationBundle.putLong(COUNTRY_ID, locationArrayList.get(0).locationId);
                    registrationBundle.putLong(REGION_ID, locationArrayList.get(1).locationId);
                    break;

                case 3:
                    registrationBundle.putLong(COUNTRY_ID, locationArrayList.get(0).locationId);
                    registrationBundle.putLong(REGION_ID, locationArrayList.get(1).locationId);
                    registrationBundle.putLong(ADVERT_CITY_ID, locationArrayList.get(2).locationId);
                    break;
            }

            if (getLoaderManager().getLoader(LoadersId.NETWORK_REGISTRATION_LOADER_ID) != null)
                getLoaderManager().restartLoader(LoadersId.NETWORK_REGISTRATION_LOADER_ID, registrationBundle, this).forceLoad();
            else
                getLoaderManager().initLoader(LoadersId.NETWORK_REGISTRATION_LOADER_ID, registrationBundle, this).forceLoad();

        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.rate_dialog_error_title)
                    .setMessage(R.string.registration_fail_text)
                    .create()
                    .show();
        }

    }

    private boolean isValidFields() {
        boolean b = etPassword.isValid()
                && etEmail.isValid()
                && etFirstName.isValid()
                && !etFirstName.getText().toString().isEmpty()
                && (etPassword.getText().toString().equals(etRepeatPassword.getText().toString()));

        boolean isPhoneValid = true;
        for (String phone : getAllPhones()) {
            if (!"".equals(phone) && phone.length() < 10) {
                isPhoneValid = false;
            }
        }

        return b && isPhoneValid;
    }

    private ArrayList<String> getAllPhones() {
        ArrayList<String> allPhones = new ArrayList<String>(MAX_PHONES_COUNT);
        for (int i = 0; i < phonesViews.size(); i++) {
            allPhones.add(phonesViews.get(i).getText().toString());
        }

        return allPhones;

    }

    @Override
    public void uiDataResponse(ResponseHolder data) {
        if (data.getStatusCode() == ResponseHolder.StatusCode.SUCCESSFULL) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.warning)
                    .setMessage(R.string.user_not_active)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getActivity().getFragmentManager().popBackStack();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.rate_dialog_error_title)
                    .setMessage(R.string.reg_fail)
                    .create()
                    .show();
        }
    }

    private ToggleButton addPhoneField(boolean isChecked) {
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.add_phone_layout, null);
        phonesLayout.addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ArtJokerEditText phoneField = (ArtJokerEditText) v.findViewById(R.id.et_reg_phone_1);
        phoneField.setValidatePhone(true);
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
                if (phonesLayout.getChildCount() == MIN_PHONE_COUNT) {
                    ToggleButton button = (ToggleButton) phonesLayout.findViewById(R.id.button_likes);
                    button.setOnCheckedChangeListener(null);
                    button.setChecked(false);
                    button.setOnCheckedChangeListener(this);

                }
            }
        }
    }

    @Override
    public void onLocationItemClick(int position, long id) {
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
