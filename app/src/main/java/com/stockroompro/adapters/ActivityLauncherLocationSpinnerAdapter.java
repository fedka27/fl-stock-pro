package com.stockroompro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artjoker.tool.core.TypefaceProducer;
import com.stockroompro.R;
import com.stockroompro.adapters.LocationSpinnerAdapter.OnLocationSpinnerItemClickListener;
import com.stockroompro.models.spinners.Location;
import com.stockroompro.models.spinners.SpinnerItem;

import java.util.ArrayList;

/**
 * Created by Alexandr.Bagach on 30.09.2015.
 */
public class ActivityLauncherLocationSpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    private Context context;
    private SpinnerItem[] data;
    private ArrayList<Location> locationPath;
    private OnLocationSpinnerItemClickListener listener;

    public ActivityLauncherLocationSpinnerAdapter(Context context,
                                                  int resource,
                                                  SpinnerItem[] objects,
                                                  ArrayList<Location> path,
                                                  OnLocationSpinnerItemClickListener l) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_location_activity_launcher, parent, false);
            holder = new HintHolder();
            holder.setLocationName((TextView) convertView.findViewById(R.id.spinner_location_activity_launcher_location_title));
            holder.setParentLocationName((TextView) convertView.findViewById(R.id.spinner_location_activity_launcher_parent_title));
            holder.getLocationName().setTypeface(TypefaceProducer.getInstance().get(context, TypefaceProducer.Key.BLACK));
            holder.getParentLocationName().setTypeface(TypefaceProducer.getInstance().get(context, TypefaceProducer.Key.LIGHT));
            convertView.setTag(holder);
        } else {
            holder = (HintHolder) convertView.getTag();
        }

        if (locationPath.size() > 0 && locationPath.size() < 4) {
            switch (locationPath.size()) {
                case 1:
                    holder.getParentLocationName().setVisibility(View.GONE);
                    holder.getLocationName().setText(locationPath.get(0).getLocationName());
                    break;

                case 2:
                case 3:
                    holder.getParentLocationName().setVisibility(View.VISIBLE);
                    holder.getLocationName().setText(locationPath.get(locationPath.size() - 1).getLocationName());
                    holder.getParentLocationName().setText(locationPath.get(0).getLocationName());
                    break;
            }
        } else {
            holder.getParentLocationName().setVisibility(View.GONE);
            holder.getLocationName().setText(context.getResources().getString(R.string.activity_launcher_location_spinner_hint));
        }
        return convertView;
    }

    public String getLocationName(int position) {
        return getItem(position).getName();
    }

    @Override
    public SpinnerItem getItem(int position) {
        return data[position];
    }

    private class HintHolder {

        private TextView parentLocationName;
        private TextView locationName;

        public TextView getParentLocationName() {
            return parentLocationName;
        }

        public void setParentLocationName(TextView parentLocationName) {
            this.parentLocationName = parentLocationName;
        }

        public TextView getLocationName() {
            return locationName;
        }

        public void setLocationName(TextView locationName) {
            this.locationName = locationName;
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
