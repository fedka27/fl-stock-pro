package com.artjoker.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.artjoker.core.R;


public class ArtJokerSearchView extends SearchView {

    public static final int DEF_ID_VALUE = 0;

    public ArtJokerSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);
        setCustomSearchIcon(context, attrs);
    }

    public ArtJokerSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        setCustomSearchIcon(context, attrs);
    }

    public ArtJokerSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomSearchIcon(context, attrs);
    }

    public ArtJokerSearchView(Context context) {
        super(context);
    }

    @SuppressWarnings("deprecation")
    protected void setCustomSearchIcon(Context ctx, AttributeSet attrs) {

        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.ArtJokerSearchView);

        int drawableId = a.getResourceId(R.styleable.ArtJokerSearchView_searchViewIcon, DEF_ID_VALUE);
        if (drawableId != DEF_ID_VALUE) {
            ImageView ivSearchIcon = (ImageView) this.findViewById(this.getIdentifier("search_mag_icon"));
            Drawable searchIconDrawable = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                searchIconDrawable = this.getContext().getResources().getDrawable(drawableId);
            else
                searchIconDrawable = this.getContext().getResources().getDrawable(drawableId, ctx.getTheme());

            TextView view = (TextView) this.findViewById(getIdentifier("search_src_text"));
            int size = (int) (view.getTextSize() );
            searchIconDrawable.setBounds(0, 0, size, size);
            ivSearchIcon.setImageDrawable(searchIconDrawable);
            setIconified(true);
            setIconifiedByDefault(false);
        }
        a.recycle();

    }

    protected int getIdentifier(String literalId) {
        return this.getContext().getResources().getIdentifier(
                String.format("android:id/%s", literalId),
                null,
                null
        );
    }
}
