package com.compet.petdoc.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
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

    private boolean convertAddress = false;

    public MapDocFragment() {
        // Required empty public constructor
    }

    public static MapDocFragment newInstance(HospitalItem hospitalItem) {
        MapDocFragment fragment = new MapDocFragment();
        Bundle args = new Bundle();
        args.putSerializable("hospital", hospitalItem);

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
            hospitalItem = (HospitalItem)getArguments().getSerializable(Constants.HOSPITAL);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_doc, container, false);

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

    public void setUpGoogleMap() {

        LatLng hospitalLatLng = null;

        if (mGoogleMap != null) {

            if(!convertAddress) {
                hospitalLatLng = findLatLng(hospitalItem.getAddress());
            }

            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(false);
            if (hospitalLatLng != null) {
                mGoogleMap.addMarker(new MarkerOptions().position(hospitalLatLng).title(hospitalItem.getHosName()));
                //                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hospitalLatLng, 15));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospitalLatLng, 15));

            }

        }
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

        //First we need to check if the GoogleMap was not created in OnCreate
        if (mGoogleMap == null) {

            // We need to check availability of play services
            if (checkPlayServices()) {

                //                if (mGoogleApiClient == null) {
                //                     Building the GoogleApi client
                //                    buildGoogleApiClient();
                //                }

                //Biulding the GoogleMap
                if (mapView != null) {
                    // Initialise the MapView
                    mapView.onCreate(null);
                    mapView.onResume();

                    // Set the map ready callback to receive the GoogleMap object
                    mapView.getMapAsync(this);
                }
            }
        }

        //GoogleMap exist
        else {
            if (!checkPermission()) {
                return;
            }
        }
    }

    //    private void getUserLocation() {
    //
    //        if (!checkPermission()) {
    //
    //            userPosition = new LatLng(34.0089919, -118.4996126);
    //            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 15));
    //
    //            return;
    //        }
    //
    //        if (ContextCompat.checkSelfPermission(mContext, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
    //            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    //        }
    //
    //        if (mLastLocation != null) {
    //
    //            userPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
    //            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 15));
    //
    //        } else {
    //            showAlert(ALERT_ADDRESS_RESULT_RECIVER);
    //        }
    //
    //    }

    //    public void showAlert(final int action) {
    //
    //        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
    //        dialog.setTitle("제목");
    //        dialog.setMessage("내용");
    //        dialog.setCancelable(false);
    //        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
    //
    //            @Override
    //            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
    //
    //                if (action == 0) {
    //                    getUserLocation();
    //                } else {
    //                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    //                    mContext.startActivity(myIntent);
    //                }
    //
    //            }
    //        });
    //
    //        dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
    //
    //            @Override
    //            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
    //            }
    //        });
    //        dialog.show();
    //    }
    //
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

        //        mGoogleApiClient.connect();

    }

    //    @Override
    //    public void onConnected(@Nullable Bundle bundle) {
    //        getUserLocation();
    //
    //    }
    //
    //    @Override
    //    public void onConnectionSuspended(int i) {
    //        mGoogleApiClient.connect();
    //
    //    }
    //
    //    @Override
    //    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    //        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    //    }
}
