package com.stockroompro.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.artjoker.tool.core.TextUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stockroompro.R;
import com.stockroompro.fragments.AdvertDetailFragment;
import com.stockroompro.fragments.AdvertType;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.Settings;
import com.stockroompro.models.columns.AdvertisementColumns;

/**
 * Created by bagach.alexandr on 03.04.15.
 */
public class AdvertSuggestionAdapter extends CursorAdapter {

    private int count;

    public AdvertSuggestionAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        count = c != null ? c.getCount() : 0;
    }

    @Override
    public int getCount() {
        return getCursor() != null ? getCursor().getCount() : 0;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_advertisement  , parent, false);
        ViewHolder holder = new ViewHolder(((SimpleDraweeView) view.findViewById(R.id.iv_advert_image)),
                                           ((TextView) view.findViewById(R.id.tv_advert_title)),
                                           ((TextView) view.findViewById(R.id.tv_advert_price)),
                                           ((TextView) view.findViewById(R.id.tv_advert_location)),
                                           ((TextView) view.findViewById(R.id.tv_type)));
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.imageView.setImageURI(cursor.getString(cursor.getColumnIndexOrThrow(AdvertisementColumns.PHOTO_URL)));
        holder.textTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(AdvertisementColumns.TITLE)));
        float price = cursor.getFloat(cursor.getColumnIndexOrThrow(AdvertisementColumns.PRICE));
        if (price > 0) {
            holder.textPrice.setText(String.format(AdvertDetailFragment.AdvertisementParameters.PRICE_FORMAT,
                    TextUtils.getInstance().getStringFromDoubleValue((double) price),
                    Settings.getInstance(context).getCurrencyById(cursor.getInt(cursor.getColumnIndexOrThrow(Advertisement.CURRENCY_ID))).getName()));
        } else {
            holder.textPrice.setText(context.getResources().getString(R.string.advert_price_type_free));
        }
        holder.textLocation.setText(cursor.getString(cursor.getColumnIndexOrThrow(AdvertisementColumns.COUNTRY_NAME)) + " , "
                + cursor.getString(cursor.getColumnIndexOrThrow(AdvertisementColumns.CITY_NAME)));
        holder.advertisementId = cursor.getLong(cursor.getColumnIndexOrThrow(AdvertisementColumns.ID));
        if (cursor.getInt(cursor.getColumnIndexOrThrow(AdvertisementColumns.TYPE))== AdvertType.BUY) {
            holder.type.setText(context.getString(R.string.buy_title));
            holder.type.setTextColor(context.getResources().getColor(R.color.green_header));
        } else {
            holder.type.setText(context.getString(R.string.sell_title));
            holder.type.setTextColor(context.getResources().getColor(R.color.red_text_color));
        }
    }

    @Override
    public void changeCursor(Cursor cursor) {
        if(cursor!= null) {
            count = cursor.getCount();
            super.changeCursor(cursor);
        } else {
           count = 0;
        }
    }

    public static class ViewHolder {

        SimpleDraweeView imageView;
        TextView textTitle;
        TextView textPrice;
        TextView textLocation;
        TextView type;
        long advertisementId;

        private ViewHolder(SimpleDraweeView imageView, TextView title, TextView price, TextView location, TextView type) {
            this.imageView = imageView;
            this.textTitle = title;
            this.textPrice = price;
            this.textLocation = location;
            this.type = type;
        }
    }
}
