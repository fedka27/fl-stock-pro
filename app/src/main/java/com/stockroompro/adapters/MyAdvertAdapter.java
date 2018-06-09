package com.stockroompro.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.core.TextUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stockroompro.R;
import com.stockroompro.fragments.AdvertType;
import com.stockroompro.models.Advertisement;
import com.stockroompro.models.Settings;
import com.stockroompro.models.converters.AdvertisementCursorConverter;

import java.util.Date;

import static com.stockroompro.fragments.AdvertDetailFragment.AdvertisementParameters.PRICE_FORMAT;

/**
 * Created by user on 18.05.15.
 */
public class MyAdvertAdapter extends DateFormatConverterCursorAdapter<AdvertisementCursorConverter> {
    public MyAdvertAdapter(Context context, Cursor c, Class<AdvertisementCursorConverter> converterClass) {
        super(context, c, converterClass);
    }

    @Override
    public View newView(Context context, Cursor cursor, AdvertisementCursorConverter cursorConverter, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_account_advert_list, parent, false);
        AdvertHolder holder = new AdvertHolder();
        holder.location = (ArtJokerTextView) view.findViewById(R.id.adapter_account_advert_location);
        holder.date = (ArtJokerTextView) view.findViewById(R.id.adapter_account_advert_date);
        holder.price = (ArtJokerTextView) view.findViewById(R.id.adapter_account_advert_price);
        holder.title = (ArtJokerTextView) view.findViewById(R.id.adapter_account_advert_title);
        holder.advertImage = (SimpleDraweeView) view.findViewById(R.id.adapter_account_advert_image);
        holder.active = (ToggleButton) view.findViewById(R.id.adapter_account_advert_toggle);
        holder.type = (ArtJokerTextView) view.findViewById(R.id.adapter_account_advert_type);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor, AdvertisementCursorConverter cursorConverter) {
        AdvertHolder holder = (AdvertHolder) view.getTag();
        if (cursorConverter != null && cursorConverter.isValid()) {
            holder.location.setText(buildAddress(cursorConverter.getObject()));
            holder.date.setText(simpleDateFormat.format(new Date(SystemHelper.getInstance().getTimeInMillisFromSec(cursorConverter.getObject().getCreatedAt()))));

            float price = cursorConverter.getObject().getPrice();
            if (price > 0) {
                holder.price.setText(String.format(PRICE_FORMAT,
                        String.valueOf(TextUtils.getInstance().getStringFromDoubleValue(cursorConverter.getObject().getPrice())),
                        Settings.getInstance(context).getCurrencyById(cursorConverter.getObject().getCurrencyId()).getName()));
            } else {
                holder.price.setText(context.getResources().getString(R.string.advert_price_type_free));
            }
            holder.title.setText(cursorConverter.getObject().getTitle());

            if(cursorConverter.getObject().getPhotoUrl() != null) {
                holder.advertImage.setImageURI(cursorConverter.getObject().getPhotoUrl());
            }
            setActiveType(context, holder, cursorConverter);

            if (cursorConverter.getObject().getType() == AdvertType.BUY) {
                holder.type.setText(context.getString(R.string.buy_title));
            } else {
                holder.type.setText(context.getString(R.string.sell_title));
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

    private void setActiveType(Context context, AdvertHolder holder, AdvertisementCursorConverter cursorConverter) {
        switch (cursorConverter.getObject().getApproved()) {
            case Advertisement.ADVERT_UNAPPROVED:
                holder.active.setTextOff(context.getResources().getString(R.string.advert_unapproved));
                holder.active.setChecked(false);
                break;

            case Advertisement.ADVERT_APPROVED:
                if (cursorConverter.getObject().getExpired() == Advertisement.ADVERT_EXPIRED) {
                    holder.active.setTextOff(context.getResources().getString(R.string.advert_inactive_title));
                    holder.active.setChecked(false);
                } else if (cursorConverter.getObject().getActive() == Advertisement.ADVERT_INACTIVE) {
                    holder.active.setTextOff(context.getResources().getString(R.string.advert_inactive_title));
                    holder.active.setChecked(false);
                } else {
                    holder.active.setChecked(true);
                }
                break;
        }
    }

    protected class AdvertHolder {
        ArtJokerTextView location;
        ArtJokerTextView date;
        ArtJokerTextView price;
        ArtJokerTextView title;
        ArtJokerTextView type;
        SimpleDraweeView advertImage;
        ToggleButton active;
    }
}
