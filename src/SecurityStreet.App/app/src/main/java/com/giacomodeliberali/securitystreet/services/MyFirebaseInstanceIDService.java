package com.giacomodeliberali.securitystreet.services;

import android.util.Log;

import com.giacomodeliberali.securitystreet.models.Defaults;
import com.giacomodeliberali.securitystreet.models.dtos;
import com.giacomodeliberali.securitystreet.tasks.NotificationSubscription;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by giacomodeliberali on 28/01/18.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to server
     */
    private void sendRegistrationToServer(String token) {

        dtos.NotificationSubscriptionDto request = new dtos.NotificationSubscriptionDto();
        request.setClientToken(token);
        request.radius = Defaults.DEFAULT_RADIUS;

        // Save the client token
        new NotificationSubscription(request).execute();
    }

}
