package com.stockroompro.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artjoker.core.database.ConverterCursorAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stockroompro.R;
import com.stockroompro.models.converters.CategoryCursorConverter;

/**
 * Created by alexsergienko on 23.03.15.
 */
public class CategoryAdapter extends ConverterCursorAdapter<CategoryCursorConverter> {

    private int height = 0;

    public CategoryAdapter(Context context, Cursor c, Class<CategoryCursorConverter> converterClass, int height) {
        super(context, c, converterClass);
        this.height = height;
    }

    @Override
    public View newView(Context context, Cursor cursor, CategoryCursorConverter cursorConverter, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_category, parent, false);
        ViewHolder holder = new ViewHolder(((SimpleDraweeView) view.findViewById(R.id.categoryImage)), ((TextView) view.findViewById(R.id.categoryName)));
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor, CategoryCursorConverter cursorConverter) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = this.height;
        view.setLayoutParams(params);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText("");
        if (cursorConverter != null && cursorConverter.isValid()) {
            holder.text.setText(cursorConverter.getObject().getName());
            holder.imageView.setImageURI(cursorConverter.getObject().getImg());
        }
    }

    public long getCategoryId(int position) {
        return getItem(position).getCategoryId();
    }


    private static class ViewHolder {

        SimpleDraweeView imageView;
        TextView text;

        private ViewHolder(SimpleDraweeView imageView, TextView text) {
            this.imageView = imageView;
            this.text = text;
        }
    }
}
