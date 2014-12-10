package com.example.saycv.stockartifact.service.fetcher;

import com.example.saycv.stockartifact.MainActivity;
import com.example.saycv.stockartifact.RadarActivity;
import com.example.saycv.stockartifact.model.Radar;
import com.example.saycv.stockartifact.view.RadarAdapter;
import com.example.saycv.utils.NetworkDetector;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class RadarUpdateTask extends AsyncTask<Void, Integer, Boolean> {
    public static final String TAG = "RadarUpdateTask";
    private MainActivity activity;
    private List<Radar> results;

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

        while(Boolean.TRUE) {

            try {
                Thread.sleep(10000);//delay 10s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!NetworkDetector.hasValidNetwork(activity)) {
                error = Error.ERROR_NO_NET;
                return Boolean.FALSE;
            }

            Log.i(TAG, "start fetcher");
            RadarFetcher fetcher = RadarFetcherFactory.getRadarFetcher(activity);
            results = fetcher.fetch();
        }

        return Boolean.TRUE;
    }

    private void updateRadars(List<Radar> radar) {
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
        if (result) {
            Log.i(TAG, "update success, number of results ..." + results.size());
            updateRadars(results);
        } else {
            Log.i(TAG, "update failure");
        }
    }
}
