package com.artjoker.core.views;

import android.widget.AbsListView;

/**
 * Created by alexsergienko on 18.03.15.
 */
public abstract class PaginationScrollListener implements AbsListView.OnScrollListener {
    private int lastTotalItemCount = 0;

    protected PaginationScrollListener() {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount - (firstVisibleItem + visibleItemCount) <= 1 && lastTotalItemCount != totalItemCount) {
            lastTotalItemCount = totalItemCount;
            onScrolledToEnd(firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public abstract void onScrolledToEnd(int firstVisibleItem, int visibleItemCount, int totalItemCount);

}
