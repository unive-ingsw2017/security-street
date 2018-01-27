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

public class ReadAutovelox extends AsyncTask<Void, Void, List<dtos.AutoveloxDto>> {

    @Override
    protected List<dtos.AutoveloxDto> doInBackground(Void... params) {
        try {
            JsonServiceClient client = new JsonServiceClient(Defaults.DEFAULT_SERVICES_URL);

            dtos.ReadAutoveloxRequest request = new dtos.ReadAutoveloxRequest();

            return client.get(request);
        } catch (Exception e) {
            return null;
        }
    }
}
