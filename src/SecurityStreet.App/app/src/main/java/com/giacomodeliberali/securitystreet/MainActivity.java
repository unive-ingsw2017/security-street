package com.giacomodeliberali.securitystreet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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


        checkLocationPermission();
    }

    private void initialize() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Define the criteria how to select the locatioin provider -> use default
        String provider = locationManager.getBestProvider(new Criteria(), false);

        if (provider == null || !checkIntenet() || !checkGps()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle(getString(R.string.main_activity_alert_title));
            alert.setMessage(getString(R.string.main_activity_alert_body));
            alert.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //System.exit(1);
                    checkLocationPermission();
                }
            });
            alert.show();
            return;
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
                Toast.makeText(this, getString(R.string.main_activity_exception_toast), Toast.LENGTH_LONG).show();
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

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    /**
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            initialize();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Defaults.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Retruns true if internet is available
     */
    private boolean checkIntenet() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Retruns true if location service is available
     */
    private boolean checkGps() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Defaults.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0)
                initialize();
        }

    }
}
