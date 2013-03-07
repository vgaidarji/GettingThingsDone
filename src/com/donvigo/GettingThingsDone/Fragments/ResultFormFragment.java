package com.donvigo.GettingThingsDone.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.donvigo.GettingThingsDone.Activity.Main;
import com.donvigo.GettingThingsDone.Adapters.AirlinesListAdapter;
import com.donvigo.GettingThingsDone.AsyncTasks.BackgroundWorker;
import com.donvigo.GettingThingsDone.R;
import com.donvigo.GettingThingsDone.Utils.ApplicationUtils;
import com.donvigo.GettingThingsDone.Wrappers.Airline;
import com.donvigo.GettingThingsDone.Wrappers.AirlineFare;
import com.donvigo.GettingThingsDone.Wrappers.ViewHolderAirlinesList;

import java.util.ArrayList;

public class ResultFormFragment extends Fragment {
    private Button buttonNewSearch;
    private LinearLayout linearLayoutListInitialization;
    private ListView listViewAirlines;
    private AirlinesListAdapter adapterAirlines;

    public static ResultFormFragment newInstance(ArrayList<Airline> airlines){
        ResultFormFragment f = new ResultFormFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList("airlines", airlines);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(container == null){
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

        return inflater.inflate(R.layout.result_form, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState == null){
            initializeFormViews();
            initializeEvents();

            loadList();
        }
    }

    private void initializeFormViews(){
        buttonNewSearch = (Button)getActivity().findViewById(R.id.buttonNewSearch);
        linearLayoutListInitialization = (LinearLayout)getActivity().findViewById(R.id.linearLayoutListInitialization);
        listViewAirlines = (ListView)getActivity().findViewById(R.id.listViewAirlines);
    }

    private void initializeEvents(){
        buttonNewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    Intent i = new Intent(getActivity(), Main.class);
                    startActivity(i);

                    getActivity().finish();
                }else{
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            }
        });

        listViewAirlines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Airline airline = (Airline) adapterAirlines.getItem(position);
                airline.setSelected(!airline.isSelected());
                ViewHolderAirlinesList viewHolder = (ViewHolderAirlinesList) view.getTag();
                viewHolder.chooser.setChecked(airline.isSelected());
            }
        });
    }

    private void loadList(){
        linearLayoutListInitialization.setVisibility(View.VISIBLE);
        listViewAirlines.setVisibility(View.GONE);
        BackgroundWorker worker = new BackgroundWorker(getActivity(), listInitializationTaskHost, false);
        worker.execute((Void)null);
    }

    private BackgroundWorker.TaskHost listInitializationTaskHost = new BackgroundWorker.TaskHost() {
        @Override
        public Object onBackgroundWorkStart() {
            Bundle d = getArguments();
            return d.getParcelableArrayList("airlines");
        }

        @Override
        public void onBackgroundWorkFinish(Object workResult) {
            linearLayoutListInitialization.setVisibility(View.GONE);
            listViewAirlines.setVisibility(View.VISIBLE);

            if(workResult != null){
                @SuppressWarnings("unchecked")
                ArrayList<Airline> airlines = (ArrayList<Airline>) workResult;
                fillList(airlines);
            }
        }
    };

    private void fillList(ArrayList<Airline> airlines){
        if(airlines != null && airlines.size() > 0){
            adapterAirlines = new AirlinesListAdapter(getActivity());
            for(Airline airline : airlines){
                adapterAirlines.addItem(airline);
            }
            listViewAirlines.setAdapter(adapterAirlines);
        }
    }
}
