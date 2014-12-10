package com.example.saycv.stockartifact.view;

import com.example.saycv.stockartifact.R;
import com.example.saycv.stockartifact.model.Radar;
import com.example.saycv.utils.PriceFormatter;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RadarAdapter extends ArrayAdapter<Radar> {
    private static final String TAG = "RadarAdapter";
    private java.text.DateFormat formatter;

    public RadarAdapter(Context context) {
        super(context, 0);
        formatter = DateFormat.getTimeFormat(context);        
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v = view;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (View) vi.inflate(R.layout.index_item, null);
        }

        Log.d(TAG, "prepare view for Radar");
        // prepare views
        TextView name = (TextView) v.findViewById(R.id.name);       
        TextView price = (TextView) v.findViewById(R.id.price);
        TextView change = (TextView) v.findViewById(R.id.change);
        TextView volume = (TextView) v.findViewById(R.id.volume);
        TextView time = (TextView) v.findViewById(R.id.time);
        
        // set data

        return v;
    }
}
