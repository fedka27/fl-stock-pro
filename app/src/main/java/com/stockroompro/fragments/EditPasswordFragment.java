package com.stockroompro.fragments;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;

import com.artjoker.core.BundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.core.views.ArtJokerEditText;
import com.artjoker.tool.core.Notification;
import com.stockroompro.R;
import com.stockroompro.api.models.requests.ChangePasswordRequest;
import com.stockroompro.fragments.base.BaseApplicationFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.PersonalData;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.EMAIL;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.FIRST_NAME;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.LAST_NAME;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.OLD_PASSWORD;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.PASSWORD;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;


public class EditPasswordFragment extends BaseApplicationFragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks, BaseRequest.UIResponseCallback {
    private ArtJokerEditText etOldPassword;
    private ArtJokerEditText etNewPassword;
    private ArtJokerButton buttonChangePassword;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_change_password;
    }

    @Override
    protected void initViews(View view) {
        etOldPassword = (ArtJokerEditText) view.findViewById(R.id.et_old_pass);
        etNewPassword = (ArtJokerEditText) view.findViewById(R.id.et_new_pass);
        buttonChangePassword = (ArtJokerButton) view.findViewById(R.id.button_commit_pass);
    }

    @Override
    protected void initListeners(View view) {
        buttonChangePassword.setOnClickListener(this);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.NETWORK_CHANGE_PASSWORD_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new ChangePasswordRequest(getActivity(), args).setUiCallback(this));


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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_commit_pass:
                editPassword();
                break;
        }
    }

    private void editPassword() {
        if (etOldPassword.isValid() && etNewPassword.isValid()) {
            Bundle bundle = new BundleBuilder()
                    .putLong(UID, PersonalData.getInstance(getActivity()).getUserId())
                    .putString(TOKEN, PersonalData.getInstance(getActivity()).getToken())
                    .putString(EMAIL, PersonalData.getInstance(getActivity()).getUserEmail())
                    .putString(FIRST_NAME, PersonalData.getInstance(getActivity()).getUserFirstName())
                    .putString(OLD_PASSWORD, etOldPassword.getText().toString())
                    .putString(PASSWORD, etNewPassword.getText().toString())
                    .putString(LAST_NAME, PersonalData.getInstance(getActivity()).getUserLastName())
                    .build();
            if (getLoaderManager().getLoader(LoadersId.NETWORK_CHANGE_PASSWORD_LOADER_ID) != null)
                getLoaderManager().restartLoader(LoadersId.NETWORK_CHANGE_PASSWORD_LOADER_ID, bundle, this).forceLoad();
            else
                getLoaderManager().initLoader(LoadersId.NETWORK_CHANGE_PASSWORD_LOADER_ID, bundle, this).forceLoad();
        }
    }

    @Override
    public void uiDataResponse(ResponseHolder data) {
        if (data.getStatusCode() == 0) {
            Notification.getInstance().show(getActivity().getApplicationContext(), getActivity().getApplicationContext().getString(R.string.change_password_done));
            popBack();
        } else {
            Notification.getInstance().show(getActivity().getApplicationContext(), getActivity().getApplicationContext().getString(R.string.change_password_fail));
        }
    }
}
