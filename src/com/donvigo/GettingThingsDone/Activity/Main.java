package com.donvigo.GettingThingsDone.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import com.donvigo.GettingThingsDone.AsyncTasks.BackgroundWorker;
import com.donvigo.GettingThingsDone.R;
import com.donvigo.GettingThingsDone.Fragments.ResultFormFragment;
import com.donvigo.GettingThingsDone.Utils.ApplicationUtils;

public class Main extends FragmentActivity {
    public static final int RESTART_ACTIVITY = 666;
    private Activity activity;
    private Button buttonNewSearch;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ApplicationUtils.showNotification(this, this.getString(R.string.click_to_open_application));

        activity = this;

        initializeFormViews();
        initializeFormEvents();
        // TODO: add "negative" action checking in application: no internet connection, etc.
        // TODO: implement loaders or other architecture, which will remove view leaks on orientation changing
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            // if fragment was created in landscape orientation
            // remove it from view hierarchy in portrait orientation
            ResultFormFragment resultFormFragment = (ResultFormFragment)getSupportFragmentManager().findFragmentById(R.id.resultForm);

            if(resultFormFragment != null){
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.remove(resultFormFragment);
                ft.commit();
            }
        }

        super.onSaveInstanceState(outState);
    }

    private void initializeFormViews(){
        buttonNewSearch = (Button)findViewById(com.donvigo.GettingThingsDone.R.id.buttonNewSearch);
    }

    private void initializeFormEvents(){
        if(buttonNewSearch != null){
            buttonNewSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(activity, Main.class);
                    startActivity(i);

                    finish();
                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESTART_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Intent i = new Intent(activity, Main.class);
                    startActivity(i);

                    finish();
                }
                break;
        }
    }
}
