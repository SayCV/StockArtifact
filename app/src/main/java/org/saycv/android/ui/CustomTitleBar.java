package org.saycv.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.example.saycv.stockartifact.R;

public class CustomTitleBar extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_main);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.customtitlebar_title);

        ((TextView) findViewById(R.id.title)).setText("Favorite Fruit");

        String[] names = new String[] { "Apples", "Oranges", "Grapes", "Strawberries", "Mulberries", "Peaches", "Blueberries", "Dates", "Rasberries", "Bananas" };

        ListView listView = (ListView) findViewById(R.id.content);

        listView.setAdapter(new CustomListAdapter(this, names));
    }
}