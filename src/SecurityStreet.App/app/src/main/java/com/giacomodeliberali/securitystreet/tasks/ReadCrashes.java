package com.giacomodeliberali.securitystreet.tasks;

import android.os.AsyncTask;

import com.giacomodeliberali.securitystreet.models.Defaults;
import com.giacomodeliberali.securitystreet.models.dtos;

import net.servicestack.client.JsonServiceClient;

import java.util.List;

/**
 * Created by giacomodeliberali on 27/01/18.
 */

public class ReadCrashes  extends AsyncTask<Void, Void, List<dtos.CrashDto>> {

    @Override
    protected List<dtos.CrashDto> doInBackground(Void... params) {
        try {
            JsonServiceClient client = new JsonServiceClient(Defaults.DEFAULT_SERVICES_URL);

            dtos.ReadCrashRequest request = new dtos.ReadCrashRequest();

            return client.get(request);
        } catch (Exception e) {
            return null;
        }
    }
}