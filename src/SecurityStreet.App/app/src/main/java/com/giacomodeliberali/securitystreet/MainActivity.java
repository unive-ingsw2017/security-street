package com.giacomodeliberali.securitystreet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.giacomodeliberali.securitystreet.models.Defaults;

public class MainActivity extends AppCompatActivity implements LocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.enter_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });


        // Get the location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);

        while (!getLocationPermission()) {
            // Continue asking for permission, lol
        }

        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            // Nothing
        }

        // Initialize the location fields
        if (location != null) {
            findViewById(R.id.enter_button).setVisibility(View.VISIBLE);
            findViewById(R.id.main_progress_spinner).setVisibility(View.INVISIBLE);
            findViewById(R.id.main_spinner_label).setVisibility(View.INVISIBLE);
        } else {
            try {
                locationManager.requestLocationUpdates(provider, 400, 1, this);
            } catch (SecurityException e) {
                Toast.makeText(this, "Impossibile rilevare la posizione corrente. Attivare la connessione e/o il GPS", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        findViewById(R.id.enter_button).setVisibility(View.VISIBLE);
        findViewById(R.id.main_progress_spinner).setVisibility(View.INVISIBLE);
        findViewById(R.id.main_spinner_label).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Enabled new provider " + s,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Disabled provider " + s,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    private boolean getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Defaults.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return false;
        }
    }
}
