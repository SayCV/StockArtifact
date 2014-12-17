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
import com.example.saycv.utils.Constants;

import java.math.BigDecimal;

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

        //Log.d(TAG, "prepare view for indexes");
        // prepare views
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView code = (TextView) v.findViewById(R.id.code);
        TextView time = (TextView) v.findViewById(R.id.time);
        TextView dayCurrent = (TextView) v.findViewById(R.id.dayCurrent);
        TextView lastDayClose = (TextView) v.findViewById(R.id.lastDayClose);
        //TextView lastDayHigh = (TextView) v.findViewById(R.id.lastDayHigh);
        //TextView lastDayLow = (TextView) v.findViewById(R.id.lastDayLow);
        TextView dayHigh = (TextView) v.findViewById(R.id.dayHigh);
        TextView dayLow = (TextView) v.findViewById(R.id.dayLow);
        //TextView dayOpen = (TextView) v.findViewById(R.id.dayOpen);
        TextView dayClose = (TextView) v.findViewById(R.id.dayClose);
        TextView dayVolume = (TextView) v.findViewById(R.id.dayVolume);
        //TextView dayMoney = (TextView) v.findViewById(R.id.dayMoney);
        TextView dayRange = (TextView) v.findViewById(R.id.dayRange);
        TextView dayRangePercent = (TextView) v.findViewById(R.id.dayRangePercent);
        TextView daySwing = (TextView) v.findViewById(R.id.daySwing);

        // set data
        Index index = getItem(position);
        if (index != null) {

            name.setText(index.getName());
            code.setText(index.getCode());
            dayCurrent.setText(index.getDayClose());
            BigDecimal vol = new BigDecimal(index.getDayVolume());
            dayVolume.setText(vol.divide(new BigDecimal(Constants.OneHundredMillion), 2).toString() + "yi");


           /*if (index.getTime() != null) {
                time.setText(index.getTime());
            } else {
                time.setText("");
            }*/

            if (index.getLastDayClose() != null) {
                lastDayClose.setText(index.getLastDayClose());
            } else {
                lastDayClose.setText("");
            }

            if (index.getDayClose() != null) {
                dayClose.setText(index.getDayClose());
            } else {
                dayClose.setText("");
            }

            if (index.getDayHigh() != null) {
                dayHigh.setText(index.getDayHigh());
            } else {
                dayHigh.setText("");
            }

            if (index.getDayLow() != null) {
                dayLow.setText(index.getDayLow());
            } else {
                dayLow.setText("");
            }

            if (index.getDayRange() != null) {
                dayRange.setText(index.getDayRange());
            } else {
                dayRange.setText("----");
            }

            if (index.getDayRangePercent() != null) {
                dayRangePercent.setText(String.format("%s%%", index.getDayRangePercent()));
            } else {
                dayRangePercent.setText("----");
            }

            if (index.getDaySwing() != null) {
                daySwing.setText(String.format("%s%%", index.getDaySwing()));
            } else {
                daySwing.setText("");
            }

            int rgb;
            if (index.getDayRange() != null && Float.parseFloat(index.getDayRange()) < 0) {
                rgb = Color.rgb(0, 213, 65);
                dayCurrent.setTextColor(rgb);
                dayRange.setTextColor(rgb);

                dayClose.setTextColor(rgb);
                dayRangePercent.setTextColor(rgb);
                daySwing.setTextColor(rgb);
            } else if (index.getDayRange() != null && Float.parseFloat(index.getDayRange()) > 0) {
                rgb = (Color.rgb(238, 30, 0));
                dayCurrent.setTextColor(rgb);
                dayRange.setTextColor(rgb);

                dayClose.setTextColor(rgb);
                dayRangePercent.setTextColor(rgb);
                daySwing.setTextColor(rgb);
            } else {
                dayCurrent.setTextColor(Color.WHITE);
                dayRange.setTextColor(Color.WHITE);
            }

        } else {
            name.setText("");
            code.setText("");
            dayCurrent.setText("");

            dayClose.setText("----");
            dayRange.setText("----");
        }
        return v;
    }
}
