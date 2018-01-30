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
import android.widget.Toast;

import com.giacomodeliberali.securitystreet.models.Defaults;
import com.giacomodeliberali.securitystreet.tasks.LoadAutoveloxHeatmapOnMap;
import com.giacomodeliberali.securitystreet.tasks.LoadAutoveloxOnMap;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


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

                Location topLeft = new Location("RadiusPositionTopLeft");
                topLeft.setLatitude(gMap.getProjection().getVisibleRegion().latLngBounds.northeast.latitude);
                topLeft.setLongitude(gMap.getProjection().getVisibleRegion().latLngBounds.northeast.longitude);

                Location bottomRight = new Location("RadiusPositionBottomRight");
                bottomRight.setLatitude(gMap.getProjection().getVisibleRegion().latLngBounds.southwest.latitude);
                bottomRight.setLongitude(gMap.getProjection().getVisibleRegion().latLngBounds.southwest.longitude);


                int radius = (int) ((topLeft.distanceTo(bottomRight) / 1000) / 2);

                new LoadAutoveloxOnMap(getActivity(), gMap, currentMapCenter, radius, false).execute();
            }
        });

        floatingButtonHeatMap = rootView.findViewById(R.id.fragment_autovelox_map_floating_button_heatmap);
        floatingButtonHeatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMarkers();
                loadHeatMapAsync();
            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMapToolbarEnabled(false);

        gMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = gMap.getCameraPosition();
                if (cameraPosition.zoom < 8) {
                    floatingButtonRefresh.setEnabled(false);
                    floatingButtonRefresh.setBackgroundTintList(getResources().getColorStateList(R.color.colorDisabled));
                } else {
                    floatingButtonRefresh.setEnabled(true);
                    floatingButtonRefresh.setBackgroundTintList(getResources().getColorStateList(R.color.colorWhite));
                }
            }
        });

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        loadAutoveloxNearMyPositionAsync();
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

            if (mLocationPermissionGranted) {

                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnSuccessListener(self, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mLastKnownLocation = location;

                            gMap.setMyLocationEnabled(true);

                            // Set the map's camera position to the current location of the device.
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), Defaults.DEFAULT_ZOOM));

                            new LoadAutoveloxOnMap(self, gMap, mLastKnownLocation, Defaults.DEFAULT_RADIUS, true).execute();

                        } else {
                            Toast.makeText(self, "Impossibile rilevare la posizione corrente", Toast.LENGTH_SHORT).show();
                            floatingButtonHere.setVisibility(View.INVISIBLE);
                            gMap.setMyLocationEnabled(false);
                        }
                    }
                });
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
                floatingButtonHere.setVisibility(View.VISIBLE);
            } else {
                floatingButtonHere.setVisibility(View.GONE);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void loadHeatMapAsync() {
        if (!isHeatMap) {
            try {

                floatingButtonHeatMap.setImageResource(R.drawable.ic_clear);

                new LoadAutoveloxHeatmapOnMap(getActivity(), gMap).execute();

            } catch (Exception e) {
                Toast.makeText(getActivity(), "Impossibile creare la mappa", Toast.LENGTH_LONG).show();
            }
        } else {
            if (heatmapOverlay != null)
                heatmapOverlay.remove();

            floatingButtonHeatMap.setImageResource(R.drawable.ic_graphic);
        }

        isHeatMap = !isHeatMap;
    }
}
