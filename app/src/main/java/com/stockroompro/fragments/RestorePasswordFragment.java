package com.stockroompro.fragments;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;

import com.artjoker.core.BundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.core.views.ArtJokerEditText;
import com.stockroompro.R;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.api.models.requests.RestoreRequest;
import com.stockroompro.fragments.base.BaseApplicationFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;

public class RestorePasswordFragment extends BaseApplicationFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks, BaseRequest.UIResponseCallback {
    private ArtJokerEditText etEmail;
    private ArtJokerButton buttonRestore;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_restore_password;
    }

    @Override
    protected void initViews(View view) {
        etEmail =(ArtJokerEditText) view.findViewById(R.id.ed_restore_email);
        etEmail.setValidateEmail(true);
        buttonRestore =(ArtJokerButton) view.findViewById(R.id.button_restore);

    }

    @Override
    protected void initListeners(View view) {
        buttonRestore.setOnClickListener(this);

    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id){
            case LoadersId.NETWORK_RESTORE_PASSWORD_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(),new RestoreRequest(getActivity(),args).setUiCallback(this));

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
        switch (v.getId()){
            case R.id.button_restore:
                restorePassword();
                break;
        }
    }

    private void restorePassword() {
        if(etEmail.isValid())
        {
            Bundle restoreBundle = new BundleBuilder()
                    .putString(RequestParams.ParamNames.EMAIL, etEmail.getText().toString())
                    .build();
            if(getLoaderManager().getLoader(LoadersId.NETWORK_RESTORE_PASSWORD_LOADER_ID)!=null)
                getLoaderManager().restartLoader(LoadersId.NETWORK_RESTORE_PASSWORD_LOADER_ID,restoreBundle,this).forceLoad();
            else
                getLoaderManager().initLoader(LoadersId.NETWORK_RESTORE_PASSWORD_LOADER_ID, restoreBundle, this).forceLoad();

        }
    }
    @Override
    public void uiDataResponse(ResponseHolder data) {
        if (data.getStatusCode() == ResponseHolder.StatusCode.SUCCESSFULL) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.send_to_email)
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
                    .setMessage(R.string.restore_fail)
                    .create()
                    .show();
        }
    }

}
