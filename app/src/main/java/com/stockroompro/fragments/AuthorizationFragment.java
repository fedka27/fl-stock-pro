package com.stockroompro.fragments;


import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;

import com.artjoker.core.BundleBuilder;
import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.activities.SocialActivity;
import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.socialnetworks.ISocialNetwork;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.core.views.ArtJokerEditText;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.stockroompro.Launcher;
import com.stockroompro.R;
import com.stockroompro.api.models.requests.AuthorizationRequest;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.api.models.requests.SocialNetworkAuthRequest;
import com.stockroompro.api.models.requests.SocialNetworkRegRequest;
import com.stockroompro.fragments.base.BaseApplicationListFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.services.GcmIntentService;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames;

public class AuthorizationFragment extends BaseApplicationListFragment implements BaseRequest.UIResponseCallback, View.OnClickListener, LoaderManager.LoaderCallbacks, ISocialNetwork.OnStartLoaderListener {
    private ArtJokerButton buttonLogin;
    private ImageButton buttonLoginVk;
    private ImageButton buttonLoginFacebook;
    private ArtJokerButton buttonForgotPassword;
    private ArtJokerButton buttonRegistration;
    private ArtJokerEditText etEmail;
    private ArtJokerEditText etPassword;
    private Bundle authBundle;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected void initViews(View view) {
        buttonLogin = (ArtJokerButton) view.findViewById(R.id.button_login);
        buttonLoginVk = (ImageButton) view.findViewById(R.id.button_login_vk);
        buttonLoginFacebook = (ImageButton) view.findViewById(R.id.button_login_facebook);

        buttonForgotPassword = (ArtJokerButton) view.findViewById(R.id.button_forgot_password);
        buttonRegistration = (ArtJokerButton) view.findViewById(R.id.button_go_to_registration);
        etEmail = (ArtJokerEditText) view.findViewById(R.id.et_login_email);
        etEmail.setValidateEmail(true);
        etPassword = (ArtJokerEditText) view.findViewById(R.id.et_login_pass);

    }

    @Override
    protected void initListeners(View view) {
        buttonLogin.setOnClickListener(this);
        buttonForgotPassword.setOnClickListener(this);
        buttonRegistration.setOnClickListener(this);
        buttonLoginVk.setOnClickListener(this);
        buttonLoginFacebook.setOnClickListener(this);

        ((SocialActivity) getActivity()).setCallbacks(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        etEmail.invalidateError(etEmail.getText().toString());
        etPassword.invalidateError(etPassword.getText().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_login:
                authorize();
                break;
            case R.id.button_forgot_password:
                commit(new RestorePasswordFragment(), RestorePasswordFragment.class.getCanonicalName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;
            case R.id.button_go_to_registration:
                commit(new RegistrationFragment(), RegistrationFragment.class.getCanonicalName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;
            case R.id.button_login_vk:
                ((Launcher) getActivity()).login(ISocialNetwork.SOCIAL_TYPE_VK);
                break;
            case R.id.button_login_facebook:
                ((Launcher) getActivity()).login(ISocialNetwork.SOCIAL_TYPE_FB);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void authorize() {
        if (etEmail.isValid() && etPassword.isValid()) {
            Bundle authBundle = new BundleBuilder()
                    .putString(ParamNames.PASSWORD, etPassword.getText().toString())
                    .putString(ParamNames.EMAIL, etEmail.getText().toString())
                    .build();
            if (getLoaderManager().getLoader(LoadersId.NETWORK_AUTHORIZATION_LOADER_ID) != null)
                getLoaderManager().restartLoader(LoadersId.NETWORK_AUTHORIZATION_LOADER_ID, authBundle, this).forceLoad();
            else
                getLoaderManager().initLoader(LoadersId.NETWORK_AUTHORIZATION_LOADER_ID, authBundle, this).forceLoad();
        }

    }

    private void startSocialLoaders(String token, long userId, int type) {
        authBundle = new BundleBuilder()

                .putLong(ParamNames.UID, userId)
                .putString(ParamNames.SERVICE_TOKEN, token)
                .putInt(ParamNames.SERVICE_ID, type)
                .build();
        startSocialAuthLoader(authBundle);

    }

    public void startSocialAuthLoader() {
        startSocialAuthLoader(authBundle);
    }

    private void startSocialAuthLoader(Bundle authBundle) {
        if (getLoaderManager().getLoader(LoadersId.NETWORK_SOCIAL_AUTHORIZATION_LOADER_ID) != null)
            getLoaderManager().restartLoader(LoadersId.NETWORK_SOCIAL_AUTHORIZATION_LOADER_ID, authBundle, this).forceLoad();
        else
            getLoaderManager().initLoader(LoadersId.NETWORK_SOCIAL_AUTHORIZATION_LOADER_ID, authBundle, this).forceLoad();
    }

    public void startSocialRegLoader() {
        Bundle bundle = new BundleBuilder()
                .putLong(RequestParams.ParamNames.UID, authBundle.getLong(RequestParams.ParamNames.UID))
                .putString(RequestParams.ParamNames.TOKEN, authBundle.getString(RequestParams.ParamNames.SERVICE_TOKEN))
                .putInt(RequestParams.ParamNames.SERVICE, authBundle.getInt(ParamNames.SERVICE_ID))
                .build();
        if (getLoaderManager().getLoader(LoadersId.NETWORK_SOCIAL_REGISTRATION_LOADER_ID) != null)
            getLoaderManager().restartLoader(LoadersId.NETWORK_SOCIAL_REGISTRATION_LOADER_ID, bundle, this).forceLoad();
        else
            getLoaderManager().initLoader(LoadersId.NETWORK_SOCIAL_REGISTRATION_LOADER_ID, bundle, this).forceLoad();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.NETWORK_AUTHORIZATION_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new AuthorizationRequest(getActivity(), args).setUiCallback(this));

            case LoadersId.NETWORK_SOCIAL_AUTHORIZATION_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new SocialNetworkAuthRequest(getActivity(), args));


            case LoadersId.NETWORK_SOCIAL_REGISTRATION_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new SocialNetworkRegRequest(getActivity(), args));


        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

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
    public void onStartLoader(String token, long userID, int socNetworkType) {
        startSocialLoaders(token, userID, socNetworkType);
    }

    @Override
    public void uiDataResponse(ResponseHolder data) {
        if (data.getStatusCode() == ResponseHolder.StatusCode.SUCCESSFULL) {
            GcmIntentService.subscribeForPush(getActivity());
            popBack();
        } else {
            etEmail.setError(getString(R.string.auth_fail));
            etPassword.setError(getString(R.string.auth_fail));
        }

    }
}
