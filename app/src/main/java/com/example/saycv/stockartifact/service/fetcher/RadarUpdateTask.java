package com.example.saycv.stockartifact.service.fetcher;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.saycv.stockartifact.Engine;
import com.example.saycv.stockartifact.MainActivity;
import com.example.saycv.stockartifact.events.RadarsEventArgs;
import com.example.saycv.stockartifact.events.RadarsEventTypes;
import com.example.saycv.stockartifact.model.RadarsHistoryEvent;
import com.example.saycv.stockartifact.model.Radar;
import com.example.saycv.stockartifact.screens.ScreenRadar;
import com.example.saycv.stockartifact.service.impl.RadarsHistoryService;
import com.example.saycv.stockartifact.service.impl.RadarsService;
import com.example.saycv.stockartifact.view.RadarAdapter;
import com.example.saycv.utils.NetworkDetector;

import org.saycv.sgs.model.SgsHistoryEvent;
import org.saycv.sgs.utils.SgsDateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class RadarUpdateTask extends AsyncTask<Void, Integer, Boolean> {
    private static final String TAG = RadarUpdateTask.class.getCanonicalName();
    private static final String EVENT_TAG = TAG;

    private Activity activity;
    private List<Radar> results;

    private Thread mRadarsUpdateThread = null;
    private Error error;

    public RadarUpdateTask(Activity activity) {
        this.activity = activity;
    }

    public void clearRadarData() {
        if (this.results != null) {
            this.results.clear();
        } else {
            this.results = new ArrayList<Radar>();
        }
    }

    public void addRadarData(List<Radar> results) {
        this.results.addAll(results);
    }

    public List<Radar> getRadarData() {
        return results;
    }

    @Override
    protected Boolean doInBackground(Void... ignored) {
        Log.i(TAG, "running Radar update in background");

        //setRadarsUpdateThreadClassEnabled(true);

        return Boolean.TRUE;
    }

    public void updateRadars(List<Radar> radar) {
        RadarAdapter adapter = ((ScreenRadar)activity).getRadarAdapter();
        adapter.clear();
        if (radar != null) {
            for (Radar i : radar) {
                adapter.add(i);
            }
            adapter.notifyDataSetChanged();
        } else {
            Log.d(TAG, "radars is null");
        }
    }

    public void updateRadars() {
        RadarAdapter adapter = ((ScreenRadar)activity).getRadarAdapter();
        adapter.clear();

        /*if ( results!=null ) {
            for (Radar i : results) {
                adapter.add(i);
            }
            adapter.notifyDataSetChanged();
        } else {
            Log.d(TAG, "results is null");
        }*/
        //RadarsService radarsService = (RadarsService)((Engine)Engine.getInstance()).getRadarsService();
        //RadarsHistoryService radarsHistoryService = (RadarsHistoryService)radarsService.getHistoryService();

            List<SgsHistoryEvent> sgsHistoryEvent = ((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).getHistoryService().getEvents();
            if (sgsHistoryEvent.size() > 0) {
                RadarsHistoryEvent radarsHistoryEvent = ((RadarsHistoryEvent) sgsHistoryEvent.get(sgsHistoryEvent.size() -1));
                List<Radar> radars = radarsHistoryEvent.getRadarContent();
                if (radars != null) {
                    for (Radar i : radars) {
                        adapter.add(i);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "radars is null");
                }
            } else {
                Log.d(TAG, "Not addEvent RADARS_EVENT_2 to history");
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
        if (result && results != null) {
            Log.i(TAG, "update success, number of results ..." + results.size());
            updateRadars(results);
        } else {
            Log.i(TAG, "update failure");
        }*/
    }

    enum Error {
        ERROR_NO_NET, ERROR_DOWNLOAD, ERROR_PARSE, ERROR_UNKNOWN
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

        RadarsHistoryEvent event;


        public RadarsUpdateThreadClass() {
            //event = new RadarsHistoryEvent(RadarsEventArgs.EXTRA_REMOTE_PARTY, SgsHistoryEvent.StatusType.RADARS_ALL);
            event = new RadarsHistoryEvent(RadarsEventArgs.EXTRA_REMOTE_PARTY, EVENT_TAG);
            //((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).getHistoryService().deleteEvent(event);
        }

        //@Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (!NetworkDetector.hasValidNetwork(activity)) {
                    error = Error.ERROR_NO_NET;
                    return;
                }

                Log.i(TAG, "start fetcher");

                    ((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).getHistoryService().clear();

                RadarFetcher fetcher = RadarFetcherFactory.getRadarFetcher(activity);
                List<Radar> results = fetcher.fetch();

                if(results!=null && results.size() > 0) {
                    //HistoryRadarsEvent event;
                    //event = new HistoryRadarsEvent(RadarsEventArgs.EXTRA_REMOTE_PARTY, SgsHistoryEvent.StatusType.RADARS_ALL);
                            /*event.setContent(new String("Start Tethering -- TotalUpload: " +
                                    Long.toString(args.getTotalUpload()) + ", TotalDownload: " + Long.toString(args.getTotalDownload())));
                            */
                    //Log.d(TAG, "Traffic Count getTotalUpload = " + args.getTotalUpload());
                    //Log.d(TAG, "Traffic Count getTotalDownload = " + args.getTotalDownload());
                    //event.setRadarContent(results);
                    //event.setStartTime(SgsDateTimeUtils.parseDate(dateString).getTime());
                            /*if (mEngine.getHistoryService().getEvents().size() != 0 && mEngine.getHistoryService().getEvents().get(0).compareTo(event) == 0) {
                                Log.d(TAG, "Found Redundant Traffic Count Event, will avoid this");
                                break;
                            }*/
                    //((RadarsHistoryEvent)event).setRadarsData(results);
                    event.setStartTime(Long.parseLong(SgsDateTimeUtils.now("yyyyMMddHHmmss")));
                    event.setRadarContent(results);
                    event.setIndexesContent(null);

                    //((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).getHistoryService().addEvent(event);
                    /* sendBroadcast when start */
                    ((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).broadcastRadarsEvent(
                            new RadarsEventArgs(RadarsEventTypes.RADARS_EVENT_2, null,
                                    null),
                            SgsDateTimeUtils.now()
                    );
                    //Only the original thread that created a view hierarchy can touch its views
                    //updateRadars(results);
                    int avoidTransactionTooLargeExceptionSize = 1000;
                    int oldWantTransactionSize = results.size();
                    int transactionCounts = 1;
                    int transactionStart = 0;
                    int transactionEnd = 0;
                    int transactionDone = 0;
                    while(transactionDone==0) {
                        if (transactionCounts * avoidTransactionTooLargeExceptionSize < oldWantTransactionSize) {
                            transactionEnd = transactionStart + avoidTransactionTooLargeExceptionSize;
                        } else {
                            transactionEnd = oldWantTransactionSize;
                            transactionDone = 1;
                        }
                        transactionStart = (transactionCounts - 1) * avoidTransactionTooLargeExceptionSize;
                        ((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).broadcastRadarsEvent(
                                new RadarsEventArgs(RadarsEventTypes.RADARS_EVENT_3, null,
                                        results.subList(transactionStart, transactionEnd)),
                                SgsDateTimeUtils.now()
                        );
                        transactionCounts++;
                    }
                    /* sendBroadcast when finished */
                    ((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).broadcastRadarsEvent(
                            new RadarsEventArgs(RadarsEventTypes.RADARS_EVENT_4, null,
                                    null),
                            SgsDateTimeUtils.now()
                    );
                    /*((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).broadcastRadarsEvent(
                            new RadarsEventArgs(RadarsEventTypes.RADARS_EVENT_2, null, results),
                            SgsDateTimeUtils.now()
                    );*/
                }
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
