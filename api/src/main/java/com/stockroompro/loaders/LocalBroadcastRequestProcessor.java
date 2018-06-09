package com.stockroompro.loaders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.artjoker.core.BundleBuilder;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestProcessor;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.StatusCode;
import com.stockroompro.api.R;
import com.stockroompro.api.models.requests.AuthorizationRequest;
import com.stockroompro.models.PersonalData;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.REFRESH_TOKEN;


/**
 * Created by alexsergienko on 25.03.15.
 */
public class LocalBroadcastRequestProcessor extends RequestProcessor {

    public static final String REQUEST_PROCESSOR_ACTION = "com.stockroompro.network.REQUEST_PROCESSOR_ACTION";
    public static final String REQUEST_PROCESSOR_MESSAGE_RES_ID = "com.stockroompro.network.REQUEST_PROCESSOR_MESSAGE_RES_ID";
    public static final String REQUEST_PROCESSOR_POLICY_ID = "com.stockroompro.network.REQUEST_PROCESSOR_POLICY_ID";
    public static final String REQUEST_PROCESSOR_COMMIT_AUTHORIZATION = "com.stockroompro.network.REQUEST_PROCESSOR_AUTHORIZATION";
    public static final String REQUEST_PROCESSOR_RESPONSE_STATUS_BANNED = "com.stockroompro.network.RESPONSE_STATUS_BANNED";
    public static final String REQUEST_PROCESSOR_RESPONSE_STATUS_NOT_ACTIVE = "com.stockroompro.network.RESPONSE_STATUS_NOT_ACTIVE";
    public static final String REQUEST_PROCESSOR_RESPONSE_ADVERT_NO_ACTIVE_OR_DELETED = "com.stockroompro.network.ADVERT_NO_ACTIVE_OR_DELETED";
    public static final String DIALOG_PREFS = "DIALOG_PREFS";
    public static final String IS_SHOWED_DIALOG_KEY = "isShowedDialog";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";

    public LocalBroadcastRequestProcessor(Context context) {
        super(context);
    }

    @Override
    protected void checkError(ResponseHolder responseHolder) {
        switch (responseHolder.getStatusCode()) {
            case StatusCode.RESPONSE_IS_BANNED:
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(REQUEST_PROCESSOR_ACTION)
                        .putExtra(REQUEST_PROCESSOR_RESPONSE_STATUS_BANNED, true));
                break;

            case StatusCode.RESPONSE_NOT_ACTIVE:
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(REQUEST_PROCESSOR_ACTION)
                        .putExtra(REQUEST_PROCESSOR_RESPONSE_STATUS_NOT_ACTIVE, true));
                break;

            case StatusCode.ADVERT_NO_ACTIVE_OR_DELETED:
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(REQUEST_PROCESSOR_RESPONSE_ADVERT_NO_ACTIVE_OR_DELETED));
                break;
        }

        if (responseHolder.getErrorMessage() != null) {
            if (responseHolder.getErrorMessage().equals(INVALID_TOKEN)) {
                reAuthorize();
            } else if (responseHolder.getErrorMessage().equals(INVALID_REFRESH_TOKEN)) {
                commitAuthFragment();
            }
        }
    }

    private void commitAuthFragment() {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(REQUEST_PROCESSOR_ACTION)
                .putExtra(REQUEST_PROCESSOR_COMMIT_AUTHORIZATION, true));
    }

    private void reAuthorize() {
        String refreshToken = PersonalData.getInstance(getContext()).getRefreshToken();
        if (refreshToken != null) {
            process(new AuthorizationRequest(getContext(), new BundleBuilder().putString(REFRESH_TOKEN, refreshToken).build()));
        } else {
            commitAuthFragment();
        }
    }

    @Override
    protected void makeNotificationWithMessage(@StringRes final int requestNameId, final int notificationPolicyId, final String message) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(REQUEST_PROCESSOR_ACTION)
                .putExtra(REQUEST_PROCESSOR_POLICY_ID, notificationPolicyId)
                .putExtra(REQUEST_PROCESSOR_MESSAGE_RES_ID, requestNameId));

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (notificationPolicyId != NotificationsPolicy.NOTIFY_SUCCESS) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void notifyUserLostConnection() {
        if (!isShowedDialog()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.warn)
                            .setMessage(R.string.connection_error)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getContext().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
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
                }
            });
        }
    }

    private boolean isShowedDialog() {
        SharedPreferences preferences = getContext().getSharedPreferences(DIALOG_PREFS, Context.MODE_PRIVATE);
        boolean b = preferences.getBoolean(IS_SHOWED_DIALOG_KEY, false);
        preferences.edit().putBoolean(IS_SHOWED_DIALOG_KEY, true).commit();
        return b;
    }

    @Override
    protected void openAuth() {
    }

}
