package com.compet.petdoc.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.compet.petdoc.R;
import com.compet.petdoc.fragment.RegionListFragment;

public class MainActivity extends BaseActivity {

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, new RegionListFragment(), "Region").commit();
    }
}
