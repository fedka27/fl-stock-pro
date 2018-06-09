package com.stockroompro.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.artjoker.tool.core.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stockroompro.R;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 28.04.15.
 */
public class AddAdvertPhotoAdapter extends BaseAdapter {

    private static String SERVER_PHOTO_TYPE = "http";
    private static String STORAGE_IMAGE_TYPE = "file:///";

    public interface OnDeletePhotoListener {
        void onDeleteClick(int position);
    }

    private Context context;
    private OnDeletePhotoListener listener;
    private ArrayList<String> items;

    public AddAdvertPhotoAdapter(Context context, ArrayList<String> items, OnDeletePhotoListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.onDeleteClick( (int) v.getTag());
        }
    };

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.add_advert_photo_item, null);
        }
        ViewHolder holder = new ViewHolder();
        holder.setImage((SimpleDraweeView) convertView.findViewById(R.id.iv_icon));
        holder.setCloseImage((ImageView) convertView.findViewById(R.id.iv_delete));
        holder.getCloseImage().setTag(position);
        holder.getCloseImage().setOnClickListener(onClickListener);

        String path = items.get(position);
        if (path.contains(SERVER_PHOTO_TYPE)) {
            holder.getImage().setImageURI(path);
        } else {
            if (path.startsWith(STORAGE_IMAGE_TYPE)) path = path.substring(8);
            Bitmap bm = ImageUtils.decodeSampledBitmapFromPath(path, 200);
            holder.getImage().setImageBitmap(bm);
        }

        return convertView;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public String getItemName(int position) {
        return items.get(position);
    }

    private static class ViewHolder {

        SimpleDraweeView image;
        ImageView closeImage;

        public void setCloseImage(ImageView closeImage) {
            this.closeImage = closeImage;
        }

        public void setImage(SimpleDraweeView image) {
            this.image = image;
        }

        public ImageView getCloseImage() {
            return closeImage;
        }

        public SimpleDraweeView getImage() {
            return image;
        }
    }
}
