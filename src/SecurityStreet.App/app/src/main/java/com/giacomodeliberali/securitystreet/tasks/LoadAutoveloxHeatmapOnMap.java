package com.giacomodeliberali.securitystreet.tasks;

import android.app.Activity;
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
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import net.servicestack.client.JsonServiceClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by giacomodeliberali on 27/01/18.
 */

public class LoadAutoveloxHeatmapOnMap extends AsyncTask<Void, Void, List<dtos.AutoveloxDto>> {

    private Activity context;
    private GoogleMap gMap;

    public LoadAutoveloxHeatmapOnMap(Activity context, GoogleMap gMap) {
        this.gMap = gMap;
        this.context = context;
    }


    @Override
    protected List<dtos.AutoveloxDto> doInBackground(Void... params) {
        try {
            JsonServiceClient client = new JsonServiceClient(Defaults.DEFAULT_SERVICES_URL);
            return client.get(new dtos.ReadAutoveloxRequest());
        } catch (Exception exception) {
            Log.e("", "Cannot insert autovelox", exception);
            return null;
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

                Collection<LatLng> data = new ArrayList<>();
                for (dtos.AutoveloxDto velox : autoveloxDtos) {
                    data.add(new LatLng(velox.getLatitude(), velox.getLongitude()));
                }

                HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                        .data(data)
                        .build();


                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(new LatLng(37.201953, 19.602473));
                builder.include(new LatLng(46.372259, 6.610899));
                LatLngBounds bounds = builder.build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                gMap.animateCamera(cameraUpdate);

                // Add a tile overlay to the map, using the heat map tile provider.
                gMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            }

            context.findViewById(R.id.autovelox_progress_spinner).setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            Log.d("LoadAutoveloxHeatmap", "The async task was interruped");
        }
    }
}