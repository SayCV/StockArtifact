package com.example.saycv.stockartifact;

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
import com.example.saycv.stockartifact.events.RadarsEventArgs;
import com.example.saycv.stockartifact.events.RadarsEventTypes;
import com.example.saycv.stockartifact.model.RadarsHistoryEvent;
import com.example.saycv.stockartifact.screens.ScreenAbout;
import com.example.saycv.stockartifact.screens.ScreenRadar;
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


public class MainActivity extends SgsFragmentActivity {
    private final static String TAG = MainActivity.class.getCanonicalName();

    public static final int ACTION_NONE = 0;

    public static IndexAdapter indexAdapter;
    public static RadarAdapter radarAdapter;
    private final Engine mEngine;
    private final IRadarsService mRadarsService;
    private Handler mHandler;

    private BroadcastReceiver mBroadCastRecv;

    public MainActivity() {
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

        setContentView(R.layout.activity_main);
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

                if (NativeService.ACTION_STATE_EVENT.equals(action)) {
                    if (intent.getBooleanExtra("started", false)) {
                        /*if(getEngine().getConfigurationService().getBoolean(SgsConfigurationEntry.NETWORK_CONNECTED,
                                SgsConfigurationEntry.DEFAULT_NETWORK_CONNECTED)) {
                            ((TetheringService)getEngine().getTetheringService()).setRegistrationState(TetheringSession.ConnectionState.CONNECTED);
                            SgsApplication.acquireWakeLock();
                        }*/
                        //mScreenService.show(ScreenHome.class);
                        //getEngine().getConfigurationService().putBoolean(SgsConfigurationEntry.GENERAL_AUTOSTART, true);
                        //finish();
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

        if (id == R.id.action_about) {
            ScreenAbout screenAbout = new ScreenAbout();
            Intent intent = new Intent(this, ScreenAbout.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           //intent.putExtra(ScreenAbout.class.getName(), ScreenAbout.class.getName());
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_radar) {
            ScreenRadar screenRadar = new ScreenRadar();
            Intent intent = new Intent(this, ScreenRadar.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ScreenRadar.class.getName(), ScreenRadar.class.getName());
            startActivity(intent);
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
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


        }
    }
}
