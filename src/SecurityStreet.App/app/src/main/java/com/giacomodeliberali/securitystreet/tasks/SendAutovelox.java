package com.giacomodeliberali.securitystreet.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.giacomodeliberali.securitystreet.R;
import com.giacomodeliberali.securitystreet.models.Defaults;
import com.giacomodeliberali.securitystreet.models.dtos;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.servicestack.client.JsonServiceClient;

/**
 * Created by giacomodeliberali on 27/01/18.
 */

public class SendAutovelox extends AsyncTask<Void, Void, dtos.AutoveloxDto> {

    private dtos.AutoveloxDto item;
    private Activity context;
    private GoogleMap googleMap;

    public SendAutovelox(Activity context, dtos.AutoveloxDto item, GoogleMap googleMap) {
        this.item = item;
        this.context = context;
        this.googleMap = googleMap;
    }

    @Override
    protected dtos.AutoveloxDto doInBackground(Void... params) {
        try {
            JsonServiceClient client = new JsonServiceClient(Defaults.DEFAULT_SERVICES_URL);

            dtos.UpdateAutoveloxRequest request = new dtos.UpdateAutoveloxRequest();
            request.setItem(item);

            return client.post(request);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        context.findViewById(R.id.send_progress_spinner).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(dtos.AutoveloxDto item) {
        if (item == null) {
            Toast.makeText(context, "Non è stato possibile segnalare l'autovelox", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, String.format("Segnalato in posizione (%s,%s)", item.latitude, item.longitude), Toast.LENGTH_SHORT).show();

            googleMap.addMarker(new MarkerOptions().position(new LatLng(item.latitude, item.longitude)));
        }

        context.findViewById(R.id.send_progress_spinner).setVisibility(View.INVISIBLE);
    }
}