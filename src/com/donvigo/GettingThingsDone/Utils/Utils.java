package com.donvigo.GettingThingsDone.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static String streamToString(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return new String(baos.toByteArray());
    }

    /**
     * Returns city name (used russian locale as default) for given coordinates, or null - if city is not determined.
     * NOTE: 'ё' character replaced by 'е', because function from API does not accept city with 'ё' character
     */
    public static String getCityNameFromCoordinates(Context context, double latitude, double longitude){
        List<Address> addresses = new ArrayList<Address>();

        Locale russianLocale = new Locale("ru");
        Geocoder geocoder = new Geocoder(context, russianLocale);

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses != null && addresses.size() > 0 && addresses.get(0).getLocality() != null){
            return addresses.get(0).getLocality().replace("ё", "е");
        }else{
            return null;
        }
    }
}
