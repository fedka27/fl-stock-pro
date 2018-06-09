package com.stockroompro.fragments;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.views.ArtJokerButton;
import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.stockroompro.R;
import com.stockroompro.fragments.base.BaseApplicationListFragment;
import com.stockroompro.views.LauncherHeaderView;

/**
 * Created by bagach.alexandr on 14.05.15.
 */
public class FragmentAdvertAdded extends BaseApplicationListFragment implements View.OnClickListener {

    public static final String ADVERT_CREATED = "ADVERT_CREATED";
    private ArtJokerTextView topText;

    public interface AdvertAction {
        String ADVERT_ACTION = "advert_action";
        int ADVERT_CREATED = 1;
        int ADVERT_EDITED = 2;
    }

    public static FragmentAdvertAdded newInstance(int advertAction) {
        FragmentAdvertAdded fragment = new FragmentAdvertAdded();
        Bundle args = new Bundle();
        args.putInt(AdvertAction.ADVERT_ACTION, advertAction);
        fragment.setArguments(args);
        return fragment;
    }

    private Button goToCategories;
    private Button createNewAdvert;

    @Override
    protected void initAdapter(AbsListView absListView) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_advert_created;
    }

    @Override
    protected void initViews(View view) {
        topText = (ArtJokerTextView) view.findViewById(R.id.created_advert_top_text);
        goToCategories = (ArtJokerButton) view.findViewById(R.id.done_show_all_adverts);
        createNewAdvert = (ArtJokerButton) view.findViewById(R.id.create_new_advert_done);
    }

    @Override
    protected void initListeners(View view) {
        goToCategories.setOnClickListener(this);
        createNewAdvert.setOnClickListener(this);
    }

    @Override
    protected void initContent() {
        if (getArguments() != null) {
            switch (getArguments().getInt(AdvertAction.ADVERT_ACTION)) {
                case AdvertAction.ADVERT_CREATED:
                    topText.setText(getResources().getString(R.string.advert_created_done));
                    break;

                case AdvertAction.ADVERT_EDITED:
                    topText.setText(getResources().getString(R.string.advert_edited_done));
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_show_all_adverts:
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(ADVERT_CREATED));
                break;

            case R.id.create_new_advert_done:
                commit(AddAdvertisementFragment.newInstance(0, ""), AddAdvertisementFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;
        }
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return NotificationsPolicy.DO_NOT_NOTIFY_ALL;
    }

    @Override
    protected void initLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder) {
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    public static void onBackPressed(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ADVERT_CREATED));
    }
}
