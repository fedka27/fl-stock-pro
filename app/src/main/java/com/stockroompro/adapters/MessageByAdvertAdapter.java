package com.stockroompro.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.core.TextUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stockroompro.R;
import com.stockroompro.fragments.AdvertDetailFragment;
import com.stockroompro.models.Settings;
import com.stockroompro.models.converters.MessageByAdvertCursorConverter;

import java.util.Date;

/**
 * Created by user on 14.04.15.
 */
public class MessageByAdvertAdapter extends DateFormatConverterCursorAdapter<MessageByAdvertCursorConverter> {
    public MessageByAdvertAdapter(Context context, Cursor c, Class<MessageByAdvertCursorConverter> converterClass) {
        super(context, c, converterClass);
    }


    @Override
    public View newView(Context context, Cursor cursor, MessageByAdvertCursorConverter cursorConverter, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_messages_by_advert, parent, false);
        ViewHolder holder = new ViewHolder(
                (TextView) view.findViewById(R.id.tv_advert_title),
                (TextView) view.findViewById(R.id.tv_advert_price),
                (TextView) view.findViewById(R.id.tv_message_by_advert),
                (TextView) view.findViewById(R.id.tv_message_by_advert_time_and_date),
                (SimpleDraweeView) view.findViewById(R.id.iv_advert_header_image)
        );
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor, MessageByAdvertCursorConverter cursorConverter) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (cursorConverter != null && cursorConverter.isValid()) {
            holder.title.setText(cursorConverter.getObject().getTitle());
            holder.messageText.setText(cursorConverter.getObject().getText());
            holder.date.setText(simpleDateFormat.format(new Date(SystemHelper.getInstance().getTimeInMillisFromSec(cursorConverter.getObject().getDate()))));
            holder.photo.setImageURI(cursorConverter.getObject().getImage());
            String price = cursorConverter.getObject().getPrice();
            if (price != null) {
                holder.price.setText(String.format(AdvertDetailFragment.AdvertisementParameters.PRICE_FORMAT,
                        TextUtils.getInstance().getStringFromDoubleValue(Double.parseDouble(price)),
                        Settings.getInstance(context).getCurrencyById(cursorConverter.getObject().getCurrencyId()).getName()));
            } else {
                holder.price.setText("");
            }
        }
    }

    private static class ViewHolder {
        TextView title;
        TextView price;
        TextView messageText;
        TextView date;
        SimpleDraweeView photo;

        private ViewHolder(TextView title, TextView price, TextView messageText, TextView date, SimpleDraweeView photo) {
            this.title = title;
            this.price = price;
            this.messageText = messageText;
            this.date = date;
            this.photo = photo;

        }
    }
}
