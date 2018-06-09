package com.stockroompro.broadcasts;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.artjoker.core.BundleBuilder;
import com.artjoker.core.network.ResponseHolder;
import com.google.gson.Gson;
import com.stockroompro.Launcher;
import com.stockroompro.R;
import com.stockroompro.models.NotificationItem;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.contracts.ChatMessageContract;
import com.stockroompro.services.GcmIntentService;
import com.stockroompro.utils.ChatConfig;

import java.util.ArrayList;

import static com.stockroompro.broadcasts.GcmBroadcastReceiver.NotificationHelper.StatusBarNotificationTypes.AD_APPROVED_NOTIFICATION;
import static com.stockroompro.broadcasts.GcmBroadcastReceiver.NotificationHelper.StatusBarNotificationTypes.AD_DISMISSED_NOTIFICATION;
import static com.stockroompro.broadcasts.GcmBroadcastReceiver.NotificationHelper.StatusBarNotificationTypes.MESSAGE_READ_TYPE;
import static com.stockroompro.broadcasts.GcmBroadcastReceiver.NotificationHelper.StatusBarNotificationTypes.NEW_MESSAGE_TYPE;


public final class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        startWakefulService(context, intent.setComponent(buildComponent(context)));
        try {
            setResultCode(Activity.RESULT_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ComponentName buildComponent(Context context) {
        return new ComponentName(context.getApplicationContext(), GcmIntentService.class);
    }

    public static class NotificationHelper {
        public static final String UPDATE_CHAT_ACTION = "com.stockroom.pro.gcm.UPDATE_CHAT";
        public static final String FRAGMENT_TAG = "fragment";
        public static final String AD_TAG = "ad_id";
        public static final String RECIPIENT_TAG = "recipient_id";
        private static boolean showNewMessageNotification = true;

        public static void processNotification(Context context, NotificationItem push) {
            Notification.Builder notificationBuilder = new Notification.Builder(context);
            Intent notificationIntent = new Intent(context, Launcher.class);
            notificationBuilder.setSmallIcon(R.drawable.person_icon);
            switch (push.getType()) {

                case MESSAGE_READ_TYPE:
                    changeMessageStatusInDB(context, push.getIds());
                    break;
                case NEW_MESSAGE_TYPE:
                    processNewMessage(context, push.getText(), (int) push.getId(), push.getUserId(), push.getAdId(), notificationBuilder, notificationIntent);
                    break;
                case AD_APPROVED_NOTIFICATION:
                    buildNotification(notificationBuilder, push.getText(), push.getText(), push.getText());
                    notificationIntent.putExtra(FRAGMENT_TAG, AD_APPROVED_NOTIFICATION);
                    showNotification(context, (int) push.getId(), notificationBuilder, notificationIntent);
                    break;
                case AD_DISMISSED_NOTIFICATION:
                    buildNotification(notificationBuilder, push.getText(), push.getText(), push.getText());
                    notificationIntent.putExtra(FRAGMENT_TAG, AD_DISMISSED_NOTIFICATION);
                    showNotification(context, (int) push.getId(), notificationBuilder, notificationIntent);
                    break;

            }

        }

        private static void processNewMessage(Context context, String text, int id, long userId, long adId, Notification.Builder notificationBuilder, Intent notificationIntent) {
            buildNotification(notificationBuilder, context.getResources().getString(R.string.new_message), context.getResources().getString(R.string.new_message), text);

            if (PersonalData.getInstance(context).getUserId() != userId)
                if (showNewMessageNotification) {
                    notificationIntent.putExtra(FRAGMENT_TAG, NEW_MESSAGE_TYPE);
                    notificationIntent.putExtra(RECIPIENT_TAG, userId);
                    notificationIntent.putExtra(AD_TAG, adId);
                    showNotification(context, (int) id, notificationBuilder, notificationIntent);
                }
        }

        private static void changeMessageStatusInDB(Context context, ArrayList<Long> id) {
            ArrayList<ContentProviderOperation> operations = new ArrayList<>(id.size());

            if (context != null) {
                for (Long lon : id) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ChatMessageContract.SENDING_STATUS, ChatConfig.STATUS_READ);
                    operations.add(ContentProviderOperation
                            .newUpdate(ChatMessageContract.CONTENT_URI)
                            .withSelection(ChatMessageContract.ID + " = ? AND 1=1", new String[]{String.valueOf(lon)})
                            .withValues(contentValues)
                            .build());
                }
                try {
                    context.getContentResolver().applyBatch(ChatMessageContract.CONTENT_URI.getAuthority(), operations);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }
                //   context.getContentResolver().update(ChatMessageContract.CONTENT_URI, contentValues, ChatMessageContract.ID + " = ? AND 1=1", new String[]{String.valueOf(id)});

            }
        }

        private static void buildNotification(Notification.Builder notificationBuilder, String ticker, String title, String text) {
            notificationBuilder
                    .setTicker(ticker)
                    .setWhen(System.currentTimeMillis())
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(text);
        }


        private static void showNotification(Context context, int id, Notification.Builder notificationBuilder, Intent notificationIntent) {

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            notificationBuilder.setContentIntent(notifyPendingIntent);
            Notification notification;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notification = notificationBuilder.build();
            } else {
                notification = notificationBuilder.getNotification();
            }
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, notification);
        }

        public static boolean isShowNewMessageNotification() {
            return showNewMessageNotification;
        }

        public static void setShowNewMessageNotification(boolean showNewMessageNotification) {
            NotificationHelper.showNewMessageNotification = showNewMessageNotification;
        }

        public interface StatusBarNotificationTypes {
            int NEW_MESSAGE_TYPE = 1;
            int MESSAGE_READ_TYPE = 2;
            int AD_APPROVED_NOTIFICATION = 4;
            int AD_DISMISSED_NOTIFICATION = 5;

          /*  int NEW_ = 4;
            int NEW_ = 5;
            int NEW_ = 6;
            int NEW_ = 7;*/

        }
    }

    public static class GCMPushNotificationStub {
        static int currentCount = 1;

        public static void startSendingPush(final Context context, final int periodSec, final int number) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendPush(periodSec, context, number);
                }
            }).start();
        }

        private static void sendPush(int periodSec, Context context, int number) {
            try {
                Thread.sleep(periodSec * 1000);
                NotificationItem item = new NotificationItem();
                item.setId(100 + currentCount);
                item.setDate(14242345);
                item.setText("Тестируем : ) , Push № " + currentCount);
                item.setType(1);
                item.setAdId(currentCount);
                item.setUserId(currentCount);
                ResponseHolder<NotificationItem> push = new ResponseHolder<NotificationItem>();
                push.setStatusCode(0);
                push.setData(item);

                context.startService(new Intent(context, GcmIntentService.class)
                        .setAction(GcmIntentService.Config.RECEIVE_MESSAGE)
                        .putExtras(new BundleBuilder()
                                        .putString("message", new Gson().toJson(push))
                                        .build()
                        ));
                currentCount++;
                if (currentCount == number)
                    Thread.currentThread().interrupt();
                else
                    sendPush(periodSec, context, number);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}