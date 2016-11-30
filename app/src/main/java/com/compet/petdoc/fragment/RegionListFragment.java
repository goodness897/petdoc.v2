package com.compet.petdoc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.compet.petdoc.R;
import com.compet.petdoc.RegionGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class RegionListFragment extends Fragment {

    private RegionGridAdapter mAdapter;

    private List<String> regionList;

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

        mAdapter = new RegionGridAdapter(getContext(), R.layout.view_region);
        GridView gridView = (GridView)view.findViewById(R.id.gridView);
        regionList = new ArrayList<>();
        regionList.add("광진구");
        regionList.add("동대문구");
        regionList.add("강남구");

        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                getFragmentManager().beginTransaction()
                                    .replace(R.id.container, new ListDocFragment())
                                    .addToBackStack(null)
                                    .commit();
            }

        });

        initData();

        return view;
    }

    private void initData() {
        if (mAdapter.isEmpty()) {
            mAdapter.addAll(regionList);
        } else {
            mAdapter.clear();
            mAdapter.addAll(regionList);
        }
    }
}
