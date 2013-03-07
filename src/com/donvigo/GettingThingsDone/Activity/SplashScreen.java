package com.donvigo.GettingThingsDone.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import com.donvigo.GettingThingsDone.R;
import com.donvigo.GettingThingsDone.Utils.ApplicationUtils;
import com.donvigo.GettingThingsDone.Wrappers.Airline;

import java.util.ArrayList;

public class SplashScreen extends Activity {
    private static int DISPLAY_DURATION = 1500;

    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        activity = this;

        waitAndChangeActivity();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    private void waitAndChangeActivity(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Thread.sleep(DISPLAY_DURATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ArrayList<Airline> airlines = ApplicationUtils.readAirlinesFromInternalFile(activity);
                Intent i = new Intent(activity, Main.class);
                i.putExtra("airlines", airlines);
                startActivity(i);

                finish();
            }
        }).start();
    }
}
