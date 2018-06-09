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
import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.stockroompro.R;
import com.stockroompro.adapters.MessagesByUserAdapter;
import com.stockroompro.api.models.requests.MessagesByUserRequest;
import com.stockroompro.fragments.base.BaseApplicationPaginationListFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.loaders.SearchLoaderCallback;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.columns.MessageByUserColumns;
import com.stockroompro.models.contracts.MessageByAdvertContract;
import com.stockroompro.models.contracts.MessageByUserContract;
import com.stockroompro.models.converters.MessageByUserCursorConverter;
import com.stockroompro.utils.ContentProviderConfig;

import static com.stockroompro.api.models.requests.RequestParams.ParamNames.DATE;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.QUERY;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.TOKEN;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.UID;


public class MessagesByUserFragment extends BaseApplicationPaginationListFragment implements SearchView.OnQueryTextListener, View.OnClickListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks {

    public static final String DEFAULT_QUERY = "";
    private String query;
    protected ArtJokerTextView noChatsHint;
    protected ArtJokerSearchView messageSearchView;
    private long myId;
    private MessagesByUserAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        adapter = new MessagesByUserAdapter(getActivity(), null, MessageByUserCursorConverter.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        getParent().setMainTitle(getString(R.string.menu_message_title));
    }

    @Override
    protected void initContent() {
        String searchText = getResources().getString(R.string.messages_search_user);
        messageSearchView.setQueryHint(searchText);
        noChatsHint.setVisibility(View.GONE);
        myId = PersonalData.getInstance(getActivity().getApplicationContext()).getUserId();
    }

    @Override
    protected void initListeners(View view) {
        getAbstractListView().setOnItemClickListener(this);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        messageSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        messageSearchView.setOnQueryTextListener(this);
    }

    @Override
    protected int getAbstractListId() {
        return R.id.lv_messages_from_user;
    }

    @Override
    protected void initAdapter(AbsListView absListView) {
        absListView.setAdapter(new MessagesByUserAdapter(getActivity(), null, MessageByUserCursorConverter.class));
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        commonBundleBuilder.putLong(UID, PersonalData.getInstance(getActivity().getApplicationContext()).getUserId());
        commonBundleBuilder.putString(TOKEN, PersonalData.getInstance(getActivity().getApplicationContext()).getToken());
        commonBundleBuilder.putLong(DATE, SystemHelper.getInstance().getTimeInSecFromMillis(System.currentTimeMillis()));
        commonBundleBuilder.putLong(UID, PersonalData.getInstance(getActivity().getApplicationContext()).getUserId());
        commonBundleBuilder.putString(QUERY, DEFAULT_QUERY);
        commonBundleBuilder.putString(TOKEN, PersonalData.getInstance(getActivity()).getToken());
        commonBundleBuilder.putLong(DATE, 0);
        manager.initLoader(LoadersId.NETWORK_MESSAGES_BY_USERS_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
        manager.initLoader(LoadersId.DB_MESSAGE_BY_USER_LOADER_ID, commonBundleBuilder.build(), this).forceLoad();
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_by_user;
    }

    @Override
    protected void initViews(View view) {
        messageSearchView = (ArtJokerSearchView) view.findViewById(R.id.sv_by_messages_in_user);
        noChatsHint = (ArtJokerTextView) view.findViewById(R.id.tv_no_chats);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.NETWORK_MESSAGES_BY_USERS_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new MessagesByUserRequest(getActivity(), args));

            case LoadersId.DB_MESSAGE_BY_USER_LOADER_ID:
                return new CursorLoader(getActivity(), MessageByUserContract.CONTENT_URI, null,
                        buildSelection(), buildSelectionArgs(), MessageByAdvertContract.DATE + " DESC");

            case LoadersId.DB_MESSAGES_SEARCH_LOADER_ID:
                return new CursorLoader(getActivity(),
                        Uri.withAppendedPath(ContentProviderConfig.BASE_CONTENT_URI, MessageByUserContract.SEARCH_USER_URI),
                        null, buildSearchSelection(), buildSearchSelectionArgs(args), null);
        }
        return null;
    }

    private String[] buildSearchSelectionArgs(Bundle args) {
        return new String[]{args.getString(SearchLoaderCallback.QUERY_KEY), String.valueOf(PersonalData.getInstance(getActivity()).getUserId())};
    }

    private String buildSearchSelection() {
        return MessageByUserColumns.INTERLOCATOR_NAME + " = ? AND "  + MessageByUserContract.USER_ID + " = ? ";
    }

    private String[] buildSelectionArgs() {
        return new String[]{String.valueOf(PersonalData.getInstance(getActivity()).getUserId())};
    }

    private String buildSelection() {
        return  MessageByUserContract.USER_ID + " = ? ";
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        switch (loader.getId()) {

            case LoadersId.DB_MESSAGE_BY_USER_LOADER_ID:
                Cursor cursor = (Cursor) data;
                if (getAbstractListView().getAdapter() != null && !((Cursor) data).isClosed()) {
                    ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor((Cursor) data);
                }

                if (!cursor.isClosed())
                    if (!cursor.moveToFirst()) {
                        noChatsHint.setVisibility(View.VISIBLE);
                    } else {
                        noChatsHint.setVisibility(View.GONE);
                    }
                break;
            case LoadersId.DB_MESSAGES_SEARCH_LOADER_ID:

                if (getAbstractListView().getAdapter() != null && !((Cursor) data).isClosed()) {
                    ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor((Cursor) data);
                }
                break;

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        switch (loader.getId()) {
            case LoadersId.DB_MESSAGE_BY_USER_LOADER_ID:
                ((CursorAdapter) getAbstractListView().getAdapter()).changeCursor(null);
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void restartLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        if (query == null)
            manager.restartLoader(LoadersId.DB_MESSAGE_BY_USER_LOADER_ID, commonBundleBuilder.build(), this);
    }

    @Override
    protected void loadPageWithOffset(int offset, int count, int limit) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long recipientId = ((MessagesByUserAdapter) parent.getAdapter()).getItem(position).getObject().getInterlocatorId();
        String userName = ((MessagesByUserAdapter) parent.getAdapter()).getItem(position).getObject().getInterlocatorName();

        commit(MessagesByAdvertFragment.newInstance(recipientId, userName),
                MessagesByAdvertFragment.class.getCanonicalName(),
                AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        startSearchLoader(query);
        return true;
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
}