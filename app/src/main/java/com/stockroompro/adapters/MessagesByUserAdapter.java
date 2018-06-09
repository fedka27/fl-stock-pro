package com.stockroompro.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artjoker.tool.core.SystemHelper;
import com.stockroompro.R;
import com.stockroompro.models.converters.MessageByUserCursorConverter;

import java.util.Date;


/**
 * Created by user on 10.04.15.
 */
public class MessagesByUserAdapter extends DateFormatConverterCursorAdapter<MessageByUserCursorConverter> {


    public MessagesByUserAdapter(Context context, Cursor c, Class<MessageByUserCursorConverter> converterClass) {
        super(context, c, converterClass);
    }

    @Override
    public View newView(Context context, Cursor cursor, MessageByUserCursorConverter cursorConverter, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_messages_from_user, parent, false);
        ViewHolder holder = new ViewHolder((TextView) view.findViewById(R.id.tv_name_user),
                (TextView) view.findViewById(R.id.tv_message_text),
                (TextView) view.findViewById(R.id.tv_message_date));
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor, MessageByUserCursorConverter cursorConverter) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (cursorConverter != null && cursorConverter.isValid()) {
            holder.userName.setText(cursorConverter.getObject().getInterlocatorName());
            holder.messageText.setText(cursorConverter.getObject().getText());
            holder.date.setText(simpleDateFormat.format(new Date(SystemHelper.getInstance().getTimeInMillisFromSec(cursorConverter.getObject().getDate()))));
        }

    }

    private static class ViewHolder {
        TextView userName;
        TextView messageText;
        TextView date;

        private ViewHolder(TextView userName, TextView messageText, TextView date) {
            this.userName = userName;
            this.messageText = messageText;
            this.date = date;

        }
    }
}
