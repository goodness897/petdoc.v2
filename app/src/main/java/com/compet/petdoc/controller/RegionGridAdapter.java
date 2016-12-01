package com.compet.petdoc.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.compet.petdoc.R;
import com.compet.petdoc.data.RegionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mu on 2016-10-24.
 */

public class RegionGridAdapter extends BaseAdapter {

    private Context context;

    private int layout;

    private ArrayList<RegionItem> items = new ArrayList<>();

    private LayoutInflater inflater;

    public void add(RegionItem regionItem) {
        items.add(regionItem);
        notifyDataSetChanged();
    }

    public void addAll(List<RegionItem> regionItemList) {

        items.addAll(regionItemList);
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
    public RegionItem getItem(int position) {
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
        regionView.setText(items.get(position).getName());

        return convertView;
    }

}
