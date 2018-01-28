package com.giacomodeliberali.securitystreet.tasks;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.giacomodeliberali.securitystreet.R;
import com.giacomodeliberali.securitystreet.models.Defaults;
import com.giacomodeliberali.securitystreet.models.dtos;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.servicestack.client.JsonServiceClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giacomodeliberali on 27/01/18.
 */

public class LoadCrashesOnMap extends AsyncTask<Void, Void, List<dtos.CrashDto>> {

    private Activity context;
    private GoogleMap gMap;

    public LoadCrashesOnMap(Activity context, GoogleMap gMap) {
        this.gMap = gMap;
        this.context = context;
    }


    @Override
    protected List<dtos.CrashDto> doInBackground(Void... params) {
        try {
            JsonServiceClient client = new JsonServiceClient(Defaults.DEFAULT_SERVICES_URL);

            return client.get(new dtos.ReadCrashRequest());

        } catch (Exception exception) {
            Log.e("", "Cannot read crashes", exception);
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        context.findViewById(R.id.crashes_progress_spinner).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<dtos.CrashDto> crashDtos) {
        if (crashDtos != null) {
            for (dtos.CrashDto crash : crashDtos) {
                if (crash.latitude != null && crash.longitude != null) {
                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(crash.latitude, crash.longitude));

                    gMap.addMarker(marker);
                }
            }
        }
        context.findViewById(R.id.crashes_progress_spinner).setVisibility(View.INVISIBLE);
    }
}