package com.stockroompro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artjoker.tool.core.TypefaceProducer;
import com.stockroompro.R;
import com.stockroompro.fragments.AddAdvertisementFragment;
import com.stockroompro.models.spinners.Location;
import com.stockroompro.models.spinners.SpinnerItem;

import java.util.ArrayList;


/**
 * Created by bagach.alexandr on 12.05.15.
 */
public class LocationSpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    public interface OnLocationSpinnerItemClickListener {
        void onLocationItemClick(int position, long id);
    }

    private Context context;
    private SpinnerItem[] data;
    private ArrayList<Location> locationPath;
    private String title = "";
    private OnLocationSpinnerItemClickListener listener;

    public LocationSpinnerAdapter(Context context, int resource, SpinnerItem[] objects, ArrayList<Location> path, OnLocationSpinnerItemClickListener l) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
        this.locationPath = path;
        this.listener = l;
    }

    private View.OnClickListener itemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ((v.getTag(R.integer.spinner_item_tag_key) != null) && listener != null) {
                String[] items = (String[]) v.getTag(R.integer.spinner_item_tag_key);
                listener.onLocationItemClick(Integer.valueOf(items[0]), Long.valueOf(items[1]));
            }
        }
    };

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ItemHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.advertisement_spinner_item, parent, false);
            holder = new ItemHolder();
            holder.setTextView((TextView) convertView.findViewById(R.id.advert_city_text));
            holder.setRootView((LinearLayout) convertView.findViewById(R.id.spinner_item_container));
            holder.getTextView().setTypeface(TypefaceProducer.getInstance().get(context, TypefaceProducer.Key.LIGHT));
            if (listener != null) {
                holder.getRootView().setOnClickListener(itemClick);
            }
            convertView.setTag(R.integer.spinner_holder_tag_key, holder);
        } else {
            holder = (ItemHolder) convertView.getTag(R.integer.spinner_holder_tag_key);
        }

        holder.getTextView().setText(data[position].getName());
        holder.getRootView().setTag(R.integer.spinner_item_tag_key,
                new String[]{String.valueOf(position), String.valueOf(data[position].getId())});
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HintHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_header_view, parent, false);
            holder = new HintHolder();
            holder.setHint((TextView) convertView.findViewById(R.id.tv_spinner_city_hint));
            holder.setTextView((TextView) convertView.findViewById(R.id.city));
            holder.getHint().setTypeface(TypefaceProducer.getInstance().get(context, TypefaceProducer.Key.LIGHT));
            holder.getTextView().setTypeface(TypefaceProducer.getInstance().get(context, TypefaceProducer.Key.LIGHT));
            convertView.setTag(holder);
        } else {
            holder = (HintHolder) convertView.getTag();
        }

        setLocationPath();
        holder.getTextView().setText(title);
        holder.getHint().setText(context.getResources().getString(R.string.tv_spinner_header_view_city_hint));
        return convertView;
    }

    public void setLocationPath() {
        if (locationPath != null) {
            title = "";
            for (Location location : locationPath) {
                title += location.getLocationName() + " / ";
            }

            notifyDataSetChanged();
        }
    }

    public void clearTitle() {
        title = "";
        notifyDataSetChanged();
    }

    public String getLocationName(int position) {
        return getItem(position).getName();
    }

    public long getPatentId(int position) {
        return getItem(position).getParentId();
    }

    @Override
    public SpinnerItem getItem(int position) {
        return data[position];
    }

    private class HintHolder {

        private TextView text;
        private TextView hint;

        public TextView getTextView() {
            return text;
        }

        public void setTextView(TextView text) {
            this.text = text;
        }

        public TextView getHint() {
            return hint;
        }

        public void setHint(TextView hint) {
            this.hint = hint;
        }
    }

    private class ItemHolder {

        private LinearLayout rootView;
        private TextView text;

        public LinearLayout getRootView() {
            return rootView;
        }

        public void setRootView(LinearLayout rootView) {
            this.rootView = rootView;
        }

        public TextView getTextView() {
            return text;
        }

        public void setTextView(TextView text) {
            this.text = text;
        }
    }
}
