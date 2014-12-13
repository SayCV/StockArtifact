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
import com.example.saycv.stockartifact.model.Index;
import com.example.saycv.utils.PriceFormatter;

public class IndexAdapter extends ArrayAdapter<Index> {
    private static final String TAG = "IndexAdapter";
    private java.text.DateFormat formatter;

    public IndexAdapter(Context context) {
        super(context, 0);
        formatter = DateFormat.getTimeFormat(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v = view;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (View) vi.inflate(R.layout.compositeindex_item, null);
        }

        Log.d(TAG, "prepare view for indexes");
        // prepare views
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView code = (TextView) v.findViewById(R.id.code);
        TextView time = (TextView) v.findViewById(R.id.time);

        TextView lastDayClose = (TextView) v.findViewById(R.id.lastDayClose);
        //TextView lastDayHigh = (TextView) v.findViewById(R.id.lastDayHigh);
        //TextView lastDayLow = (TextView) v.findViewById(R.id.lastDayLow);
        TextView dayHigh = (TextView) v.findViewById(R.id.dayHigh);
        TextView dayLow = (TextView) v.findViewById(R.id.dayLow);
        //TextView dayOpen = (TextView) v.findViewById(R.id.dayOpen);
        TextView dayClose = (TextView) v.findViewById(R.id.dayClose);
        //TextView dayVolume = (TextView) v.findViewById(R.id.dayVolume);
        //TextView dayMoney = (TextView) v.findViewById(R.id.dayMoney);
        TextView dayRange = (TextView) v.findViewById(R.id.dayRange);
        TextView dayRangePercent = (TextView) v.findViewById(R.id.dayRangePercent);
        TextView daySwing = (TextView) v.findViewById(R.id.daySwing);

        // set data
        Index index = getItem(position);
        if (index != null) {
            //dayVolume.setText("");
            name.setText(index.getName());
            dayClose.setText(PriceFormatter.forPrice(index.getValue().doubleValue()));

            if (index.getUpdatedAt() != null) {
                time.setText(formatter.format(index.getUpdatedAt().getTime()));
            } else {
                time.setText("");
            }

            if (index.getChange() != null) {
                dayRange.setText(String.format("%s (%s)",
                        PriceFormatter.forPrice(index.getChange().doubleValue()),
                        PriceFormatter.forPercent(index.getChangePercent().doubleValue())));
            } else {
                dayRange.setText("---- (---)");
            }

            if (index.getChange() != null && index.getChange().floatValue() > 0) {
                dayClose.setTextColor(Color.rgb(0, 213, 65));
                dayRange.setTextColor(Color.rgb(0, 213, 65));
            } else if (index.getChange() != null && index.getChange().floatValue() < 0) {
                dayClose.setTextColor(Color.rgb(238, 30, 0));
                dayRange.setTextColor(Color.rgb(238, 30, 0));
            } else {
                dayClose.setTextColor(Color.WHITE);
                dayRange.setTextColor(Color.WHITE);
            }

        } else {
            time.setText("");
            //dayVolume.setText("---");
            name.setText("");
            dayClose.setText("----");
            dayRange.setText("---- (---)");
        }
        return v;
    }
}
