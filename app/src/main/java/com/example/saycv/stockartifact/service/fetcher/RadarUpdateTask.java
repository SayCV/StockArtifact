package com.example.saycv.stockartifact.service.fetcher;

import com.example.saycv.stockartifact.RadarActivity;
import com.example.saycv.stockartifact.model.Radar;
import com.example.saycv.stockartifact.view.RadarAdapter;
import com.example.saycv.utils.NetworkDetector;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

public class RadarUpdateTask extends AsyncTask<Void, Integer, Boolean> {
    public static final String TAG = "RadarUpdateTask";
    private RadarActivity activity;
    private List<Radar> results;

    private Error error;
    enum Error {
        ERROR_NO_NET, ERROR_DOWNLOAD, ERROR_PARSE, ERROR_UNKNOWN
    }

    public RadarUpdateTask(RadarActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void ... ignored) {
        Log.i(TAG, "running indexes update in background");
        if (!NetworkDetector.hasValidNetwork(activity)) {
            error = Error.ERROR_NO_NET;
            return Boolean.FALSE;
        }

        Log.i(TAG, "start fetcher");
        RadarFetcher fetcher = RadarFetcherFactory.getRadarFetcher(activity);
        results = fetcher.fetch();

        return Boolean.TRUE;
    }

    private void updateIndexes(List<Radar> radar) {
        RadarAdapter adapter = activity.getRadarAdapter();
        adapter.clear();
        for(Radar i : radar) {
            adapter.add(i);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPreExecute() {
        activity.getParent().setProgressBarVisibility(true);
        activity.getParent().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected void onCancelled() {
        activity.getParent().setProgressBarVisibility(false);
        activity.getParent().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        activity.getParent().setProgressBarVisibility(false);
        activity.getParent().setProgressBarIndeterminateVisibility(false);
        if (result) {
            Log.i(TAG, "update success, number of results ..." + results.size());
            updateIndexes(results);
        } else {
            Log.i(TAG, "update failure");
        }
    }
}
