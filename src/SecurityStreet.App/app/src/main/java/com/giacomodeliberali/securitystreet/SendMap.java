package com.giacomodeliberali.securitystreet;


import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.giacomodeliberali.securitystreet.models.Defaults;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendMap extends Fragment implements OnMapReadyCallback {

    /**
     * The Logger tag
     */
    private static final String TAG = MapsActivity.class.getSimpleName();

    /**
     * The Google Map instance
     */
    public GoogleMap gMap;

    /**
     * Indicates if the permission to get current position is granted
     */
    private boolean mLocationPermissionGranted = false;

    /**
     * The last known position
     */
    private Location mLastKnownLocation;

    /**
     * The entry point to the Fused Location Provider
     */
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private FloatingActionButton floatingButtonHere;
    private Button sendPositionButton;
    private ImageView sendCross;
    private ProgressBar sendPorgressBar;

    public SendMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Segnala autovelox");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_send_map, container, false);

        floatingButtonHere = rootView.findViewById(R.id.fragment_send_map_floating_button_here);
        sendPositionButton = rootView.findViewById(R.id.send_position);
        sendCross = rootView.findViewById(R.id.send_cross);
        sendPorgressBar = rootView.findViewById(R.id.send_progress_spinner);

        // Construct a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        // Create the map async
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.send_map);
        mapFragment.getMapAsync(this);


        return rootView;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMapToolbarEnabled(false);

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        panToMyMpositionAsync();

        sendPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LatLng coordinates = new LatLng(googleMap.getCameraPosition().target.longitude, googleMap.getCameraPosition().target.latitude);

                Toast.makeText(getActivity(), String.format("Segnala in posizione (%s,%s)", coordinates.latitude, coordinates.longitude), Toast.LENGTH_SHORT).show();
            }
        });

        floatingButtonHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panToMyMpositionAsync();
            }
        });

    }

    /**
     * Loads autovelox near my current position
     */
    private void panToMyMpositionAsync() {
        try {
            final MapsActivity self = (MapsActivity) this.getActivity();

            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnSuccessListener(self, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mLastKnownLocation = location;

                            // Set the map's camera position to the current location of the device.
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), Defaults.DEFAULT_ZOOM));

                            sendPorgressBar.setVisibility(View.INVISIBLE);
                            sendCross.setVisibility(View.VISIBLE);

                        } else {
                            Toast.makeText(self, "Impossibile rilevare la psozione corrente", Toast.LENGTH_SHORT).show();
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaults.DEFAULT_LOCATION, Defaults.DEFAULT_ZOOM));
                            floatingButtonHere.setVisibility(View.GONE);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void updateLocationUI() {
        if (gMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                //floatingButtonHere.setVisibility(View.VISIBLE);
            } else {
                //loatingButtonHere.setVisibility(View.GONE);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Permission granted, update the position in the map
            mLocationPermissionGranted = true;
            updateLocationUI();

        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Defaults.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}
