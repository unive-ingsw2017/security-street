package com.giacomodeliberali.securitystreet.tasks;

import android.location.Location;
import android.os.AsyncTask;

import com.giacomodeliberali.securitystreet.models.Defaults;
import com.giacomodeliberali.securitystreet.models.dtos;

import net.servicestack.client.JsonServiceClient;

import java.util.List;

/**
 * Created by Giacomo De Liberali on 23/01/2018.
 */

public class LoadNearAutovelox extends AsyncTask<Void, Void, List<dtos.AutoveloxDto>> {

    private Location location;
    private int radius = 10;

    public LoadNearAutovelox(Location location) {
        this.location = location;
    }

    public LoadNearAutovelox(Location location, int radius) {
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

            // TODO: Retrive location
            if (location != null) {
                request.setLatitude(location.getLatitude());
                request.setLongitude(location.getLongitude());
            } else {
                request.setLatitude(45.521134);
                request.setLongitude(12.015341);
            }
            request.setDistance(radius);

            List<dtos.AutoveloxDto> list = client.get(request);

            return list;
        } catch (Exception e) {
            return null;
        }
    }
}
