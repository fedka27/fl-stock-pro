package com.artjoker.core.fragments;

import android.app.LoaderManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.CommonBundleBuilderArgs;

/**
 * Created by alexsergienko on 18.03.15.
 */
public abstract class AbstractBasicListPagination extends AbstractBasicList implements AbsListView.OnScrollListener {
    protected static final int DEFAULT_LIMIT = 20;
    private static final int DEFAULT_OFFSET = 0;
    protected int lastTotalItemCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = getViewFromLayout(inflater, container);
            initAbsListViews(rootView);
            if (getAbstractListView() != null) {
                getAbstractListView().setOnScrollListener(this);
            }
            initViews(rootView);
            initAdapter(getAbstractListView());
            initAdapters();
            initListeners(rootView);
            initTypefaces(rootView);
            if (isFirstLaunch()) {//fdgfg
                initLoader(getLoaderManager(), new CommonBundleBuilder().putIntOffset(DEFAULT_OFFSET).putIntLimit(DEFAULT_LIMIT).putIntCount(DEFAULT_LIMIT));
            }
        }
        return rootView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount - (firstVisibleItem + visibleItemCount) <= 1 && lastTotalItemCount != totalItemCount) {
            lastTotalItemCount = totalItemCount;
            loadPageWithOffset(totalItemCount, visibleItemCount * 2, visibleItemCount * 2 + totalItemCount);
            restartLoader(getLoaderManager(), new CommonBundleBuilder()
                    .putIntOffset(totalItemCount)
                    .putIntLimit(totalItemCount + visibleItemCount * 2)
                    .putIntCount(visibleItemCount * 2));
        }
    }

    protected abstract void restartLoader(LoaderManager manager, CommonBundleBuilder commonBundleBuilder);

    protected abstract void loadPageWithOffset(int offset, int count, int limit);

    protected String getAppendLimitSQLString(Bundle bundle, String sortOrder) {
        StringBuilder appendingSQL = new StringBuilder(sortOrder);
        if (bundle != null) {
            if (bundle.getInt(CommonBundleBuilderArgs.LIMIT) != 0) {
                appendingSQL.append(" ");
                appendingSQL.append(CommonBundleBuilderArgs.LIMIT);
                appendingSQL.append(" ");
                appendingSQL.append(bundle.getInt(CommonBundleBuilderArgs.LIMIT));
            }
        }
        return appendingSQL.toString();
    }
}
