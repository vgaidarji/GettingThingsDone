package com.donvigo.GettingThingsDone.Utils;

import android.location.Location;
import android.os.Bundle;
import com.donvigo.GettingThingsDone.Interfaces.LocationResultListener;

public class CustomLocationListener implements android.location.LocationListener{
    private Location lastLocation;
    private LocationResultListener locationResultListener;

    public CustomLocationListener(String provider, LocationResultListener locationResultListener){
        lastLocation = new Location(provider);
        this.locationResultListener = locationResultListener;
    }

    @Override
    public void onLocationChanged(Location location){
        locationResultListener.onLocationReceived(location);
        lastLocation.set(location);
    }
    @Override
    public void onProviderDisabled(String provider){
    }
    @Override
    public void onProviderEnabled(String provider){
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){
    }
}
