package com.compet.petdoc.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
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
import android.widget.Button;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

/**
 * A simple {@link Fragment} subclass. Use the {@link MapDocFragment#newInstance} factory method to create an instance
 * of this fragment.
 */
public class MapDocFragment extends Fragment implements OnMapReadyCallback {

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

    private ProgressDialog dialog;

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
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("잠시만 기다려주세요");
        dialog.setCancelable(false);
        dialog.show();

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

        mapView = (MapView)view.findViewById(R.id.map);
        Button callButton = (Button)view.findViewById(R.id.btn_call);

        final StringBuilder stringBuilder = new StringBuilder();

        if (hospitalItem != null) {
            stringBuilder.append("tel:").append(hospitalItem.getPhoneNumber());
        } else {
            callButton.setVisibility(View.GONE);
        }

        callButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(stringBuilder.toString()));
                startActivity(intent);
            }
        });

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

            if (!convertAddress) {
                if (hospitalItem != null) {
                    hospitalLatLng = findLatLng(hospitalItem.getAddress());
                    if (hospitalLatLng != null) {
                        mGoogleMap.addMarker(new MarkerOptions().position(hospitalLatLng)
                                                                .title(hospitalItem.getHosName()));
                        //                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hospitalLatLng, 15));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospitalLatLng, 15));
                        dialog.hide();

                    }
                } else {
                    for (int i = 0; i < hospitalItemList.size(); i++) {
                        hospitalLatLng = findLatLng(hospitalItemList.get(i).getAddress());
                        if (hospitalLatLng != null) {
                            mGoogleMap.addMarker(new MarkerOptions().position(hospitalLatLng)
                                                                    .title(hospitalItemList.get(i).getHosName()));
                        }
                    }
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(findLatLng(hospitalItemList.get(0)
                                                                                                       .getAddress()),
                                                                            15));
                    dialog.hide();

                }
            }

        }
    }

    private LatLng findLatLng(String address) {

        convertAddress = true;

        Log.d(TAG, "바뀌기 전 주소 : " + address);
        Geocoder geocoder = new Geocoder(mContext);
        try {

            List<Address> addressList = geocoder.getFromLocationName(address, 5);
            Iterator<Address> addrs = addressList.iterator();

            String infoAddr = "";
            double lat = 0;
            double lng = 0;
            while (addrs.hasNext()) {
                Address loc = addrs.next();
                infoAddr += String.format(Locale.KOREA, "Coord : %f. %f", loc.getLatitude(), loc.getLatitude());
                lat = loc.getLatitude();
                lng = loc.getLongitude();

                Log.d(TAG, "병원 좌표 : " + lat + "," + lng);

                return new LatLng(lat, lng);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            convertAddress = false;
        }

        return null;
    }

    //    protected synchronized void buildGoogleApiClient() {
    //
    //        mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this)
    //                                                                .addOnConnectionFailedListener(this)
    //                                                                .addApi(LocationServices.API)
    //                                                                .build();
    //    }

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
