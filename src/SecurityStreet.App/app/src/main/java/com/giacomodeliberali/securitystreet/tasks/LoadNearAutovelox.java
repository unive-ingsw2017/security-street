package com.giacomodeliberali.securitystreet.tasks;

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

    private WeakReference<MapsActivity> activityReference;
    private Location location;

    public LoadNearAutovelox(MapsActivity context, Location location) {
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        MapsActivity activity = activityReference.get();
        ProgressBar progressBar = activity.findViewById(R.id.progress_spinner);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<dtos.AutoveloxDto> result) {

        MapsActivity activity = activityReference.get();

        if (activity == null)
            return;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (dtos.AutoveloxDto velox : result){

            LatLng coordinates = new LatLng(velox.getLatitude(), velox.getLongitude());

            MarkerOptions marker = new MarkerOptions()
                    .position(coordinates);
                    //.icon(bitmapDescriptorFromVector(activity,R.drawable.ic_directions_car_black_24dp));

            activity.mMap.addMarker(marker);

            builder.include(coordinates);
        }

        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 10);

        activity.mMap.animateCamera(cameraUpdate);

        ProgressBar progressBar = activity.findViewById(R.id.progress_spinner);
        progressBar.setVisibility(View.GONE);

        Toast.makeText(activity.getApplicationContext(),"Trovati " + result.size() + " autovelox nel raggio di 10KM",Toast.LENGTH_SHORT);

        //TextView textView = activity.findViewById(R.id.label);
        //textView.setText("Found " + result.size() + " autovelox near 100KM around you");
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.marker_blank);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
