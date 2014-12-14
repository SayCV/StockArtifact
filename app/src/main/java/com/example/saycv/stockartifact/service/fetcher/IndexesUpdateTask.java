package com.example.saycv.stockartifact.service.fetcher;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.saycv.stockartifact.IndexActivity;
import com.example.saycv.stockartifact.MainActivity;
import com.example.saycv.stockartifact.model.Index;
import com.example.saycv.stockartifact.screens.ScreenRadar;
import com.example.saycv.stockartifact.view.IndexAdapter;
import com.example.saycv.utils.NetworkDetector;

import java.util.List;

public class IndexesUpdateTask extends AsyncTask<Void, Integer, Boolean> {
    public static final String TAG = "IndexesUpdateTask";
    private Activity activity;
    private List<Index> results;

    private Error error;

    public IndexesUpdateTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... ignored) {
        Log.i(TAG, "running indexes update in background");
        if (!NetworkDetector.hasValidNetwork(activity)) {
            error = Error.ERROR_NO_NET;
            return Boolean.FALSE;
        }

        Log.i(TAG, "start fetcher");
        IndexesFetcher fetcher = IndexesFetcherFactory.getIndexesFetcher(activity);
        results = fetcher.fetch();

        return Boolean.TRUE;
    }

    private void updateIndexes(List<Index> indexes) {
        IndexAdapter adapter = ((ScreenRadar)activity).getIndexAdapter();
        adapter.clear();
        for (Index i : indexes) {
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
            updateIndexes(results);
        } else {
            Log.i(TAG, "update failure");
        }
    }

    enum Error {
        ERROR_NO_NET, ERROR_DOWNLOAD, ERROR_PARSE, ERROR_UNKNOWN
    }
}
