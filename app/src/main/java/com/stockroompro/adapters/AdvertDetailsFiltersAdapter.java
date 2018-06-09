package com.stockroompro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stockroompro.R;

import java.util.List;

/**
 * Created by Alexandr.Bagach on 17.03.2016.
 */
public class AdvertDetailsFiltersAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> filtersList;

    public AdvertDetailsFiltersAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.filtersList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilterHolder filterHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_full_advert_filter_item, parent, false);
            filterHolder = new FilterHolder();
            filterHolder.setFilterName((TextView) convertView.findViewById(R.id.fragment_full_advert_filter_name));
            convertView.setTag(filterHolder);
        } else {
            filterHolder = (FilterHolder) convertView.getTag();
        }

        filterHolder.filterName.setText(filtersList.get(position));

        return convertView;
    }

    private class FilterHolder {

        private TextView filterName;

        public TextView getFilterName() {
            return filterName;
        }

        public void setFilterName(TextView filterName) {
            this.filterName = filterName;
        }
    }
}
