package com.compet.petdoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mu on 2016-10-24.
 */

public class RegionGridAdapter extends BaseAdapter {

    private Context context;

    private int layout;

    private ArrayList<String> items = new ArrayList<>();

    private LayoutInflater inflater;

    public void add(String image) {
        items.add(image);
        notifyDataSetChanged();
    }

    public void addAll(List<String> image) {

        items.addAll(image);
        notifyDataSetChanged();
    }

    public void clear() {

        items.clear();
        notifyDataSetChanged();
    }


    public RegionGridAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
        this.items = items;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.isEmpty() ? 0 : items.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(layout, null);

        TextView regionView = (TextView)convertView.findViewById(R.id.text_region);
        regionView.setText(items.get(position));

        return convertView;
    }

}
