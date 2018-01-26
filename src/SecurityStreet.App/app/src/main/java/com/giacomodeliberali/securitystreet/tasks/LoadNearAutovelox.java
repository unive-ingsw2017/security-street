package com.giacomodeliberali.securitystreet.tasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.giacomodeliberali.securitystreet.MainActivity;
import com.giacomodeliberali.securitystreet.MapsActivity;
import com.giacomodeliberali.securitystreet.R;
import com.giacomodeliberali.securitystreet.models.Defaults;
import com.giacomodeliberali.securitystreet.models.dtos;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
