package com.stockroompro.fragments;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.RadioGroup;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.stockroompro.R;
import com.stockroompro.adapters.NotificationAdapter;
import com.stockroompro.api.models.requests.NotificationRequest;
import com.stockroompro.fragments.base.BaseApplicationListFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.NotificationItem;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.contracts.MessageByAdvertContract;
import com.stockroompro.models.contracts.NotificationContract;
import com.stockroompro.models.converters.NotificationCursorConverter;
import com.stockroompro.services.GcmIntentService;

import static com.artjoker.tool.fragments.collections.AnimationCollectionFactory.Type.SLIDE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.fragments.NotificationFragment.NotificationDataTypes.COMMUNICATION;
import static com.stockroompro.fragments.NotificationFragment.NotificationDataTypes.IMPORTANT;
import static com.stockroompro.fragments.NotificationFragment.NotificationDataTypes.RATES;
import static com.stockroompro.loaders.LoadersId.DB_NOTIFICATION_LOADER;
import static com.stockroompro.loaders.LoadersId.NETWORK_GCM_LOADER;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.ADVERT_COMMITED;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.ADVERT_DISMISSED;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.COMMENT_DISLIKE;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.COMMENT_FOR_MY_ADVERT;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.COMMENT_LIKE;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.GREETING;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.NEW_MESSAGE;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.RATING;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.RESPONSE_ON_COMMENT;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.SYSTEM_INFO;


/**
 * Created by user on 15.05.15.
 */
public class NotificationFragment extends BaseApplicationListFragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    public static final String SELECTION_KEY = "selection";
    public static final String SELECTION_ARGS_KEY = "selection_args";
    public static final String EMPTY_NAME = "";
    private NotificationAdapter adapter;
    protected ArtJokerTextView noHint;
    private RadioGroup radioGroup;
    private static int TAB_TYPE;

    public static NotificationFragment newInstance(int type) {
        NotificationFragment fragment = new NotificationFragment();
        TAB_TYPE = type;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getParent().setMainTitle(getString(R.string.notification_title));
    }

    @Override
    protected void initAdapter(AbsListView absListView) {
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        String token = PersonalData.getInstance(getActivity()).getToken();
        commonBundleBuilder.putString(TOKEN, token);
        manager.initLoader(NETWORK_GCM_LOADER, commonBundleBuilder.build(), this).forceLoad();
    }

    @Override
    protected int getAbstractListId() {
        return R.id.lv_notifications;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notification;
    }


    @Override

    protected void initViews(View view) {
        GcmIntentService.cancelNotifications(getActivity());
        radioGroup = (RadioGroup) view.findViewById(R.id.notificationRadioGroup);
        noHint = (ArtJokerTextView) view.findViewById(R.id.tv_no_chats);
        radioGroup.setOnCheckedChangeListener(this);
        switch (TAB_TYPE) {
            case NotificationDataTypes.IMPORTANT:
                radioGroup.check(R.id.button_important);
                changeListData(IMPORTANT);
                break;
            default:
                radioGroup.check(R.id.button_communication);
                changeListData(COMMUNICATION);
                break;
        }
    }


    @Override
    protected int getHeaderIconsPolicy() {
        return 0;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case NETWORK_GCM_LOADER:
                return new LocalRequestDescriptorLoader(getActivity(), new NotificationRequest(getActivity(), args));

            case DB_NOTIFICATION_LOADER:
                return new CursorLoader(getActivity(), NotificationContract.CONTENT_URI, null,
                        args.getString(SELECTION_KEY), args.getStringArray(SELECTION_ARGS_KEY), NotificationContract.DATE + " DESC");
        }
        return null;
    }

    private String[] buildSelectionArgsImportant() {
        return new String[]{String.valueOf((System.currentTimeMillis() / 1000) - ((DateUtils.WEEK_IN_MILLIS * 2) / 1000)),
                String.valueOf(GREETING),
                String.valueOf(ADVERT_COMMITED),
                String.valueOf(ADVERT_DISMISSED),
                String.valueOf(SYSTEM_INFO),

        };
    }

    private String buildSelectionImportant() {
        return NotificationContract.DATE + " > ? AND ( "
                + NotificationContract.TYPE + " = ? OR "
                + NotificationContract.TYPE + " = ? OR "
                + NotificationContract.TYPE + " = ? OR "
                + NotificationContract.TYPE + " = ? )";
    }

    private String[] buildSelectionArgsCommunication() {
        return new String[]{String.valueOf((System.currentTimeMillis() / 1000) - ((DateUtils.WEEK_IN_MILLIS * 2) / 1000)),
                String.valueOf(COMMENT_FOR_MY_ADVERT),
                String.valueOf(RESPONSE_ON_COMMENT),
                String.valueOf(NEW_MESSAGE),
        };
    }

    private String buildSelectionCommunication() {
        return NotificationContract.DATE + " > ? AND ( "
                + NotificationContract.TYPE + " = ? OR "
                + NotificationContract.TYPE + " = ? OR "
                + NotificationContract.TYPE + " = ? )";
    }

    private String[] buildSelectionArgsRates() {
        return new String[]{String.valueOf((System.currentTimeMillis() / 1000) - ((DateUtils.WEEK_IN_MILLIS * 2) / 1000)),
                String.valueOf(COMMENT_LIKE),
                String.valueOf(COMMENT_DISLIKE),
                String.valueOf(RATING),
        };
    }

    private String buildSelectionRates() {
        return NotificationContract.DATE + " > ? AND ( "
                + NotificationContract.TYPE + " = ? OR "
                + NotificationContract.TYPE + " = ? OR "
                + NotificationContract.TYPE + " = ? )";
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LoadersId.DB_NOTIFICATION_LOADER:
                setCursor((Cursor) data);
                break;
        }
    }

    private void setCursor(Cursor cursor) {

        if (getAbstractListView().getAdapter() != null) {
            if (!cursor.isClosed())
                ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor(cursor);
        } else {
            if (cursor.moveToFirst() && !cursor.isClosed()) {
                adapter = new NotificationAdapter(getActivity(), cursor, NotificationCursorConverter.class, this);
                getAbstractListView().setAdapter(adapter);
            }
        }
        if (cursor.moveToFirst()) {
            noHint.setVisibility(View.GONE);
        } else {
            noHint.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.button_likes:
                changeListData(RATES);
                break;
            case R.id.button_communication:
                changeListData(COMMUNICATION);
                break;
            case R.id.button_important:
                changeListData(IMPORTANT);
                break;
        }
    }

    private void changeListData(int type) {
        switch (type) {
            case IMPORTANT:
                getLoaderManager().restartLoader(DB_NOTIFICATION_LOADER, new CommonBundleBuilder()
                        .putString(SELECTION_KEY, buildSelectionImportant())
                        .putStringArray(SELECTION_ARGS_KEY, buildSelectionArgsImportant())
                        .build(), this).forceLoad();
                break;
            case RATES:
                getLoaderManager().restartLoader(DB_NOTIFICATION_LOADER, new CommonBundleBuilder()
                        .putString(SELECTION_KEY, buildSelectionRates())
                        .putStringArray(SELECTION_ARGS_KEY, buildSelectionArgsRates())
                        .build(), this).forceLoad();
                break;
            case COMMUNICATION:
                getLoaderManager().restartLoader(DB_NOTIFICATION_LOADER, new CommonBundleBuilder()
                        .putString(SELECTION_KEY, buildSelectionCommunication())
                        .putStringArray(SELECTION_ARGS_KEY, buildSelectionArgsCommunication())
                        .build(), this).forceLoad();
                break;
        }
    }

    @Override
    protected void initListeners(View view) {
        getAbstractListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NotificationItem item = ((NotificationAdapter) getAbstractListView().getAdapter()).getItem(position).getObject();
        switch (item.getType()) {
            case COMMENT_LIKE:
                commit(CommentsFragment.newInstance(item.getAdId()), CommentsFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(SLIDE));
                break;

            case COMMENT_DISLIKE:
                commit(CommentsFragment.newInstance(item.getAdId()), CommentsFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(SLIDE));
                break;

            case RATING:
                commit(SellerProfileFragment.newInstance(item.getUserId()), SellerProfileFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(SLIDE));
                break;
            case GREETING:
                showInfoDialog(item.getText());
                break;
            case ADVERT_COMMITED:
                commit(AdvertDetailFragment.newInstance(item.getAdId(), PersonalData.getInstance(getActivity()).getUserId()), AdvertDetailFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(SLIDE));
                break;

            case ADVERT_DISMISSED:
                commit(AdvertDetailFragment.newInstance(item.getAdId(), PersonalData.getInstance(getActivity()).getUserId()), AdvertDetailFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(SLIDE));
                break;

            case SYSTEM_INFO:
                showInfoDialog(item.getText());
                break;
            case COMMENT_FOR_MY_ADVERT:
                commit(CommentsFragment.newInstance(item.getAdId()), CommentsFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(SLIDE));
                break;

            case RESPONSE_ON_COMMENT:
                commit(CommentsFragment.newInstance(item.getAdId()), CommentsFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(SLIDE));
                break;

            case NEW_MESSAGE:
                Bundle bundle = new CommonBundleBuilder()
                        .putLong(MessageByAdvertContract.AD_ID, item.getAdId())
                        .putString(MessageByAdvertContract.SENDER_NAME, EMPTY_NAME)
                        .putLong(MessageByAdvertContract.RECIPIENT_ID, item.getUserId())
                        .putInt(ChatFragment.FROM_LIST_CHATS_KEY, ChatFragment.FROM_LIST_CHATS)
                        .build();
                commit(ChatFragment.newInstance(bundle), ChatFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(SLIDE));
                break;
        }

    }

    protected void showInfoDialog(String text) {
        new AlertDialog.Builder(getActivity())
                .setMessage(text)
                .create()
                .show();
    }

    @Override
    public void onClick(View v) {
        View convertView = ((View) v.getParent().getParent());
        NotificationAdapter.ViewHolder holder = ((NotificationAdapter.ViewHolder) convertView.getTag());
        NotificationCursorConverter converter = new NotificationCursorConverter();
        converter.setCursor(((CursorAdapter) getAbstractListView().getAdapter()).getCursor());
        commit(SellerProfileFragment.newInstance(converter.getObject(holder.position).getUserId()), SellerProfileFragment.class.getCanonicalName(),
                AnimationCollectionFactory.getInstance().get(SLIDE));


    }


    public interface NotificationDataTypes {
        int IMPORTANT = 1;
        int RATES = 2;
        int COMMUNICATION = 3;
    }
}
