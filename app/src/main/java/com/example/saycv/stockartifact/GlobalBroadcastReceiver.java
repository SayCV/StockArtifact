package com.example.saycv.stockartifact;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.saycv.sgs.utils.SgsConfigurationEntry;

public class GlobalBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            SharedPreferences settings = context.getSharedPreferences(SgsConfigurationEntry.SHARED_PREF_NAME, 0);
            if (settings != null && settings.getBoolean(SgsConfigurationEntry.GENERAL_AUTOSTART.toString(), SgsConfigurationEntry.DEFAULT_GENERAL_AUTOSTART)) {
                Intent i = new Intent(context, NativeService.class);
                i.putExtra("autostarted", true);
                context.startService(i);
            }
        }

    }
}