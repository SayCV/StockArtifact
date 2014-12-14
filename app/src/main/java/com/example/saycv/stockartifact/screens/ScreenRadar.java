/*
 * Copyright (C) 2014, sayCV.
 *
 * Copyright 2014 The sayCV's Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.saycv.stockartifact.screens;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.saycv.android.activities.SgsFragmentActivity;
import com.example.saycv.stockartifact.Engine;
import com.example.saycv.stockartifact.NativeService;
import com.example.saycv.stockartifact.R;
import com.example.saycv.stockartifact.events.RadarsEventArgs;
import com.example.saycv.stockartifact.events.RadarsEventTypes;
import com.example.saycv.stockartifact.model.RadarsHistoryEvent;
import com.example.saycv.stockartifact.service.IRadarsService;
import com.example.saycv.stockartifact.service.fetcher.IndexesUpdateTask;
import com.example.saycv.stockartifact.service.fetcher.RadarUpdateTask;
import com.example.saycv.stockartifact.service.impl.RadarsService;
import com.example.saycv.stockartifact.view.IndexAdapter;
import com.example.saycv.stockartifact.view.RadarAdapter;

import org.saycv.sgs.events.SgsEventArgs;
import org.saycv.sgs.model.SgsHistoryEvent;
import org.saycv.sgs.utils.SgsDateTimeUtils;
import org.saycv.sgs.utils.SgsStringUtils;


public class ScreenRadar extends SgsFragmentActivity {
    public static final String TAG = "MainActivity";

    public static final int ACTION_NONE = 0;

    public static IndexAdapter indexAdapter;
    public static RadarAdapter radarAdapter;
    private final Engine mEngine;
    private final IRadarsService mRadarsService;
    private Handler mHandler;

    private BroadcastReceiver mBroadCastRecv;

    public ScreenRadar() {
        super();

        // Sets main activity (should be done before starting services)
        mEngine = (Engine)Engine.getInstance();
        mEngine.setMainActivity(this);
        mRadarsService = ((Engine)Engine.getInstance()).getRadarsService();
    }

    public IndexAdapter getIndexAdapter() {
        return indexAdapter;
    }
    public RadarAdapter getRadarAdapter() {
        return radarAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_screens_radar);
        mHandler = new Handler();

        Bundle bundle = savedInstanceState;
        if(bundle == null){
            Intent intent = getIntent();
            bundle = intent == null ? null : intent.getExtras();
        }
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        mBroadCastRecv = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                Log.d(TAG, "BroadcastReceiver: " + action.toString());

                if(NativeService.ACTION_STATE_EVENT.equals(action)){
                    if(intent.getBooleanExtra("started", false)){
                        /*if(getEngine().getConfigurationService().getBoolean(SgsConfigurationEntry.NETWORK_CONNECTED,
                                SgsConfigurationEntry.DEFAULT_NETWORK_CONNECTED)) {
                            ((TetheringService)getEngine().getTetheringService()).setRegistrationState(TetheringSession.ConnectionState.CONNECTED);
                            SgsApplication.acquireWakeLock();
                        }*/
                        //mScreenService.show(ScreenHome.class);
                        //getEngine().getConfigurationService().putBoolean(SgsConfigurationEntry.GENERAL_AUTOSTART, true);
                        //finish();
                    } else if (RadarsEventArgs.ACTION_RADARS_EVENT.equals(action)) {
                        org.saycv.logger.Log.d(TAG, "RadarsEventArgs.ACTION_RADARS_EVENT.");
                        RadarsEventArgs args = intent.getParcelableExtra(SgsEventArgs.EXTRA_EMBEDDED);
                        final RadarsEventTypes type;
                        if (args == null) {
                            org.saycv.logger.Log.e(TAG, "Invalid event args");
                            return;
                        }
                        String dateString = intent.getStringExtra(RadarsEventArgs.EXTRA_DATE);
                        String remoteParty = intent.getStringExtra(RadarsEventArgs.EXTRA_REMOTE_PARTY);
                        if (SgsStringUtils.isNullOrEmpty(remoteParty)) {
                            remoteParty = SgsStringUtils.nullValue();
                        }
                        remoteParty = "RadarsEvent";//SgsUriUtils.getUserName(remoteParty);
                        RadarsHistoryEvent event;
                        switch ((type = args.getEventType())) {
                            case RADARS_EVENT_1:
                                event = new RadarsHistoryEvent();
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
                                //((RadarsService) mEngine.getRadarsService()).getDefaultTask().updateRadars(args.getRadarsData());
                                break;
                            default:
                                org.saycv.logger.Log.d(TAG, "Invalid event args.getEventType().");
                                break;
                        }

                    }
                }
            }
        };
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NativeService.ACTION_STATE_EVENT);
        registerReceiver(mBroadCastRecv, intentFilter);
    }

    @Override
    protected void onDestroy() {
        if(mBroadCastRecv != null){
            unregisterReceiver(mBroadCastRecv);
            //SgsApplication.releaseWakeLock();
        }
        ((RadarsService)(mEngine.getRadarsService())).getRadarsDataTask().setRadarsUpdateThreadClassEnabled(false);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Engine engine = mEngine;

        final Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                if(!engine.isStarted()){
                    Log.d(TAG, "Starts the engine from the splash screen");
                    engine.start();
//                    if(getEngine().getConfigurationService().getBoolean(SgsConfigurationEntry.NETWORK_CONNECTED,
//                            SgsConfigurationEntry.DEFAULT_NETWORK_CONNECTED)) {
//                        //((TetheringService)getEngine().getTetheringService()).setRegistrationState(TetheringSession.ConnectionState.CONNECTED);
//                        SgsApplication.acquireWakeLock();
//                        ((TetheringService)getEngine().getTetheringService()).reRegister(null);
//                    }
                    engine.getRadarsService().start();
                    //((RadarsService)(engine.getRadarsService())).getRadarsDataTask().setRadarsUpdateThreadClassEnabled(true);
                }
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_screens_radar, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            /*RelativeLayout szCompositeIndexLayoutView = (RelativeLayout) view.findViewById(R.id.include_compositeindex_item_sz);
            TextView tv = (TextView)szCompositeIndexLayoutView.findViewById(R.id.name);
            tv.setText(R.string.compositeindex_sz_name);*/

            ListView lvIndex = (ListView)view.findViewById(R.id.listViewIndex);
            indexAdapter = new IndexAdapter(view.getContext());
            lvIndex.setAdapter(indexAdapter);

            ListView lvRadars = (ListView)view.findViewById(R.id.listViewRadars);
            radarAdapter = new RadarAdapter(view.getContext());
            lvRadars.setAdapter(radarAdapter);

            Log.i(TAG, "start radar activity");
            IndexesUpdateTask indexesUpdateTask = new IndexesUpdateTask((Activity)view.getContext());
            indexesUpdateTask.execute();

            RadarUpdateTask radarUpdateTask = new RadarUpdateTask((Activity)view.getContext());
            radarUpdateTask.execute();
            ((RadarsService) ((Engine)Engine.getInstance()).getRadarsService()).setDefaultTask(radarUpdateTask);
            radarUpdateTask.setRadarsUpdateThreadClassEnabled(true);
        }
    }
}
