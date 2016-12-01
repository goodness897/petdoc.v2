package com.compet.petdoc.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.compet.petdoc.R;
import com.compet.petdoc.controller.RegionGridAdapter;
import com.compet.petdoc.data.RegionItem;
import com.compet.petdoc.util.HospitalURL;

import java.util.ArrayList;
import java.util.List;

public class RegionListFragment extends Fragment {

    private RegionGridAdapter mAdapter;

    private List<RegionItem> regionList;

    public RegionListFragment() {
        // Required empty public constructor
    }

    public static RegionListFragment newInstance() {
        RegionListFragment fragment = new RegionListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_region_list, container, false);

        regionList = new ArrayList<>();
        mAdapter = new RegionGridAdapter(getContext(), R.layout.view_region);
        GridView gridView = (GridView)view.findViewById(R.id.gridView);

        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                getFragmentManager().beginTransaction()
                                    .replace(R.id.container, ListDocFragment.newInstance(regionList.get(position)))
                                    .addToBackStack(null)
                                    .commit();
            }

        });

        initData();

        return view;
    }

    private void initData() {

        Resources res = getResources();
        String[] arrString = res.getStringArray(R.array.seoul_region);

        for (int i = 0; i < arrString.length; i++) {
            RegionItem regionItem = new RegionItem();
            regionItem.setName(arrString[i]);
            regionItem.setUrl(HospitalURL.URL[i]);
            regionList.add(regionItem);
        }

        if (mAdapter.isEmpty()) {
            mAdapter.addAll(regionList);
        } else {
            mAdapter.clear();
            mAdapter.addAll(regionList);
        }
    }
}
