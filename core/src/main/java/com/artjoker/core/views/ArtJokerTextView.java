package com.artjoker.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.artjoker.core.R;


public class ArtJokerTextView extends TextView {

    public ArtJokerTextView(Context context) {
        super(context);
    }

    public ArtJokerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);

    }

    public ArtJokerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.ArtJokerTextView);
        String customFont = a.getString(R.styleable.ArtJokerTextView_customFont);
        ArtJokerViewsUtils.setCustomFont(ctx,this, customFont);
        a.recycle();
    }
}
