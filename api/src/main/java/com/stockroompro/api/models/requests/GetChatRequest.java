package com.stockroompro.api.models.requests;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.artjoker.core.BundleBuilder;
import com.artjoker.core.network.ButchUpdateArrayDatabaseRequest;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestWithNotifications;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.ResponseItemHolder;
import com.stockroompro.api.AppServerSpecs;
import com.stockroompro.api.Communicator;
import com.stockroompro.api.R;
import com.stockroompro.loaders.LocalBroadcastRequestProcessor;
import com.stockroompro.models.Message;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.contracts.ChatMessageContract;
import com.stockroompro.models.converters.ChatMessagesContentValuesConverter;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.LIMIT;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.OFFSET;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.USER_ID;

/**
 * Created by user on 27.04.15.
 */
public class GetChatRequest extends ButchUpdateArrayDatabaseRequest<ResponseItemHolder<Message>, ChatMessagesContentValuesConverter> implements RequestWithNotifications {

    public GetChatRequest(Context context, Uri uri, Class<ChatMessagesContentValuesConverter> chatMessagesContentValuesConverterClass, Bundle bundle) {
        super(context, uri, chatMessagesContentValuesConverterClass, bundle);
    }

    private static final int STATUS_READ = 2;
    private static final int STATUS_RECIVED = 1;
    private static final int STATUS_NEW = 0;

    @Override
    public int getNotificationPolicy() {
        return NotificationsPolicy.NOTIFY_IF_SERVER_ERROR_RESPONSE | NotificationsPolicy.NOTIFY_IF_ERROR_PROCESSING| NotificationsPolicy.SHOW_INTERNET_CONNECTION_LOST;
    }

    @Override
    public void processResponse(ResponseHolder<ResponseItemHolder<Message>> responseHolder) throws Exception {
        super.processResponse(responseHolder);
        long adId = responseHolder.getData().getAdId();

        if (responseHolder.getStatusCode() == 0) {
//            ContentResolver cr = deleteUnsendedMesseges();
            ContentResolver cr = getContext().getContentResolver();
            String token = PersonalData.getInstance(getContext()).getToken();

            Cursor cursor = cr.query(ChatMessageContract.CONTENT_URI, null, buildWhereForUnreadMessages(), buildSelectionForUnreadMessages(), null);
            if (!cursor.isClosed()) {
                if (cursor.moveToFirst())
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                        Bundle bundle = new BundleBuilder()
                                .putString(RequestParams.ParamNames.TOKEN, token)
                                .putInt(RequestParams.ParamNames.STATUS, STATUS_READ)
                                .putLong(RequestParams.ParamNames.MESSAGE_ID, cursor.getLong(cursor.getColumnIndex(ChatMessageContract.ID)))
                                .build();
                        new LocalBroadcastRequestProcessor(getContext()).process(new ChangeMessageStatusRequest(getContext(), bundle));
                    }
                cursor.close();
            }


        }

    }

    private ContentResolver deleteUnsendedMesseges() {
        ContentResolver cr = getContext().getContentResolver();
        cr.delete(ChatMessageContract.CONTENT_URI, buildWhereForUnsendedMessages(), buildSelectionForUnsendedMessages());
        return cr;
    }

    private String[] buildSelectionForUnreadMessages() {
        return new String[]{String.valueOf(STATUS_RECIVED), String.valueOf(STATUS_NEW), String.valueOf(getBundle().getLong(USER_ID)),
                String.valueOf(PersonalData.getInstance(getContext()).getUserId()),
                String.valueOf(getBundle().getLong(AD_ID))};
    }

    private String buildWhereForUnreadMessages() {

        return "(" + ChatMessageContract.SENDING_STATUS + " = ? OR " + ChatMessageContract.SENDING_STATUS + " = ? ) AND " + ChatMessageContract.SENDER_ID + " = ? AND " +
                ChatMessageContract.RECIPIENT_ID + " = ? AND " + ChatMessageContract.AD_ID + " = ? ";
    }

    private String[] buildSelectionForUnsendedMessages() {
        return new String[]{String.valueOf(getBundle().getLong(ChatMessageContract.RECIPIENT_ID))};
    }

    @Override
    public ResponseHolder<ResponseItemHolder<Message>> makeRequest(long date) throws Exception {

        return Communicator.getAppServer().getChat(getBundle().getLong(AD_ID), new RequestParams()
                .addParameter(TOKEN, getBundle().getString(TOKEN))
                .addParameter(USER_ID, getBundle().getLong(USER_ID))
                .addParameter(DATE, date)
                .addParameter(LIMIT, getBundle().getInt(LIMIT))
                .addParameter(OFFSET, getBundle().getInt(OFFSET))
                .buildJSONWithPathParams(AppServerSpecs.CHAT_PATH, AppServerSpecs.ADVERTISEMENT_ID, getBundle().getLong(AD_ID)));
    }

    @Override
    public int getRequestName() {
        return R.string.request_chat;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    private String buildWhereForUnsendedMessages() {
        return ChatMessageContract.UNSEND + " = 1 AND " + ChatMessageContract.RECIPIENT_ID + " = ? ";
    }
}

