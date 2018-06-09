package com.stockroompro.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.artjoker.core.network.ResponseHolder;
import com.artjoker.tool.core.Network;
import com.artjoker.tool.core.Preferences;
import com.artjoker.tool.core.SystemHelper;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.models.NotificationItem;
import com.stockroompro.models.PersonalData;

import java.io.IOException;

import static com.google.android.gms.common.ConnectionResult.SUCCESS;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DEVICE_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.GCM_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.RECIPIENT_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.USER_ID;
import static com.stockroompro.broadcasts.GcmBroadcastReceiver.NotificationHelper;
import static com.stockroompro.broadcasts.GcmBroadcastReceiver.NotificationHelper.StatusBarNotificationTypes.NEW_MESSAGE_TYPE;
import static com.stockroompro.broadcasts.GcmBroadcastReceiver.NotificationHelper.UPDATE_CHAT_ACTION;

/**
 * Created by bagach.alexandr on 25.03.15.
 */
public class GcmIntentService extends IntentService {


    public static final String MESSAGE_TAG = "message";
    private RegistrationTask registrationTask;

    public GcmIntentService() {
        super(GcmIntentService.class.getCanonicalName());
    }

    public static void subscribeForPush(Context context) {
        if (context != null)
            context.startService(new Intent(context, GcmIntentService.class).setAction(Config.SUBSCRIBE));
    }

    public static void unSubscribeForPush(Context context, String token, Long userId) {
        if (context != null)
            context.startService(new Intent(context, GcmIntentService.class).setAction(Config.UN_SUBSCRIBE)
                    .putExtra(TOKEN, token)
                    .putExtra(USER_ID, userId));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (valid(intent)) {
            final String action = intent.getAction();
            if (Config.RECEIVE_MESSAGE.equals(action)) {
                handleMessage(intent);
            } else if (Config.REGISTRATION.equals(action)) {
                Preferences preferences = new Preferences(getApplicationContext(), Properties.GCM_PREFS);
                storeRegistrationId(preferences, intent.getStringExtra(Properties.REGISTRATION_ID));
                stopRegistrationTask();
            } else if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                    || Intent.ACTION_PACKAGE_REPLACED.equals(action)) {
                clearRegistrationId();
            } else if (Config.SUBSCRIBE.equals(action)) {
                subscribe();
            } else if (Config.UN_SUBSCRIBE.equals(action)) {
                unSubscribe(intent.getStringExtra(TOKEN),
                        intent.getLongExtra(USER_ID, 0L));
            }
        }
    }

    private void stopRegistrationTask() {
        if ((registrationTask != null) && !registrationTask.isInterrupted()) {
            registrationTask.interrupt();
            registrationTask = null;
        }
    }

    private void handleMessage(Intent intent) {
        final Bundle data = intent.getExtras();
        final GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);
        final String messageType = googleCloudMessaging.getMessageType(intent);
        if (validData(data)) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {

            } else {
                if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                    String json = data.getString(MESSAGE_TAG);
                    //Log.e("Push",""+json);
                    processPushNotification(json);
                }
            }
        }
//        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void processPushNotification(String json) {
        final PersonalData personalData = PersonalData.getInstance(getBaseContext());
        if (personalData.getUserToken() != null || personalData.getUserServiceToken() != null) {
            NotificationItem push = new Gson().fromJson(json, NotificationItem.class);
            if (push != null) {
                NotificationHelper.processNotification(this, push);
                if (push.getType() == NEW_MESSAGE_TYPE)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(UPDATE_CHAT_ACTION)
                            .putExtra(RECIPIENT_ID, push.getUserId()));
            }
        }
    }

    private boolean validPushIntent(Intent intent) {
        return (intent != null) && (intent.getExtras() != null) && intent.getExtras().containsKey(ExtraKey.ACTION);
    }

    private boolean valid(Intent intent) {
        return (intent != null) && (intent.getAction() != null);
    }

    private boolean validData(Bundle data) {
        return (data != null) && !data.isEmpty();
    }

    private void clearRegistrationId() {

        Preferences preferences = new Preferences(getApplicationContext(), Properties.GCM_PREFS);
        preferences.putString(Properties.REGISTRATION_ID, "");
    }

    private void subscribe() {

        if (googlePlayServicesAvailable(getApplicationContext())) {
            final GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);

            final Preferences preferences = new Preferences(getApplicationContext(), Properties.GCM_PREFS);
            final String registrationId = getRegistrationId(preferences);
            if (TextUtils.isEmpty(registrationId)) {
                retrieveRegistrationId(googleCloudMessaging, preferences);
            } else {
                sendRegistrationIdToBackend(registrationId);
            }
        }
    }

    private void unSubscribe(String token, long userId) {
        final Preferences preferences = new Preferences(getApplicationContext(), Properties.GCM_PREFS);
        final String registrationId = getRegistrationId(preferences);
        if (!registrationId.isEmpty())
            try {
                ResponseHolder responseHolder = Communicator.getAppServer().unsubscribeGCM(new RequestParams()
                        .addParameter(TOKEN, token)
                        .addParameter(GCM_ID, registrationId)
                        .addParameter(DEVICE_ID, SystemHelper.getInstance().getUniqueDeviceId())
                        .buildJSON(AppServerSpecs.UNSUBSCRIBE_GCM_PATH));
                if (responseHolder.getStatusCode() != 0) {
                    unSubscribe(token, userId);
                    return;
                }
                Communicator.getAppServer().logOutSocial(userId, new RequestParams()
                        .addParameter(DEVICE_ID, SystemHelper.getInstance().getUniqueDeviceId())
                        .buildJSON(AppServerSpecs.LOG_OUT_PATH));
            } catch (Exception e) {
                e.printStackTrace();
            }

    }


    private int getAppVersion() {
        try {
            return getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }

    }

    private boolean googlePlayServicesAvailable(Context context) {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == SUCCESS;
    }

    private void storeRegistrationId(Preferences preferences, String registrationId) {
        if (!TextUtils.isEmpty(registrationId)) {
            int appVersion = getAppVersion();
            preferences.putString(Properties.REGISTRATION_ID, registrationId);
            preferences.putInt(Properties.APP_VERSION, appVersion);
            preferences.putBoolean(Properties.ALREADY_SENT_TO_BACKEND, false);
        }
    }

    private String getRegistrationId(Preferences preferences) {
        final String registrationId = preferences.getString(Properties.REGISTRATION_ID);
        if (registrationId.isEmpty()) {
            return "";
        }
        if (preferences.getInt(Properties.APP_VERSION) != getAppVersion()) {
            return "";
        }
        return registrationId;
    }

    private void retrieveRegistrationId(final GoogleCloudMessaging googleCloudMessaging, final Preferences preferences) {
        registrationTask = new RegistrationTask(googleCloudMessaging, preferences);
        registrationTask.start();
    }


    private void sendRegistrationIdToBackend(final String registrationId) {
        Handler handler = new Handler(Looper.getMainLooper());
        try {
            ResponseHolder responseHolder = Communicator.getAppServer().subscribeGCM(new RequestParams()
                    .addParameter(TOKEN, PersonalData.getInstance(this).getToken())
                    .addParameter(GCM_ID, registrationId)
                            // .addParameter(DEVICE_ID, SystemHelper.getInstance().getUniqueDeviceId())
                    .buildJSON(AppServerSpecs.SUBSCRIBE_GCM_PATH));
            if (responseHolder.getStatusCode() != 0) {
                subscribe();
            } else {
                stopRegistrationTask();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Config {
        String SENDER_ID = "784127836682";
        String BASE = Config.class.getCanonicalName();
        String SUBSCRIBE = BASE + ".Subscribe";
        String UN_SUBSCRIBE = BASE + ".UnSubscribe";
        String REGISTRATION = "com.google.android.c2dm.intent.REGISTRATION";
        String RECEIVE_MESSAGE = "com.google.android.c2dm.intent.RECEIVE";
    }

    public interface Properties {
        String REGISTRATION_ID = "registration_id";
        String APP_VERSION = "app_version";
        String ALREADY_SENT_TO_BACKEND = "already_sent_to_backend";
        String GCM_PREFS = "GCMPrefs";
    }

    private interface ExtraKey {
        String ACTION = "action";
    }

    private class RegistrationTask extends Thread {
        private final GoogleCloudMessaging googleCloudMessaging;
        private final Preferences preferences;

        private RegistrationTask(GoogleCloudMessaging googleCloudMessaging, Preferences preferences) {
            this.googleCloudMessaging = googleCloudMessaging;
            this.preferences = preferences;
        }


        private RegistrationTask(Context context) {
            this(GoogleCloudMessaging.getInstance(context), new Preferences(context, Properties.GCM_PREFS));
        }

        @Override
        public void run() {
            try {
                String registrationId = null;
                while (true) {
                    try {
                        while (TextUtils.isEmpty(registrationId)) {
                            if (Network.getInstance().isConnected(getApplicationContext())) {
                                registrationId = googleCloudMessaging.register(Config.SENDER_ID);
                            } else {
                                return;
                            }
                            Thread.sleep(1000);
                        }
                        storeRegistrationId(preferences, registrationId);
                        sendRegistrationIdToBackend(registrationId);
                        return;
                    } catch (IOException exception) {
                        synchronized (this) {
                            Thread.sleep(1000);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void cancelNotifications(Context context) {
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyManager.cancelAll();
    }
}
