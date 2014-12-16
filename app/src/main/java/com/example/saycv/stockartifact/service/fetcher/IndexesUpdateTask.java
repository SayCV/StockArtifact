package com.example.saycv.stockartifact.service.fetcher;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.saycv.stockartifact.Engine;
import com.example.saycv.stockartifact.IndexActivity;
import com.example.saycv.stockartifact.MainActivity;
import com.example.saycv.stockartifact.events.RadarsEventArgs;
import com.example.saycv.stockartifact.events.RadarsEventTypes;
import com.example.saycv.stockartifact.model.Index;
import com.example.saycv.stockartifact.model.RadarsHistoryEvent;
import com.example.saycv.stockartifact.screens.ScreenRadar;
import com.example.saycv.stockartifact.service.impl.RadarsService;
import com.example.saycv.stockartifact.view.IndexAdapter;
import com.example.saycv.utils.NetworkDetector;

import org.saycv.sgs.model.SgsHistoryEvent;
import org.saycv.sgs.utils.SgsDateTimeUtils;

import java.util.List;

public class IndexesUpdateTask extends AsyncTask<Void, Integer, Boolean> {
    private static final String TAG = IndexesUpdateTask.class.getCanonicalName();
    private static final String EVENT_TAG = TAG;

    private Activity activity;
    private List<Index> results;

    private Thread mIndexesUpdateThread = null;
    private Error error;

    public IndexesUpdateTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... ignored) {
        Log.i(TAG, "running indexes update in background");


        return Boolean.TRUE;
    }

    public void updateIndexes(List<Index> indexes) {
        IndexAdapter adapter = ((ScreenRadar)activity).getIndexAdapter();
        adapter.clear();
        if (indexes != null) {
            for (Index i : indexes) {
                adapter.add(i);
            }
            adapter.notifyDataSetChanged();
        } else {
            Log.d(TAG, "indexes is null");
        }
    }

    public void updateIndexes() {
        IndexAdapter adapter = ((ScreenRadar)activity).getIndexAdapter();
        adapter.clear();


            List<SgsHistoryEvent> sgsHistoryEvent = ((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).getHistoryService().getEvents();
            if (sgsHistoryEvent.size() > 0) {
                RadarsHistoryEvent radarsHistoryEvent = ((RadarsHistoryEvent) sgsHistoryEvent.get(RadarsEventTypes.RADARS_EVENT_1.getValue()));
                List<Index> indexes = radarsHistoryEvent.getIndexesContent();
                if (indexes != null) {
                    for (Index i : indexes) {
                        adapter.add(i);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "indexes is null");
                }
            } else {
                Log.d(TAG, "Not addEvent RADARS_EVENT_1 to history");
            }

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
        /*activity.setProgressBarVisibility(false);
        activity.setProgressBarIndeterminateVisibility(false);
        if (result) {
            Log.i(TAG, "update success, number of results ..." + results.size());
            updateIndexes(results);
        } else {
            Log.i(TAG, "update failure");
        }*/
    }

    enum Error {
        ERROR_NO_NET, ERROR_DOWNLOAD, ERROR_PARSE, ERROR_UNKNOWN
    }

    public void setIndexesUpdateThreadClassEnabled(boolean enabled) {
        if (enabled == true) {
            if (this.mIndexesUpdateThread == null || this.mIndexesUpdateThread.isAlive() == false) {
                this.mIndexesUpdateThread = new Thread(new IndexesUpdateThreadClass());
                this.mIndexesUpdateThread.start();
            }
        } else {
            if (this.mIndexesUpdateThread != null)
                this.mIndexesUpdateThread.interrupt();
        }
    }

    class IndexesUpdateThreadClass implements Runnable {

        RadarsHistoryEvent event;


        public IndexesUpdateThreadClass() {
            //event = new RadarsHistoryEvent(RadarsEventArgs.EXTRA_REMOTE_PARTY, SgsHistoryEvent.StatusType.RADARS_ALL);
            event = new RadarsHistoryEvent(RadarsEventArgs.EXTRA_REMOTE_PARTY, EVENT_TAG);
            //((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).getHistoryService().deleteEvent(event);
        }

        //@Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (!NetworkDetector.hasValidNetwork(activity)) {
                    error = Error.ERROR_NO_NET;
                    return ;
                }

                Log.i(TAG, "start fetcher");

                 ((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).getHistoryService().deleteEvent(event);

                IndexesFetcher fetcher = IndexesFetcherFactory.getIndexesFetcher(activity);
                results = fetcher.fetch();

                if(results!=null && results.size() > 0) {
                    //HistoryRadarsEvent event;
                    //event = new HistoryRadarsEvent(RadarsEventArgs.EXTRA_REMOTE_PARTY, SgsHistoryEvent.StatusType.RADARS_ALL);
                            /*event.setContent(new String("Start Tethering -- TotalUpload: " +
                                    Long.toString(args.getTotalUpload()) + ", TotalDownload: " + Long.toString(args.getTotalDownload())));
                            */
                    //Log.d(TAG, "Traffic Count getTotalUpload = " + args.getTotalUpload());
                    //Log.d(TAG, "Traffic Count getTotalDownload = " + args.getTotalDownload());
                    //event.setIndexesContent(results);
                    //event.setStartTime(SgsDateTimeUtils.parseDate(dateString).getTime());
                            /*if (mEngine.getHistoryService().getEvents().size() != 0 && mEngine.getHistoryService().getEvents().get(0).compareTo(event) == 0) {
                                Log.d(TAG, "Found Redundant Traffic Count Event, will avoid this");
                                break;
                            }*/
                    //((RadarsHistoryEvent)event).setRadarsData(results);
                    event.setStartTime(Long.parseLong(SgsDateTimeUtils.now("yyyyMMddHHmmss")));
                    event.setIndexesContent(results);
                    event.setRadarContent(null);


                     //((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).getHistoryService().addEvent(event);

                    //Only the original thread that created a view hierarchy can touch its views
                    //updateRadars(results);

                    ((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).broadcastRadarsEvent(
                            new RadarsEventArgs(RadarsEventTypes.RADARS_EVENT_1, results, null),
                            SgsDateTimeUtils.now()
                    );
                }
                //activity.

                // Taking a nap - 1s
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }
}
