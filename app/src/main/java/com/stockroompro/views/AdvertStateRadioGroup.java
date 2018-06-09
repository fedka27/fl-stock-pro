package com.stockroompro.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.stockroompro.R;
import com.stockroompro.models.contracts.AdvertisementContract;

public class AdvertStateRadioGroup extends LinearLayout implements View.OnClickListener {
    private RadioButton rButtonNew;
    private RadioButton rButtonBeUsed;

    public AdvertStateRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public AdvertStateRadioGroup(Context context) {
        super(context);
        initViews(context);
    }

    public void initViews(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.advert_state_radio_group, this, true);
        rButtonNew = (RadioButton) findViewById(R.id.rb_new);
        rButtonBeUsed = (RadioButton) findViewById(R.id.rb_to_be_used);
        rButtonBeUsed.setOnClickListener(this);
        rButtonNew.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_new:
                rButtonBeUsed.setChecked(false);
                break;
            case R.id.rb_to_be_used:
                rButtonNew.setChecked(false);
                break;
        }
    }

    public int getType() {
        if (rButtonBeUsed.isChecked()) {
            return AdvertisementContract.TYPE_USED;
        } else if (rButtonNew.isChecked()) {
            return AdvertisementContract.TYPE_NEW;
        } else {
            return AdvertisementContract.TYPE_NEW;
        }
    }

    public void setSelected(int type) {
        switch (type) {
            case 0:
                rButtonNew.setChecked(true);
                break;

            case 1:
                rButtonBeUsed.setChecked(true);
                break;
        }
    }
}
