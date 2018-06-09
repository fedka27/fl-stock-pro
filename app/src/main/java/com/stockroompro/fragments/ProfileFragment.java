package com.stockroompro.fragments;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.artjoker.core.BundleBuilder;
import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.socialnetworks.ISocialNetwork;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stockroompro.R;
import com.stockroompro.api.models.requests.DeleteProfileRequest;
import com.stockroompro.api.models.requests.SelfDataRequest;
import com.stockroompro.api.models.requests.SyncWithSocialNetworkRequest;
import com.stockroompro.fragments.base.BaseApplicationListFragment;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.PersonalData;
import com.stockroompro.socialnetworks.FacebookNetwork;
import com.stockroompro.socialnetworks.VKNetwork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.SERVICE_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.SERVICE_TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.SERVICE_UID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;
import static com.stockroompro.loaders.LoadersId.NETWORK_DELETE_PROFILE_LOADER_ID;
import static com.stockroompro.loaders.LoadersId.NETWORK_SOCIAL_SYNCHRONIZATION_LOADER_ID;
import static com.stockroompro.loaders.LoadersId.NETWORK_USER_DATA_LOADER_ID;


public class ProfileFragment extends BaseApplicationListFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks {
    private SimpleDraweeView photoIv;
    private ArtJokerTextView regDate;
    private ArtJokerTextView nameTv;
    private ArtJokerTextView unreadMessagesTv;
    private ArtJokerTextView phonesTv;
    private ArtJokerTextView emailTv;
    private ArtJokerTextView addressTv;
    private ArtJokerTextView voicesTv;
    private ArtJokerButton deleteProfileButton;
    private ArtJokerButton editProfileButton;
    private ArtJokerButton editPasswordButton;
    private RatingBar ratingBar;
    private ImageButton syncVkButton;
    private ImageButton messagesButton;
    private ImageButton myAdvertsButton;
    private ImageButton favouriteButton;
    private ImageButton syncFacebookButton;
    private final SimpleDateFormat simpleDateFormat;

    {
        simpleDateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = new Bundle();
        args.putLong(UID, PersonalData.getInstance(getActivity()).getUserId());
        if (getLoaderManager().getLoader(NETWORK_USER_DATA_LOADER_ID) != null) {
            getLoaderManager().getLoader(NETWORK_USER_DATA_LOADER_ID).reset();
        }
        getLoaderManager().restartLoader(NETWORK_USER_DATA_LOADER_ID, args, this).forceLoad();
        getParent().setMainTitle(getString(R.string.personal_account_title));
    }

    @Override
    protected void initAdapter(AbsListView absListView) {
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initListeners(View view) {
        messagesButton.setOnClickListener(this);
        deleteProfileButton.setOnClickListener(this);
        syncFacebookButton.setOnClickListener(this);
        syncVkButton.setOnClickListener(this);
        editProfileButton.setOnClickListener(this);
        editPasswordButton.setOnClickListener(this);
        favouriteButton.setOnClickListener(this);
        myAdvertsButton.setOnClickListener(this);
    }

    @Override
    protected void initViews(View view) {
        myAdvertsButton = (ImageButton) view.findViewById(R.id.button_my_adverts);
        messagesButton = (ImageButton) view.findViewById(R.id.button_personal_messages);
        favouriteButton = (ImageButton) view.findViewById(R.id.button_favourites);
        photoIv = (SimpleDraweeView) view.findViewById(R.id.iv_profile_seller_image);
        regDate = (ArtJokerTextView) view.findViewById(R.id.tv_profile_seller_reg_date);
        nameTv = (ArtJokerTextView) view.findViewById(R.id.tv_profile_seller_name);
        phonesTv = (ArtJokerTextView) view.findViewById(R.id.tv_profile_seller_phones);
        emailTv = (ArtJokerTextView) view.findViewById(R.id.tv__profile_seller_email);
        addressTv = (ArtJokerTextView) view.findViewById(R.id.tv__profile_seller_address);
        voicesTv = (ArtJokerTextView) view.findViewById(R.id.tv_profile_number_of_ratings);
        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar_profile_seller);
        deleteProfileButton = (ArtJokerButton) view.findViewById(R.id.button_profile_delete);
        syncVkButton = (ImageButton) view.findViewById(R.id.button_vk);
        syncFacebookButton = (ImageButton) view.findViewById(R.id.button_facebook);
        editProfileButton = (ArtJokerButton) view.findViewById(R.id.button_edit_profile);
        editPasswordButton = (ArtJokerButton) view.findViewById(R.id.button_profile_change_password);
        unreadMessagesTv = (ArtJokerTextView) view.findViewById(R.id.tv_profile_number_unread_messages);
        if (PersonalData.getInstance(getActivity()).getUserToken() == null) { // USER TOKEN !!!!!! NOT getToken() !!!!!
            editProfileButton.setVisibility(View.GONE);
            editPasswordButton.setVisibility(View.GONE);
            syncVkButton.setVisibility(View.GONE);
            syncFacebookButton.setVisibility(View.GONE);
            ((ArtJokerTextView) view.findViewById(R.id.tv_synchronize_title)).setVisibility(View.GONE);
        }
        setContent();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case NETWORK_USER_DATA_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new SelfDataRequest(getActivity(), args.getLong(UID)));

            case NETWORK_DELETE_PROFILE_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new DeleteProfileRequest(getActivity(), args));

            case NETWORK_SOCIAL_SYNCHRONIZATION_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new SyncWithSocialNetworkRequest(getActivity(), args));
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case NETWORK_USER_DATA_LOADER_ID:
                setContent();
                break;
        }
    }

    private void setContent() {
        photoIv.setImageURI(PersonalData.getInstance(getActivity()).getUserPicture());
        if (PersonalData.getInstance(getActivity()).getUserPhones() != null) {
            List<String> allPhones = new ArrayList<String>(PersonalData.getInstance(getActivity()).getUserPhones());
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < allPhones.size(); i++) {
                stringBuilder.append(allPhones.get(i));
                if (i != allPhones.size() - 1) {
                    stringBuilder.append("\n");
                }
            }
            phonesTv.setText(stringBuilder.toString());
        }
        long unreadMessageCount = PersonalData.getInstance(getActivity()).getUserUnreadMessagesCount();
        if (unreadMessageCount > 0) {
            unreadMessagesTv.setText(Long.toString(unreadMessageCount));
        } else {
            unreadMessagesTv.setVisibility(View.INVISIBLE);
        }
        regDate.setText(String.format(getString(R.string.fragment_profile_registration_date), simpleDateFormat.format(
                new Date(SystemHelper.getInstance().getTimeInMillisFromSec(
                        PersonalData.getInstance(getActivity()).getUserRegistrationDate())))));
        emailTv.setText(PersonalData.getInstance(getActivity()).getUserEmail());
        nameTv.setText(String.format("%s %s", PersonalData.getInstance(getActivity()).getUserFirstName(), PersonalData.getInstance(getActivity()).getUserLastName()));
        voicesTv.setText(getResources().getString(R.string.rate_number_string, PersonalData.getInstance(getActivity()).getUserVoices()));
        setAddress();

        ratingBar.setRating(PersonalData.getInstance(getActivity()).getUserRating());
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private void setAddress() {
        String format = "%s %s %s";;
        String countryName = PersonalData.getInstance(getActivity()).getUserCountryName();
        String regionName = PersonalData.getInstance(getActivity()).getUserRegionName();
        String cityName = PersonalData.getInstance(getActivity()).getUserCityName();

        if (regionName != null) {
            format = "%s, %s %s";
        }

        if (cityName != null) {
            format = "%s, %s, %s";
        }

        addressTv.setText(String.format(format,
                countryName !=  null ? countryName : "",
                regionName !=  null ? regionName : "",
                cityName != null ? cityName : ""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_profile_delete:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.warning)
                        .setMessage(R.string.warning_delete_profile)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProfile();
                                dialog.dismiss();
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

            case R.id.button_vk:
                startSyncWithSocialNetwork(ISocialNetwork.SOCIAL_TYPE_VK);
                break;

            case R.id.button_facebook:
                startSyncWithSocialNetwork(ISocialNetwork.SOCIAL_TYPE_FB);
                break;

            case R.id.button_edit_profile:
                commit(new EditProfileFragment(), EditProfileFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;

            case R.id.button_profile_change_password:
                commit(new EditPasswordFragment(), EditPasswordFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;

            case R.id.button_personal_messages:
                commit(new MessagesByUserFragment(), MessagesByUserFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;

            case R.id.button_favourites:
                commit(FavoritesFragment.newInstance(), FavoritesFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;

            case R.id.button_my_adverts:
                commit(new MyAdvertisementsFragment(), MyAdvertisementsFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;
        }
    }

    private void startSyncWithSocialNetwork(int socialNetworkCode) {
        String serviceToken = null;
        switch (socialNetworkCode) {
            case ISocialNetwork.SOCIAL_TYPE_VK:
                serviceToken = VKNetwork.getSavedToken(getActivity());
                break;

            case ISocialNetwork.SOCIAL_TYPE_FB:
                serviceToken = FacebookNetwork.getSavedToken(getActivity());
                break;
        }
        if (PersonalData.getInstance(getActivity()).getUserToken() != null && serviceToken != null) {
            Bundle bundle = new BundleBuilder()
                    .putInt(SERVICE_ID, socialNetworkCode)
                    .putString(TOKEN, PersonalData.getInstance(getActivity()).getUserToken())
                    .putString(SERVICE_TOKEN, serviceToken)
                    .putLong(SERVICE_UID, PersonalData.getInstance(getActivity()).getUserId())
                    .build();

            if (getLoaderManager().getLoader(NETWORK_SOCIAL_SYNCHRONIZATION_LOADER_ID) != null)
                getLoaderManager().restartLoader(NETWORK_SOCIAL_SYNCHRONIZATION_LOADER_ID, bundle, this).forceLoad();
            else
                getLoaderManager().initLoader(NETWORK_SOCIAL_SYNCHRONIZATION_LOADER_ID, bundle, this).forceLoad();
        } else {
            if (serviceToken == null)
                Toast.makeText(getActivity(), R.string.social_network_never_used, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProfile() {
        String token = PersonalData.getInstance(getActivity()).getToken();
        if (token != null) {
            Bundle bundle = new BundleBuilder()
                    .putString(TOKEN, token)
                    .putLong(UID, PersonalData.getInstance(getActivity()).getUserId())
                    .build();
            if (getLoaderManager().getLoader(NETWORK_DELETE_PROFILE_LOADER_ID) != null)
                getLoaderManager().restartLoader(NETWORK_DELETE_PROFILE_LOADER_ID, bundle, this).forceLoad();
            else
                getLoaderManager().initLoader(NETWORK_DELETE_PROFILE_LOADER_ID, bundle, this).forceLoad();
        }
    }
}
