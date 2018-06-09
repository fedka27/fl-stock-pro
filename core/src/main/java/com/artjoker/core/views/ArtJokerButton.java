package com.artjoker.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.artjoker.core.R;



public class ArtJokerButton extends Button {
    public ArtJokerButton(Context context) {
        super(context);
    }

    public ArtJokerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);

    }

    public ArtJokerButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.ArtJokerButton);
        String customFont = a.getString(R.styleable.ArtJokerButton_customFont);
        ArtJokerViewsUtils.setCustomFont(ctx,this, customFont);
        a.recycle();
    }

}
