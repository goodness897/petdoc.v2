package com.compet.petdoc.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.compet.petdoc.R;
import com.compet.petdoc.data.HospitalItem;
import com.compet.petdoc.fragment.MapDocFragment;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final StringBuilder stringBuilder = new StringBuilder();
        if (!items.get(position).getPhoneNumber().startsWith("02")) {
            stringBuilder.append("tel:02-").append(items.get(position).getPhoneNumber());
        } else {
            stringBuilder.append("tel:").append(items.get(position).getPhoneNumber());
        }

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_hospital, null);

        }

        TextView titleView = (TextView)convertView.findViewById(R.id.text_hospital_name);
        TextView addressView = (TextView)convertView.findViewById(R.id.text_hospital_address);
        ImageView mapView = (ImageView)convertView.findViewById(R.id.image_map);


        Button mapButton = (Button)convertView.findViewById(R.id.btn_map);
        Button callButton = (Button)convertView.findViewById(R.id.btn_call);

        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                manager.beginTransaction()
                       .replace(R.id.container, MapDocFragment.newInstance(getItem(position)))
                       .addToBackStack(null)
                       .commit();
            }
        });

        if(items.get(position).getPhoneNumber().equals("")) {
            callButton.setEnabled(false);
            callButton.setTextColor(Color.GRAY);
        }
        callButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(stringBuilder.toString()));
                context.startActivity(intent);
            }
        });
        
        

        HospitalItem hospitalItem = items.get(position);


        titleView.setText(hospitalItem.getHosName());
        addressView.setText(hospitalItem.getAddress());


        return convertView;

    }

}
