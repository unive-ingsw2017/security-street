package com.giacomodeliberali.securitystreet.tasks;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.giacomodeliberali.securitystreet.R;
import com.giacomodeliberali.securitystreet.models.Defaults;
import com.giacomodeliberali.securitystreet.models.dtos;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import net.servicestack.client.JsonServiceClient;

import java.util.List;

/**
 * Created by giacomodeliberali on 27/01/18.
 */

public class LoadAutoveloxOnMap extends AsyncTask<Void, Void, List<dtos.AutoveloxDto>> {

    private Activity context;
    private GoogleMap gMap;
    private Location location;
    private int radius;
    private boolean panToBounds;

    private List<dtos.AutoveloxDto> results;

    public LoadAutoveloxOnMap(Activity context, GoogleMap gMap, Location location, int radius, boolean panToBounds) {
        this.gMap = gMap;
        this.panToBounds = panToBounds;
        this.context = context;

        if (location == null) {
            location = new Location("");
            location.setLongitude(Defaults.DEFAULT_LOCATION.longitude);
            location.setLatitude(Defaults.DEFAULT_LOCATION.latitude);
        }
        this.location = location;

        if (radius <= 0)
            radius = Defaults.DEFAULT_RADIUS;

        this.radius = radius;
    }


    @Override
    protected List<dtos.AutoveloxDto> doInBackground(Void... params) {
        try {
            JsonServiceClient client = new JsonServiceClient(Defaults.DEFAULT_SERVICES_URL);

            dtos.ReadAutoveloxByDistanceRequest request = new dtos.ReadAutoveloxByDistanceRequest();

            request.setDistance(radius);
            request.setLatitude(location.getLatitude());
            request.setLongitude(location.getLongitude());

            results = client.get(request);
        } catch (Exception exception) {
            results = null;
            Log.e("", "Cannot insert autovelox", exception);
        } finally {
            return results;
        }
    }

    @Override
    protected void onPreExecute() {
        if (context.findViewById(R.id.autovelox_progress_spinner) != null)
            context.findViewById(R.id.autovelox_progress_spinner).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<dtos.AutoveloxDto> autoveloxDtos) {
        try {
            if (autoveloxDtos != null) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (dtos.AutoveloxDto velox : autoveloxDtos) {

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
            }

            context.findViewById(R.id.autovelox_progress_spinner).setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            Log.d("LoadAutoveloxOnMap", "The async task was interruped");
        }
    }
}