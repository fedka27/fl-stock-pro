package com.stockroompro.adapters.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Александр on 15.10.2016.
 */

public class BasicAdapter<T> extends ArrayAdapter<T> {

    public LayoutInflater inflater;

    public BasicAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
