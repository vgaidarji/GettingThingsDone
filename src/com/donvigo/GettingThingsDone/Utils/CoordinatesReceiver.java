package com.donvigo.GettingThingsDone.Utils;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import com.donvigo.GettingThingsDone.Interfaces.LocationResultListener;

public class CoordinatesReceiver implements LocationResultListener {
    private static final int LOCATION_INTERVAL = 5 * 1000;
    private static final float LOCATION_DISTANCE = 0;

    private Activity activity;

    private LocationManager locationManager = null;
    private LocationListener[] locationListeners;

    public interface CoordinatesReceiverListener{
        public void coordinatesReceived(Location location);
    }

    private CoordinatesReceiverListener coordinatesReceiverListener;

    public CoordinatesReceiver(Activity activity, CoordinatesReceiverListener coordinatesReceiverListener){
        this.activity = activity;
        this.coordinatesReceiverListener = coordinatesReceiverListener;
    }

    public void start(){
        requestLocationUpdates();
    }

    public void initialize(){
        if (locationManager == null) {
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        }

        locationListeners = new LocationListener[] {
                new CustomLocationListener(LocationManager.GPS_PROVIDER, this),
                new CustomLocationListener(LocationManager.NETWORK_PROVIDER, this)
        };
    }

    private void requestLocationUpdates(){
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(CoordinatesReceiver.class.getSimpleName(), "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(CoordinatesReceiver.class.getSimpleName(), "gps provider does not exist " + ex.getMessage());
        }

        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(CoordinatesReceiver.class.getSimpleName(), "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(CoordinatesReceiver.class.getSimpleName(), "network provider does not exist, " + ex.getMessage());
        }
    }

    @Override
    public void onLocationReceived(Location location) {
        removeLocationUpdates();
        coordinatesReceiverListener.coordinatesReceived(location);
    }

    private void removeLocationUpdates(){
        if (locationManager != null) {
            for(LocationListener listener : locationListeners){
                try{
                    locationManager.removeUpdates(listener);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }
}
