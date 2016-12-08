package com.compet.petdoc.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.compet.petdoc.R;
import com.compet.petdoc.controller.HospitalAdapter;
import com.compet.petdoc.data.HospitalItem;
import com.compet.petdoc.data.RegionItem;
import com.compet.petdoc.manager.NetworkManager;
import com.compet.petdoc.manager.NetworkRequest;
import com.compet.petdoc.request.HospitalListRequest;
import com.compet.petdoc.util.Constants;
import com.compet.petdoc.util.HospitalURL;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainFragment extends BaseFragment implements
                          GoogleApiClient.ConnectionCallbacks,
                          GoogleApiClient.OnConnectionFailedListener,
                          AbsListView.OnScrollListener {

    private static final String TAG = MainFragment.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private static final int RC_PERMISSION = 1000;

    private TextView locationView;

    private Context mContext;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private LatLng userPosition;

    private HospitalAdapter mAdapter;

    private View footerView;

    private boolean mLockListView;

    private boolean mListAdd;

    private int startIndex = 1;

    private int endIndex = 10;

    private RegionItem regionItem;

    private HospitalItem hospitalItem;

    private String url;

    private List<RegionItem> regionList;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(RegionItem regionItem) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.REGION, regionItem);
        fragment.setArguments(args);
        return fragment;
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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
            regionItem = (RegionItem)getArguments().getSerializable(Constants.REGION);
        }
        checkPermission(mContext);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_region_list, container, false);
        setHasOptionsMenu(true);
        initToolBar(getString(R.string.app_name), view, mContext);

        mListAdd = true;

        locationView = (TextView)view.findViewById(R.id.text_location);

        LinearLayout layoutLocation = (LinearLayout)view.findViewById(R.id.layout_location);
        layoutLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, SearchFragment.newInstance())
                        .commit();
            }
        });
        Button mapButton = (Button)view.findViewById(R.id.btn_map);
        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                                    .replace(R.id.container, MapDocFragment.getListItem(mAdapter.getListItem()))
                                    .addToBackStack(null)
                                    .commit();
            }
        });

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        mGoogleApiClient.connect();
        regionList = new ArrayList<>();

        mAdapter = new HospitalAdapter(getContext());
        ListView listView = (ListView)view.findViewById(R.id.listView);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_footer, null);
        footerView.setVisibility(View.GONE);
        listView.addFooterView(footerView);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                getFragmentManager().beginTransaction()
                                    .replace(R.id.container, DetailFragment.newInstance(mAdapter.getItem(position)))
                                    .addToBackStack(null)
                                    .commit();
            }
        });
        listView.setAdapter(mAdapter);

        if (regionItem == null) {
            setRegionData();
        } else {
            initData();
            locationView.setText(regionItem.getName());
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this)
                                                                .addOnConnectionFailedListener(this)
                                                                .addApi(LocationServices.API)
                                                                .build();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(mContext);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        startIndex = 1;
        endIndex = 10;

    }

    private void setRegionData() {
        Resources res = getResources();
        String[] arrString = res.getStringArray(R.array.seoul_region);

        for (int i = 0; i < arrString.length; i++) {
            RegionItem regionItem = new RegionItem();
            regionItem.setName(arrString[i]);
            regionItem.setUrl(HospitalURL.URL[i]);
            regionList.add(regionItem);
        }
    }

    private void initData() {

        url = regionItem.getUrl();
        footerView.setVisibility(View.VISIBLE);

        mLockListView = true;

        HospitalListRequest request = new HospitalListRequest(getContext(),
                                                              url,
                                                              Constants.GET,
                                                              String.valueOf(startIndex),
                                                              String.valueOf(endIndex));
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener() {

            @Override
            public void onSuccess(NetworkRequest request, Object result) {

                if (result != null) {

                    mAdapter.addAll((List<HospitalItem>)result);
                    mLockListView = false;
                    footerView.setVisibility(View.GONE);
                    mListAdd = false;

                }

            }

            @Override
            public void onFail(NetworkRequest request, int errorCode, String errorMessage) {
                mLockListView = false;
                mListAdd = false;
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
                                                              Constants.GET,
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

    private void getUserLocation() {

        if (ContextCompat.checkSelfPermission(mContext, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        if (mLastLocation != null) {

            Log.d(TAG,
                  "latitude : " + mLastLocation.getLatitude() + " " + "longitude : " + mLastLocation.getLongitude());
            String address = changeAddress(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            String[] addressLine = address.split(" ");
            StringBuilder stringBuilder = new StringBuilder();
            regionItem = searchRegion(addressLine);
            stringBuilder.append(addressLine[2]).append(" ").append(addressLine[3]);
            locationView.setText(stringBuilder.toString());
            if (regionItem != null) {
                initData();

            } else {
            }
        } else {
        }
    }

    private RegionItem searchRegion(String[] addressLine) {
        for (int i = 0; i < regionList.size(); i++) {
            if (regionList.get(i).getName().equals(addressLine[2])) {
                regionItem = regionList.get(i);
                return regionItem;
            } else {
                //                    getFragmentManager().beginTransaction()
                //                            .replace(R.id.container, SearchFragment.newInstance())
                //                            .commit();
            }
        }
        return null;
    }

    private String changeAddress(double latitude, double longitude) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(latitude, longitude, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소

                    bf.append(address.get(0).getAddressLine(0));

                }
            }

        } catch (IOException e) {
            Toast.makeText(mContext, "주소취득 실패", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return bf.toString();
    }

    public void checkPermission(final Context context) {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getContext(),
                                              Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(getContext(),
                                              Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(getContext(),
                                              Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissions.size() > 0) {
            boolean isShowUI = false;
            for (String perm : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), perm)) {
                    isShowUI = true;
                    break;
                }
            }

            final String[] perms = permissions.toArray(new String[permissions.size()]);

            if (isShowUI) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle("Permission");
                builder.setMessage("Permission");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions((Activity)context, perms, RC_PERMISSION);
                    }
                });
                builder.create().show();
                return;
            }

            requestPermissions(perms, RC_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (permissions != null) {
                boolean granted = true;
                if (regionItem == null) {
                    getUserLocation();

                }
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        granted = false;
                    }
                }
                if (!granted) {
                    Toast.makeText(getContext(), "permission not granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (mAdapter.isEmpty() && locationView.getText().toString().equals("위치")) {
            getUserLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int count = totalItemCount - visibleItemCount;

        if (firstVisibleItem >= count && totalItemCount != 0 && !mLockListView && !mListAdd) {
            addData();

        }

    }

}
