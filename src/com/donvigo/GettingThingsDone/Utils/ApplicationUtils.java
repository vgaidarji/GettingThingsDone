package com.donvigo.GettingThingsDone.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.donvigo.GettingThingsDone.Activity.Main;
import com.donvigo.GettingThingsDone.R;
import com.donvigo.GettingThingsDone.Services.ServiceUtils.GetFaresResponse;
import com.donvigo.GettingThingsDone.Wrappers.Airline;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationUtils {
    public static final int NOTIFICATION_ID = 1;

    public static void showNotification(Context context, String contentText) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(contentText);

        Intent notificationIntent = new Intent(context, Main.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }

    public static void saveAirlinesToInternalFile(Context context, ArrayList<Airline> airlines){
        FileOutputStream outputStream = null;
        String airlinesJSON;
        try {
            Type listType = new TypeToken<List<Airline>>() {}.getType();
            Gson gson = new Gson();
            airlinesJSON = gson.toJson(airlines, listType);

            outputStream = context.openFileOutput(Constants.INTRENAL_JSON_FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(airlinesJSON.getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ArrayList<Airline> readAirlinesFromInternalFile(Context context){
        FileInputStream inputStream = null;
        try {
            inputStream = context.openFileInput(Constants.INTRENAL_JSON_FILE_NAME);

            if (inputStream != null) {
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(inputStream);

                Airline[] result = gson.fromJson(reader,
                        Airline[].class);
                if(result != null)
                    return new ArrayList<Airline>(Arrays.asList(result));
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
