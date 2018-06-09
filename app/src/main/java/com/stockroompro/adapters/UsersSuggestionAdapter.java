package com.stockroompro.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.stockroompro.R;
import com.stockroompro.fragments.AdvertDetailFragment;
import com.stockroompro.models.columns.UserDataColumns;

/**
 * Created by bagach.alexandr on 08.07.15.
 */
public class UsersSuggestionAdapter extends CursorAdapter {
    private int count;

    public UsersSuggestionAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        count = c != null ? c.getCount() : 0;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_search_by_users, parent, false);
        ViewHolder holder = new ViewHolder(((SimpleDraweeView) view.findViewById(R.id.iv_user_image)),
                ((TextView) view.findViewById(R.id.tv_user_name)));
        view.setTag(holder);
        return view;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.userPhoto.setImageURI(cursor.getString(cursor.getColumnIndexOrThrow(UserDataColumns.PICTURE_URL)));
        holder.userName.setText(String.format(AdvertDetailFragment.AdvertisementParameters.USER_NAME_FORMAT,
                                              cursor.getString(cursor.getColumnIndexOrThrow(UserDataColumns.FIRST_NAME)),
                                              cursor.getString(cursor.getColumnIndexOrThrow(UserDataColumns.LAST_NAME))));
        holder.userId = cursor.getLong(cursor.getColumnIndexOrThrow(UserDataColumns.ID));
    }

    @Override
    public void changeCursor(Cursor cursor) {
        if (cursor != null) {
            count = cursor.getCount();
            super.changeCursor(cursor);
        } else {
            count = 0;
        }
    }

    public static class ViewHolder {

        SimpleDraweeView userPhoto;
        TextView userName;
        long userId;

        private ViewHolder(SimpleDraweeView imageView, TextView name) {
            this.userPhoto = imageView;
            this.userName = name;
        }
    }
}
