package com.artjoker.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.artjoker.core.R;



public class ArtJokerCheckBox extends CheckBox {
    public ArtJokerCheckBox(Context context) {
        super(context);
    }

    public ArtJokerCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);

    }

    public ArtJokerCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.ArtJokerCheckBox);
        String customFont = a.getString(R.styleable.ArtJokerCheckBox_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {

            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        setTypeface(tf);
        return true;
    }
}
