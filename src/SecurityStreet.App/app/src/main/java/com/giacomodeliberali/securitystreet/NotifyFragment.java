package com.giacomodeliberali.securitystreet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.giacomodeliberali.securitystreet.models.Defaults;
import com.giacomodeliberali.securitystreet.models.dtos;
import com.giacomodeliberali.securitystreet.tasks.LoadCrashesOnMap;
import com.giacomodeliberali.securitystreet.tasks.NotificationSubscription;
import com.giacomodeliberali.securitystreet.tasks.NotificationUnsubscription;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * A simple fragment
 */
public class NotifyFragment extends Fragment implements OnMapReadyCallback {
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
    private SeekBar seekBar;
    private TextView seekBarValue;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private Button subscribe;
    private Button updateSubscriptionButton;

    private SharedPreferences mPreferences;

    public NotifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        getActivity().setTitle("Sottoscrizione autovelox");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notify, container, false);

        floatingButtonHere = rootView.findViewById(R.id.fragment_notify_map_floating_button_here);
        seekBar = rootView.findViewById(R.id.seekBar);
        seekBarValue = rootView.findViewById(R.id.notifiction_radius_seekbar_value);
        longitudeTextView = rootView.findViewById(R.id.notify_longitude);
        latitudeTextView = rootView.findViewById(R.id.notify_latitude);
        subscribe = rootView.findViewById(R.id.subscribe_button);
        updateSubscriptionButton = rootView.findViewById(R.id.update_subscription_button);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarValue.setText("" + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        floatingButtonHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panToMyMpositionAsync();
            }
        });

        int radius = mPreferences.getInt("NOTIFY_RADIUS", Defaults.DEFAULT_RADIUS);
        seekBar.setProgress(radius);
        seekBarValue.setText("" + radius);

        if (mPreferences.getBoolean("NOTIFY_SUBSCRIPTION", false)) {
            subscribe.setText("Disattiva");
            updateSubscriptionButton.setVisibility(View.VISIBLE);
        } else {
            subscribe.setText("Attiva");
            updateSubscriptionButton.setVisibility(View.GONE);
        }

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String clientToken = FirebaseInstanceId.getInstance().getToken();

                if (!mPreferences.getBoolean("NOTIFY_SUBSCRIPTION", false)) {

                    // Make a new subscription

                    dtos.NotificationSubscriptionDto request = new dtos.NotificationSubscriptionDto();
                    request.clientToken = clientToken;
                    request.radius = Integer.parseInt(seekBarValue.getText().toString());
                    request.latitude = mLastKnownLocation.getLatitude();
                    request.longitude = mLastKnownLocation.getLongitude();

                    try {
                        dtos.NotificationSubscriptionDto response = new NotificationSubscription(request, getActivity(), true).execute().get();

                        subscribe.setText("Disattiva");
                        updateSubscriptionButton.setVisibility(View.VISIBLE);

                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putBoolean("NOTIFY_SUBSCRIPTION", true);
                        editor.putInt("NOTIFY_RADIUS", request.getRadius());
                        editor.commit();


                    } catch (Exception e) {
                        // FK
                    }
                } else {

                    // Remove the subscription

                    dtos.UnsubscriptionRequest request = new dtos.UnsubscriptionRequest();
                    request.clientToken = clientToken;

                    try {
                        Void response = new NotificationUnsubscription(getActivity(), request).execute().get();

                        subscribe.setText("Attiva");
                        updateSubscriptionButton.setVisibility(View.GONE);

                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putBoolean("NOTIFY_SUBSCRIPTION", false);
                        editor.putInt("NOTIFY_RADIUS", Defaults.DEFAULT_RADIUS);
                        editor.commit();

                        seekBar.setProgress(Defaults.DEFAULT_RADIUS);
                        seekBarValue.setText("" + Defaults.DEFAULT_RADIUS);

                    } catch (Exception e) {
                        // FK
                    }
                }
            }
        });

        updateSubscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Update the subscription

                String clientToken = FirebaseInstanceId.getInstance().getToken();

                dtos.NotificationSubscriptionDto request = new dtos.NotificationSubscriptionDto();
                request.clientToken = clientToken;
                request.radius = Integer.parseInt(seekBarValue.getText().toString());
                request.latitude = mLastKnownLocation.getLatitude();
                request.longitude = mLastKnownLocation.getLongitude();

                try {
                    dtos.NotificationSubscriptionDto response = new NotificationSubscription(request, getActivity(), true).execute().get();

                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean("NOTIFY_SUBSCRIPTION", true);
                    editor.putInt("NOTIFY_RADIUS", response.getRadius());
                    editor.commit();
                } catch (Exception e) {
                    // FK
                }
            }
        });


        // Construct the detection client
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this.getActivity(), null);

        // Construct a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        // Create the map async
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.notify_map);
        mapFragment.getMapAsync(this);


        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMapToolbarEnabled(false);

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        panToMyMpositionAsync();
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

                            gMap.setMyLocationEnabled(true);

                            // Set the map's camera position to the current location of the device.
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), Defaults.DEFAULT_ZOOM));

                            latitudeTextView.setText("" + mLastKnownLocation.getLatitude());
                            longitudeTextView.setText("" + mLastKnownLocation.getLongitude());

                        } else {
                            Toast.makeText(self, "Impossibile rilevare la psozione corrente", Toast.LENGTH_SHORT).show();
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaults.DEFAULT_LOCATION, Defaults.DEFAULT_ZOOM));
                            floatingButtonHere.setVisibility(View.GONE);
                            gMap.setMyLocationEnabled(false);
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
