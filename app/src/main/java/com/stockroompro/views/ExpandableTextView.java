package com.stockroompro.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.stockroompro.R;

/**
 * Created by bagach.alexandr on 09.04.15.
 */
public class ExpandableTextView extends TextView {

    private int mAnimDuration = 280;
    private int mAnimProtectionTime = 50;

    private boolean mAnimating = false;
    private long timeAnimStarted;

    public ExpandableTextView(Context context) {
        super(context);
        init();
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        extractAttributes(context, attrs);
        init();
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        extractAttributes(context, attrs);
        init();
    }

    public boolean isAnimating() {
        return mAnimating;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private CharSequence mTextToSet;
    private int mLinesToSet;

    /**
     * Sets new text and starts animation.
     *
     * @param text
     * @return true if text was set and animation was started. false in case of animation is in process and it was started less then AnimProtectionTime ago
     */
    public boolean setTextWithAnimation(CharSequence text) {

        if (isAnimating() && ((System.currentTimeMillis() - timeAnimStarted) < mAnimProtectionTime)) {
            return false;
        }

        int Hstart = getHeight();
        CharSequence oldText = getText();
        int oldLines = getLineCount();

        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        setText(text);
        buildLayer();
        mLinesToSet = getLineCount();
        int Hline = getLineHeight();
        int delta = Math.round((Hline/* - getLineSpacingExtra()*/) * 0.152f);

        setText(oldText);
        buildLayer();
        setLayerType(View.LAYER_TYPE_NONE, null);

        int Hend = Hstart;
        if (oldLines < mLinesToSet) { // Expand
            Hend = mLinesToSet * Hline + getCompoundPaddingTop() + getCompoundPaddingBottom() + delta;
            mTextToSet = null;
            setText(text);
        } else if (oldLines > mLinesToSet) { // Collapse
            Hend = mLinesToSet * Hline + getCompoundPaddingTop() + getCompoundPaddingBottom() + delta;
            //mTextToSet = null;
            mTextToSet = text;
        }

        if (Hstart == Hend) {
            setText(text);
            return true; // Same num of lines - just change text
        } else {
            ValueAnimator anim = ValueAnimator.ofFloat(Hstart, Hend);
            anim.setDuration(mAnimDuration);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    updateHeightNotifyParent(((Float) animation.getAnimatedValue()).floatValue());
                }
            });
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    setMaxLines(mLinesToSet);
                    if (mTextToSet != null) {
                        setText(mTextToSet);
                    }
                    ExpandableTextView.this.invalidate();
                    notifyParent();
                    mAnimating = false;

                    super.onAnimationEnd(animation);
                }
            });
            anim.start();
            timeAnimStarted = System.currentTimeMillis();
            mAnimating = true;
        }
        return true;
    }

    private void notifyParent() {
        ViewParent parent = getParent();
        if ((parent != null) && (parent instanceof ViewGroup)) {
            ((ViewGroup) parent).invalidate();
        }
    }

    private void updateHeightNotifyParent(float h) {
        setHeight(Math.round(h));
        ViewParent parent = getParent();
        if (parent != null) {
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).invalidate();
            }
        }
    }

    private void extractAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AnimatedSwitchedTextView, 0, 0);
        try {
            mAnimDuration = a.getInt(R.styleable.AnimatedSwitchedTextView_animDuration, mAnimDuration);
            mAnimProtectionTime = a.getInt(R.styleable.AnimatedSwitchedTextView_animProtectionTime, mAnimProtectionTime);
        } finally {
            a.recycle();
        }
    }

    private void init() {
        setPivotY(0);
        setPivotX(0);
        setScaleY(1);
    }

}



