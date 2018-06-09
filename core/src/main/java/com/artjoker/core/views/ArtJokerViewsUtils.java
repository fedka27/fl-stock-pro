package com.artjoker.core.views;


import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class ArtJokerViewsUtils {
    public static boolean setCustomFont(Context ctx,TextView view, String asset) {
        Typeface tf = null;
        if(asset!=null)
        try {

            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
            view.setTypeface(tf);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }
}
