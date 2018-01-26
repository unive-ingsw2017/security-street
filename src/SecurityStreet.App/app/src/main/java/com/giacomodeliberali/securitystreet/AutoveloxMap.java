package com.giacomodeliberali.securitystreet;


import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AutoveloxMap extends Fragment implements OnMapReadyCallback {

    /**
     * The Logger tag
     */
    private static final String TAG = MapsActivity.class.getSimpleName();

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

    private boolean isHeatMap = false;
    private TileOverlay heatmapOverlay;

    public AutoveloxMap() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Mappa autovelox");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_autovelox_map, container, false);

        // Construct the detection client
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this.getActivity(), null);

        // Construct a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        // Create the map async

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.autovelox_map);
        mapFragment.getMapAsync(this);


        floatingButtonHere = rootView.findViewById(R.id.fragment_autovelox_map_floating_button_here);
        floatingButtonHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Report the last zoom and position
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mLastKnownLocation.getLatitude(),
                                mLastKnownLocation.getLongitude()), Defaults.DEFAULT_ZOOM));
            }
        });


        floatingButtonRefresh = rootView.findViewById(R.id.fragment_autovelox_map_floating_button_refresh);
        floatingButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMarkers();
                LatLng currentMapCenterCoordinates = gMap.getCameraPosition().target;
                Location currentMapCenter = new Location("dummyprovider");
                currentMapCenter.setLongitude(currentMapCenterCoordinates.longitude);
                currentMapCenter.setLatitude(currentMapCenterCoordinates.latitude);
                loadAutoveloxNearAsync(currentMapCenter, Defaults.DEFAULT_RADIUS, false);
            }
        });

        floatingButtonHeatMap = rootView.findViewById(R.id.fragment_autovelox_map_floating_button_heatmap);
        floatingButtonHeatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMarkers();
                getHeatMap();
            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        loadAutoveloxNearMyPositionAsync();
    }

    private void showProgressBar(boolean show) {
        ProgressBar progressBar = getActivity().findViewById(R.id.progress_spinner);

        if (show)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
    }

    private void clearMarkers() {
        gMap.clear();
    }

    /**
     * Loads autovelox near my current position
     */
    private void loadAutoveloxNearMyPositionAsync() {
        try {
            final MapsActivity self = (MapsActivity) this.getActivity();

            showProgressBar(true);

            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(self, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        mLastKnownLocation = task.getResult();
                        if (task.isSuccessful() && mLastKnownLocation != null) {
                            // Set the map's camera position to the current location of the device.
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), Defaults.DEFAULT_ZOOM));

                            loadAutoveloxNearAsync(mLastKnownLocation, Defaults.DEFAULT_RADIUS, true);

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaults.DEFAULT_LOCATION, Defaults.DEFAULT_ZOOM));
                            floatingButtonHere.setVisibility(View.GONE);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        } finally {
            showProgressBar(false);
        }
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
            Log.e(TAG, "Cannot insert autovelox", exception);
        } finally {
            showProgressBar(false);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case Defaults.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (gMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                gMap.setMyLocationEnabled(true);
                floatingButtonHere.setVisibility(View.VISIBLE);
            } else {
                gMap.setMyLocationEnabled(false);
                floatingButtonHere.setVisibility(View.GONE);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getHeatMap() {
        showProgressBar(true);
        if (!isHeatMap) {
            try {
                LatLng currentMapCenterCoordinates = gMap.getCameraPosition().target;
                Location currentMapCenter = new Location("dummyprovider");
                currentMapCenter.setLongitude(currentMapCenterCoordinates.longitude);
                currentMapCenter.setLatitude(currentMapCenterCoordinates.latitude);

                List<dtos.AutoveloxDto> results = new LoadNearAutovelox(currentMapCenter, 1000).execute().get();

                Collection<LatLng> data = new ArrayList<>();
                for (dtos.AutoveloxDto velox : results) {
                    data.add(new LatLng(velox.getLatitude(), velox.getLongitude()));
                }


                HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                        .data(data)
                        .build();

                // Add a tile overlay to the map, using the heat map tile provider.

                heatmapOverlay = gMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Impossibile creare la mappa", Toast.LENGTH_LONG).show();
        }
        } else {
            if (heatmapOverlay != null)
                heatmapOverlay.remove();
        }

        isHeatMap = !isHeatMap;

        showProgressBar(false);
    }
}
