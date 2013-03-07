package com.donvigo.GettingThingsDone.Services;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import com.donvigo.GettingThingsDone.AsyncTasks.BackgroundWorker;
import com.donvigo.GettingThingsDone.R;
import com.donvigo.GettingThingsDone.Services.ServiceUtils.RequestStateStatus;
import com.donvigo.GettingThingsDone.Utils.ApplicationUtils;

import java.util.Timer;

public class TicketsFinderService extends Service {
    private static final int SEND_REQUESTS_INTERVAL = 3 * 1000;

    public static final int REGISTER_OBSERVER = 1;
    public static final int UNREGISTER_OBSERVER = 2;
    public static final int START_TICKETS_FINDING = 3;
    public static final int TICKETS_FOUND = 4;

    private Timer requestsTimer;
    private Messenger observer;
    private RunnableRequest request;
    private Handler requestSender = new Handler();
    private String ID;

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    final Messenger messenger = new Messenger(new IncomingHandler());

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationUtils.showNotification(this, this.getString(R.string.click_to_open_application));
    }

    class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REGISTER_OBSERVER:
                    observer = msg.replyTo;
                    break;
                case UNREGISTER_OBSERVER:
                    observer = null;
                    break;
                case START_TICKETS_FINDING:
                    Bundle bundle = msg.getData();
                    if(bundle != null){
                        ID = bundle.getString("ID");
                    }

                    request = new RunnableRequest();
                    requestSender.postDelayed(request, SEND_REQUESTS_INTERVAL);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private class RunnableRequest implements Runnable {
        @Override
        public void run() {
            BackgroundWorker worker = new BackgroundWorker(getBaseContext(), requestStateTaskHost, false);
            worker.execute((Void)null);
        }
    }

    private BackgroundWorker.TaskHost requestStateTaskHost = new BackgroundWorker.TaskHost() {
        @Override
        public Object onBackgroundWorkStart() {
            return WebService.requestState(ID);
        }

        @Override
        public void onBackgroundWorkFinish(Object workResult) {
            if(workResult != null){
                String completed = String.valueOf(workResult);

                if(completed != null && completed.equalsIgnoreCase(RequestStateStatus.COMPLETED)){
                    try {
                        Message msg = Message.obtain(null, TICKETS_FOUND);
                        observer.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        // observer is "dead"
                        observer = null;
                    }
                    requestSender.removeCallbacks(request);

                    if(requestsTimer != null)
                        requestsTimer.cancel();
                }
            }else{
                requestSender.postDelayed(request, SEND_REQUESTS_INTERVAL);
            }
        }
    };
}
