package com.mohsenoid.androidutils.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

public class LocationHelper {
    String TAG = this.getClass().getSimpleName();
    Context context;
    private final LocationManager locationManager;

    private final CurrentLocationListener listener;
    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged() Status=" + status);

            if (listener != null)
                switch (status) {
                    case LocationProvider.AVAILABLE:
                        listener.onProviderAvailable();
                        break;
                    case LocationProvider.OUT_OF_SERVICE:
                        locationManager.removeUpdates(locationListener);
                        listener.onProviderUnavailable();
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        locationManager.removeUpdates(locationListener);
                        listener.onProviderUnavailable();
                        break;
                }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled()");

            if (listener != null)
                listener.onProviderAvailable();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled()");

            if (listener != null)
                listener.onProviderUnavailable();
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged() Location=" + location.toString());

            locationManager.removeUpdates(locationListener);
            if (listener != null)
                listener.onLocationChanged(location);
        }
    };

    public LocationHelper(Context context, String provider,
                          CurrentLocationListener listener) {
        Log.i(TAG, "Cunstructor! Provider=" + provider);
        this.context = context;
        this.listener = listener;

        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(provider, 5000, 0,
                    locationListener);
        } catch (Exception e) {
            if (listener != null)
                listener.onProviderUnavailable();
            e.printStackTrace();
        }
    }

    public LocationHelper(Context context, CurrentLocationListener listener) {
        Log.i(TAG, "Cunstructor!");
        this.context = context;
        this.listener = listener;

        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            networkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // don't start listeners if no provider is enabled
        if (!gpsEnabled && !networkEnabled) {
            Log.i(TAG, "onProviderUnavailable()");
            if (listener != null)
                listener.onProviderUnavailable();
        }

        if (gpsEnabled) {
            Log.i(TAG, "GPS Provider Available!");

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        }
        if (networkEnabled) {
            Log.i(TAG, "Network Provider Available!");

            locationManager
                    .requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            5000, 0, locationListener);
        }

    }

    public void pause() {
        Log.i(TAG, "Paused!");
        locationManager.removeUpdates(locationListener);
    }

    public interface CurrentLocationListener {
        void onProviderAvailable();

        void onProviderUnavailable();

        void onLocationChanged(Location location);
    }
}
