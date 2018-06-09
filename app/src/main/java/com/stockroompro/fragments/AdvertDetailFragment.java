package com.stockroompro.fragments;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.RequestDescriptorLoader;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.core.views.ExpandableHeightGridView;
import com.artjoker.tool.core.Network;
import com.artjoker.tool.core.Notification;
import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stockroompro.R;
import com.stockroompro.adapters.AdvertDetailsFiltersAdapter;
import com.stockroompro.api.models.requests.AddOrRemoveFavouritesRequest;
import com.stockroompro.api.models.requests.AdvertExtendRequest;
import com.stockroompro.api.models.requests.AdvertisementDetailsRequest;
import com.stockroompro.api.models.requests.DeleteAdvertRequest;
import com.stockroompro.api.models.requests.UserDataRequest;
import com.stockroompro.fragments.base.BaseApplicationListFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalBroadcastRequestProcessor;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.Settings;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.contracts.MessageByAdvertContract;
import com.stockroompro.models.contracts.PhotosContract;
import com.stockroompro.models.contracts.UserDataContract;
import com.stockroompro.models.converters.AdvertisementContentValuesConverter;
import com.stockroompro.models.converters.AdvertisementCursorConverter;
import com.stockroompro.models.converters.UserDataCursorConverter;
import com.stockroompro.views.AdvertPhotoGroup;
import com.stockroompro.views.AdvertPhotoSwitcher.OnImageClick;
import com.stockroompro.views.ExpandableTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ACTION;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.USER_ID;

/**
 * Created by bagach.alexandr on 08.04.15.
 */
public class AdvertDetailFragment extends BaseApplicationListFragment implements View.OnClickListener,
        BaseRequest.UIResponseCallback, OnImageClick {

    private static final String ADVERTISEMENT_ID = "advert_id";

    protected LinearLayout mainContainer;
    protected LinearLayout priceContainer;
    protected LinearLayout locationContainer;
    protected LinearLayout exchangeContainer;
    protected AdvertPhotoGroup advertPhoto;
    protected ImageView emptyPhotoImageView;
    protected ArtJokerTextView title;
    protected ArtJokerTextView price;
    protected ArtJokerTextView auction;
    protected ArtJokerTextView isAuction;
    protected ArtJokerTextView isNew;
    protected ArtJokerTextView location;
    protected ArtJokerTextView date;
    protected ArtJokerTextView type;
    protected ArtJokerTextView categoryName;

    protected LinearLayout descriptionContainer;
    protected ExpandableTextView expandableDescriptionText;
    protected FrameLayout expandableDescriptionBadgeContainer;
    protected View expandableDescriptionBadge;

    protected ArtJokerButton btnComments;
    protected ImageButton btnCallToSeller;
    protected ImageButton btnWriteToSeller;
    protected ImageButton btnAddToFavourite;
    private ImageButton btnEditAdvert;
    private ImageButton btnDeleteAdvert;

    protected SimpleDraweeView userPhoto;
    protected ArtJokerTextView userName;
    protected ArtJokerTextView userPhone;
    protected RatingBar userRating;
    protected ImageButton btnSellerProfile;
    protected LinearLayout changeAdvert;
    protected LinearLayout deleteAdvert;
    protected LinearLayout callToSeller;
    protected LinearLayout writeToSeller;
    protected RelativeLayout goToSeller;

    protected ToggleButton advertActive;
    protected RelativeLayout extensionContainer;
    protected ArtJokerTextView extensionPeriodText;
    protected ArtJokerTextView extensionFinishedText;
    protected ArtJokerButton extendAdvert;

    private int advertState;
    public long authorId;
    public long advertisementId;
    public String fullDescription;
    public String shortDescription;
    public boolean isFavourite = false;
    public boolean isDescriptionExpanded = false;
    private boolean isAdvertDeleting = false;
    private ArrayList<String> advertPhonesList = new ArrayList<>();
    private final SimpleDateFormat dateFormat;
    private ArrayList<String> photosList = new ArrayList<>();

    private GridView filtersGridView;

    {
        dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
    }

    public static AdvertDetailFragment newInstance(long advertId, long authorId) {
        AdvertDetailFragment fragment = new AdvertDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ADVERTISEMENT_ID, advertId);
        args.putLong(USER_ID, authorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_one_advert_full;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        advertisementId = getArguments().getLong(ADVERTISEMENT_ID);
        authorId = getArguments().getLong(USER_ID);
    }

    @Override
    protected void initViews(View view) {
        mainContainer = (LinearLayout) view.findViewById(R.id.fragment_one_advert_full_ver2_main_container);

        advertPhoto = (AdvertPhotoGroup) view.findViewById(R.id.photo_switcher_advert_photos);
        emptyPhotoImageView = (ImageView) view.findViewById(R.id.iv_empty_photo);

        type = (ArtJokerTextView) view.findViewById(R.id.tv_one_advert_type);
        categoryName = (ArtJokerTextView) view.findViewById(R.id.tv_one_advert_category_name);
        title = (ArtJokerTextView) view.findViewById(R.id.tv_one_advert_title);

        priceContainer = (LinearLayout) view.findViewById(R.id.rl_with_price);
        price = (ArtJokerTextView) view.findViewById(R.id.tv_full_advert_price);
        auction = (ArtJokerTextView) view.findViewById(R.id.tv_full_advert_auction);
        isAuction = (ArtJokerTextView) view.findViewById(R.id.tv_full_advert_is_auction);
        isNew = (ArtJokerTextView) view.findViewById(R.id.tv_full_advert_is_new);
        exchangeContainer = (LinearLayout) view.findViewById(R.id.ll_full_advert_exchange_container);

        locationContainer = (LinearLayout) view.findViewById(R.id.rl_location);
        location = (ArtJokerTextView) view.findViewById(R.id.tv_full_advert_location);
        date = (ArtJokerTextView) view.findViewById(R.id.tv_full_advert_date);

        descriptionContainer = (LinearLayout) view.findViewById(R.id.rl_full_advert_container_description);
        expandableDescriptionBadge = view.findViewById(R.id.v_full_advert_description_badge);
        expandableDescriptionText = (ExpandableTextView) view.findViewById(R.id.etv_full_advert_description);
        expandableDescriptionBadgeContainer = (FrameLayout) view.findViewById(R.id.fl_full_advert_description_expander);

        btnComments = (ArtJokerButton) view.findViewById(R.id.button_full_advert_to_comments);
        btnCallToSeller = (ImageButton) view.findViewById(R.id.button_full_advert_call_to_seller);
        btnWriteToSeller = (ImageButton) view.findViewById(R.id.button_full_advert_write_to_seller);
        btnAddToFavourite = (ImageButton) view.findViewById(R.id.button_full_advert_favourites);

        userPhoto = (SimpleDraweeView) view.findViewById(R.id.iv_full_advert_seller_image);
        userName = (ArtJokerTextView) view.findViewById(R.id.tv_full_advert_seller_name);
        userPhone = (ArtJokerTextView) view.findViewById(R.id.tv_profile_seller_phones);
        userRating = (RatingBar) view.findViewById(R.id.rating_bar_full_advert_seller);

        btnEditAdvert = (ImageButton) view.findViewById(R.id.button_full_advert_edit);
        btnDeleteAdvert = (ImageButton) view.findViewById(R.id.button_full_advert_delete);
        btnSellerProfile = (ImageButton) view.findViewById(R.id.ib_full_advert_to_seller);

        changeAdvert = (LinearLayout) view.findViewById(R.id.ll_full_advert_edit);
        deleteAdvert = (LinearLayout) view.findViewById(R.id.ll_full_advert_delete);
        callToSeller = (LinearLayout) view.findViewById(R.id.ll_full_advert_call_to_seller);
        writeToSeller = (LinearLayout) view.findViewById(R.id.ll_full_advert_write_to_seller);

        goToSeller = (RelativeLayout) view.findViewById(R.id.person_details);

        advertActive = (ToggleButton) view.findViewById(R.id.advert_detail_active_toggle);
        extensionContainer = (RelativeLayout) view.findViewById(R.id.my_advert_extension_container);
        extensionPeriodText = (ArtJokerTextView) view.findViewById(R.id.my_advert_extension_date);
        extensionFinishedText = (ArtJokerTextView) view.findViewById(R.id.extension_finished_text);
        extendAdvert = (ArtJokerButton) view.findViewById(R.id.extend_advert_button);
        filtersGridView = (ExpandableHeightGridView) view.findViewById(R.id.fragment_full_advert_filters_grid_view);
    }

    @Override
    protected void initAdapter(AbsListView absListView) {
    }

    @Override
    protected void initListeners(View view) {
        expandableDescriptionText.setOnClickListener(this);
        expandableDescriptionBadge.setOnClickListener(this);
        btnAddToFavourite.setOnClickListener(this);
        btnComments.setOnClickListener(this);
        btnWriteToSeller.setOnClickListener(this);
        btnCallToSeller.setOnClickListener(this);
        btnSellerProfile.setOnClickListener(this);
        btnEditAdvert.setOnClickListener(this);
        btnDeleteAdvert.setOnClickListener(this);
        goToSeller.setOnClickListener(this);
        extendAdvert.setOnClickListener(this);
        advertPhoto.setCallback(this);
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        mainContainer.setVisibility(View.INVISIBLE);
        manager.initLoader(LoadersId.DB_PHOTO_LOADER_ID, commonBundleBuilder.putLong(ADVERTISEMENT_ID, advertisementId).build(), this);
        if (Network.getInstance().isConnected(getActivity())) {

            if (PersonalData.getInstance(getActivity()).getToken() != null) {
                Bundle args = new Bundle();
                args.putLong(AD_ID, advertisementId);
                args.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
                if (authorId == PersonalData.getInstance(getActivity()).getUserId()) {
                    setAdvertState(Config.OWN_ADVERT);
                } else {
                    setAdvertState(Config.SELLER_ADVERT);
                }
                getLoaderManager().restartLoader(LoadersId.NETWORK_ADVERTISEMENT_DETAILS_LOADER_ID, args, this).forceLoad();
            } else {
                setAdvertState(Config.SELLER_ADVERT);
                manager.restartLoader(LoadersId.NETWORK_ADVERTISEMENT_DETAILS_LOADER_ID,
                        commonBundleBuilder.putLong(AD_ID, advertisementId).build(), this).forceLoad();
            }
        } else {
            manager.restartLoader(LoadersId.DB_ADVERTISEMENT_DETAILS_LOADER_ID,
                    commonBundleBuilder.putLong(ADVERTISEMENT_ID, advertisementId).build(), this);
        }
    }

    @Override
    protected void initContent() {

    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.DB_PHOTO_LOADER_ID:
                return new CursorLoader(getActivity(), PhotosContract.CONTENT_URI, Config.PHOTO_PROJECTION, Config.PHOTO_SELECTION,
                        buildSelectionArgs(args.getLong(ADVERTISEMENT_ID, advertisementId)), null);

            case LoadersId.DB_ADVERTISEMENT_DETAILS_LOADER_ID:
                return new CursorLoader(getActivity(), AdvertisementContract.CONTENT_URI, null, Config.ADVERT_SELECTION,
                        buildSelectionArgs(args.getLong(ADVERTISEMENT_ID, advertisementId)), null);

            case LoadersId.ADD_FAVOURITES_LOADER:
                Bundle bundle = new Bundle();
                bundle.putLong(AD_ID, advertisementId);
                bundle.putLong(UID, PersonalData.getInstance(getActivity()).getUserId());
                bundle.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
                AddOrRemoveFavouritesRequest addToFavouritesRequest = new AddOrRemoveFavouritesRequest(getActivity().getApplicationContext(), bundle);
                return new RequestDescriptorLoader(getActivity().getApplicationContext(), addToFavouritesRequest);

            case LoadersId.NETWORK_ADVERT_USER_DATA_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new UserDataRequest(getActivity(), args));

            case LoadersId.DB_ADVERT_USER_DATA_LOADER_ID:
                return new CursorLoader(getActivity(), UserDataContract.CONTENT_URI, null, Config.USER_DATA_SELECTION,
                        buildSelectionArgs(args.getLong(USER_ID, authorId)), null);

            case LoadersId.NETWORK_DELETE_ADVERT:
                return new LocalRequestDescriptorLoader(getActivity(), new DeleteAdvertRequest(getActivity(), args).setUiCallback(this));

            case LoadersId.EXTEND_ADVERT_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new AdvertExtendRequest(getActivity(), args).setUiCallback(this));

            case LoadersId.NETWORK_ADVERTISEMENT_DETAILS_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new AdvertisementDetailsRequest(getActivity(), AdvertisementContract.CONTENT_URI,
                        AdvertisementContentValuesConverter.class, args).setUiCallback(this));
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LoadersId.DB_PHOTO_LOADER_ID:
                if (data != null) {
                    ArrayList<String> photosList = new ArrayList<>();
                    if (((Cursor) data).moveToFirst()) {
                        do {
                            photosList.add(((Cursor) data).getString(0));
                        } while (((Cursor) data).moveToNext());
                        setImageContainerVisibility(View.GONE, View.VISIBLE);
                        if (this.photosList.size() > photosList.size()) {
                            advertPhoto.setPhotos(this.photosList, 0);
                        } else {
                            this.photosList = photosList;
                            advertPhoto.setPhotos(photosList, 0);
                        }
                    } else {
                        setImageContainerVisibility(View.VISIBLE, View.GONE);
                    }
                } else {
                    setImageContainerVisibility(View.VISIBLE, View.GONE);
                }
                break;

            case LoadersId.DB_ADVERTISEMENT_DETAILS_LOADER_ID:
                if (data != null) {
                    Cursor c = (Cursor) data;
                    if (c.moveToFirst()) {
                        AdvertisementCursorConverter converter = new AdvertisementCursorConverter();
                        converter.setCursor(c);
                        refreshContent(converter.getObject());
                    }
                    Bundle args = new Bundle();
                    args.putLong(USER_ID, authorId);
                    getLoaderManager().restartLoader(LoadersId.DB_ADVERT_USER_DATA_LOADER_ID, args, this);
                } else {
                    popBack();
                }
                break;

            case LoadersId.NETWORK_ADVERT_USER_DATA_LOADER_ID:
                Bundle args = new Bundle();
                args.putLong(USER_ID, authorId);
                getLoaderManager().restartLoader(LoadersId.DB_ADVERT_USER_DATA_LOADER_ID, args, this);
                break;

            case LoadersId.DB_ADVERT_USER_DATA_LOADER_ID:
                if (data != null) {
                    Cursor c = (Cursor) data;
                    if (c.moveToFirst()) {
                        UserDataCursorConverter converter = new UserDataCursorConverter();
                        converter.setCursor((Cursor) data);
                        setUserInfo(converter);
                    }
                }
                break;

            case LoadersId.ADD_FAVOURITES_LOADER:
                if ((boolean) data) {
                    btnAddToFavourite.setImageDrawable(isFavourite ?
                            getResources().getDrawable(R.drawable.add_to_favourites_button_selector) :
                            getResources().getDrawable(R.drawable.remove_from_favourites_button_selector));
                    isFavourite = !isFavourite;
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    private void refreshContent(Advertisement converter) {
        if (converter != null) {
            title.setText(converter.getTitle());

            float priceValue = converter.getPrice();
            authorId = converter.getUserId();
            setAdvertState((PersonalData.getInstance(getActivity()).getToken() != null &&
                    PersonalData.getInstance(getActivity()).getUserId() == authorId) ?
                    Config.OWN_ADVERT : Config.SELLER_ADVERT);
            type.setText(converter.getType() == Advertisement.ADVERT_TYPE_BUY ?
                    getString(R.string.buy_title) : getString(R.string.sell_title));
            type.setTextColor(converter.getType() == Advertisement.ADVERT_TYPE_BUY ?
                    getResources().getColor(R.color.red_text_color) : getResources().getColor(R.color.green_header));
            categoryName.setText(converter.getPath());
            if (priceValue > 0) {
                switch (converter.getPriceType()) {
                    case Advertisement.PRICE_TYPE_SELL:
                        price.setText(String.format(AdvertisementParameters.PRICE_FORMAT,
                                com.artjoker.tool.core.TextUtils.getInstance().getStringFromDoubleValue(priceValue),
                                Settings.getInstance(getActivity()).getCurrencyById(converter.getCurrencyId()).getName()));
                        isAuction.setText((converter.getBargain() == AdvertisementParameters.BARGAIN_TRUE) ?
                                getString(R.string.full_advert_bagrain_true) : getString(R.string.full_advert_bagrain_false));
                        break;

                    case Advertisement.PRICE_TYPE_EXCHANGE:
                        price.setText(String.format(AdvertisementParameters.PRICE_FORMAT,
                                com.artjoker.tool.core.TextUtils.getInstance().getStringFromDoubleValue(priceValue),
                                Settings.getInstance(getActivity()).getCurrencyById(converter.getCurrencyId()).getName()));
                        isAuction.setText((converter.getBargain() == AdvertisementParameters.BARGAIN_TRUE) ?
                                getString(R.string.full_advert_bagrain_true) : getString(R.string.full_advert_bagrain_false));
                        exchangeContainer.setVisibility(View.VISIBLE);
                        break;

                    case Advertisement.PRICE_TYPE_FREE:
                        price.setText(getString(R.string.advert_price_type_free));
                        isAuction.setText(getString(R.string.full_advert_bagrain_false));
                        break;
                }
            } else {
                price.setText(getString(R.string.advert_price_type_free));
                isAuction.setText(getString(R.string.full_advert_bagrain_false));
            }
            isNew.setText((converter.getUsed() == AdvertisementContract.TYPE_NEW) ? getString(R.string.full_advert_is_used_false) : getString(R.string.full_advert_is_used_true));

            location.setText(String.format(AdvertisementParameters.LOCATION_FORMAT,
                    converter.getCountryName(),
                    converter.getCityName()));
            date.setText(dateFormat.format(new Date(SystemHelper.getInstance().getTimeInMillisFromSec(converter.getCreatedAt()))));
            setDescription(converter.getDescription());

            initFilters(converter.getFiltersNames());

            advertPhonesList = converter.getPhones();
            btnCallToSeller.setEnabled(converter.getPhones() != null && converter.getPhones().size() > 0);

            isFavourite = converter.isFavourite() == 1;
            btnAddToFavourite.setImageDrawable(!isFavourite ?
                    getResources().getDrawable(R.drawable.add_to_favourites_button_selector) :
                    getResources().getDrawable(R.drawable.remove_from_favourites_button_selector));

            switch (getAdvertState()) {
                case Config.OWN_ADVERT:
                    setPersonalDataContainerVisibility(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.VISIBLE, View.GONE);
                    advertActive.setVisibility(View.VISIBLE);
                    extensionPeriodText.setText(String.format(getResources().getString(R.string.my_advert_extension_date_text),
                            dateFormat.format(new Date(converter.getRenewalDate() != 0 ?
                                    SystemHelper.getInstance().getTimeInMillisFromSec(converter.getRenewalDate())
                                    : SystemHelper.getInstance().getTimeInMillisFromSec(converter.getCreatedAt())))));
                    if (converter.getApproved() == Advertisement.ADVERT_UNAPPROVED) {
                        advertActive.setTextOff(getResources().getString(R.string.advert_unapproved));
                        advertActive.setChecked(false);
                        extensionFinishedText.setText(getResources().getString(R.string.my_advert_is_unapproved));
                        extendAdvert.setEnabled(false);
                    } else {
                        if (converter.getExpired() == 1) {
                            extensionFinishedText.setText(getResources().getString(R.string.advert_expiry_date_over));
                        } else {
                            advertActive.setChecked(converter.getActive() == Advertisement.ADVERT_ACTIVE);
                            long expiryDate = SystemHelper.getInstance().getTimeInMillisFromSec(converter.getExpiryDate()) - System.currentTimeMillis();
                            extensionFinishedText.setText(String.format(getResources().getString(R.string.my_advert_extension_finished_text),
                                    expiryDate / DateUtils.DAY_IN_MILLIS));
                            extendAdvert.setEnabled(true);
                        }
                    }
                    break;

                case Config.SELLER_ADVERT:
                    setPersonalDataContainerVisibility(View.GONE, View.GONE, View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
                    break;
            }
        }

        mainContainer.setVisibility(View.VISIBLE);
    }

    public void initFilters(ArrayList<String> filtersNames) {
        if (filtersNames != null && filtersNames.size() > 0) {
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_full_advert_filter_item, filtersNames);
            AdvertDetailsFiltersAdapter adapter = new AdvertDetailsFiltersAdapter(getActivity(), R.layout.fragment_full_advert_filter_item, filtersNames);
            filtersGridView.setAdapter(adapter);
        }
        advertPhoto.requestFocus();
        advertPhoto.setFocusableInTouchMode(true);
    }

    private void setPhones() {
        if (advertPhonesList.size() > 0) {
            btnCallToSeller.setEnabled(true);
            if (advertPhonesList.size() == 1) {
                callToSeller(advertPhonesList.get(0));
            } else {
                final CharSequence[] phonesList = new CharSequence[advertPhonesList.size()];
                int i = 0;
                for (String phone : advertPhonesList) {
                    phonesList[i] = phone;
                    i++;
                }

                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.add_advert_select_phone_title))
                        .setItems(phonesList, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callToSeller(phonesList[which].toString());
                                dialog.dismiss();
                            }
                        }).show();
            }
        } else {
            btnCallToSeller.setEnabled(false);
        }
    }

    private void setUserInfo(UserDataCursorConverter converter) {
        if (converter != null) {
            userName.setText(String.format(AdvertisementParameters.USER_NAME_FORMAT,
                    converter.getObject().getFirstName(),
                    converter.getObject().getLastName()));
            userPhone.setText(converter.getObject().getPhones().get(0));
            userRating.setRating(converter.getObject().getRating());
            userPhoto.setImageURI(converter.getObject().getPictureUrl());
        }
    }

    public void setDescription(String description) {
        if (description != null) {
            if (description.length() > AdvertisementParameters.SHORT_DESCRIPTION_LENGTH) {
                shortDescription = description.substring(0, AdvertisementParameters.SHORT_DESCRIPTION_LENGTH);
                fullDescription = description;
                expandableDescriptionText.setText(shortDescription);
            } else {
                fullDescription = description;
                expandableDescriptionText.setText(fullDescription);
                expandableDescriptionBadgeContainer.setVisibility(View.GONE);
            }
        }
    }

    public String[] buildSelectionArgs(long id) {
        return new String[]{
                String.valueOf(id),
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_full_advert_description_badge:
            case R.id.etv_full_advert_description:
                if (!android.text.TextUtils.isEmpty(shortDescription)) {
                    String newDescription = isDescriptionExpanded ? shortDescription : fullDescription;
                    if (!android.text.TextUtils.isEmpty(newDescription)) {
                        if (expandableDescriptionText.setTextWithAnimation(newDescription)) {
                            isDescriptionExpanded = !isDescriptionExpanded;
                            expandableDescriptionBadge.getBackground().setLevel(isDescriptionExpanded ? 1 : 0);
                        }
                    }
                }
                break;

            case R.id.button_full_advert_call_to_seller:
                setPhones();
                break;

            case R.id.button_full_advert_favourites:
                if (PersonalData.getInstance(getActivity()).getToken() == null) {
                    commit(new AuthorizationFragment(), AuthorizationFragment.class.getCanonicalName(),
                            AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                } else {
                    getLoaderManager().initLoader(LoadersId.ADD_FAVOURITES_LOADER, null, AdvertDetailFragment.this).forceLoad();
                }
                break;

            case R.id.button_full_advert_to_comments:
                commit(CommentsFragment.newInstance(advertisementId), CommentsFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;

            case R.id.button_full_advert_write_to_seller:
                Bundle bundle = new CommonBundleBuilder()
                        .putLong(MessageByAdvertContract.AD_ID, advertisementId)
                        .putString(MessageByAdvertContract.SENDER_NAME, userName.getText().toString())
                        .putLong(MessageByAdvertContract.RECIPIENT_ID, authorId)
                        .build();
                if (PersonalData.getInstance(getActivity()).getToken() == null) {
                    commit(new AuthorizationFragment(), AuthorizationFragment.class.getCanonicalName(),
                            AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                } else
                    commit(ChatFragment.newInstance(bundle), ChatFragment.class.getCanonicalName(),
                            AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;

            case R.id.ib_full_advert_to_seller:
                commit(SellerProfileFragment.newInstance(authorId), SellerProfileFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;

            case R.id.button_full_advert_edit:
                commit(EditAdvertFragment.newInstance(advertisementId), EditAdvertFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;

            case R.id.button_full_advert_delete:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.warning)
                        .setMessage(R.string.warning_delete_advert)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                deleteAdvert();
                                isAdvertDeleting = true;
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;

            case R.id.person_details:
                if (authorId == PersonalData.getInstance(getActivity()).getUserId()) {
                    commit(new ProfileFragment(), ProfileFragment.class.getCanonicalName(),
                            AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                } else {
                    commit(SellerProfileFragment.newInstance(authorId), SellerProfileFragment.class.getCanonicalName(),
                            AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                }
                break;

            case R.id.extend_advert_button:
                Bundle extendAdvertBundle = new Bundle();
                extendAdvertBundle.putString(ACTION, Config.EXTEND_ACTION);
                extendAdvertBundle.putLong(USER_ID, PersonalData.getInstance(getActivity()).getUserId());
                extendAdvertBundle.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
                extendAdvertBundle.putLong(AD_ID, advertisementId);
                getLoaderManager().initLoader(LoadersId.EXTEND_ADVERT_LOADER_ID, extendAdvertBundle, this).forceLoad();
                break;
        }
    }

    private void callToSeller(String number) {
        if (!TextUtils.isEmpty(number)) {
            if (isTelephonyEnabled()) {
                Uri call = Uri.parse(String.format(Config.PHONE_FORMAT, number));
                Intent intent = new Intent(Intent.ACTION_DIAL, call);
                startActivity(intent);
            } else {
                Notification.getInstance().show(getActivity(), R.string.dialer_not_installed);
            }
        }
    }

    private boolean isTelephonyEnabled() {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    private void deleteAdvert() {
        Bundle bundle = new CommonBundleBuilder()
                .putString(TOKEN, PersonalData.getInstance(getActivity()).getToken())
                .putLong(UID, authorId)
                .putLong(AD_ID, advertisementId)
                .build();
        getLoaderManager().restartLoader(LoadersId.NETWORK_DELETE_ADVERT, bundle, this).forceLoad();
    }

    public void setImageContainerVisibility(int emptyViewVisibility, int imageContainerVisibility) {
        emptyPhotoImageView.setVisibility(emptyViewVisibility);
        advertPhoto.setVisibility(imageContainerVisibility);
    }

    private void setPersonalDataContainerVisibility(int changeAdvertVisibility,
                                                    int deleteAdvertVisibility,
                                                    int callToSellerVisibility,
                                                    int writeToSellerVisibility,
                                                    int extensionContainerVisibility,
                                                    int goToSellerVisibility) {

        changeAdvert.setVisibility(changeAdvertVisibility);
        deleteAdvert.setVisibility(deleteAdvertVisibility);
        callToSeller.setVisibility(callToSellerVisibility);
        writeToSeller.setVisibility(writeToSellerVisibility);
        extensionContainer.setVisibility(extensionContainerVisibility);
        goToSeller.setVisibility(goToSellerVisibility);
    }

    private void setAdvertState(int state) {
        this.advertState = state;
    }

    public int getAdvertState() {
        return advertState;
    }

    @Override
    public void uiDataResponse(ResponseHolder data) {
        if (data.getData() instanceof Advertisement) {
            refreshContent((Advertisement) data.getData());
            Bundle args = new Bundle();
            args.putLong(USER_ID, authorId);
            getLoaderManager().restartLoader(LoadersId.NETWORK_ADVERT_USER_DATA_LOADER_ID, args, this).forceLoad();
        } else if (data.getStatusCode() == 0) {
            if (isAdvertDeleting) {
                Notification.getInstance().show(getActivity().getApplicationContext(), R.string.advert_is_deleted);
            } else {
                Notification.getInstance().show(getActivity().getApplicationContext(), R.string.expiry_done);
            }
            popBack();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_RESPONSE_ADVERT_NO_ACTIVE_OR_DELETED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(advertDeleted, intentFilter);

        advertPhoto.requestFocus();
        advertPhoto.setFocusableInTouchMode(true);
        getParent().setMainTitle(getString(R.string.advert_text_title));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(advertDeleted);
    }

    private BroadcastReceiver advertDeleted = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_RESPONSE_ADVERT_NO_ACTIVE_OR_DELETED)) {
                Notification.getInstance().show(getActivity().getApplicationContext(), R.string.advert_no_availdable);
                getActivity().getContentResolver().delete(AdvertisementContract.CONTENT_URI,
                        AdvertisementContract.ID + " = ?", new String[]{String.valueOf(advertisementId)});
                popBack();
            }
        }
    };

    @Override
    public void onImageClick(ArrayList<String> photos, int position) {
        commit(FragmentShowAdvertPhoto.newInstance(photos, position), FragmentShowAdvertPhoto.class.getCanonicalName(), null);
    }

    public interface AdvertisementParameters {
        int USED_TRUE = 1;
        int BARGAIN_TRUE = 1;
        int SHORT_DESCRIPTION_LENGTH = 100;

        String PRICE_FORMAT = "%s %s";
        String USER_NAME_FORMAT = "%s %s";
        String LOCATION_FORMAT = "%s, \n %s";
        String LOCATION_SINGLE_LINE_FORMAT = "%s, %s";
        String ADVERT_LOCAL_FILES_FORMAT = "file://%s";
    }

    private interface Config {
        String USER_DATA_SELECTION = UserDataContract.ID + "=?";
        String ADVERT_SELECTION = AdvertisementContract.ID + "=?";
        String PHOTO_SELECTION = PhotosContract.ADVERTISEMENT_ID + "=?";
        String[] PHOTO_PROJECTION = new String[]{PhotosContract.PHOTO_URL};

        int OWN_ADVERT = 1;
        int SELLER_ADVERT = 2;

        String PHONE_FORMAT = "tel: %s";
        String EXTEND_ACTION = "prolong";
    }
}
