package com.example.saycv.stockartifact.view;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.saycv.stockartifact.R;
import com.example.saycv.stockartifact.model.Radar;

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
            v = (View) vi.inflate(R.layout.radar_item, null);
            if (v.getLayoutParams() != null) {
                v.getLayoutParams().height = 22;
            }
        }

        Log.d(TAG, "prepare view for Radar");
        // prepare views
        TextView time = (TextView) v.findViewById(R.id.time);
        TextView code = (TextView) v.findViewById(R.id.code);
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView type = (TextView) v.findViewById(R.id.type);
        TextView price = (TextView) v.findViewById(R.id.price);
        TextView volume = (TextView) v.findViewById(R.id.volume);
        TextView numbers = (TextView) v.findViewById(R.id.numbers);
        // set data
        Radar index = getItem(position);
        if (index != null) {
            time.setText(index.getTime());
            code.setText(index.getCode());
            name.setText(index.getName());

            type.setText(index.getType());
            price.setText(index.getPrice());
            volume.setText(index.getVolume());
            numbers.setText(index.getNumbers());
            numbers.setVisibility(View.VISIBLE);

            int rgb;
            if (index.getType().equals("大买单") || index.getType().equals("封涨停板") || index.getType().equals("快速上涨")) {
                rgb = (Color.rgb(238, 30, 0));
                time.setTextColor(rgb);
                code.setTextColor(rgb);
                name.setTextColor(rgb);
                type.setTextColor(rgb);
                price.setTextColor(rgb);
                volume.setTextColor(rgb);
            } else {
                rgb = Color.rgb(0, 213, 65);
                time.setTextColor(rgb);
                code.setTextColor(rgb);
                name.setTextColor(rgb);
                type.setTextColor(rgb);
                price.setTextColor(rgb);
                volume.setTextColor(rgb);
            }

        } else {
            time.setText("");
            code.setText("");
            name.setText("");

            type.setText("");
            price.setText("----");
            volume.setText("---");
        }
        return v;
    }
}
