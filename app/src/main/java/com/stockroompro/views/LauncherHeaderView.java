package com.stockroompro.views;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.views.ArtJokerTextView;
import com.stockroompro.R;
import com.stockroompro.adapters.LocationSpinnerAdapter;


public final class LauncherHeaderView extends LinearLayout implements View.OnClickListener,
        LocationSpinnerAdapter.OnLocationSpinnerItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private HeaderCallback headerCallback;
    private ImageView add;
    private ImageView filters;
    private ArtJokerTextView mainTitle;
    private ImageView delete;
    private ImageView edit;
    private ImageView close;

    public LauncherHeaderView(Context context) {
        super(context);
        initViews();
    }

    public LauncherHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public LauncherHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.action_bar, this, true);
        ImageView menu = (ImageView) root.findViewById(R.id.iv_menu);
        add = (ImageView) root.findViewById(R.id.btn_add_advertisement);
        filters = (ImageView) root.findViewById(R.id.btn_filters);
        mainTitle = (ArtJokerTextView) root.findViewById(R.id.tv_main_name);
        delete = (ImageView) root.findViewById(R.id.btn_delete);
        edit = (ImageView) root.findViewById(R.id.btn_edit);
        close = (ImageView) root.findViewById(R.id.btn_close);

        menu.setOnClickListener(this);
        add.setOnClickListener(this);
        filters.setOnClickListener(this);
        delete.setOnClickListener(this);
        close.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    public final void setHeaderCallback(final HeaderCallback headerCallback) {
        this.headerCallback = headerCallback;
    }

    public void setFiltersSelected(boolean selected) {
        filters.setSelected(selected);
    }


    @Override
    public void onClick(View v) {
        if (headerCallback != null) {
            switch (v.getId()) {
                case R.id.iv_menu:
                    headerCallback.onMenuClick();
                    //Ui.hideKeyboard(getContext(), v);
                    break;

                case R.id.btn_add_advertisement:
                    headerCallback.onAddAdvertClick();
                    break;

                case R.id.btn_filters:
                    headerCallback.onFiltersClick();
                    break;

                case R.id.btn_delete:
                    headerCallback.onDeleteClick();
                    break;

                case R.id.btn_close:
                    headerCallback.onCloseClick();
                    break;

                case R.id.btn_edit:
                    headerCallback.onEditClick();
                    break;
            }
        }
    }

    public ArtJokerTextView getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle.setText(mainTitle);
    }

    public void manageHeaderIcons(int strategy) {
        if ((strategy & HeaderIconsPolicy.SHOW_BUTTON_ADD) == HeaderIconsPolicy.SHOW_BUTTON_ADD) {
            add.setVisibility(VISIBLE);
        } else {
            add.setVisibility(GONE);
        }

        if ((strategy & HeaderIconsPolicy.SHOW_BUTTON_CLOSE) == HeaderIconsPolicy.SHOW_BUTTON_CLOSE) {
            close.setVisibility(VISIBLE);
        } else {
            close.setVisibility(GONE);
        }

        if ((strategy & HeaderIconsPolicy.SHOW_BUTTON_DELETE) == HeaderIconsPolicy.SHOW_BUTTON_DELETE) {
            delete.setVisibility(VISIBLE);
        } else {
            delete.setVisibility(GONE);
        }

        if ((strategy & HeaderIconsPolicy.SHOW_BUTTON_EDIT) == HeaderIconsPolicy.SHOW_BUTTON_EDIT) {
            edit.setVisibility(VISIBLE);
        } else {
            edit.setVisibility(GONE);
        }

        if ((strategy & HeaderIconsPolicy.SHOW_BUTTON_FILTERS) == HeaderIconsPolicy.SHOW_BUTTON_FILTERS) {
            filters.setVisibility(VISIBLE);
        } else {
            filters.setVisibility(GONE);
        }
    }

    @Override
    public void onLocationItemClick(int position, long id) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface HeaderCallback {

        void onMenuClick();
        void onBackClick();
        void onAddAdvertClick();
        void onFiltersClick();
        void onDeleteClick();
        void onEditClick();
        void onCloseClick();
        void commitMainFragment();
    }
}
