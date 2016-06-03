package com.github.javon.labassistant.classes.helpers;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 17/01/16.
 */
public final class Constants {

    private Constants() {
    }

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 2;

    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 1;

    private static final double DOWNSTAIRS_LAB_LAT = 18.0051943;
    private static final double DOWNSTAIRS_LAB_LONG = -76.7499932;

    public static final Location DOWNSTAIRS_LAB_LOCATION = new Location("Computer lab location");

    /**
     * Map for storing information about airports in the San Francisco bay area.
     */
    public static final Map<String, Location> LAB_LOCATIONS = new HashMap<>();

    static {
        DOWNSTAIRS_LAB_LOCATION.setLatitude(DOWNSTAIRS_LAB_LAT);
        DOWNSTAIRS_LAB_LOCATION.setLongitude(DOWNSTAIRS_LAB_LONG);
        // Downstairs Computer Lab
        LAB_LOCATIONS.put("Downstairs", DOWNSTAIRS_LAB_LOCATION);

        // Upstairs Computer Lab
        //TODO - get correct upstairs Latitude and longitude
       // LAB_LOCATIONS.put("Upstairs", UPSTAIRS_LAB_LOCATION);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
