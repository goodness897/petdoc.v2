package com.compet.petdoc.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.compet.petdoc.R;
import com.compet.petdoc.data.HospitalItem;
import com.compet.petdoc.util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

/**
 * A simple {@link Fragment} subclass. Use the {@link MapDocFragment#newInstance} factory method to create an instance
 * of this fragment.
 */
public class MapDocFragment extends BaseFragment implements OnMapReadyCallback {

    private static final String TAG = MapDocFragment.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    public static final int ALERT_ADDRESS_RESULT_RECIVER = 0;

    public static final int REQUEST_PERMISSION_ACCESS_COARSE_LOCATION = 1;

    private Context mContext;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private LatLng userPosition;

    // FragmentMap UI
    private GoogleMap mGoogleMap;

    private MapView mapView;

    private HospitalItem hospitalItem;

    private List<HospitalItem> hospitalItemList;

    private boolean convertAddress = false;

    public MapDocFragment() {
        // Required empty public constructor
    }

    public static MapDocFragment newInstance(HospitalItem hospitalItem) {
        MapDocFragment fragment = new MapDocFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.HOSPITAL, hospitalItem);
        fragment.setArguments(args);
        return fragment;
    }

    public static MapDocFragment getListItem(List<HospitalItem> list) {
        MapDocFragment fragment = new MapDocFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.HOSPITAL_LIST, (Serializable)list);
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
        MapsInitializer.initialize(mContext);

        if (getArguments() != null) {
            if (getArguments().getSerializable(Constants.HOSPITAL) != null) {
                hospitalItem = (HospitalItem)getArguments().getSerializable(Constants.HOSPITAL);
            } else if (getArguments().getSerializable(Constants.HOSPITAL_LIST) != null) {
                hospitalItemList = (List<HospitalItem>)getArguments().getSerializable(Constants.HOSPITAL_LIST);
                Log.d(TAG, "list item : " + hospitalItemList.get(0).getHosName());
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_doc, container, false);
        setHasOptionsMenu(true);

        initToolBar("지도", view, mContext);

        mapView = (MapView)view.findViewById(R.id.map);

        if (checkPlayServices()) {

            //            buildGoogleApiClient();

            //Biulding the GoogleMap
            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                mapView.onResume();

                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);
            }
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public void setUpGoogleMap() {

        LatLng hospitalLatLng = null;

        if (mGoogleMap != null) {

            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(false);
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    if (hospitalItem != null) {
                        getFragmentManager().beginTransaction()
                                            .replace(R.id.container, DetailFragment.newInstance(hospitalItem))
                                            .addToBackStack(null)
                                            .commit();
                    } else {
                        for (int i = 0; i < hospitalItemList.size(); i++) {
                            if (marker.getTitle().equals(hospitalItemList.get(i).getHosName())) {
                                hospitalItem = hospitalItemList.get(i);
                                Log.d(TAG, "병원 이름 : " + hospitalItem.getHosName());
                                getFragmentManager().beginTransaction()
                                                    .replace(R.id.container, DetailFragment.newInstance(hospitalItem))
                                                    .addToBackStack(null)
                                                    .commit();
                                return;
                            }
                        }

                    }

                }
            });

            if (!convertAddress) {
                if (hospitalItem != null) {
                    Log.d(TAG,
                          "latitude : " + hospitalItem.getLatitude() + " longitude : " + hospitalItem.getLongitude());

                    hospitalLatLng = new LatLng(hospitalItem.getLongitude(), hospitalItem.getLatitude());
                    if (hospitalLatLng != null) {

                        Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(hospitalLatLng)
                                                                                .title(hospitalItem.getHosName()));
                        marker.showInfoWindow();

                        //                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hospitalLatLng, 15));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospitalLatLng, 15));

                    }
                } else {
                    if (hospitalItemList.size() > 0) {
                        for (int i = 0; i < hospitalItemList.size(); i++) {
                            if (hospitalItem.getLongitude() != 0 && hospitalItem.getLatitude() != 0) {
                                hospitalLatLng = new LatLng(hospitalItem.getLongitude(), hospitalItem.getLatitude());
                            }
                            if (hospitalLatLng != null) {
                                mGoogleMap.addMarker(new MarkerOptions().position(hospitalLatLng)
                                                                        .title(hospitalItemList.get(i).getHosName()));
                            }
                        }
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospitalLatLng, 15));
                    }

                }
            }

        }
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

        if (mGoogleMap == null) {

            if (checkPlayServices()) {

                if (mapView != null) {

                    mapView.onCreate(null);
                    mapView.onResume();

                    mapView.getMapAsync(this);
                }
            }
        }

        else {
            if (!checkPermission()) {
                return;
            }
        }
    }

    public boolean checkPermission() {

        List<String> permissions = new ArrayList<>();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            if (!mGoogleMap.isMyLocationEnabled()) {
                mGoogleMap.setMyLocationEnabled(true);
            }

            return true;
        }

        if (ContextCompat.checkSelfPermission(mContext,
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
                        requestPermissions(perms, REQUEST_PERMISSION_ACCESS_COARSE_LOCATION);
                    }
                });
                builder.create().show();
                return true;
            }

            requestPermissions(perms, REQUEST_PERMISSION_ACCESS_COARSE_LOCATION);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_ACCESS_COARSE_LOCATION) {
            if (permissions != null) {
                boolean granted = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        granted = false;
                    }
                }
                if (!granted) {
                    Toast.makeText(mContext, "permission not granted", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleMap != null) {

            if (ContextCompat.checkSelfPermission(mContext,
                                                  ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMyLocationEnabled(false);
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(mContext);

        mGoogleMap = googleMap;

        setUpGoogleMap();

    }

}
