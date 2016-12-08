package com.compet.petdoc.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.compet.petdoc.R;

/**
 * Created by Mu on 2016-12-02.
 */

public class BaseFragment extends Fragment {

    protected  Toolbar toolbar;

    public void initToolBar(final String title, View view, Context context) {

        toolbar = (Toolbar)view.findViewById(R.id.main_toolbar);
//        toolbar.bringToFront();
        AppCompatActivity activity = (AppCompatActivity)context;

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titleText = (TextView)view.findViewById(R.id.toolbar_title);
        titleText.setText(title);
        if (!title.equals(getString(R.string.app_name))) {
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
        }

    }
}
