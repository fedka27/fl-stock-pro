package com.stockroompro.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artjoker.core.database.ConverterCursorAdapter;
import com.stockroompro.R;
import com.stockroompro.models.converters.SubCategoryCursorConverter;

/**
 * Created by bagach.alexandr on 26.03.15.
 */
public class SubCategoryAdapter extends ConverterCursorAdapter<SubCategoryCursorConverter> {

    public SubCategoryAdapter(Context context, Cursor cursor, Class<SubCategoryCursorConverter> converterClass) {
        super(context, cursor, converterClass);
    }

    @Override
    public View newView(Context context, Cursor cursor, SubCategoryCursorConverter cursorConverter, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_subcategories, parent, false);
        ViewHolder holder = new ViewHolder((TextView) view.findViewById(R.id.tv_subcategory_name));
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor, SubCategoryCursorConverter cursorConverter) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        view.setLayoutParams(params);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText("");
        if (cursorConverter != null && cursorConverter.isValid()) {
            holder.text.setText(cursorConverter.getObject().getName());
        }
    }

    private static class ViewHolder {
        TextView text;

        private ViewHolder(TextView text) {
            this.text = text;
        }
    }
}
