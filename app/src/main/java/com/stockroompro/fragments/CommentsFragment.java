package com.stockroompro.fragments;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.artjoker.core.BundleBuilder;
import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.network.BaseRequest;
import com.artjoker.core.network.ResponseHolder;
import com.artjoker.core.network.ResponseItemHolder;
import com.artjoker.core.network.StatusCode;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.core.views.ArtJokerEditText;
import com.artjoker.core.views.ArtJokerTextView;
import com.stockroompro.Launcher;
import com.stockroompro.R;
import com.stockroompro.adapters.CommentsAdapter;
import com.stockroompro.adapters.CommentsAdapter.CommentActionListener;
import com.stockroompro.api.models.requests.AddCommentLikeRequest;
import com.stockroompro.api.models.requests.AddCommentRequest;
import com.stockroompro.api.models.requests.AdvertCommentsRequest;
import com.stockroompro.api.models.requests.AdvertisementDetailsRequest;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.api.models.responses.user.UserData;
import com.stockroompro.fragments.base.BaseApplicationPaginationListFragment;
import com.stockroompro.loaders.CommentsLoader;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalRequestDescriptorLoader;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.Comment;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.contracts.AdvertisementContract;
import com.stockroompro.models.contracts.CommentsContract;
import com.stockroompro.models.converters.AdvertisementContentValuesConverter;

import java.util.ArrayList;

/**
 * Created by artjoker on 22.04.2015.
 */
public class CommentsFragment extends BaseApplicationPaginationListFragment
        implements View.OnClickListener, CommentActionListener, BaseRequest.UIResponseCallback {

    private static final String ADVERT_ID = "advert_id";
    private static final int ADDING_TO_COUNT = 20;

    private TextView commentCount;
    private View empty;
    private ArtJokerEditText commentText;
    private CommentsAdapter adapter;
    private long parentId;
    private ArtJokerButton buttonSend;
    private boolean responseWasCalled = false;
    private ArtJokerTextView advertTitle;
    private ImageButton goToAdvert;

    private boolean isAdvertActive;
    private long authorId;
    private long adId;

    public static CommentsFragment newInstance(long adId) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ADVERT_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CommentsAdapter(getActivity().getApplicationContext(), new ArrayList<Comment>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adId = getArguments().getLong(ADVERT_ID);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void restartLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        manager.restartLoader(LoadersId.COMMENTS_LOADER, commonBundleBuilder.putLong(RequestParams.ParamNames.AD_ID, adId).build(), this).forceLoad();
        manager.restartLoader(LoadersId.DB_COMMENTS_LOADER, commonBundleBuilder.putLong(RequestParams.ParamNames.AD_ID, adId).build(), this).forceLoad();
    }

    @Override
    protected void loadPageWithOffset(int offset, int count, int limit) {

    }

    @Override
    protected void initAdapter(AbsListView absListView) {
        adapter.setCommentActionListener(this);
        absListView.setAdapter(adapter);
    }

    @Override
    protected void initListeners(View view) {
        super.initListeners(view);
        buttonSend = (ArtJokerButton) view.findViewById(R.id.button_add_comment);
        buttonSend.setOnClickListener(this);
        goToAdvert.setOnClickListener(this);
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
        manager.initLoader(LoadersId.COMMENTS_LOADER, commonBundleBuilder.putLong(RequestParams.ParamNames.AD_ID, adId).build(), this).forceLoad();
        manager.initLoader(LoadersId.DB_COMMENTS_LOADER, commonBundleBuilder.putLong(RequestParams.ParamNames.AD_ID, adId).build(), this).forceLoad();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comments;
    }

    @Override
    protected void initViews(View view) {
        commentCount = (TextView) view.findViewById(R.id.tv_comments_count);
        empty = view.findViewById(android.R.id.empty);
        commentText = (ArtJokerEditText) view.findViewById(R.id.et_comment);
        advertTitle = (ArtJokerTextView) view.findViewById(R.id.fragment_comments_advert_title);
        goToAdvert = (ImageButton) view.findViewById(R.id.fragment_comments_go_to_advert);
    }

    @Override
    protected void initContent() {
        super.initContent();
        commentCount.setText(getString(R.string.comment_count, 0));
        if (TextUtils.isEmpty(PersonalData.getInstance(getActivity()).getToken())) {
            commentText.setVisibility(View.GONE);
            buttonSend.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return HeaderIconsPolicy.DO_NOT_SHOW_ALL;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.COMMENTS_LOADER:
                return new LocalRequestDescriptorLoader(getActivity(), new AdvertCommentsRequest(getActivity(), args).setUiCallback(new BaseRequest.UIResponseCallback() {
                    @Override
                    public void uiDataResponse(ResponseHolder data) {
                        if (data.getStatusCode() == StatusCode.RESPONSE_SUCCESS) {
                            responseWasCalled = true;
                            ResponseItemHolder<Comment> responseData = (ResponseItemHolder<Comment>) data.getData();
                            if (getActivity() != null) {
                                commentCount.setText(getString(R.string.comment_count, responseData.getAmount()));
                            }
                        } else {
                            getLoaderManager().initLoader(LoadersId.COMMENTS_COUNT_LOADER_ID, null, CommentsFragment.this);
                        }
                    }
                }));

            case LoadersId.ADD_COMMENT_LOADER:
                return new LocalRequestDescriptorLoader(getActivity(), new AddCommentRequest(getActivity(), args));

            case LoadersId.DB_COMMENTS_LOADER:
                return new CommentsLoader(getActivity().getApplicationContext(), args);

            case LoadersId.COMMENTS_COUNT_LOADER_ID:
                return new CursorLoader(getActivity().getApplicationContext(), CommentsContract.CONTENT_URI,
                        null, CommentsContract.ADVERTISEMENT_ID + "=?", new String[]{String.valueOf(adId)}, null);

            case LoadersId.ADD_LIKE_TO_COMMENT:
                return new LocalRequestDescriptorLoader(getActivity(), new AddCommentLikeRequest(getActivity(), args).setUiCallback(new BaseRequest.UIResponseCallback() {
                    @Override
                    public void uiDataResponse(ResponseHolder data) {
                        if (data.getStatusCode() == 0) {
                            Bundle args = new Bundle();
                            args.putLong(RequestParams.ParamNames.AD_ID, adId);
                            args.putString(RequestParams.ParamNames.TOKEN, PersonalData.getInstance(getActivity().getApplicationContext()).getToken());
                            getLoaderManager().restartLoader(LoadersId.COMMENTS_LOADER, args, CommentsFragment.this).forceLoad();
                        }
                    }
                }));

            case LoadersId.ADVERT_DETAIL_FROM_COMMENTS_LOADER_ID:
                return new LocalRequestDescriptorLoader(getActivity(), new AdvertisementDetailsRequest(getActivity(), AdvertisementContract.CONTENT_URI,
                        AdvertisementContentValuesConverter.class, args).setUiCallback(this));
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LoadersId.COMMENTS_LOADER:
                getLoaderManager().restartLoader(LoadersId.DB_COMMENTS_LOADER, new CommonBundleBuilder().putIntLimit(adapter.getCount()
                        + ADDING_TO_COUNT).putIntOffset(0).putIntCount(adapter.getCount() + ADDING_TO_COUNT)
                        .putLong(RequestParams.ParamNames.AD_ID, adId).build(), this).forceLoad();
                break;

            case LoadersId.ADD_COMMENT_LOADER:
                restartLoader(getLoaderManager(), new CommonBundleBuilder().putIntLimit(adapter.getCount()
                        + ADDING_TO_COUNT).putIntOffset(0).putIntCount(adapter.getCount() + ADDING_TO_COUNT));
                commentText.setText("");
                commentText.clearFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);
                parentId = 0;
                break;

            case LoadersId.DB_COMMENTS_LOADER:
                ArrayList<Comment> comments = (ArrayList<Comment>) data;
                if (comments.size() > 0) {
                    adapter.changeData(comments);
                    empty.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.VISIBLE);
                }
                if (!responseWasCalled) {
                    getLoaderManager().initLoader(LoadersId.COMMENTS_COUNT_LOADER_ID, null, this);
                    responseWasCalled = false;
                }
                Bundle args = new Bundle();
                args.putLong(RequestParams.ParamNames.AD_ID, adId);
                getLoaderManager().restartLoader(LoadersId.ADVERT_DETAIL_FROM_COMMENTS_LOADER_ID, args, this).forceLoad();
                break;

            case LoadersId.COMMENTS_COUNT_LOADER_ID:
                commentCount.setText(getString(R.string.comment_count, ((Cursor) data).getCount()));
                break;

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.changeData(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_comment:
                sendComment();
                break;

            case R.id.fragment_comments_go_to_advert:
                commit(AdvertDetailFragment.newInstance(adId, authorId),
                        AdvertDetailFragment.class.getCanonicalName(), null);
                break;
        }
    }

    private void sendComment() {
        if (!TextUtils.isEmpty(commentText.getText())) {
            BundleBuilder bundle = new BundleBuilder()
                    .putLong(RequestParams.ParamNames.AD_ID, adId)
                    .putString(RequestParams.ParamNames.TOKEN, PersonalData.getInstance(getActivity().getApplicationContext()).getToken())
                    .putLong(RequestParams.ParamNames.COMMENT_ID, parentId)
                    .putString(RequestParams.ParamNames.TEXT, commentText.getText().toString());
            getLoaderManager().restartLoader(LoadersId.ADD_COMMENT_LOADER, bundle.build(), CommentsFragment.this).forceLoad();
        } else {
            commentText.setError(getString(R.string.comment_text_error));
        }
    }

    @Override
    public void replyComment(long parentId) {
        this.parentId = parentId;
        commentText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(commentText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);

    }

    @Override
    public void like(long commentId) {
        BundleBuilder bundle = new BundleBuilder()
                .putLong(RequestParams.ParamNames.COMMENT_ID, commentId)
                .putString(RequestParams.ParamNames.TOKEN, PersonalData.getInstance(getActivity().getApplicationContext()).getToken())
                .putInt(RequestParams.ParamNames.ACTION, Comment.USER_CHOICE_IS_LIKE);
        getLoaderManager().restartLoader(LoadersId.ADD_LIKE_TO_COMMENT, bundle.build(), CommentsFragment.this).forceLoad();
    }

    @Override
    public void dislike(long commentId) {
        BundleBuilder bundle = new BundleBuilder()
                .putLong(RequestParams.ParamNames.COMMENT_ID, commentId)
                .putString(RequestParams.ParamNames.TOKEN, PersonalData.getInstance(getActivity().getApplicationContext()).getToken())
                .putInt(RequestParams.ParamNames.ACTION, Comment.USER_CHOICE_IS_DISLIKE);
        getLoaderManager().restartLoader(LoadersId.ADD_LIKE_TO_COMMENT, bundle.build(), CommentsFragment.this).forceLoad();
    }

    @Override
    public void goToSenderProfile(long senderId) {
        commit(SellerProfileFragment.newInstance(senderId), SellerProfileFragment.class.getCanonicalName(), null);
    }

    @Override
    public void uiDataResponse(ResponseHolder data) {
        if (data.getStatusCode() == 0) {
            if (data.getData() instanceof Advertisement) {
                authorId = ((Advertisement) data.getData()).getUserId();
                advertTitle.setText(((Advertisement) data.getData()).getTitle());
            }
        } else {
            advertTitle.setText(getString(R.string.advert_no_active));
            goToAdvert.setOnClickListener(null);
        }
    }
}