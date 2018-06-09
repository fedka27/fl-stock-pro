package com.stockroompro.controllers;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

import com.stockroompro.R;


public final class AnimatorController {
    private final View view;

    private final LayoutParamsUpdateListener layoutParamsUpdateListener;
    private final CollapseListener collapseListener;

    {
        layoutParamsUpdateListener = new LayoutParamsUpdateListener();
        collapseListener = new CollapseListener();
    }


    public AnimatorController(View view) {
        if (view == null)
            throw new NullPointerException("You must provide existing view to controller!");
        this.view = view;
    }

    public final void expand() {
        measureView();
        slideAnimator(0, view.getMeasuredHeight()).start();
    }

    public final void collapse() {
        Animator animator = slideAnimator(view.getHeight(), 0);
        animator.addListener(collapseListener);
        animator.start();
    }

    private void measureView() {
        view.setVisibility(View.VISIBLE);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);
    }

    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(view.getResources().getInteger(R.integer.very_short_animation_duration));
        animator.addUpdateListener(layoutParamsUpdateListener);
        return animator;
    }

    private final class LayoutParamsUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
            view.setLayoutParams(layoutParams);
        }

    }

    private final class CollapseListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            view.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

    }

}