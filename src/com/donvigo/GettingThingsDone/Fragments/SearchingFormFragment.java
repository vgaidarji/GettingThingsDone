package com.donvigo.GettingThingsDone.Fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.donvigo.GettingThingsDone.Activity.Main;
import com.donvigo.GettingThingsDone.Activity.ResultFormActivity;
import com.donvigo.GettingThingsDone.AsyncTasks.BackgroundWorker;
import com.donvigo.GettingThingsDone.Services.TicketsFinderService;
import com.donvigo.GettingThingsDone.Services.WebService;
import com.donvigo.GettingThingsDone.Utils.*;
import com.donvigo.GettingThingsDone.R;
import com.donvigo.GettingThingsDone.Wrappers.Airline;
import com.donvigo.GettingThingsDone.Wrappers.AirlineFare;
import com.donvigo.GettingThingsDone.Wrappers.CityInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SearchingFormFragment extends Fragment implements CoordinatesReceiver.CoordinatesReceiverListener{
    private boolean dualPane;

    private LinearLayout linearLayoutCitySearchingProgress;
    private LinearLayout linearLayoutTicketsSearchingProgress;
    private LinearLayout linearLayoutSearchResult;
    private Button buttonFindAirlineTickets;
    private TextView textViewCity;
    private TextView textViewCitySearching;
    private TextView textViewTicketsSearching;

    private Messenger serviceMessenger;
    private Messenger formMessenger = new Messenger(new IncomingHandler());
    private boolean isServiceBound;
    private CoordinatesReceiver coordinatesReceiver;
    private CityInfo cityInfo;
    private String cityName;

    class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TicketsFinderService.TICKETS_FOUND:
                    ApplicationUtils.showNotification(getActivity(), getActivity().getString(R.string.tickets_found));

                    textViewTicketsSearching.setText(getString(R.string.loading_airline_tickets_caption));

                    BackgroundWorker worker = new BackgroundWorker(getActivity(), loadingTicketsTaskHost, false);
                    worker.execute((Void)null);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.searching_form, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            cityInfo = savedInstanceState.getParcelable("cityInfo");
            cityName = savedInstanceState.getString("cityName");
        }

        View resultFrame = getActivity().findViewById(R.id.resultForm);
        dualPane = resultFrame != null && resultFrame.getVisibility() == View.VISIBLE;

        initializeFormViews();
        initializeFormEvents();

        ArrayList<Airline> airlines = getActivity().getIntent().getParcelableArrayListExtra("airlines");
        if(airlines != null){
            showAirlines(airlines);

            linearLayoutCitySearchingProgress.setVisibility(View.GONE);
            linearLayoutSearchResult.setVisibility(View.VISIBLE);
            buttonFindAirlineTickets.setEnabled(true);

            cityName = PreferencesUtils.getCityName(getActivity());
            cityInfo = new CityInfo();
            cityInfo.setCity(cityName);
            cityInfo.setCityCode(PreferencesUtils.getCityCode(getActivity()));

            if(cityName != null){
                textViewCity.setText(cityName);
            }

            return;
        }

        if(cityInfo == null){
            ApplicationUtils.showNotification(getActivity(), getActivity().getString(R.string.searching_city_caption));
            startCoordinatesReceiver();
        }else{
            refreshFormViews(cityName);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("cityInfo", cityInfo);
        savedInstanceState.putString("cityName", cityName);

        if(getActivity() != null && cityInfo != null){
            PreferencesUtils.putCityCode(getActivity(), cityInfo.getCityCode());
            PreferencesUtils.putCityName(getActivity(), cityName);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        bindBackgroundService();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if(isServiceBound){
            if(serviceMessenger != null){
                try{
                    Message msg = Message.obtain(null, TicketsFinderService.UNREGISTER_OBSERVER);
                    msg.replyTo = formMessenger;
                    serviceMessenger.send(msg);
                }catch(RemoteException e){
                    // There is nothing special we need to do if the service has crashed.
                }
            }

            getActivity().unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            serviceMessenger = new Messenger(service);
            isServiceBound = true;

            registerServiceObserver();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceMessenger = null;
            isServiceBound = false;
        }
    };

    private void bindBackgroundService(){
        getActivity().bindService(
                new Intent(getActivity(), TicketsFinderService.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE);
    }

    private void sendObjectToService(CityInfo cityInfo){
        if(!isServiceBound){
            return;
        }

        Bundle data = new Bundle();
        data.putString("ID", cityInfo.getID());

        Message msg = Message.obtain(null, TicketsFinderService.START_TICKETS_FINDING);
        msg.setData(data);

        try {
            serviceMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void registerServiceObserver(){
        try {
            Message msg = Message.obtain(null, TicketsFinderService.REGISTER_OBSERVER);
            msg.replyTo = formMessenger;
            serviceMessenger.send(msg);
        } catch (RemoteException e) {
            // In this case the service has crashed before we could even do anything with it
        }
    }

    private void startCoordinatesReceiver(){
        coordinatesReceiver = new CoordinatesReceiver(getActivity(), SearchingFormFragment.this);
        coordinatesReceiver.initialize();
        coordinatesReceiver.start();
    }

    private void initializeFormViews(){
        linearLayoutCitySearchingProgress =
                (LinearLayout)getActivity().findViewById(R.id.linearLayoutCitySearchingProgress);
        linearLayoutTicketsSearchingProgress =
                (LinearLayout)getActivity().findViewById(R.id.linearLayoutTicketsSearchingProgress);
        linearLayoutSearchResult = (LinearLayout)getActivity().findViewById(R.id.linearLayoutSearchResult);
        buttonFindAirlineTickets = (Button)getActivity().findViewById(R.id.buttonFindAirlineTickets);
        buttonFindAirlineTickets.setEnabled(false);
        textViewCity = (TextView)getActivity().findViewById(R.id.textViewCity);
        textViewCitySearching = (TextView)getActivity().findViewById(R.id.textViewCitySearching);
        textViewTicketsSearching = (TextView)getActivity().findViewById(R.id.textViewTicketsSearching);
    }

    private void initializeFormEvents(){
        buttonFindAirlineTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationUtils.showNotification(
                        getActivity(),
                        getActivity().getString(R.string.searching_airline_tickets_caption));

                linearLayoutTicketsSearchingProgress.setVisibility(View.VISIBLE);
                buttonFindAirlineTickets.setVisibility(View.GONE);
                textViewTicketsSearching.setText(getString(R.string.search_initialization_caption));

                registerServiceObserver();

                BackgroundWorker worker = new BackgroundWorker(getActivity(), getSearchValuesTaskHost, false);
                worker.execute((Void) null);
            }
        });
    }

    private void refreshFormViews(String city){
        linearLayoutCitySearchingProgress.setVisibility(View.GONE);
        linearLayoutSearchResult.setVisibility(View.VISIBLE);

        if(city != null){
            textViewCity.setText(city);
            buttonFindAirlineTickets.setEnabled(true);
        }else{
            textViewCity.setText(getActivity().getString(R.string.city_is_not_determined));
        }
    }

    @Override
    public void coordinatesReceived(Location location) {
        if(getActivity() != null){
            cityName = Utils.getCityNameFromCoordinates(getActivity(), location.getLatitude(), location.getLongitude());
            Toast.makeText(getActivity(), "City = " + cityName, Toast.LENGTH_SHORT).show();

            textViewCitySearching.setText(getString(R.string.city_code_identification_caption));

            BackgroundWorker worker = new BackgroundWorker(getActivity(), getCityCodeTaskHost, false);
            worker.execute((Void)null);
        }
    }

    private BackgroundWorker.TaskHost getCityCodeTaskHost = new BackgroundWorker.TaskHost(){
        @Override
        public Object onBackgroundWorkStart() {
            return WebService.getCityInfo(cityName);
        }

        @Override
        public void onBackgroundWorkFinish(Object workResult) {
            if(workResult != null){
                cityInfo = (CityInfo) workResult;
                Toast.makeText(getActivity(), "City code = " + cityInfo.getCityCode(), Toast.LENGTH_SHORT).show();
                refreshFormViews(cityName);
            }
        }
    };

    private BackgroundWorker.TaskHost getSearchValuesTaskHost = new BackgroundWorker.TaskHost() {
        @Override
        public Object onBackgroundWorkStart() {
            String shortDateString = DateUtils.getShortDateString(new Date(), 7);
            return WebService.newRequest(shortDateString, cityInfo.getCityCode());
        }

        @Override
        public void onBackgroundWorkFinish(Object workResult) {
            if(workResult != null){
                cityInfo.setID(String.valueOf(workResult));

                if(isServiceBound){
                    sendObjectToService(cityInfo);
                }
            }
        }
    };

    private BackgroundWorker.TaskHost loadingTicketsTaskHost = new BackgroundWorker.TaskHost() {
        @Override
        public Object onBackgroundWorkStart() {
            ArrayList<Airline> airlines = WebService.getAirlines(cityInfo.getID());
            if(airlines != null && airlines.size() > 0){
                fillAdditionalAirlinesInformation(airlines);

                ApplicationUtils.saveAirlinesToInternalFile(getActivity(), airlines);
            }

            return airlines;
        }

        @Override
        public void onBackgroundWorkFinish(Object workResult) {
            linearLayoutTicketsSearchingProgress.setVisibility(View.GONE);
            buttonFindAirlineTickets.setVisibility(View.VISIBLE);

            if(workResult != null){
                @SuppressWarnings("unchecked")
                ArrayList<Airline> airlines = (ArrayList<Airline>) workResult;
                showAirlines(airlines);
            }else{
                Toast.makeText(getActivity(), getString(R.string.no_available_data), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void fillAdditionalAirlinesInformation(final ArrayList<Airline> airlines){
        for(Airline airline : airlines){
            airline.setTotalAmount(calculateTotalFaresAmount(airline));
            airline.setSelected(false);

            if(airline.getFaresFull() != null && airline.getFaresFull().size() > 0){
                airline.setCurrency(airline.getFaresFull().get(0).getCurrency());
            }
        }
    }

    private long calculateTotalFaresAmount(Airline airline){
        long totalAmount = 0;
        long currentAmount;
        ArrayList<AirlineFare> airlineFares = airline.getFaresFull();
        if(airlineFares != null && airlineFares.size() > 0){
            for(AirlineFare fare : airlineFares){
                currentAmount = 0;
                try{
                    currentAmount = Long.parseLong(fare.getTotalAmount());
                }catch(NumberFormatException nfe){}
                totalAmount += currentAmount;
            }
        }
        return totalAmount;
    }

    public void showAirlines(ArrayList<Airline> airlines){
        if(dualPane){
            ResultFormFragment resultFormFragment = (ResultFormFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.resultForm);
            if(resultFormFragment == null){
                resultFormFragment = ResultFormFragment.newInstance(airlines);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.resultForm, resultFormFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }else{
            Intent intent = new Intent(getActivity(), ResultFormActivity.class);
            intent.putExtra("airlines", airlines);
            startActivityForResult(intent, Main.RESTART_ACTIVITY);
        }
    }
}
