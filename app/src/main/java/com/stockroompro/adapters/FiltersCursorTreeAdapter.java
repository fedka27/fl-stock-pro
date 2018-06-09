package com.stockroompro.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.artjoker.database.SecureDatabaseProvider;
import com.artjoker.tool.core.TypefaceProducer;
import com.stockroompro.R;
import com.stockroompro.models.columns.FiltersColumns;
import com.stockroompro.models.contracts.FilterValuesContract;

import java.util.ArrayList;

import static com.artjoker.tool.core.TypefaceProducer.Key.LIGHT;
import static com.artjoker.tool.core.TypefaceProducer.Key.REGULAR;

/**
 * Created by bagach.alexandr on 24.04.15.
 */
public class FiltersCursorTreeAdapter extends CursorTreeAdapter {

    public interface OnFilterItemSelectListener {
        void onSelect(View v);
    }

    private LayoutInflater mInflater;
    private Context context;
    private Typeface light;
    private Typeface regular;
    protected ArrayList<Integer> selectedFiltersId = new ArrayList<>();
    private OnFilterItemSelectListener listener;

    public FiltersCursorTreeAdapter(Cursor cursor, Context context, OnFilterItemSelectListener l) {
        super(cursor, context);
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = l;
        light = TypefaceProducer.getInstance().get(context.getApplicationContext(), LIGHT);
        regular = TypefaceProducer.getInstance().get(context.getApplicationContext(), REGULAR);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof CheckBox) {
                listener.onSelect(v);
            }
        }
    };

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        return context.getContentResolver().query(FilterValuesContract.CONTENT_URI, null, FiltersColumns.FILTER_ID + " = ?",
                new String[]{String.valueOf(groupCursor.getLong(groupCursor.getColumnIndexOrThrow(FiltersColumns.ID)))}, null);
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.filters_list_header, parent, false);
        HeaderHolder groupViewHolder = new HeaderHolder();
        groupViewHolder.name = (TextView) v.findViewById(R.id.tv_filter_name);
        v.setTag(groupViewHolder);
        return v;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        HeaderHolder groupViewHolder = (HeaderHolder) view.getTag();
        groupViewHolder.getName().setText(cursor.getString(cursor.getColumnIndexOrThrow(FiltersColumns.NAME)));
        groupViewHolder.getName().setTypeface(regular);
    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.filters_list_view_item, parent, false);
        ItemHolder groupViewHolder = new ItemHolder();
        groupViewHolder.text = (TextView) v.findViewById(R.id.filter_value_text);
        groupViewHolder.checkBox = (CheckBox) v.findViewById(R.id.filter_value_check_box);
        v.setTag(groupViewHolder);
        return v;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        ItemHolder groupViewHolder = (ItemHolder) view.getTag();
        groupViewHolder.getText().setText(cursor.getString(cursor.getColumnIndexOrThrow(FiltersColumns.NAME)));
        groupViewHolder.getText().setTypeface(light);
        if (!selectedFiltersId.contains(cursor.getInt(cursor.getColumnIndexOrThrow(FiltersColumns.ID)))) {
            groupViewHolder.getCheckBox().setChecked(false);
        } else {
            groupViewHolder.getCheckBox().setChecked(true);
        }
        groupViewHolder.getCheckBox().setTag(R.integer.filter_id_key, cursor.getInt(cursor.getColumnIndexOrThrow(FiltersColumns.ID)));
        groupViewHolder.getCheckBox().setTag(R.integer.filter_name_key, cursor.getString(cursor.getColumnIndexOrThrow(FiltersColumns.NAME)));
        groupViewHolder.getCheckBox().setOnClickListener(onClickListener);
    }

    public void setSelectedFiltersId(ArrayList<Integer> selectedFiltersId) {
        this.selectedFiltersId = selectedFiltersId;
    }

    private class HeaderHolder {
        private TextView name;

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }
    }

    private class ItemHolder {

        private TextView text;
        private CheckBox checkBox;

        public TextView getText() {
            return text;
        }

        public void setText(TextView text) {
            this.text = text;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
    }
}
