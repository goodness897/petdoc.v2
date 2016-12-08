package com.compet.petdoc.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.compet.petdoc.R;
import com.compet.petdoc.controller.RegionGridAdapter;
import com.compet.petdoc.data.RegionItem;
import com.compet.petdoc.util.HospitalURL;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseFragment {

    private RegionGridAdapter mAdapter;

    private List<RegionItem> regionList;

    private Context mContext;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
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
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);
        initToolBar("구 검색", view, mContext);

        regionList = new ArrayList<>();
        mAdapter = new RegionGridAdapter(getContext(), R.layout.view_region);

        Button button = (Button)view.findViewById(R.id.btn_my_location);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
                fm.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit();
            }
        });
        GridView gridView = (GridView)view.findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
                fm.beginTransaction()
                  .replace(R.id.container, MainFragment.newInstance(regionList.get(position)))
                  .commit();

            }

        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
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
