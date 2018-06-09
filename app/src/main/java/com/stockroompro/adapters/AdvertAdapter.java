package com.stockroompro.adapters;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.artjoker.core.database.ConverterCursorAdapter;
import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.database.SecureDatabaseProvider;
import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.core.TextUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stockroompro.R;
import com.stockroompro.fragments.AdvertType;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.Settings;
import com.stockroompro.models.converters.AdvertisementCursorConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.stockroompro.fragments.AdvertDetailFragment.AdvertisementParameters.LOCATION_SINGLE_LINE_FORMAT;
import static com.stockroompro.fragments.AdvertDetailFragment.AdvertisementParameters.PRICE_FORMAT;

public class AdvertAdapter extends ConverterCursorAdapter<AdvertisementCursorConverter> {

    protected final LayoutInflater inflater;
    private final SimpleDateFormat simpleDateFormat;
    private int adapterType = TYPE_SMALL;
    public final static int TYPE_SMALL = 0;
    public final static int TYPE_BIG = 1;

    {
        simpleDateFormat = new SimpleDateFormat("H:mm, d MMMM yyyy", Locale.getDefault());
    }

    public AdvertAdapter(Context context, Cursor c, Class<AdvertisementCursorConverter> converterClass) {
        super(context, c, converterClass);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getAdapterType();
    }

    protected View getTypedView(ViewGroup parent) {
        View view = null;
        if (getAdapterType() == TYPE_SMALL) {
            view = inflater.inflate(R.layout.adapter_advertisement, parent, false);
        } else {
            view = inflater.inflate(R.layout.adapter_big_photos, parent, false);
        }
        return view;
    }

    @Override
    public View newView(Context context, Cursor cursor, AdvertisementCursorConverter cursorConverter, ViewGroup parent) {
        View view = getTypedView(parent);
        AdvertHolder holder = new AdvertHolder();
        holder.location = (ArtJokerTextView) view.findViewById(R.id.tv_advert_location);
        holder.icon = (ImageView) view.findViewById(R.id.iv_advert_header_image);
        holder.date = (ArtJokerTextView) view.findViewById(R.id.tv_advert_date);
        holder.price = (ArtJokerTextView) view.findViewById(R.id.tv_advert_price);
        holder.title = (ArtJokerTextView) view.findViewById(R.id.tv_advert_title);
        holder.type = (ArtJokerTextView) view.findViewById(R.id.tv_type);
        holder.advertImage = (SimpleDraweeView) view.findViewById(R.id.iv_advert_image);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor, AdvertisementCursorConverter cursorConverter) {
        AdvertHolder holder = (AdvertHolder) view.getTag();
        if (cursorConverter != null && cursorConverter.isValid()) {
            holder.title.setText(cursorConverter.getObject().getTitle());
            holder.date.setText(simpleDateFormat.format(new Date(
                    SystemHelper.getInstance().getTimeInMillisFromSec(cursorConverter.getObject().getCreatedAt()))));

            float price = cursorConverter.getObject().getPrice();
            if (price > 0) {
                holder.price.setText(String.format(PRICE_FORMAT,
                        String.valueOf(TextUtils.getInstance().getStringFromDoubleValue(cursorConverter.getObject().getPrice())),
                        Settings.getInstance(context).getCurrencyById(cursorConverter.getObject().getCurrencyId()).getName()));
            } else {
                holder.price.setText(context.getResources().getString(R.string.advert_price_type_free));
            }
            holder.location.setText(buildAddress(cursorConverter.getObject()));
            if (cursorConverter.getObject().getPhotoUrl() != null) {
                holder.advertImage.setImageURI(cursorConverter.getObject().getPhotoUrl());
            }
            if (cursorConverter.getObject().getType() == AdvertType.BUY) {
                holder.type.setText(context.getString(R.string.buy_title));
                holder.type.setTextColor(context.getResources().getColor(R.color.green_header));
            } else {
                holder.type.setText(context.getString(R.string.sell_title));
                holder.type.setTextColor(context.getResources().getColor(R.color.red_text_color));
            }
        }
    }

    private String buildAddress(Advertisement advertisement) {
        StringBuilder builder = new StringBuilder();
        if (!android.text.TextUtils.isEmpty(advertisement.getCountryName())) {
            builder.append(advertisement.getCountryName());
        } else if (!android.text.TextUtils.isEmpty(advertisement.getCityName())) {
            builder.append(advertisement.getCityName());
            return builder.toString();
        }

        if (!android.text.TextUtils.isEmpty(advertisement.getCityName())) {
            builder.append(", ");
            builder.append(advertisement.getCityName());
        }

        return builder.toString();
    }

    public void setAdapterType(int adapterType) {
        this.adapterType = adapterType;
        notifyDataSetChanged();
    }

    public int getAdapterType() {
        return adapterType;
    }

    protected class AdvertHolder {
        ArtJokerTextView location;
        ArtJokerTextView date;
        ArtJokerTextView price;
        ArtJokerTextView title;
        ArtJokerTextView type;
        SimpleDraweeView advertImage;
        ImageView icon;
        CheckedTextView delete;
    }
}
