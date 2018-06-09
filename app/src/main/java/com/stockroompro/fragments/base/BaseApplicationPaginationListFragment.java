package com.stockroompro.fragments.base;

import android.view.View;

import com.artjoker.core.fragments.AbstractBasicListPagination;
import com.stockroompro.Launcher;
import com.stockroompro.R;

/**
 * Created by artjoker on 21.04.2015.
 */
public abstract class BaseApplicationPaginationListFragment extends AbstractBasicListPagination {

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            Launcher parent = getParent();
            parent.setMainTitle(getString(R.string.main_name));
            parent.hideProgressBar();
            parent.setLocationSpinnerContainer(View.GONE);
            parent.manageHeaderIcons(getHeaderIconsPolicy());
            hideKeyBoard();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getParent().hideProgressBar();
    }

    protected Launcher getParent() {
        return (Launcher) getActivity();
    }
}
