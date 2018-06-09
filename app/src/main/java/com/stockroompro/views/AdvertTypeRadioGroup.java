package com.stockroompro.views;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.stockroompro.R;
import com.stockroompro.fragments.AddAdvertisementFragment;
import com.stockroompro.models.Advertisement;

/**
 * Created by bagach.alexandr on 05.05.15.
 */
public class AdvertTypeRadioGroup extends LinearLayout implements View.OnClickListener {
    private RadioButton buttonBuy;
    private RadioButton buttonSell;

    private int advertType;
    private OnAdvertTypeSelectedListener listener;

    public interface OnAdvertTypeSelectedListener {
        void onAdvertTypeSelected(View v);
    }

    public void setListener(OnAdvertTypeSelectedListener listener) {
        this.listener = listener;
    }

    public AdvertTypeRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public AdvertTypeRadioGroup(Context context) {
        super(context);
        initViews(context);

    }

    private void initViews(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.advert_type_radio_group, this, true);
        buttonBuy = (RadioButton) findViewById(R.id.rb_type_buy);
        buttonSell = (RadioButton) findViewById(R.id.rb_type_sell);
        initListeners();
    }

    private void initListeners() {
        buttonBuy.setOnClickListener(this);
        buttonSell.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_type_sell:
                buttonBuy.setChecked(false);
                listener.onAdvertTypeSelected(v);
                break;

            case R.id.rb_type_buy:
                buttonSell.setChecked(false);
                listener.onAdvertTypeSelected(v);
                break;
        }
    }

    public void setAdvertType(int advertType) {
        this.advertType = advertType;
        switch (this.advertType) {
            case Advertisement.ADVERT_TYPE_BUY:
                buttonBuy.setChecked(true);
                buttonSell.setChecked(false);
                break;

            case Advertisement.ADVERT_TYPE_SELL:
                buttonSell.setChecked(true);
                buttonBuy.setChecked(false);
                break;
        }
    }

    public int getAdvertType() {
        return advertType;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.stateToSave = this.advertType;
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
        this.advertType = savedState.stateToSave;
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
