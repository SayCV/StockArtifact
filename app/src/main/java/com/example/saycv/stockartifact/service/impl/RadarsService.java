package com.example.saycv.stockartifact.service.impl;

import android.content.Context;
import android.content.Intent;

import com.example.saycv.stockartifact.events.RadarsEventArgs;
import com.example.saycv.stockartifact.service.IRadarsService;
import com.example.saycv.stockartifact.service.fetcher.RadarUpdateTask;

import org.saycv.sgs.SgsApplication;
import org.saycv.sgs.services.ISgsBaseService;

import org.saycv.logger.Log;
import org.saycv.sgs.services.impl.SgsBaseService;

public class RadarsService extends SgsBaseService
        implements IRadarsService {
    private final static String TAG = RadarsService.class.getCanonicalName();

    private RadarUpdateTask mRadarUpdateTask;

    public RadarsService() {
        super();

    }

    @Override
    public boolean start() {
        Log.d(TAG, "starting...");
        return true;
    }

    @Override
    public boolean stop() {
        Log.d(TAG, "stopping...");
        return true;
    }

    @Override
    public String getDefaultIdentity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDefaultIdentity(String identity) {
        // TODO Auto-generated method stub

    }

    public void setDefaultTask(RadarUpdateTask task) {  mRadarUpdateTask = task; }
    public RadarUpdateTask getDefaultTask() { return mRadarUpdateTask; }

    public void broadcastRadarsEvent(RadarsEventArgs args, String date){
        final Intent intent = new Intent(RadarsEventArgs.ACTION_RADARS_EVENT);
        /*intent.putExtra(TrafficCountEventArgs.EXTRA_DATA_COUNT_TOTAL_UPLOAD, dataCount.totalUpload);
        intent.putExtra(TrafficCountEventArgs.EXTRA_DATA_COUNT_TOTAL_DOWNLOAD, dataCount.totalDownload);
        intent.putExtra(TrafficCountEventArgs.EXTRA_DATA_COUNT_UPLOAD_RATE, dataCount.uploadRate);
        intent.putExtra(TrafficCountEventArgs.EXTRA_DATA_COUNT_DOWNLOAD_RATE, dataCount.downloadRate);*/
        intent.putExtra(RadarsEventArgs.EXTRA_DATE, date);
        intent.putExtra(RadarsEventArgs.EXTRA_EMBEDDED, args);
        SgsApplication.getContext().sendBroadcast(intent);
    }
}
