package com.artjoker.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.artjoker.core.R;



public class ArtJokerRadioButton extends RadioButton {
    public ArtJokerRadioButton(Context context) {
        super(context);
    }

    public ArtJokerRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);

    }

    public ArtJokerRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.ArtJokerRadioButton);
        String customFont = a.getString(R.styleable.ArtJokerRadioButton_customFont);
        ArtJokerViewsUtils.setCustomFont(ctx,this, customFont);
        a.recycle();
    }

}
