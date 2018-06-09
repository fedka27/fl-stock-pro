package com.stockroompro.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.stockroompro.controllers.AnimatorController;


public class ExpandableLinearLayout extends LinearLayout {

    private final AnimatorController animatorController;

    {
        animatorController = new AnimatorController(this);
    }

    public ExpandableLinearLayout(Context context) {
        super(context);

        collapse();
    }

    public ExpandableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        collapse();
    }

    public ExpandableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        collapse();
    }


    public final boolean isExpanded() {
        return (getVisibility() == VISIBLE);
    }

    public final void expand() {
        setVisibility(VISIBLE);
    }

    public final void collapse() {
        setVisibility(GONE);
    }

    public final void animateExpand() {
        animatorController.expand();
    }

    public final void animateCollapse() {
        animatorController.collapse();
    }

}
