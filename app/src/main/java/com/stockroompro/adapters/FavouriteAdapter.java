package com.stockroompro.adapters;


import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.stockroompro.R;
import com.stockroompro.fragments.AdvertType;
import com.stockroompro.models.converters.AdvertisementCursorConverter;

import java.util.ArrayList;

public class FavouriteAdapter extends AdvertAdapter {

    private ArrayList<Long> selectedId = new ArrayList<>();
    private boolean editMode = false;

    public FavouriteAdapter(Context context, Cursor c, Class<AdvertisementCursorConverter> converterClass) {
        super(context, c, converterClass);
    }

    @Override
    public View newView(Context context, Cursor cursor, AdvertisementCursorConverter cursorConverter, ViewGroup parent) {
        View v = super.newView(context, cursor, cursorConverter, parent);
        ((AdvertHolder) v.getTag()).delete = (CheckedTextView) v.findViewById(R.id.checkedTextView);
        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    protected View getTypedView(ViewGroup parent) {
        return inflater.inflate(R.layout.adapter_account_favourites_adverts, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor, AdvertisementCursorConverter cursorConverter) {
        super.bindView(view, context, cursor, cursorConverter);
        if (cursorConverter != null && cursorConverter.isValid()) {
            final long id = cursorConverter.getAdvertisementId();
            final AdvertHolder holder = (AdvertHolder) view.getTag();
            if (editMode) {
                holder.delete.setVisibility(View.VISIBLE);
                holder.delete.setChecked(selectedId.contains(getItem(cursor.getPosition()).getAdvertisementId()));
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.delete.isChecked()) {
                            holder.delete.setChecked(false);
                            selectedId.remove(id);
                        } else {
                            holder.delete.setChecked(true);
                            selectedId.add(id);
                        }
                    }
                });
            } else {
                holder.delete.setVisibility(View.GONE);
                selectedId.clear();
            }
            if (cursorConverter.getObject().getType() == AdvertType.BUY) {
                holder.type.setText(context.getString(R.string.buy_title));
            } else {
                holder.type.setText(context.getString(R.string.sell_title));
            }
        }
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public ArrayList<Long> getSelectedId() {
        return selectedId;
    }
}
