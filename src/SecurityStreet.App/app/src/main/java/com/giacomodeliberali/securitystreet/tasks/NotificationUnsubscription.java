package com.giacomodeliberali.securitystreet.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.giacomodeliberali.securitystreet.models.Defaults;
import com.giacomodeliberali.securitystreet.models.dtos;

import net.servicestack.client.JsonServiceClient;

/**
 * Created by giacomodeliberali on 27/01/18.
 */

public class NotificationUnsubscription extends AsyncTask<Void, Void, Void> {

    private dtos.UnsubscriptionRequest item;
    private Context context;

    public NotificationUnsubscription(Context context, dtos.UnsubscriptionRequest item) {
        this.item = item;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            JsonServiceClient client = new JsonServiceClient(Defaults.DEFAULT_SERVICES_URL);

            client.post(item);

            return null;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    protected void onPostExecute(Void result) {
        Toast.makeText(context, "Sottoscrizione rimossa", Toast.LENGTH_SHORT).show();
    }
}