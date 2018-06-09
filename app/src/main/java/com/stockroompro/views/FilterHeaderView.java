package com.stockroompro.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.artjoker.core.views.ArtJokerTextView;
import com.stockroompro.R;

public class FilterHeaderView extends RelativeLayout implements View.OnClickListener {
    private View view;
    private ArtJokerTextView viewSubTitle;
    private OnClickListener onClickListener;
    private ExpandableLinearLayout expandableLinearLayout;
    public FilterHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initListeners();
        setCustomText(context, attrs);
    }

    public FilterHeaderView(Context context) {
        super(context);
        initView();
        initListeners();

    }
    private void initView() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.header_filters_button, this, true);
        viewSubTitle = (ArtJokerTextView) view.findViewById(R.id.tv_filter_checked_item_name);
    }
    private void initListeners() {
        super.setOnClickListener(this);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        onClickListener = l;
    }

    public boolean isChecked() {
        return isSelected();
    }

    public void setSubtitleText(String text) {
        viewSubTitle.setText(text);
    }

    @Override
    public void onClick(View v) {
        if (isSelected()) {
            setSelected(false);
        } else {
            setSelected(true);
        }
        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
    }

    public void setTitleText(String text) {

        ((ArtJokerTextView) view.findViewById(R.id.tv_filter_name)).setText(text);
    }
    private void setCustomText(Context ctx, AttributeSet attrs) {
        try {
            TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.FilterHeaderView);
            String customText = a.getString(R.styleable.FilterHeaderView_customText);
            setTitleText(customText);
            a.recycle();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (expandableLinearLayout != null) {
            if (selected) {
                expandableLinearLayout.animateExpand();
            } else {
                expandableLinearLayout.animateCollapse();
            }
        }
    }

    /**
     * Overridden to save instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the {@link #setId(int)} method. Other members of this class than the normalized min and max values don't need to be saved.
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER", super.onSaveInstanceState());
        bundle.putBoolean("SELECTION", isSelected());
        return bundle;
    }

    /**
     * Overridden to restore instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the {@link #setId(int)} method.
     */
    @Override
    protected void onRestoreInstanceState(Parcelable parcel) {
        final Bundle bundle = (Bundle) parcel;
        super.onRestoreInstanceState(bundle.getParcelable("SUPER"));
        setSelected(bundle.getBoolean("SELECTION"));
    }

    public void setExpandableLinearLayout(ExpandableLinearLayout expandableLinearLayout) {
        this.expandableLinearLayout = expandableLinearLayout;
    }
}
