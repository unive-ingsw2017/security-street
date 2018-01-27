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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.giacomodeliberali.securitystreet.models.Defaults;
import com.giacomodeliberali.securitystreet.models.dtos;
import com.giacomodeliberali.securitystreet.tasks.LoadNearAutovelox;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrashesMap extends Fragment implements OnMapReadyCallback {

    /**
     * The Google Map instance
     */
    public GoogleMap gMap;

    /**
     * The Detection client
     */
    private PlaceDetectionClient mPlaceDetectionClient;

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
    private FloatingActionButton floatingButtonRefresh;
    private FloatingActionButton floatingButtonHeatMap;


    public CrashesMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Mappa incidenti");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_crashes_map, container, false);

        // Construct the detection client
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this.getActivity(), null);

        // Construct a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        // Create the map async

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.crashes_map);
        mapFragment.getMapAsync(this);


        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        //loadAutoveloxNearMyPositionAsync();
    }

    private void showProgressBar(boolean show) {
        ProgressBar progressBar = getActivity().findViewById(R.id.progress_spinner);

        if (show)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
    }


    /**
     * Load the autovelox in the specified center in the specified radius
     *
     * @param center The map center location
     * @param radius The radius of the circle, in KM
     */
    private void loadAutoveloxNearAsync(Location center, int radius, boolean panToBounds) {
        if (radius <= 0)
            radius = Defaults.DEFAULT_RADIUS;


        showProgressBar(true);

        try {
            List<dtos.AutoveloxDto> results = new LoadNearAutovelox(center, radius).execute().get();

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (dtos.AutoveloxDto velox : results) {

                LatLng coordinates = new LatLng(velox.getLatitude(), velox.getLongitude());

                if (coordinates.latitude > 0 && coordinates.longitude > 0) {
                    // Valid position

                    MarkerOptions marker = new MarkerOptions()
                            .position(coordinates);

                    gMap.addMarker(marker);

                    builder.include(coordinates);
                }
            }

            if (panToBounds) {
                LatLngBounds bounds = builder.build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 10);
                gMap.animateCamera(cameraUpdate);
            }

            Toast.makeText(getActivity(), "Trovati " + results.size() + " autovelox", Toast.LENGTH_LONG);

        } catch (Exception exception) {
            Toast.makeText(getActivity(), "Nessun autovelox trvato", Toast.LENGTH_LONG).show();
            Log.e("Crashes", "Cannot insert autovelox", exception);
        } finally {
            showProgressBar(false);
        }
    }

    private void updateLocationUI() {
        if (gMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                gMap.setMyLocationEnabled(true);
                //floatingButtonHere.setVisibility(View.VISIBLE);
            } else {
                gMap.setMyLocationEnabled(false);
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
