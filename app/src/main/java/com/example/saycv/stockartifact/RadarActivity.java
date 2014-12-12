package com.example.saycv.stockartifact;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.saycv.stockartifact.service.fetcher.RadarUpdateTask;
import com.example.saycv.stockartifact.view.RadarAdapter;

public class RadarActivity extends BaseStockActivity {
    public static final String TAG = "RadarActivity";
    private RadarAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.listview);

        adapter = new RadarAdapter(this);
        setListAdapter(adapter);

        TextView empty = (TextView) findViewById(android.R.id.empty);
        empty.setText(R.string.msg_loading);

        Log.i(TAG, "start Radar activity");
        RadarUpdateTask task = new RadarUpdateTask(this);
        task.execute();
    }

    public RadarAdapter getRadarAdapter() {
        return adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.index_menu, menu);
        menu.getItem(0).setIcon(R.drawable.ic_menu_rotate);
        menu.getItem(1).setIcon(R.drawable.ic_menu_help);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                RadarUpdateTask task = new RadarUpdateTask(this);
                task.execute();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}
