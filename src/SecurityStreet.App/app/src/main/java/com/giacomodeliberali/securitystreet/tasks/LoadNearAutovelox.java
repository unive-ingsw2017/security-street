package com.giacomodeliberali.securitystreet.tasks;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;

import com.giacomodeliberali.securitystreet.models.dtos;

import net.servicestack.client.JsonServiceClient;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Giacomo De Liberali on 23/01/2018.
 */

public class LoadNearAutovelox extends AsyncTask<Void, Void, List<dtos.AutoveloxDto>> {

    private WeakReference<Activity> activityReference;
    private Location location;

    public LoadNearAutovelox(Activity context, Location location) {
        this.activityReference = new WeakReference<>(context);
        this.location = location;
    }

    @Override
    protected List<dtos.AutoveloxDto> doInBackground(Void... params) {
        try {
            JsonServiceClient client = new JsonServiceClient("http://unive-development-swe-2018.azurewebsites.net/api");

            dtos.ReadAutoveloxByDistanceRequest request = new dtos.ReadAutoveloxByDistanceRequest();

            // TODO: Retrive location
            if(location != null) {
                request.setLatitude(location.getLatitude());
                request.setLongitude(location.getLongitude());
            }else{
                request.setLatitude(45.521134);
                request.setLongitude(12.015341);
            }
            request.setDistance(10);

            List<dtos.AutoveloxDto> list = client.get(request);

            return list;
        } catch (Exception e) {
            return null;
        }
    }
}
