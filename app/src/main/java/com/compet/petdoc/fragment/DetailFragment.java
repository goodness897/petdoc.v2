package com.compet.petdoc.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.compet.petdoc.R;
import com.compet.petdoc.data.HospitalItem;
import com.compet.petdoc.util.Constants;

public class DetailFragment extends BaseFragment {

    private HospitalItem hospitalItem;

    private static final String TAG = DetailFragment.class.getSimpleName();

    private Context mContext;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(HospitalItem item) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.HOSPITAL, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            hospitalItem = (HospitalItem)getArguments().getSerializable(Constants.HOSPITAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        initToolBar("", view, mContext);

        toolbar.bringToFront();

        final StringBuilder stringBuilder = new StringBuilder();

        TextView titleView = (TextView)view.findViewById(R.id.text_title);
        TextView locationView = (TextView)view.findViewById(R.id.text_location);

        titleView.setText(hospitalItem.getHosName());
        locationView.setText(hospitalItem.getAddress());

        String path = "https://maps.googleapis.com/maps/api/staticmap?center=" + hospitalItem.getLongitude()
                      + ","
                      + hospitalItem.getLatitude()
                      + "&zoom=14&size=400x400&markers=icon:http://www.free-icons-download.net/images/location-map-marker-icons-47237.png%7C"
                      + hospitalItem.getLongitude()
                      + ","
                      + hospitalItem.getLatitude();
        Log.d(TAG, "url : " + path);

        ImageView hospitalImage = (ImageView)view.findViewById(R.id.image_hospital);
        Glide.with(mContext).load(path).centerCrop().placeholder(R.drawable.image_petdoc).into(hospitalImage);

        Button callButton = (Button)view.findViewById(R.id.btn_call);
        if (hospitalItem.getPhoneNumber().equals("")) {
            callButton.setEnabled(false);
            callButton.setBackgroundColor(Color.GRAY);
        } else {
            callButton.setEnabled(true);
        }

        callButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!hospitalItem.getPhoneNumber().startsWith("02")) {
                    stringBuilder.append("tel:02-").append(hospitalItem.getPhoneNumber());
                } else {
                    stringBuilder.append("tel:").append(hospitalItem.getPhoneNumber());
                }

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(stringBuilder.toString()));
                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }
}
