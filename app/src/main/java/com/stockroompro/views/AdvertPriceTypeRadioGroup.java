package com.stockroompro.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.stockroompro.R;
import com.stockroompro.fragments.AddAdvertisementFragment.Config;

public class AdvertPriceTypeRadioGroup extends LinearLayout implements View.OnClickListener {
    private RadioButton rButtonSell;
    private RadioButton rButtonExchange;
    private RadioButton rButtonFree;

    private int priceType;
    private OnPriceTypeSelectedListener listener;

    public interface OnPriceTypeSelectedListener {
        void onPriceTypeSelected(View v);
    }

    public void setListener(OnPriceTypeSelectedListener listener) {
        this.listener = listener;
    }

    public AdvertPriceTypeRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public AdvertPriceTypeRadioGroup(Context context) {
        super(context);
        initViews(context);
    }

    public void initViews(final Context context) {

        LayoutInflater.from(context).inflate(R.layout.advert_price_type_radio_group, this, true);
        rButtonSell = (RadioButton) findViewById(R.id.rb_sell);
        rButtonExchange = (RadioButton) findViewById(R.id.rb_exchange);
        rButtonFree = (RadioButton) findViewById(R.id.rb_free);

        rButtonFree.setOnClickListener(this);
        rButtonSell.setOnClickListener(this);
        rButtonExchange.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_sell:
                rButtonExchange.setChecked(false);
                rButtonFree.setChecked(false);
                listener.onPriceTypeSelected(view);
                break;
            case R.id.rb_exchange:
                rButtonSell.setChecked(false);
                rButtonFree.setChecked(false);
                listener.onPriceTypeSelected(view);
                break;
            case R.id.rb_free:
                rButtonSell.setChecked(false);
                rButtonExchange.setChecked(false);
                listener.onPriceTypeSelected(view);
                break;
        }
    }

    public void setButtons(int state) {
        switch (state) {
            case 1:
                rButtonSell.setChecked(true);
                rButtonExchange.setChecked(false);
                rButtonFree.setChecked(false);
                break;

            case 2:
                rButtonExchange.setChecked(true);
                rButtonSell.setChecked(false);
                rButtonFree.setChecked(false);
                break;

            case 3:
                rButtonFree.setChecked(true);
                rButtonSell.setChecked(false);
                rButtonExchange.setChecked(false);
                break;
        }
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public int getPriceType() {
        return priceType;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.stateToSave = this.priceType;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.priceType = savedState.stateToSave;
    }

    static class SavedState extends BaseSavedState {
        int stateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}
