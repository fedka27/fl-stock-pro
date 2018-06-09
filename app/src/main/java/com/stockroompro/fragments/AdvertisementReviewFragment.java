package com.stockroompro.fragments;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.tool.core.Notification;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.stockroompro.R;
import com.stockroompro.api.models.requests.AddAdvertisementPhotos;
import com.stockroompro.api.models.requests.AddAdvertisementRequest;
import com.stockroompro.fragments.AddAdvertisementFragment.Config;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.AdvertPhoto;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.NewAdvert;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.Settings;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit.mime.TypedFile;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_BARGAIN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CITY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_CURRENCY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_DESCRIPTION;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_OPTIONS;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_PRICE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_PRICE_TYPE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_REGION_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_TITLE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_TYPE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.ADVERT_USED;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CATEGORY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.CITY_NAME;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DELETED_PHOTOS;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.FILTERS_NAMES;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.PATH;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.fragments.AddAdvertisementFragment.Config.PHOTOS;
import static com.stockroompro.fragments.AdvertDetailFragment.AdvertisementParameters.BARGAIN_TRUE;
import static com.stockroompro.fragments.AdvertDetailFragment.AdvertisementParameters.USED_TRUE;

/**
 * Created by bagach.alexandr on 06.05.15.
 */
public class AdvertisementReviewFragment extends AdvertDetailFragment {

    protected FrameLayout progressBarContainer;
    private Button addAdvert;
    private final SimpleDateFormat dateFormat;
    protected HashMap<String, TypedFile> photos;

    {
        dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
    }

    public static AdvertisementReviewFragment newInstance(Bundle bundle) {
        Log.d(AdvertisementReviewFragment.class.getSimpleName(), bundle.toString());
        AdvertisementReviewFragment fragment = new AdvertisementReviewFragment();
        Bundle args = new Bundle();
        args.putLong(ADVERT_TYPE, bundle.getLong(ADVERT_TYPE));
        args.putString(ADVERT_TITLE, bundle.getString(ADVERT_TITLE));
        args.putString(ADVERT_DESCRIPTION, bundle.getString(ADVERT_DESCRIPTION));
        args.putString(TOKEN, bundle.getString(TOKEN));
        args.putInt(ADVERT_PRICE_TYPE, bundle.getInt(ADVERT_PRICE_TYPE));
        args.putLong(CATEGORY_ID, bundle.getLong(CATEGORY_ID));
        args.putLong(ADVERT_PRICE, bundle.getLong(ADVERT_PRICE));
        args.putLong(ADVERT_CURRENCY_ID, bundle.getLong(ADVERT_CURRENCY_ID));
        args.putLong(ADVERT_COUNTRY_ID, bundle.getLong(ADVERT_COUNTRY_ID));
        args.putLong(ADVERT_REGION_ID, bundle.getLong(ADVERT_REGION_ID));
        args.putLong(ADVERT_CITY_ID, bundle.getLong(ADVERT_CITY_ID));
        args.putInt(ADVERT_BARGAIN, bundle.getInt(ADVERT_BARGAIN));
        args.putInt(ADVERT_USED, bundle.getInt(ADVERT_USED));
        args.putLong(DATE, bundle.getLong(DATE));
        args.putStringArrayList(PATH, bundle.getStringArrayList(PATH));
        args.putStringArrayList(FILTERS_NAMES, bundle.getStringArrayList(FILTERS_NAMES));
        args.putIntegerArrayList(ADVERT_OPTIONS, bundle.getIntegerArrayList(ADVERT_OPTIONS));
        args.putStringArrayList(PHOTOS, bundle.getStringArrayList(PHOTOS));
        args.putString(CITY_NAME, bundle.getString(CITY_NAME));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        addAdvert = (ArtJokerButton) view.findViewById(R.id.button_add_advert);
        progressBarContainer = (FrameLayout) view.findViewById(R.id.add_advert_progress_bar_container);
    }

    @Override
    protected void initListeners(View view) {
        expandableDescriptionText.setOnClickListener(this);
        expandableDescriptionBadge.setOnClickListener(this);
        addAdvert.setOnClickListener(this);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_advert_review;
    }

    @Override
    protected void initContent() {
        Log.d(AdvertisementReviewFragment.class.getSimpleName(), getArguments().toString());

        type.setText(getArguments().getLong(ADVERT_TYPE) == Advertisement.ADVERT_TYPE_BUY ?
                getString(R.string.buy_title) : getString(R.string.sell_title));

        type.setTextColor(getArguments().getLong(ADVERT_TYPE) == Advertisement.ADVERT_TYPE_BUY ?
                getResources().getColor(R.color.red_text_color) : getResources().getColor(R.color.green_header));

        categoryName.setText(initPath(getArguments().getStringArrayList(PATH)));

        initFilters(getArguments().getStringArrayList(FILTERS_NAMES));
        title.setText(getArguments().getString(ADVERT_TITLE));

        if (getArguments().getInt(ADVERT_PRICE_TYPE) != Advertisement.PRICE_TYPE_FREE) {
            if (getArguments().getLong(ADVERT_PRICE) > 0) {
                price.setText(String.format(AdvertisementParameters.PRICE_FORMAT,
                        getArguments().getLong(ADVERT_PRICE),
                        Settings.getInstance(getActivity()).getCurrencyById((int)getArguments().getLong(ADVERT_CURRENCY_ID)).getName()));
            } else {
                price.setText(getResources().getString(R.string.advert_price_type_free));
            }
        } else {
            price.setText(getResources().getString(R.string.advert_price_type_free));
        }

        isAuction.setText((getArguments().getInt(ADVERT_BARGAIN) == BARGAIN_TRUE) ?
                getString(R.string.full_advert_bagrain_true) : getString(R.string.full_advert_bagrain_false));
        isNew.setText(getArguments().getInt(ADVERT_USED) == USED_TRUE ?
                getString(R.string.full_advert_is_used_true) : getString(R.string.full_advert_is_used_false));
        location.setText(getArguments().getString(CITY_NAME));
        exchangeContainer.setVisibility((getArguments().getInt(ADVERT_PRICE_TYPE) == Advertisement.PRICE_TYPE_EXCHANGE) ? View.VISIBLE : View.GONE);
        date.setText(dateFormat.format(new Date(System.currentTimeMillis())));
        String txtDescription = getArguments().getString(ADVERT_DESCRIPTION);
        setDescription(txtDescription);
        setPhotos(getArguments().getStringArrayList(PHOTOS));
        setButtonsState(false);
        initPersonalData();
    }

    private String initPath(ArrayList<String> stringArrayList) {
        StringBuilder builder = new StringBuilder();
        for (String string : stringArrayList) {
            builder.append(string);
            builder.append(" / ");
        }
        return builder.toString();
    }


    private void setPhotos(ArrayList<String> list) {
        ArrayList<String> photos = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (String uri : list) {
                photos.add(String.format(AdvertisementParameters.ADVERT_LOCAL_FILES_FORMAT, uri));
            }
            setImageContainerVisibility(View.GONE, View.VISIBLE);
            advertPhoto.setPhotos(photos, 0);
        } else {
            setImageContainerVisibility(View.VISIBLE, View.GONE);
        }
    }

    public void setButtonsState(boolean state) {
        btnCallToSeller.setEnabled(state);
        btnAddToFavourite.setEnabled(state);
        btnComments.setEnabled(state);
        btnWriteToSeller.setEnabled(state);
    }

    private void initPersonalData() {
        userName.setText(String.format(AdvertisementParameters.USER_NAME_FORMAT,
                PersonalData.getInstance(getActivity().getApplicationContext()).getUserFirstName(),
                PersonalData.getInstance(getActivity().getApplicationContext()).getUserLastName()));
        userRating.setRating(PersonalData.getInstance(getActivity().getApplicationContext()).getUserRating());
        userPhoto.setImageURI(PersonalData.getInstance(getActivity().getApplicationContext()).getUserPicture());
    }

    @Override
    protected void initAdapter(AbsListView absListView) {
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.NETWORK_ADD_ADVERTISEMENT_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new AddAdvertisementRequest(getActivity(), args).setUiCallback(this));

            case LoadersId.ADD_ADVERT_PHOTOS_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new AddAdvertisementPhotos(getActivity(), args, photos).setUiCallback(this));

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    @Override
    public void uiDataResponse(ResponseHolder data) {
        if (data.getData() instanceof NewAdvert) {
            if (getArguments().getStringArrayList(Config.PHOTOS).size() > 0) {
                sendPhotos(((NewAdvert) data.getData()).getId());
            } else {
                hideAddAdvertProgressBar();
                commit(FragmentAdvertAdded.newInstance(FragmentAdvertAdded.AdvertAction.ADVERT_CREATED), FragmentAdvertAdded.class.getCanonicalName(), null);
            }
        } else if (data.getData() instanceof AdvertPhoto) {
            hideAddAdvertProgressBar();
            commit(FragmentAdvertAdded.newInstance(FragmentAdvertAdded.AdvertAction.ADVERT_CREATED),
                    FragmentAdvertAdded.class.getCanonicalName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
        } else {
            hideAddAdvertProgressBar();
            Notification.getInstance().show(getActivity(), getResources().getString(R.string.add_advert_fail));
        }
    }

    public void sendPhotos(long adId) {
        if (getArguments().getStringArrayList(Config.PHOTOS) != null && getArguments().getStringArrayList(Config.PHOTOS).size() > 0) {
            photos = new HashMap<>();
            for (int i = 0; i < getArguments().getStringArrayList(Config.PHOTOS).size(); i++) {
                File file = new File(getArguments().getStringArrayList(AddAdvertisementFragment.Config.PHOTOS).get(i));
                photos.put(String.format(Config.PHOTO_PARAMETER, i), new TypedFile(Config.JPEG_MIME_TYPE, file));
            }
            Bundle args = new Bundle();
            args.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
            args.putStringArrayList(DELETED_PHOTOS, new ArrayList<String>());
            args.putLong(AD_ID, adId);
            getLoaderManager().restartLoader(LoadersId.ADD_ADVERT_PHOTOS_LOADER_ID, args, this).forceLoad();
        }
    }

    public void hideAddAdvertProgressBar() {
        progressBarContainer.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.button_add_advert:
//                listener.addAdvert(getArguments());
                progressBarContainer.setVisibility(View.VISIBLE);
                progressBarContainer.setOnClickListener(null);
                if (getLoaderManager().getLoader(LoadersId.NETWORK_ADD_ADVERTISEMENT_LOADER_ID) != null) {
                    getLoaderManager().getLoader(LoadersId.NETWORK_ADD_ADVERTISEMENT_LOADER_ID).reset();
                }
                getLoaderManager().initLoader(LoadersId.NETWORK_ADD_ADVERTISEMENT_LOADER_ID, getArguments(), this).forceLoad();
                break;
        }
    }
}
