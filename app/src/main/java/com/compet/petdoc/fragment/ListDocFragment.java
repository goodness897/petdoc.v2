package com.compet.petdoc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.compet.petdoc.R;
import com.compet.petdoc.controller.HospitalAdapter;
import com.compet.petdoc.data.HospitalItem;
import com.compet.petdoc.data.RegionItem;
import com.compet.petdoc.manager.NetworkManager;
import com.compet.petdoc.manager.NetworkRequest;
import com.compet.petdoc.request.HospitalListRequest;
import com.compet.petdoc.util.Constants;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDocFragment extends Fragment implements AbsListView.OnScrollListener {

    private HospitalAdapter mAdapter;

    private boolean mLockListView;

    private int startIndex = 1;

    private int endIndex = 10;

    private View footerView;

    private RegionItem regionItem;

    private String url;

    public ListDocFragment() {
        // Required empty public constructor
    }

    public static ListDocFragment newInstance(RegionItem regionItem) {
        ListDocFragment fragment = new ListDocFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.REGION, regionItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            regionItem = (RegionItem)getArguments().getSerializable(Constants.REGION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_doc, container, false);

        mAdapter = new HospitalAdapter(getContext());
        ListView listView = (ListView)view.findViewById(R.id.listView);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_footer, null);
        footerView.setVisibility(View.GONE);
        listView.addFooterView(footerView);
        listView.setOnScrollListener(this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                getFragmentManager().beginTransaction()
                                    .replace(R.id.container, MapDocFragment.newInstance(mAdapter.getItem(position)))
                                    .addToBackStack(null)
                                    .commit();

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        startIndex = 1;
        endIndex = 10;

        initData();

    }

    private void initData() {

        url = regionItem.getUrl();
        footerView.setVisibility(View.VISIBLE);

        mLockListView = true;

        HospitalListRequest request = new HospitalListRequest(getContext(),
                                                              url,
                                                              "GET",
                                                              String.valueOf(startIndex),
                                                              String.valueOf(endIndex));
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener() {

            @Override
            public void onSuccess(NetworkRequest request, Object result) {

                if (result != null) {

                    mAdapter.addAll((List<HospitalItem>)result);
                    mLockListView = false;
                    footerView.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFail(NetworkRequest request, int errorCode, String errorMessage) {
                mLockListView = false;
                footerView.setVisibility(View.GONE);
                Toast.makeText(getContext(), "서버 오류 입니다. 다음에 다시 시도해주세요.", Toast.LENGTH_LONG);

            }
        });
    }

    private void addData() {

        url = regionItem.getUrl();

        footerView.setVisibility(View.VISIBLE);
        mLockListView = true;
        startIndex = endIndex + 1;
        endIndex = endIndex + 10;

        HospitalListRequest request = new HospitalListRequest(getContext(),
                                                              url,
                                                              "GET",
                                                              String.valueOf(startIndex),
                                                              String.valueOf(endIndex));
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener() {

            @Override
            public void onSuccess(NetworkRequest request, Object result) {

                if (result != null) {

                    mAdapter.addItem((List<HospitalItem>)result);
                    mLockListView = false;
                } else {
                    footerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFail(NetworkRequest request, int errorCode, String errorMessage) {

                Toast.makeText(getContext(), "서버 오류 입니다. 다음에 다시 시도해주세요.", Toast.LENGTH_LONG);

                mLockListView = true;
                footerView.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int count = totalItemCount - visibleItemCount;

        if (firstVisibleItem >= count && totalItemCount != 0 && !mLockListView) {
            addData();

        }

    }

}
