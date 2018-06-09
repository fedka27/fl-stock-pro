package com.stockroompro.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.artjoker.tool.core.SystemHelper;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stockroompro.R;
import com.stockroompro.models.converters.NotificationCursorConverter;

import java.util.Date;

import static com.stockroompro.models.NotificationItem.NotificationsTypes.ADVERT_COMMITED;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.ADVERT_DISMISSED;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.COMMENT_DISLIKE;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.COMMENT_FOR_MY_ADVERT;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.COMMENT_LIKE;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.GREETING;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.NEW_MESSAGE;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.RATING;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.RESPONSE_ON_COMMENT;
import static com.stockroompro.models.NotificationItem.NotificationsTypes.SYSTEM_INFO;

/**
 * Created by user on 01.06.15.
 */
public class NotificationAdapter extends DateFormatConverterCursorAdapter<NotificationCursorConverter> {
    private Context context;
    private View.OnClickListener onClickListener;

    public NotificationAdapter(Context context, Cursor c, Class<NotificationCursorConverter> converterClass, View.OnClickListener onClickListener) {
        super(context, c, converterClass);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public View newView(Context context, Cursor cursor, NotificationCursorConverter cursorConverter, ViewGroup parent) {
        return null;
    }

    @Deprecated
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getCursor() != null && !getCursor().isClosed() && !getCursor().isBeforeFirst() && !getCursor().isAfterLast()) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_notification_ratings, parent, false);
            ViewHolder holder = new ViewHolder(
                    position,
                    (TextView) convertView.findViewById(R.id.tv_name),
                    (TextView) convertView.findViewById(R.id.tv_message),
                    (TextView) convertView.findViewById(R.id.tv_text),
                    (TextView) convertView.findViewById(R.id.tv_date),
                    (SimpleDraweeView) convertView.findViewById(R.id.iv_photo),
                    (RatingBar) convertView.findViewById(R.id.rating_bar),
                    (ImageView) convertView.findViewById(R.id.iv_black_icon)
            );
            convertView.setTag(holder);
        }
        bindView(convertView, context, getCursor());
        return convertView;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor, NotificationCursorConverter cursorConverter) {

        if (cursorConverter != null && cursorConverter.isValid()) {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.date.setText(simpleDateFormat.format(new Date(SystemHelper.getInstance().getTimeInMillisFromSec(cursorConverter.getObject().getDate()))));
            switch (cursorConverter.getObject(holder.position).getType()) {
                case GREETING:
                    setSystemContent(cursorConverter, holder, R.drawable.greeting);
                    break;

                case COMMENT_FOR_MY_ADVERT:
                    setContent(holder, cursorConverter, R.drawable.comm_black);
                    break;

                case RESPONSE_ON_COMMENT:
                    setContent(holder, cursorConverter, R.drawable.comm_black);
                    break;

                case NEW_MESSAGE:
                    setContent(holder, cursorConverter, R.drawable.user_black);
                    break;

                case COMMENT_LIKE:
                    setContent(holder, cursorConverter, R.drawable.like_green_small);
                    holder.message.setText(cursorConverter.getObject().getCommentText());
                    break;

                case COMMENT_DISLIKE:
                    setContent(holder, cursorConverter, R.drawable.like_red_small);
                    holder.message.setText(cursorConverter.getObject().getCommentText());
                    break;

                case RATING:
                    holder.photo.setOnClickListener(onClickListener);
                    holder.rating.setVisibility(View.VISIBLE);
                    holder.rating.setRating(cursorConverter.getObject().getRating());
                    holder.title.setText(cursorConverter.getObject().getUserName());
                    holder.text.setText(cursorConverter.getObject().getText());
                    loadImage(holder, cursorConverter);
                    break;

                case ADVERT_COMMITED:
                    setSystemContent(cursorConverter, holder, R.drawable.accept);
                    break;

                case ADVERT_DISMISSED:
                    setSystemContent(cursorConverter, holder, R.drawable.dismiss);
                    break;

                case SYSTEM_INFO:
                    setSystemContent(cursorConverter, holder, R.drawable.system);
                    break;
            }
        }
    }

    private void setContent(ViewHolder holder, NotificationCursorConverter cursorConverter, int imageResource) {
        holder.photo.setOnClickListener(onClickListener);
        holder.title.setText(cursorConverter.getObject().getUserName());
        holder.text.setText(cursorConverter.getObject().getText());
        holder.smallIcon.setVisibility(View.VISIBLE);
        holder.smallIcon.setImageResource(imageResource);
        loadImage(holder, cursorConverter);
    }

    private void loadImage(ViewHolder holder, NotificationCursorConverter cursorConverter) {
        if (cursorConverter.getObject().getUserPhoto() != null) {
            holder.photo.setImageURI(cursorConverter.getObject().getUserPhoto());
        }
    }

    private void setSystemContent(NotificationCursorConverter cursorConverter, ViewHolder holder, int accept) {
        holder.title.setVisibility(View.GONE);
        holder.text.setText(cursorConverter.getObject().getText());
        holder.photo.setImageResource(accept);
        holder.smallIcon.setVisibility(View.GONE);
    }

    public static class ViewHolder {
        TextView title;
        TextView message;
        TextView text;
        TextView date;
        SimpleDraweeView photo;
        ImageView smallIcon;
        RatingBar rating;
        public int position;

        private ViewHolder(int position, TextView title, TextView message, TextView text, TextView date, SimpleDraweeView photo, RatingBar rating, ImageView smallIcon) {
            this.position = position;
            this.title = title;
            this.message = message;
            this.text = text;
            this.date = date;
            this.photo = photo;
            this.rating = rating;
            this.smallIcon = smallIcon;
        }
    }
}
