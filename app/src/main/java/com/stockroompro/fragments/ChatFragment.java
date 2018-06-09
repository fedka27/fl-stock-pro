package com.stockroompro.fragments;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.ResponseItemHolder;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.core.views.ArtJokerEditText;
import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.tool.core.Crypto;
import com.artjoker.tool.core.Network;
import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.core.TextUtils;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stockroompro.R;
import com.stockroompro.adapters.ChatMessageAdapter;
import com.stockroompro.api.models.requests.GetChatRequest;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.api.models.requests.SellerUserDataRequest;
import com.stockroompro.api.models.requests.SendMessageRequest;
import com.stockroompro.fragments.base.BaseApplicationPaginationListFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.Message;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.Settings;
import com.stockroompro.models.columns.AdvertisementColumns;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.contracts.ChatMessageContract;
import com.stockroompro.models.contracts.MessageByAdvertContract;
import com.stockroompro.models.contracts.PhotosContract;
import com.stockroompro.models.converters.ChatMessageCursorConverter;
import com.stockroompro.models.converters.ChatMessagesContentValuesConverter;
import com.stockroompro.services.GcmIntentService;
import com.stockroompro.utils.ChatConfig;

import java.util.List;
import java.util.UUID;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.AD_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.QUERY;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.RECIPIENT_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TEXT;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.USER_ID;
import static com.stockroompro.broadcasts.GcmBroadcastReceiver.NotificationHelper;
import static com.stockroompro.broadcasts.GcmBroadcastReceiver.NotificationHelper.UPDATE_CHAT_ACTION;


public class ChatFragment extends BaseApplicationPaginationListFragment implements View.OnClickListener, ChatMessageAdapter.OnMessageClickListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks, BaseRequest.UIResponseCallback {

    public static final int FROM_LIST_CHATS = 1;
    public static final String FROM_LIST_CHATS_KEY = "from_list";
    private static final String ADVERTISEMENT_ID = "advert_id";
    private static final String NO_PRICE = "0.00";

    private Bundle bundle;
    private ArtJokerEditText textField;
    private ArtJokerTextView priceTv;
    private ArtJokerTextView titleTv;
    private ArtJokerTextView nameRecipient;
    private ArtJokerButton buttonSend;
    private ArtJokerButton buttonDeleteMessage;
    private ArtJokerButton buttonRetrySending;
    private LinearLayout retryDialog;
    private FrameLayout advertNoActiveContainer;
    private RelativeLayout advertInfoContainer;
    private ImageButton buttonSenderProfile;
    private SimpleDraweeView photoIv;
    private long addId;
    private long myId;
    private List<Message> checkedMessages;
    private ChatMessageAdapter adapter;
    private Bundle chatNetworkBundle;

    public static ChatFragment newInstance(Bundle bundle) {
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initFields();
    }

    private void initFields() {
        bundle = getArguments();
        myId = PersonalData.getInstance(getActivity().getApplicationContext()).getUserId();
        addId = bundle.getLong(MessageByAdvertContract.AD_ID);
        adapter = new ChatMessageAdapter(getActivity(), null, ChatMessageCursorConverter.class, this);
    }

    @Override
    protected int getAbstractListId() {
        return R.id.lv_chat;
    }

    @Override
    protected void initAdapter(AbsListView absListView) {
        //getAbstractListView().setAdapter(adapter);
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        commonBundleBuilder.putLong(RequestParams.ParamNames.USER_ID, bundle.getLong(MessageByAdvertContract.RECIPIENT_ID));
        manager.initLoader(LoadersId.NETWORK_USER_DATA_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
        manager.initLoader(LoadersId.DB_CHAT_MESSAGE_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();

        manager.initLoader(LoadersId.DB_ADVERTISEMENT_DETAILS_LOADER_ID, commonBundleBuilder.putLong(ADVERTISEMENT_ID, addId).build(), this).forceLoad();
        chatNetworkBundle = commonBundleBuilder
                .putLong(AD_ID, addId)
                .putString(TOKEN, PersonalData.getInstance(getActivity()).getToken())
                .putLong(USER_ID, bundle.getLong(MessageByAdvertContract.RECIPIENT_ID))
                .putString(QUERY, "")
                .putLong(DATE, 0)
                .build();
        manager.initLoader(LoadersId.NETWORK_CHAT_MESSAGE_LOADER_ID, chatNetworkBundle, this).forceLoad();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initViews(View view) {
        GcmIntentService.cancelNotifications(getActivity());
        textField = (ArtJokerEditText) view.findViewById(R.id.et_message_field);
        buttonSend = (ArtJokerButton) view.findViewById(R.id.button_send);
        buttonSenderProfile = (ImageButton) view.findViewById(R.id.iv_person_icon);
        nameRecipient = ((ArtJokerTextView) view.findViewById(R.id.tv_chat_name));
        nameRecipient.setText(bundle.getString(MessageByAdvertContract.SENDER_NAME));
        priceTv = (ArtJokerTextView) view.findViewById(R.id.tv_advert_header_price);
        titleTv = (ArtJokerTextView) view.findViewById(R.id.tv_advert_header_title);
        buttonRetrySending = (ArtJokerButton) view.findViewById(R.id.button_retry_sending);
        buttonDeleteMessage = (ArtJokerButton) view.findViewById(R.id.delete_unsend_message);
        photoIv = (SimpleDraweeView) view.findViewById(R.id.iv_advert_header_image);
        advertInfoContainer = (RelativeLayout) view.findViewById(R.id.chat_advert_info);
        advertNoActiveContainer = (FrameLayout) view.findViewById(R.id.fragment_chat_advert_no_active);
        retryDialog = (LinearLayout) view.findViewById(R.id.retry_dialog);
        retryDialog.setVisibility(View.GONE);
        getAbstractListView().setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }

    private String[] buildSelectionArgsDBMessages(long id, long addId) {
        return new String[]{
                String.valueOf(addId),
                String.valueOf(id),
                String.valueOf(id)
        };
    }

    @Override
    public Loader onCreateLoader(int id, final Bundle args) {
        switch (id) {
            case LoadersId.DB_CHAT_MESSAGE_LOADER_ID:
                return new CursorLoader(getActivity(), ChatMessageContract.CONTENT_URI, null,
                        ChatMessageContract.AD_ID + " = ? AND (" + ChatMessageContract.RECIPIENT_ID + " = ? OR " + ChatMessageContract.SENDER_ID + " = ? )",
                        buildSelectionArgsDBMessages(bundle.getLong(MessageByAdvertContract.RECIPIENT_ID), addId), ChatMessageContract.DATE);

            case LoadersId.DB_ADVERTISEMENT_DETAILS_LOADER_ID:
                return new CursorLoader(getActivity(), AdvertisementContract.CONTENT_URI, null, Config.ADVERT_SELECTION,
                        buildSelectionArgs(args.getLong(ADVERTISEMENT_ID, addId)), null);

            case LoadersId.DB_PHOTO_LOADER_ID:
                return new CursorLoader(getActivity(), PhotosContract.CONTENT_URI, Config.PHOTO_PROJECTION, Config.PHOTO_SELECTION,
                        buildSelectionArgs(args.getLong(ADVERTISEMENT_ID, addId)), null);

            case LoadersId.NETWORK_SEND_MESSAGE_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new SendMessageRequest(getActivity(), args));

            case LoadersId.NETWORK_CHAT_MESSAGE_LOADER_ID:
                args.putLong(ChatMessageContract.RECIPIENT_ID, bundle.getLong(MessageByAdvertContract.RECIPIENT_ID));
                args.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
                return new LocalRequestDescriptorLoader(getActivity(), new GetChatRequest(getActivity(), ChatMessageContract.CONTENT_URI,
                        ChatMessagesContentValuesConverter.class, args).setUiCallback(this));

            case LoadersId.DB_INSERT_CHAT_MESSAGE_TO_DB_LOADER_ID:
                return new AsyncTaskLoader<Boolean>(getActivity()) {
                    @Override
                    public Boolean loadInBackground() {
                        ContentResolver cr = getActivity().getContentResolver();

                        ContentValues cv = new ContentValues();
                        cv.put(ChatMessageContract.ID, 0);
                        cv.put(ChatMessageContract.UNSEND, 1);
                        cv.put(ChatMessageContract.TEMP_ID_FOR_UNSENDED, generateTempId(args.getString(ChatMessageContract.MESSAGE_TEXT)));
                        cv.put(ChatMessageContract.DATE, SystemHelper.getInstance().getTimeInSecFromMillis(System.currentTimeMillis()));
                        cv.put(ChatMessageContract.MESSAGE_TEXT, args.getString(ChatMessageContract.MESSAGE_TEXT));
                        cv.put(ChatMessageContract.SENDING_STATUS, ChatConfig.SEND_STATUS_FAIL);
                        cv.put(ChatMessageContract.SENDER_ID, myId);
                        cv.put(ChatMessageContract.AD_ID, addId);
                        cv.put(ChatMessageContract.RECIPIENT_ID, bundle.getLong(MessageByAdvertContract.RECIPIENT_ID));
                        cr.insert(ChatMessageContract.CONTENT_URI, cv);
                        return true;
                    }
                };
            case LoadersId.NETWORK_USER_DATA_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new SellerUserDataRequest(getActivity(), args).setUiCallback(new BaseRequest.UIResponseCallback() {
                    @Override
                    public void uiDataResponse(ResponseHolder data) {
                        final com.stockroompro.models.UserData userData = (com.stockroompro.models.UserData) data.getData();
                        if (userData != null)
                            nameRecipient.setText(String.format("%s %s", userData.getFirstName(), userData.getLastName()));
                    }
                }));
        }
        return null;
    }

    private String generateTempId(String s) {
        UUID uid = UUID.randomUUID();
        return Crypto.md5(uid.getMostSignificantBits() + uid.getLeastSignificantBits() + s);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(messageBroadcastReceiver);
        NotificationHelper.setShowNewMessageNotification(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_CHAT_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageBroadcastReceiver, new IntentFilter(filter));
        NotificationHelper.setShowNewMessageNotification(false);
        textField.clearFocus();
        textField.setSelected(false);
        buttonSend.requestFocus();
        hideKeyBoard();
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LoadersId.DB_CHAT_MESSAGE_LOADER_ID:
                Cursor cursor = ((Cursor) data);
                if (cursor.moveToFirst()) {
                    if (getAbstractListView().getAdapter() != null) {
                        ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor(cursor);
                    } else {
                        adapter = new ChatMessageAdapter(getActivity(), cursor, ChatMessageCursorConverter.class, this);
                        adapter.setOnMessageClickListener(this);
                        getAbstractListView().setAdapter(adapter);
                    }
                }
                break;

            case LoadersId.DB_ADVERTISEMENT_DETAILS_LOADER_ID:
                Cursor detailsAdvertCursor = ((Cursor) data);
                if (detailsAdvertCursor.moveToFirst()) {
                    if (titleTv.getText().toString().isEmpty())
                        titleTv.setText(detailsAdvertCursor.getString(detailsAdvertCursor.getColumnIndex(AdvertisementColumns.TITLE)));
                }
                break;

            case LoadersId.DB_PHOTO_LOADER_ID:
                Cursor detailsAdvertPhotoCursor = ((Cursor) data);
                if (detailsAdvertPhotoCursor.moveToFirst()) {
                    photoIv.setImageURI(detailsAdvertPhotoCursor.getString(detailsAdvertPhotoCursor.getColumnIndex(PhotosContract.PHOTO_URL)));
                }
                break;

            case LoadersId.DB_INSERT_CHAT_MESSAGE_TO_DB_LOADER_ID:
                getLoaderManager().restartLoader(LoadersId.DB_CHAT_MESSAGE_LOADER_ID, null, this).forceLoad();
                break;

            case LoadersId.NETWORK_SEND_MESSAGE_LOADER_ID:
                startUpdateChatLoader();
                getAbstractListView().setSelection(getAbstractListView().getCount() - 1);
                break;
        }
    }

    private void startUpdateChatLoader() {
        Bundle bndl = new CommonBundleBuilder()
                .putLong(AD_ID, addId)
                .putString(TOKEN, PersonalData.getInstance(getActivity()).getToken())
                .putLong(USER_ID, bundle.getLong(MessageByAdvertContract.RECIPIENT_ID))
                .putString(QUERY, "")
                .putLong(DATE, 0)
                .build();

        getLoaderManager().restartLoader(LoadersId.NETWORK_CHAT_MESSAGE_LOADER_ID, bndl, this).forceLoad();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        switch (loader.getId()) {
            case LoadersId.DB_CHAT_MESSAGE_LOADER_ID:
                if (getAbstractListView().getAdapter() != null) {
                    ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor(null);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send:
                if (!android.text.TextUtils.isEmpty(textField.getText().toString())) {
                    sendMessage(textField.getText().toString());
                    textField.setText("");
                    hideKeyBoard();
                }
                break;
            case R.id.iv_person_icon:
                goToPersonProfile();
                break;
            case R.id.chat_advert_info:
                goToAdvert();
                break;
            case R.id.button_retry_sending:
                retrySending();
                retryDialog.setVisibility(View.GONE);
                break;
            case R.id.delete_unsend_message:
                deleteMessageFromDB();
                retryDialog.setVisibility(View.GONE);
                break;
        }
    }

    private void goToAdvert() {
//        if (bundle.getInt(FROM_LIST_CHATS_KEY) == FROM_LIST_CHATS) {
        commit(AdvertDetailFragment.newInstance(addId, PersonalData.getInstance(getActivity()).getUserId()),
                AdvertDetailFragment.class.getSimpleName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
//        } else {
//            popBack();
//        }
    }

    private void goToPersonProfile() {
        commit(SellerProfileFragment.newInstance(bundle.getLong(MessageByAdvertContract.RECIPIENT_ID)),
                SellerProfileFragment.class.getCanonicalName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
    }

    private void sendMessage(String s) {
        if (Network.getInstance().isConnected(getActivity().getApplicationContext())) {
            sendMessageToServer(s, bundle.getLong(MessageByAdvertContract.RECIPIENT_ID), addId);
        } else {
            insertMessageToDB(s);
        }
    }

    private void sendMessageToServer(String text, long recipientId, long addId) {
        Bundle messageBundle = new CommonBundleBuilder()
                .putLong(AD_ID, addId)
                .putString(TOKEN, PersonalData.getInstance(getActivity()).getToken())
                .putString(TEXT, text)
                .putLong(RequestParams.ParamNames.RECIPIENT_ID, recipientId)
                .build();
        getLoaderManager().restartLoader(LoadersId.NETWORK_SEND_MESSAGE_LOADER_ID, messageBundle, this).forceLoad();
    }

    @Override
    protected void initListeners(View view) {
        getAbstractListView().setOnItemClickListener(this);
        buttonSend.setOnClickListener(this);
        buttonSenderProfile.setOnClickListener(this);
        view.findViewById(R.id.chat_advert_info).setOnClickListener(this);
        buttonRetrySending.setOnClickListener(this);
        buttonDeleteMessage.setOnClickListener(this);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }

    private void insertMessageToDB(String s) {
        Bundle messBundle = new CommonBundleBuilder()
                .putString(ChatMessageContract.MESSAGE_TEXT, s)
                .build();

        getLoaderManager().restartLoader(LoadersId.DB_INSERT_CHAT_MESSAGE_TO_DB_LOADER_ID, messBundle, this).forceLoad();

        textField.getText().clear();
        hideKeyBoard();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    public String[] buildSelectionArgs(long id) {
        return new String[]{
                String.valueOf(id),
        };
    }

    @Override
    public void uiDataResponse(ResponseHolder data) {
        ResponseItemHolder<Message> response = (ResponseItemHolder<Message>) data.getData();
        priceTv.bringToFront();
        if (response != null) {
            deleteMessageFromDB();
            if (response.getAdActive() == 1) {
                advertNoActiveContainer.setVisibility(View.GONE);
                advertInfoContainer.setVisibility(View.VISIBLE);
                if (response.getPrice() != null && !response.getPrice().equals(NO_PRICE)) {
                    priceTv.setText(String.format("%s %s", TextUtils.getInstance().getStringFromDoubleValue(Double.valueOf(response.getPrice())),
                            Settings.getInstance(getActivity()).getCurrencyById((int) response.getCurrency()).getName()));
                } else {
                    priceTv.setText(getResources().getString(R.string.advert_price_type_free));
                }
            } else {
                advertInfoContainer.setVisibility(View.GONE);
                advertNoActiveContainer.setVisibility(View.VISIBLE);
            }
            titleTv.setText(response.getTitle());
            photoIv.setImageURI(response.getPhoto());
        }
    }

    @Override
    public void onMessageClick(List<Message> messages, int currentClickPosition, boolean isPressed) {
        checkedMessages = messages;
        setVisibleRetryDialog(isPressed);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem <= visibleItemCount && lastTotalItemCount != totalItemCount) {
            lastTotalItemCount = totalItemCount;
            loadPageWithOffset(totalItemCount, visibleItemCount * 2, visibleItemCount * 2 + totalItemCount);
            restartLoader(getLoaderManager(), new CommonBundleBuilder(chatNetworkBundle).putIntOffset(totalItemCount)
                    .putIntLimit(totalItemCount + visibleItemCount * 2)
                    .putIntCount(visibleItemCount * 2));
        }
    }

    @Override
    protected void restartLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        manager.restartLoader(LoadersId.NETWORK_CHAT_MESSAGE_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
        manager.restartLoader(LoadersId.DB_CHAT_MESSAGE_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
    }

    @Override
    protected void loadPageWithOffset(int offset, int count, int limit) {

    }

    private interface Config {
        String ADVERT_SELECTION = AdvertisementContract.ID + "=?";
        String[] PHOTO_PROJECTION = new String[]{PhotosContract.PHOTO_URL};
        String PHOTO_SELECTION = PhotosContract.ADVERTISEMENT_ID + "=?";
    }

    private void setVisibleRetryDialog(boolean isVisible) {
        if (isVisible) {
            retryDialog.setVisibility(View.VISIBLE);
        } else {
            retryDialog.setVisibility(View.GONE);
        }
    }

    private void deleteMessageFromDB() {
        if (checkedMessages != null) {
            ContentResolver cr = getActivity().getContentResolver();
            for (Message message : checkedMessages) {
                cr.delete(ChatMessageContract.CONTENT_URI, ChatMessageContract.ID + " = ? AND " + ChatMessageContract.TEMP_ID_FOR_UNSENDED + " = ? "
                        , new String[]{String.valueOf(message.getId()), message.getTempId()});
            }
        }
    }

    private void retrySending() {
        if (Network.getInstance().isConnected(getActivity().getApplicationContext())) {
            ChatMessageAdapter adapter = ((ChatMessageAdapter) getAbstractListView().getAdapter());
            adapter.setSelectedView(adapter.getSelectedPosition(), false);
            sendMessageToServer(checkedMessages.get(0).getText(), bundle.getLong(MessageByAdvertContract.RECIPIENT_ID), addId);
        }
    }

    private BroadcastReceiver messageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UPDATE_CHAT_ACTION)) {
                if (intent.getLongExtra(RECIPIENT_ID, 0) == bundle.getLong(MessageByAdvertContract.RECIPIENT_ID)) {
                    NotificationHelper.setShowNewMessageNotification(false);
                    startUpdateChatLoader();
                } else {
                    NotificationHelper.setShowNewMessageNotification(true);
                }
            }
        }
    };
}