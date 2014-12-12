package com.example.saycv.stockartifact.service.fetcher;

import com.example.saycv.stockartifact.Engine;
import com.example.saycv.stockartifact.MainActivity;
import com.example.saycv.stockartifact.RadarActivity;
import com.example.saycv.stockartifact.events.RadarsEventArgs;
import com.example.saycv.stockartifact.events.RadarsEventTypes;
import com.example.saycv.stockartifact.model.Radar;
import com.example.saycv.stockartifact.service.impl.RadarsService;
import com.example.saycv.stockartifact.view.RadarAdapter;
import com.example.saycv.utils.NetworkDetector;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.saycv.sgs.utils.SgsDateTimeUtils;

public class RadarUpdateTask extends AsyncTask<Void, Integer, Boolean> {
    public static final String TAG = "RadarUpdateTask";
    private MainActivity activity;
    private List<Radar> results;

    private Thread mRadarsUpdateThread = null;
    private Error error;
    enum Error {
        ERROR_NO_NET, ERROR_DOWNLOAD, ERROR_PARSE, ERROR_UNKNOWN
    }

    public RadarUpdateTask(Activity activity) {
        this.activity = (MainActivity) activity;
    }

    @Override
    protected Boolean doInBackground(Void ... ignored) {
        Log.i(TAG, "running Radar update in background");

        setRadarsUpdateThreadClassEnabled(true);

        return Boolean.TRUE;
    }

    public void updateRadars(List<Radar> radar) {
        RadarAdapter adapter = activity.getRadarAdapter();
        adapter.clear();
        for(Radar i : radar) {
            adapter.add(i);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPreExecute() {
        activity.setProgressBarVisibility(true);
        activity.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected void onCancelled() {
        activity.setProgressBarVisibility(false);
        activity.setProgressBarIndeterminateVisibility(false);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        activity.setProgressBarVisibility(false);
        activity.setProgressBarIndeterminateVisibility(false);
        if (result && results != null) {
            Log.i(TAG, "update success, number of results ..." + results.size());
            updateRadars(results);
        } else {
            Log.i(TAG, "update failure");
        }
    }

    public void setRadarsUpdateThreadClassEnabled(boolean enabled) {
        if (enabled == true) {
            if (this.mRadarsUpdateThread == null || this.mRadarsUpdateThread.isAlive() == false) {
                this.mRadarsUpdateThread = new Thread(new RadarsUpdateThreadClass());
                this.mRadarsUpdateThread.start();
            }
        } else {
            if (this.mRadarsUpdateThread != null)
                this.mRadarsUpdateThread.interrupt();
        }
    }

    class RadarsUpdateThreadClass implements Runnable {

        public RadarsUpdateThreadClass() {
        }
        //@Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (!NetworkDetector.hasValidNetwork(activity)) {
                    error = Error.ERROR_NO_NET;
                    return ;
                }

                Log.i(TAG, "start fetcher");
                RadarFetcher fetcher = RadarFetcherFactory.getRadarFetcher(activity);
                results = fetcher.fetch();

                //Only the original thread that created a view hierarchy can touch its views
                //updateRadars(results);
                ((RadarsService)((Engine)Engine.getInstance()).getRadarsService()).broadcastRadarsEvent(
                        new RadarsEventArgs(RadarsEventTypes.RADARS_EVENT_1,
                                results),
                        SgsDateTimeUtils.now()
                );
                //activity.

                // Taking a nap - 10s
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }
}
