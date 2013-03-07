package com.donvigo.GettingThingsDone.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * <br><br>Provides background work execution. Followed by showing <code>ProgressDialog</code>
 * <br>Background working methods are provided with <code>BackgroundWorkerTaskHost</code>
 * @see TaskHost
 * @author VGaidarji
 */
public class BackgroundWorker extends AsyncTask<Void, Void, Object>{
    private Context context;
	private TaskHost workerTaskHost;
	private static ProgressDialog progressDialog;
	private String progressMessage;
	private boolean isProgressDialog;

    public interface TaskHost {
        public Object onBackgroundWorkStart();
        public void onBackgroundWorkFinish(Object workResult);
    }

    public BackgroundWorker(Context context, TaskHost workerTaskHost, boolean isProgressDialog){
        this.context = context;
        this.workerTaskHost = workerTaskHost;
        this.isProgressDialog = isProgressDialog;
        progressMessage = "";
    }

	/**
	 * Set progress message, which would be shown during background work execution
	 * @param message Progress message to be shown
	 */
	public void setProgressMessage(String message){
		progressMessage = message;
	}
	
	public static ProgressDialog getProgressDialog(){
		return progressDialog;
	}

	@Override
	protected Object doInBackground(Void... params) {
		return workerTaskHost.onBackgroundWorkStart();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(isProgressDialog){
            if(context != null){
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage(progressMessage);
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            }
		}
	}
	
	@Override
	protected void onProgressUpdate(Void... progress) {
		super.onProgressUpdate(progress);
	}

	@Override
	protected void onPostExecute(Object result) {
		if(isCancelled())
			return;
		
		if (progressDialog != null && progressDialog.isShowing())
			progressDialog.cancel();

		workerTaskHost.onBackgroundWorkFinish(result);
	}
}
