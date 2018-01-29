package com.giacomodeliberali.securitystreet.tasks;

import android.app.Activity;
import android.content.Context;
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

public class NotificationSubscription extends AsyncTask<Void, Void, dtos.NotificationSubscriptionDto> {

    private dtos.NotificationSubscriptionDto item;
    private Context context;
    private boolean emitNotify;

    public NotificationSubscription(dtos.NotificationSubscriptionDto item) {
        this.item = item;
        this.context = null;
        this.emitNotify = false;
    }

    public NotificationSubscription(dtos.NotificationSubscriptionDto item, Context context, boolean emitNotify) {
        this.item = item;
        this.context = context;
        this.emitNotify = emitNotify;
    }

    @Override
    protected dtos.NotificationSubscriptionDto doInBackground(Void... params) {
        try {
            JsonServiceClient client = new JsonServiceClient(Defaults.DEFAULT_SERVICES_URL);

            dtos.SubscriptionRequest request = new dtos.SubscriptionRequest();
            request.setItem(item);

            return client.post(request);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    protected void onPostExecute(dtos.NotificationSubscriptionDto result) {

        if (!emitNotify)
            return;

        Toast.makeText(context, "Sottoscrizione effettuata", Toast.LENGTH_SHORT).show();
    }
}