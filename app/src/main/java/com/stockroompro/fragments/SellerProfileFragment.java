package com.stockroompro.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.artjoker.core.BundleBuilder;
import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stockroompro.R;
import com.stockroompro.adapters.AdvertAdapter;
import com.stockroompro.api.models.requests.AllUserAdsRequest;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.api.models.requests.SellerUserDataRequest;
import com.stockroompro.api.models.requests.SetRatingRequest;
import com.stockroompro.fragments.base.BaseApplicationListFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.columns.AdvertisementColumns;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.converters.AdvertisementCursorConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 22.04.15.
 */
public class SellerProfileFragment extends BaseApplicationListFragment implements AdapterView.OnItemClickListener, View.OnClickListener,
        LoaderManager.LoaderCallbacks, BaseRequest.UIResponseCallback, DialogInterface.OnDismissListener {
    private long userId;

    private ArtJokerTextView regDate;
    private ArtJokerTextView nameTv;
    private ArtJokerTextView phonesTv;
    private ArtJokerTextView emailTv;
    private ArtJokerTextView voicesTv;
    private ArtJokerTextView addressTv;
    private ArtJokerTextView advertsTitle;
    private ArtJokerButton rateButton;
    private RatingBar ratingBar;
    private SimpleDraweeView photoIv;
    private ImageButton callButton;
    private ImageButton messagesButton;
    private ArrayList<String> userPhones = new ArrayList<>();
    private RelativeLayout listAdvertsTitle;
    private AdvertAdapter adapter;
    private Dialog dialog;
    private int rating = 0;
    private com.stockroompro.models.UserData data;

    private final SimpleDateFormat simpleDateFormat;

    {
        simpleDateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
    }

    public static SellerProfileFragment newInstance(long id) {

        SellerProfileFragment fragment = new SellerProfileFragment();
        Bundle args = new Bundle();
        args.putLong(RequestParams.ParamNames.USER_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        userId = getArguments().getLong(RequestParams.ParamNames.USER_ID);
//        adapter = new AdvertAdapter(getActivity(), null, AdvertisementCursorConverter.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_seller;
    }

    @Override
    protected int getAbstractListId() {
        return R.id.lv_seller_asverts;
    }

    @Override
    protected void initListeners(View view) {
        callButton.setOnClickListener(this);
        messagesButton.setOnClickListener(this);
        rateButton.setOnClickListener(this);
        getAbstractListView().setOnItemClickListener(this);
    }

    @Override
    protected void initViews(View view) {
        View headerView = getActivity().getLayoutInflater().inflate(R.layout.header_seller_list_view, null);
        ((ListView) getAbstractListView()).addHeaderView(headerView);
        photoIv = (SimpleDraweeView) headerView.findViewById(R.id.iv_seller_image);
        regDate = (ArtJokerTextView) headerView.findViewById(R.id.tv_seller_reg_date);
        nameTv = (ArtJokerTextView) headerView.findViewById(R.id.tv_seller_name);
        phonesTv = (ArtJokerTextView) headerView.findViewById(R.id.tv_seller_phones);
        emailTv = (ArtJokerTextView) headerView.findViewById(R.id.tv_seller_email);
        addressTv = (ArtJokerTextView) headerView.findViewById(R.id.tv_seller_address);
        voicesTv = (ArtJokerTextView) headerView.findViewById(R.id.tv_number_of_ratings);
        callButton = (ImageButton) headerView.findViewById(R.id.button_call_to_seller);
        messagesButton = (ImageButton) headerView.findViewById(R.id.button_write_to_seller);
        rateButton = (ArtJokerButton) headerView.findViewById(R.id.button_rate);
        ratingBar = (RatingBar) headerView.findViewById(R.id.rating_bar_seller);
        listAdvertsTitle = (RelativeLayout) headerView.findViewById(R.id.rl_advert_title);
        listAdvertsTitle.setVisibility(View.GONE);
        advertsTitle = (ArtJokerTextView) headerView.findViewById(R.id.tv_number_advert_title);
        if (userId == PersonalData.getInstance(getActivity()).getUserId()) {
            headerView.findViewById(R.id.linearLayout2).setVisibility(View.GONE);
            rateButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.NETWORK_USER_DATA_LOADER_ID:
                args.putLong(RequestParams.ParamNames.USER_ID, userId);
                return new LocalRequestDescriptorLoader(getActivity(), new SellerUserDataRequest(getActivity(), args).setUiCallback(this));
            case LoadersId.NETWORK_RATING_SETTER_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new SetRatingRequest(getActivity(), args));
            case LoadersId.NETWORK_USER_ADS_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new AllUserAdsRequest(getActivity(), args));
            case LoadersId.DB_USER_ADS_LOADER_ID:
                return new CursorLoader(getActivity().getApplicationContext(), AdvertisementContract.CONTENT_URI, null,
                        AdvertisementColumns.USER_ID + " = ? ", new String[]{Long.toString(args.getLong(RequestParams.ParamNames.UID))}, null);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LoadersId.DB_USER_ADS_LOADER_ID:
                if (((HeaderViewListAdapter) getAbstractListView().getAdapter()).getWrappedAdapter() != null) {
                    Cursor cursor = (Cursor) data;
                    if (cursor.moveToFirst()) {
                        ((CursorAdapter) ((HeaderViewListAdapter) getAbstractListView().getAdapter()).getWrappedAdapter()).changeCursor(cursor);
                        advertsTitle.setText(getResources().getString(R.string.seller_number_ads_title, cursor.getCount()));
                        listAdvertsTitle.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    @Override
    protected void initAdapter(AbsListView absListView) {
        adapter = new AdvertAdapter(getActivity(), null, AdvertisementCursorConverter.class);
        getAbstractListView().setAdapter(adapter);
        if (data != null) {
            setContent();
        }
    }

    private void setContent() {
        photoIv.setImageURI(data.getPictureUrl());
        regDate.setText(String.format(getString(R.string.fragment_profile_registration_date), simpleDateFormat.format(
                new Date(SystemHelper.getInstance().getTimeInMillisFromSec(data.getRegistrationDate())))));
        nameTv.setText(String.format("%s %s", data.getFirstName(), data.getLastName()));
        if (data.getPhones() != null && data.getPhones().size() > 0) {
            userPhones = data.getPhones();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < userPhones.size(); i++) {
                stringBuilder.append(userPhones.get(i));
                if (i != userPhones.size() - 1) {
                    stringBuilder.append("\n");
                }
            }
            phonesTv.setText(stringBuilder.toString());
        }
        emailTv.setText(data.getEmail());
        voicesTv.setText(getResources().getString(R.string.rate_number_string, data.getVoices()));
        ratingBar.setRating(Float.valueOf(data.getRating()));
        setAddress();
        if (userPhones.size() == 0) {
            callButton.setEnabled(false);
        }
    }

    private void setAddress() {
        String format = "%s %s %s";
        ;
        String countryName = data.getCountryName();
        String regionName = data.getRegionName();
        String cityName = data.getCityName();

        if (regionName != null) {
            format = "%s, %s %s";
        }
        if (cityName != null) {
            format = "%s, %s, %s";
        }
        addressTv.setText(String.format(format,
                countryName != null ? countryName : "",
                regionName != null ? regionName : "",
                cityName != null ? cityName : ""));
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        commonBundleBuilder.putLong(RequestParams.ParamNames.UID, userId);
        manager.initLoader(LoadersId.NETWORK_USER_DATA_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
        commonBundleBuilder.putString(RequestParams.ParamNames.TOKEN, PersonalData.getInstance(getActivity().getApplicationContext()).getToken());
        manager.initLoader(LoadersId.NETWORK_USER_ADS_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
        manager.initLoader(LoadersId.DB_USER_ADS_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_call_to_seller:
                if (userPhones.size() != 0) {
                    switch (userPhones.size()) {
                        case 1:
                            callToSeller(userPhones.get(0));
                            break;

                        default:
                            final CharSequence[] phones = new CharSequence[userPhones.size()];
                            int i = 0;
                            for (String phone : userPhones) {
                                phones[i] = phone;
                                i++;
                            }

                            new AlertDialog.Builder(getActivity())
                                    .setTitle(getResources().getString(R.string.add_advert_select_phone_title))
                                    .setItems(phones, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            callToSeller(phones[which].toString());
                                            dialog.dismiss();
                                        }
                                    }).show();
                            break;
                    }
                }
                break;
            case R.id.button_write_to_seller:
                commit(MessagesByAdvertFragment.newInstance(userId, nameTv.getText().toString()),
                        MessagesByAdvertFragment.class.getCanonicalName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;
            case R.id.button_rate:
                showRateDialog();
                break;
            case R.id.button_send_rating:
                startRatingLoader();
                dialog.dismiss();
                break;
        }
    }

    private void callToSeller(String phoneNumber) {
        if (phoneNumber != null) {
            if (isTelephonyEnabled()) {
                Uri call = Uri.parse(String.format(Config.PHONE_FORMAT, phoneNumber));
                Intent intent = new Intent(Intent.ACTION_DIAL, call);
                startActivity(intent);
            } else {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.rate_dialog_error_title)
                        .setMessage(R.string.no_phone_caller)
                        .setPositiveButton(R.string.rate_dialog_error_ok_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.rate_dialog_error_title)
                    .setMessage(R.string.no_phone_number)
                    .setPositiveButton(R.string.rate_dialog_error_ok_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    private boolean isTelephonyEnabled() {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    private void startRatingLoader() {
        if (userId == PersonalData.getInstance(getActivity().getApplicationContext()).getUserId()) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.rate_dialog_error_title)
                    .setMessage(R.string.rate_dialog_error_text)
                    .setPositiveButton(R.string.rate_dialog_error_ok_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return;
        }
        if (rating != 0) {

            Bundle bundle = new BundleBuilder()
                    .putInt(RequestParams.ParamNames.RATE, rating)
                    .putString(RequestParams.ParamNames.TOKEN, PersonalData.getInstance(getActivity()).getToken())
                    .putLong(RequestParams.ParamNames.UID, userId)
                    .build();

            if (getLoaderManager().getLoader(LoadersId.NETWORK_RATING_SETTER_LOADER_ID) != null)
                getLoaderManager().restartLoader(LoadersId.NETWORK_RATING_SETTER_LOADER_ID, bundle, this).forceLoad();
            else
                getLoaderManager().initLoader(LoadersId.NETWORK_RATING_SETTER_LOADER_ID, bundle, this).forceLoad();
        }
    }

    private void showRateDialog() {
        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rate_dialog);

        dialog.findViewById(R.id.button_send_rating).setOnClickListener(this);
        RadioGroup radiogroup = (RadioGroup) dialog.findViewById(R.id.rate_radio_group);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_rate_1:
                        rating = 1;
                        break;
                    case R.id.rb_rate_2:
                        rating = 2;
                        break;
                    case R.id.rb_rate_3:
                        rating = 3;
                        break;
                    case R.id.rb_rate_4:
                        rating = 4;
                        break;
                    case R.id.rb_rate_5:
                        rating = 5;
                        break;
                }
            }
        });
        dialog.show();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            commit(AdvertDetailFragment.newInstance(((AdvertAdapter) (((HeaderViewListAdapter) getAbstractListView().getAdapter()).getWrappedAdapter()))
                    .getItem(position - 1).getObject().getId(), userId), AdvertDetailFragment.class.getCanonicalName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
        }
    }


    @Override
    public void uiDataResponse(ResponseHolder responseData) {
        data = (com.stockroompro.models.UserData) responseData.getData();
        if (isAdded()) {
            setContent();
        }
    }

    private interface Config {
        String PHONE_FORMAT = "tel:%s";
    }
}
