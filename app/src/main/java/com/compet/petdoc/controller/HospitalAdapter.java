package com.compet.petdoc.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.compet.petdoc.R;
import com.compet.petdoc.data.HospitalItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mu on 2016-10-20.
 */
public class HospitalAdapter extends BaseAdapter {

    private Context context = null;

    private List<HospitalItem> items = new ArrayList<>();

    public HospitalAdapter(Context context) {
        super();
        this.context = context;
    }

    public void add(HospitalItem hospitalItem) {
        items.add(hospitalItem);
        notifyDataSetChanged();
    }

    public void addItem(List<HospitalItem> items) {
        this.items.addAll(items);
        notifyDataSetChanged();

    }

    public void addAll(List<HospitalItem> items) {
        if (!this.items.isEmpty())
            this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public List<HospitalItem> getListItem() {
        return items;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public HospitalItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_hospital, null);

        }

        TextView titleView = (TextView)convertView.findViewById(R.id.text_hospital_name);
        TextView addressView = (TextView)convertView.findViewById(R.id.text_hospital_address);

        HospitalItem hospitalItem = items.get(position);
        titleView.setText(hospitalItem.getHosName());
        addressView.setText(hospitalItem.getAddress());

        return convertView;

    }
}
