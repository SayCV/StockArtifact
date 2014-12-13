package com.example.saycv.stockartifact;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

import com.example.saycv.stockartifact.events.RadarsEventArgs;
import com.example.saycv.stockartifact.events.RadarsEventTypes;
import com.example.saycv.stockartifact.model.HistoryRadarsEvent;
import com.example.saycv.stockartifact.model.Radar;
import com.example.saycv.stockartifact.service.impl.RadarsService;

import org.saycv.logger.Log;
import org.saycv.sgs.SgsNativeService;
import org.saycv.sgs.events.SgsEventArgs;
import org.saycv.sgs.model.SgsHistoryEvent;
import org.saycv.sgs.utils.SgsDateTimeUtils;
import org.saycv.sgs.utils.SgsStringUtils;

import java.util.ArrayList;
import java.util.List;

public class NativeService extends SgsNativeService {
    private final static String TAG = NativeService.class.getCanonicalName();

    public static final String ACTION_STATE_EVENT = TAG + ".ACTION_STATE_EVENT";

    private PowerManager.WakeLock mWakeLock;
    private BroadcastReceiver mBroadcastReceiver;
    private Engine mEngine;

    public NativeService() {
        super();
        mEngine = (Engine) Engine.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");

        final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null && mWakeLock == null) {
            mWakeLock = powerManager.newWakeLock(PowerManager.ON_AFTER_RELEASE
                    | PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
        }
        //final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        /*if(mEngine != null){
            mEngine.getInstance().getMainActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }*/
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart()");

        // register()
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                Log.d(TAG, "BroadcastReceiver: " + action.toString());
                // Registration Events

                if (RadarsEventArgs.ACTION_RADARS_EVENT.equals(action)) {
                    Log.d(TAG, "RadarsEventArgs.ACTION_RADARS_EVENT.");
                    RadarsEventArgs args = intent.getParcelableExtra(SgsEventArgs.EXTRA_EMBEDDED);
                    final RadarsEventTypes type;
                    if (args == null) {
                        Log.e(TAG, "Invalid event args");
                        return;
                    }
                    String dateString = intent.getStringExtra(RadarsEventArgs.EXTRA_DATE);
                    String remoteParty = intent.getStringExtra(RadarsEventArgs.EXTRA_REMOTE_PARTY);
                    if (SgsStringUtils.isNullOrEmpty(remoteParty)) {
                        remoteParty = SgsStringUtils.nullValue();
                    }
                    remoteParty = "RadarsEvent";//SgsUriUtils.getUserName(remoteParty);
                    HistoryRadarsEvent event;
                    switch ((type = args.getEventType())) {
                        case RADARS_EVENT_1:
                            event = new HistoryRadarsEvent(remoteParty, SgsHistoryEvent.StatusType.RADARS_ALL);
                            /*event.setContent(new String("Start Tethering -- TotalUpload: " +
                                    Long.toString(args.getTotalUpload()) + ", TotalDownload: " + Long.toString(args.getTotalDownload())));
                            */
                            //Log.d(TAG, "Traffic Count getTotalUpload = " + args.getTotalUpload());
                            //Log.d(TAG, "Traffic Count getTotalDownload = " + args.getTotalDownload());
                            //event.setRadarsData(args.getRadarsData());
                            event.setStartTime(SgsDateTimeUtils.parseDate(dateString).getTime());
                            /*if (mEngine.getHistoryService().getEvents().size() != 0 && mEngine.getHistoryService().getEvents().get(0).compareTo(event) == 0) {
                                Log.d(TAG, "Found Redundant Traffic Count Event, will avoid this");
                                break;
                            }*/
                            //mEngine.getHistoryService().addEvent(event);
                            int results;
                            results = args.getRadarsNumber();
                            //results = intent.getParcelableArrayListExtra(RadarsEventArgs.EXTRA_DATA);
                            ((RadarsService) mEngine.getRadarsService()).getDefaultTask().updateRadars(results);
                            break;
                        default:
                            Log.d(TAG, "Invalid event args.getEventType().");
                            break;
                    }

                }
            }
        };
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RadarsEventArgs.ACTION_RADARS_EVENT);
        registerReceiver(mBroadcastReceiver, intentFilter);

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && bundle.getBoolean("autostarted")) {
                if (mEngine.start()) {
                    //mEngine.getRadarsService().register(null);
                }
            }
        }

        // alert()
        final Intent i = new Intent(ACTION_STATE_EVENT);
        i.putExtra("started", true);
        sendBroadcast(i);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
        if (mWakeLock != null) {
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
                mWakeLock = null;
            }
        }
        super.onDestroy();
    }
}
