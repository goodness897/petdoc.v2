package com.compet.petdoc.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.compet.petdoc.R;
import com.compet.petdoc.RegionGridAdapter;
import com.compet.petdoc.manager.NetworkManager;
import com.compet.petdoc.manager.NetworkRequest;
import com.compet.petdoc.request.GwangjinHospitalListRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDocFragment extends Fragment {

    private RegionGridAdapter mAdapter;

    public ListDocFragment() {
        // Required empty public constructor
    }


    List<String> hospitalItem = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_doc, container, false);

        GwangjinHospitalListRequest request = new GwangjinHospitalListRequest(getContext(), "GET", "1");
        mAdapter = new RegionGridAdapter(getContext(), R.layout.view_region);
        GridView gridView = (GridView)view.findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);

        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener() {
            @Override
            public void onSuccess(NetworkRequest request, Object result) {

                hospitalItem = (List<String>) result;
                mAdapter.addAll(hospitalItem);

            }

            @Override
            public void onFail(NetworkRequest request, int errorCode, String errorMessage) {

            }
        });

        return view;
    }

}
