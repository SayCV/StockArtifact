package com.example.saycv.stockartifact;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.os.Handler;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.saycv.stockartifact.service.fetcher.RadarUpdateTask;
import com.example.saycv.stockartifact.view.RadarAdapter;


public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";
    public static RadarAdapter adapter;

		private Handler mHandler;
    private final Engine mEngine;
    private final IRadarsService mRadarsService;

    public MainActivity(){
        super();

        // Sets main activity (should be done before starting services)
        mEngine = (Engine)Engine.getInstance();
        mEngine.setMainActivity(this);
        mRadarsService = ((Engine)Engine.getInstance()).getRadarsService();
    }
    
    public RadarAdapter getRadarAdapter() {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            RelativeLayout szCompositeIndexLayoutView = (RelativeLayout) view.findViewById(R.id.include_compositeindex_item_sz);
            TextView tv = (TextView)szCompositeIndexLayoutView.findViewById(R.id.name);
            tv.setText(R.string.compositeindex_sz_name);

            ListView lv = (ListView)view.findViewById(R.id.listView);
            adapter = new RadarAdapter(view.getContext());
            lv.setAdapter(adapter);

            Log.i(TAG, "start radar activity");
            RadarUpdateTask task = new RadarUpdateTask((Activity)view.getContext());
            task.execute();

        }
    }
}
