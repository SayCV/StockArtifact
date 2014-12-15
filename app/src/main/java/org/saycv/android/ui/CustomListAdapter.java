package org.saycv.android.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.saycv.stockartifact.R;

public class CustomListAdapter extends ArrayAdapter<String>
{
    private final Activity context;

    public CustomListAdapter(Activity context, String [] names)
    {
        super(context, R.layout.customtitlebar_rowlayout, names);
        this.context = context;
    }

    static class ViewHolder
    {
        protected TextView label;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = null;

        if (convertView == null) {

            LayoutInflater inflator = context.getLayoutInflater();

            view = inflator.inflate(R.layout.customtitlebar_rowlayout, null);

            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.label = (TextView) view.findViewById(R.id.label);

            view.setTag(viewHolder);

        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        holder.label.setText(getItem(position));

        return view;
    }
}