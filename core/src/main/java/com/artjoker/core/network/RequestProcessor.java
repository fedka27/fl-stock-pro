package com.artjoker.core.network;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

import com.artjoker.core.BackgroundUtils;
import com.artjoker.core.R;
import com.artjoker.core.Validator;
import com.artjoker.tool.core.Network;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexsergienko on 16.03.15.
 */
public abstract class RequestProcessor {

    private static final String TAG = RequestProcessor.class.getSimpleName();
    public static final String HIDE_ADD_ADVERT_PROGRESS_BAR = "com.artjoker.core.newtwork.HIDE_PROGRESS";
    private static final Map<Integer, Integer> notificationPolicyVSStringsIds = new HashMap<>();
    private Context context;
    private ServerErrorsDefiner serverErrorsDefiner;

    public RequestProcessor(Context context) {
        this.context = context;
        notificationPolicyVSStringsIds.put(NotificationsPolicy.NOTIFY_IF_IS_NOT_VALID, R.string.error_request_validation);
        notificationPolicyVSStringsIds.put(NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE, R.string.error_response_from_server);
        notificationPolicyVSStringsIds.put(NotificationsPolicy.NOTIFY_IF_NO_INTERNET_CONNECTION, R.string.error_internet_connection);
        notificationPolicyVSStringsIds.put(NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING, R.string.error_process_response);
        notificationPolicyVSStringsIds.put(NotificationsPolicy.NOTIFY_IF_ERROR_REQUEST, R.string.error_in_request);
        notificationPolicyVSStringsIds.put(NotificationsPolicy.NOTIFY_SUCCESS, R.string.success_request);
    }

    public void process(final RequestDescriptor requestDescriptor) {
        if (!isValidateRequest(requestDescriptor)) {
            return;
        }
        if (!isValidInternetConnection()) {
            notifyWithNotificationPolicy(requestDescriptor, NotificationsPolicy.NOTIFY_IF_NO_INTERNET_CONNECTION);
            if (isContainRequestNotificationPolicy(requestDescriptor, NotificationsPolicy.NOTIFY_ALL) ||
                    isContainRequestNotificationPolicy(requestDescriptor, NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST)) {
                notifyUserLostConnection();
            }
            return;
        }
        ResponseHolder responseHolder = null;
        try {
            BackgroundUtils.requestsCountChanged(context, true);
            responseHolder = requestDescriptor.makeRequest();
        } catch (Exception e) {
            notifyWithNotificationPolicy(requestDescriptor, NotificationsPolicy.NOTIFY_IF_ERROR_REQUEST);
            getContext().sendBroadcast(new Intent(HIDE_ADD_ADVERT_PROGRESS_BAR));
            BackgroundUtils.requestsCountChanged(context, false);
            Log.e(TAG, "Cannot make Request to server", e);
            return;
        }
        if (responseHolder != null) {
            try {
                if (responseHolder.getStatusCode() == StatusCode.RESPONSE_SUCCESS) {
                    requestDescriptor.processResponse(responseHolder);
                } else {
                    checkError(responseHolder);
                    if (isContainRequestNotificationPolicy(requestDescriptor, NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE)) {
                        final int resourceStringIdMessageByPolicyId = getResourceStringIdMessageByPolicyId(NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE);
                        String errorMessage = context.getString(resourceStringIdMessageByPolicyId);
                        if (serverErrorsDefiner != null) {
                            serverErrorsDefiner.getErrorMessageByCode(responseHolder.getStatusCode());
                        }
                        makeNotificationWithMessage(requestDescriptor.getRequestName(), NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE, errorMessage);
                    }
                    BackgroundUtils.requestsCountChanged(context, false);
                }
            } catch (Exception e) {
                notifyWithNotificationPolicy(requestDescriptor, NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING);
                BackgroundUtils.requestsCountChanged(context, false);
                Log.e(TAG, "Cannot process response " + responseHolder, e);
                return;
            } finally {
                BackgroundUtils.requestsCountChanged(context, false);
            }
        } else {
            BackgroundUtils.requestsCountChanged(context, false);
        }
        notifyWithNotificationPolicy(requestDescriptor, NotificationsPolicy.NOTIFY_SUCCESS);
        final ResponseHolder holder = responseHolder;
        if (((BaseRequest) requestDescriptor).callback != null)
            if (((BaseRequest) requestDescriptor).callback instanceof Fragment) {
                if (((Fragment) ((BaseRequest) requestDescriptor).callback).isAdded())
                    ((BaseRequest) requestDescriptor).callback.dataResponse(responseHolder);
            } else {
                ((BaseRequest) requestDescriptor).callback.dataResponse(responseHolder);
            }
        if (((BaseRequest) requestDescriptor).uiCallback != null)
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (((BaseRequest) requestDescriptor).uiCallback instanceof Fragment) {
                        if (((Fragment) ((BaseRequest) requestDescriptor).uiCallback).isAdded())
                            ((BaseRequest) requestDescriptor).uiCallback.uiDataResponse(holder);
                    } else {
                        ((BaseRequest) requestDescriptor).uiCallback.uiDataResponse(holder);
                    }
                }
            });
    }

    protected abstract void checkError(ResponseHolder responseHolder);


    protected boolean isContainRequestNotificationPolicy(RequestDescriptor requestDescriptor, int policy) {
        if (requestDescriptor instanceof RequestWithNotifications) {
            int policyId = ((RequestWithNotifications) requestDescriptor).getNotificationPolicy();
            return (policyId & policy) == policy;
        }
        return false;
    }

    private void notifyWithNotificationPolicy(RequestDescriptor requestDescriptor, int policy) {
        if (isContainRequestNotificationPolicy(requestDescriptor, policy)) {
            makeNotificationWithPolicyId(requestDescriptor.getRequestName(), policy);
        }
    }

    protected int getResourceStringIdMessageByPolicyId(int policyId) {
        return notificationPolicyVSStringsIds.get(policyId);
    }

    private boolean isValidateRequest(RequestDescriptor requestDescriptor) {
        boolean isValidRequest = true;
        if (requestDescriptor instanceof Validator) {
            isValidRequest = ((Validator) requestDescriptor).isValid();
            if (!isValidRequest) {
                notifyWithNotificationPolicy(requestDescriptor, NotificationsPolicy.NOTIFY_IF_IS_NOT_VALID);
            } else if (requestDescriptor instanceof SessionRequestWithNotifications) {
                isValidRequest = ((SessionRequestWithNotifications) requestDescriptor).isSessionValid();
                int policyId = ((SessionRequestWithNotifications) requestDescriptor).getAuthNotificationPolicy();
                if (policyId != NotificationsPolicy.DO_NOT_NOTIFY_ALL) {
                    if ((policyId & NotificationsPolicy.SHOW_TOAST) == NotificationsPolicy.SHOW_TOAST && !isValidRequest) {
                        Toast.makeText(context, R.string.error_session_not_valid, Toast.LENGTH_LONG).show();
                    }
                    if ((policyId & NotificationsPolicy.OPEN_AUTH_WINDOW) == NotificationsPolicy.OPEN_AUTH_WINDOW && !isValidRequest) {
                        openAuth();
                    }
                }
            }
        }
        return isValidRequest;
    }


    private boolean isValidInternetConnection() {
        return Network.getInstance().isConnected(getContext());
    }

    private void makeNotificationWithPolicyId(@StringRes int requestNameId, int notificationPolicyId) {

        makeNotificationWithMessage(requestNameId, notificationPolicyId, context.getString(getResourceStringIdMessageByPolicyId(notificationPolicyId)));
    }

    protected abstract void makeNotificationWithMessage(@StringRes int requestNameId, int notificationPolicyId, String message);

    protected abstract void notifyUserLostConnection();

    protected abstract void openAuth();

    public Context getContext() {
        return context;
    }

    public void setServerErrorsDefiner(ServerErrorsDefiner serverErrorsDefiner) {
        this.serverErrorsDefiner = serverErrorsDefiner;
    }
}
