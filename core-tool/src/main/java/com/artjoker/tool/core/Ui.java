package com.artjoker.tool.core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR;
import static android.graphics.PorterDuff.Mode.SRC_IN;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.support.v4.view.GravityCompat.START;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_NULL;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static android.view.View.FOCUS_DOWN;

public final class Ui {

    private Ui() {

    }

    private static InputMethodManager getInputMethodManager(final Context context) {
        return (InputMethodManager) context.getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
    }

    public static void showKeyboard(final Context context, final View view) {
        getInputMethodManager(context).showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideKeyboard(final Context context, final View view) {
        getInputMethodManager(context).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void toggleDrawer(final DrawerLayout drawerHolder) {
        if (drawerHolder.isDrawerVisible(START)) {
            drawerHolder.closeDrawer(START);
        } else {
            drawerHolder.openDrawer(START);
        }
    }

    public static void openDrawer(final DrawerLayout drawerHolder) {
        if (!drawerHolder.isDrawerVisible(START)) {
            drawerHolder.openDrawer(START);
        }
    }

    public static void closeDrawer(final DrawerLayout drawerHolder) {
        if (drawerHolder.isDrawerVisible(START)) {
            drawerHolder.closeDrawer(START);
        }
    }

    public static void requestSensorOrientation(final Activity activity, final boolean enable) {
        if (activity != null) {
            activity.setRequestedOrientation(enable ? SCREEN_ORIENTATION_SENSOR : SCREEN_ORIENTATION_NOSENSOR);
        }
    }

    public static int showPassword(final boolean state) {
        return TYPE_CLASS_TEXT + (state ? TYPE_NULL : TYPE_TEXT_VARIATION_PASSWORD);
    }

    public static void setViewsGravity(final int gravity, final TextView... views) {
        for (TextView view : views) {
            if (view != null) {
                view.setGravity(gravity);
            }
        }
    }

    public static void scrollToBottom(final ScrollView view) {
        if (view != null) {
            view.fullScroll(FOCUS_DOWN);
        }
    }

    public static void adjustBackground(final View view, final int value) {
        ((ColorDrawable) view.getBackground()).setColor(value);
    }

    public static Drawable getTintedDrawable(final Resources res, @DrawableRes final int drawableResId, @ColorRes final int colorResId) {
        final Drawable drawable = res.getDrawable(drawableResId);
        drawable.setColorFilter(res.getColor(colorResId), SRC_IN);
        return drawable;
    }

    public static void changeViewState(final View view, final boolean isEnabled) {
        view.setEnabled(isEnabled);
        view.setClickable(isEnabled);
        view.setFocusable(isEnabled);
    }

    @SuppressWarnings("deprecation")
    @TargetApi(JELLY_BEAN)
    public static void setBackground(final View view, final Drawable background) {
        if (SDK_INT >= JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

}