package com.stockroompro.fragments;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.SearchView;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.views.ArtJokerSearchView;
import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.database.SecureDatabaseProvider;
import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.stockroompro.R;
import com.stockroompro.adapters.MessageByAdvertAdapter;
import com.stockroompro.api.models.requests.MessagesByAdvertRequest;
import com.stockroompro.fragments.base.BaseApplicationPaginationListFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.loaders.SearchLoaderCallback;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.columns.MessageByAdvertColumns;
import com.stockroompro.models.contracts.MessageByAdvertContract;
import com.stockroompro.models.contracts.MessageByUserContract;
import com.stockroompro.models.converters.MessageByAdvertCursorConverter;
import com.stockroompro.utils.ContentProviderConfig;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.INTERLOCUTOR_UID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.QUERY;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;

/**
 * Created by user on 14.04.15.
 */
public class MessagesByAdvertFragment extends BaseApplicationPaginationListFragment implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks {

    private long userId;
    private String userName;
    private String query;
    private long myId;
    protected ArtJokerTextView noChatsHint;
    public static final String DEFAULT_QUERY = "";
    protected ArtJokerSearchView messageSearchView;
    private static final String USER_ID_KEY = "user_id";
    private static final String USER_NAME_KEY = "user_name";
    private MessageByAdvertAdapter adapter;

    public static MessagesByAdvertFragment newInstance(long userId, String userName) {

        MessagesByAdvertFragment fragment = new MessagesByAdvertFragment();
        Bundle args = new Bundle();
        args.putLong(USER_ID_KEY, userId);
        args.putString(USER_NAME_KEY, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        userId = getArguments().getLong(USER_ID_KEY);
        userName = getArguments().getString(USER_NAME_KEY);
        myId = PersonalData.getInstance(getActivity()).getUserId();
        adapter = new MessageByAdvertAdapter(getActivity(), null, MessageByAdvertCursorConverter.class);
    }

    @Override
    protected void restartLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        if (query == null)
            manager.restartLoader(LoadersId.DB_MESSAGE_BY_ADVERT_LOADER_ID, commonBundleBuilder.build(), this);
    }

    @Override
    protected int getAbstractListId() {
        return R.id.lv_messages_by_advert;
    }

    @Override
    protected void loadPageWithOffset(int offset, int count, int limit) {

    }

    @Override
    protected void initAdapter(AbsListView absListView) {
        absListView.setAdapter(adapter);
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        commonBundleBuilder.putLong(UID, myId);
        commonBundleBuilder.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
        commonBundleBuilder.putLong(DATE, SystemHelper.getInstance().getTimeInSecFromMillis(System.currentTimeMillis()));
        commonBundleBuilder.putString(QUERY, DEFAULT_QUERY);
        commonBundleBuilder.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
        commonBundleBuilder.putLong(INTERLOCUTOR_UID, userId);
        commonBundleBuilder.putLong(DATE, 0);
        manager.restartLoader(LoadersId.NETWORK_MESSAGES_BY_ADVERT_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
        manager.restartLoader(LoadersId.DB_MESSAGE_BY_ADVERT_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }

    @Override
    protected void initListeners(View view) {

        getAbstractListView().setOnItemClickListener(this);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        messageSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        messageSearchView.setOnQueryTextListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_by_advert;
    }

    @Override
    protected void initViews(View view) {
        noChatsHint = (ArtJokerTextView) view.findViewById(R.id.tv_no_chats);
        messageSearchView = (ArtJokerSearchView) view.findViewById(R.id.sv_by_messages);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.NETWORK_MESSAGES_BY_ADVERT_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new MessagesByAdvertRequest(getActivity(), args));

            case LoadersId.DB_MESSAGE_BY_ADVERT_LOADER_ID:
                return new CursorLoader(getActivity(), MessageByAdvertContract.CONTENT_URI, null,
                        MessageByAdvertContract.INTERLOCATOR_ID + " = ? AND " + MessageByAdvertContract.USER_ID + " = ? ",
                        new String[]{String.valueOf(userId), String.valueOf(myId)}, MessageByAdvertContract.DATE + " DESC");

            case LoadersId.DB_MESSAGES_SEARCH_LOADER_ID:
                return new CursorLoader(getActivity(),
                        Uri.withAppendedPath(ContentProviderConfig.BASE_CONTENT_URI, MessageByAdvertContract.SEARCH_MESSAGES_URI), null, buildSelection(),
                        buildSelectionArgs(args), null);
        }
        return null;
    }

    private String[] buildSelectionArgs(Bundle args) {
        return new String[]{args.getString(SearchLoaderCallback.QUERY_KEY), String.valueOf(userId), String.valueOf(myId)};
    }

    private String buildSelection() {
        return MessageByAdvertColumns.INTERLOCATOR_ID + " = ? AND " +
                MessageByAdvertColumns.USER_ID + " = ? ";
    }

    @Override
    protected void initContent() {
        noChatsHint.setVisibility(View.GONE);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LoadersId.DB_MESSAGE_BY_ADVERT_LOADER_ID:
                if (getAbstractListView().getAdapter() != null && !((Cursor) data).isClosed()) {
                    SecureDatabaseProvider.printLogs(true);
                    SecureDatabaseProvider.logCursor((Cursor) data);
                    ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor((Cursor) data);
                }
                Cursor cursor = (Cursor) data;
                if (!cursor.isClosed())
                    if (!cursor.moveToFirst()) {
                        noChatsHint.setVisibility(View.VISIBLE);
                    } else {
                        noChatsHint.setVisibility(View.GONE);
                    }
                break;

            case LoadersId.DB_MESSAGES_SEARCH_LOADER_ID:
                if (getAbstractListView().getAdapter() != null) {
                    if (!((Cursor) data).isClosed())
                        ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor((Cursor) data);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        switch (loader.getId()) {
            case LoadersId.DB_MESSAGE_BY_ADVERT_LOADER_ID:
                ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor(null);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long recipientId = ((MessageByAdvertAdapter) parent.getAdapter()).getItem(position).getObject().getInterlocatorId();

        Bundle bundle = new CommonBundleBuilder()
                .putLong(MessageByAdvertContract.AD_ID, ((MessageByAdvertAdapter) parent.getAdapter()).getItem(position).getAvertId())
                .putString(MessageByAdvertContract.SENDER_NAME, userName)
                .putLong(MessageByAdvertContract.RECIPIENT_ID, recipientId)
                .putInt(ChatFragment.FROM_LIST_CHATS_KEY, ChatFragment.FROM_LIST_CHATS)

                .build();

        commit(ChatFragment.newInstance(bundle), ChatFragment.class.getCanonicalName(),
                AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
    }

    private void startSearchLoader(String query) {
        this.query = query;
        Bundle bundle = new CommonBundleBuilder()
                .putString(SearchLoaderCallback.QUERY_KEY, query)
                .build();
        if (getLoaderManager().getLoader(LoadersId.DB_MESSAGES_SEARCH_LOADER_ID) != null)
            getLoaderManager().restartLoader(LoadersId.DB_MESSAGES_SEARCH_LOADER_ID, bundle, this).forceLoad();
        else
            getLoaderManager().initLoader(LoadersId.DB_MESSAGES_SEARCH_LOADER_ID, bundle, this).forceLoad();

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        startSearchLoader(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        startSearchLoader(query);
        return true;
    }
}
