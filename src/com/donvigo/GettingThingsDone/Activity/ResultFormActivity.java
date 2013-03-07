package com.donvigo.GettingThingsDone.Activity;

import android.R;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import com.donvigo.GettingThingsDone.Fragments.ResultFormFragment;

public class ResultFormActivity extends FragmentActivity {
    private ResultFormFragment resultFormFragment;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            // If the screen is now in landscape mode
            // we don't need this activity.
            finish();
            return;
        }

        openContentFragment();
    }

    private void openContentFragment(){
        resultFormFragment = new ResultFormFragment();
        resultFormFragment.setArguments(getIntent().getExtras());

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, resultFormFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
