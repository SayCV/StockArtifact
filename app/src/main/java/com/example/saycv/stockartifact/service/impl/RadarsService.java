package com.example.saycv.stockartifact.service.impl;

import android.content.Intent;

import com.example.saycv.stockartifact.SgsDroid;
import com.example.saycv.stockartifact.events.RadarsEventArgs;
import com.example.saycv.stockartifact.model.Radar;
import com.example.saycv.stockartifact.service.IRadarsService;
import com.example.saycv.stockartifact.service.fetcher.RadarUpdateTask;

import org.saycv.logger.Log;
import org.saycv.sgs.SgsApplication;
import org.saycv.sgs.services.ISgsHistoryService;
import org.saycv.sgs.services.impl.SgsBaseService;
import org.saycv.sgs.services.impl.SgsHistoryService;

import java.io.Serializable;
import java.util.ArrayList;

public class RadarsService extends SgsBaseService
        implements IRadarsService {
    private final static String TAG = RadarsService.class.getCanonicalName();

    private RadarsHistoryService mRadarsHistoryService;
    private RadarUpdateTask mRadarUpdateTask;
    //private RadarIndexUpdateTask mRadarUpdateTask;

    public RadarsService() {
        super();

        mRadarsHistoryService = new RadarsHistoryService();
    }

    @Override
    public boolean start() {
        Log.d(TAG, "starting...");
        mRadarsHistoryService.start();
        return true;
    }

    @Override
    public boolean stop() {
        Log.d(TAG, "stopping...");
        mRadarsHistoryService.stop();
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

    public ISgsHistoryService getHistoryService() {
        if (mRadarsHistoryService == null) {
            mRadarsHistoryService = new RadarsHistoryService();
        }
        return mRadarsHistoryService;
    }

    public RadarUpdateTask getRadarsDataTask() {
        return mRadarUpdateTask;
    }

    public void setDefaultTask(RadarUpdateTask task) {
        mRadarUpdateTask = task;
    }

    public RadarUpdateTask getRadarsIndexTask() {
        return mRadarUpdateTask;
    }

    public void setRadarsIndexTask(RadarUpdateTask task) {
        mRadarUpdateTask = task;
    }

    public void broadcastRadarsEvent(RadarsEventArgs args, String date) {
        final Intent intent = new Intent(RadarsEventArgs.ACTION_RADARS_EVENT);
        /*intent.putExtra(TrafficCountEventArgs.EXTRA_DATA_COUNT_TOTAL_UPLOAD, dataCount.totalUpload);
        intent.putExtra(TrafficCountEventArgs.EXTRA_DATA_COUNT_TOTAL_DOWNLOAD, dataCount.totalDownload);
        intent.putExtra(TrafficCountEventArgs.EXTRA_DATA_COUNT_UPLOAD_RATE, dataCount.uploadRate);
        intent.putExtra(TrafficCountEventArgs.EXTRA_DATA_COUNT_DOWNLOAD_RATE, dataCount.downloadRate);*/
        intent.putExtra(RadarsEventArgs.EXTRA_DATE, date);
        intent.putExtra(RadarsEventArgs.EXTRA_EMBEDDED, args);
        //intent.putParcelableArrayListExtra(RadarsEventArgs.EXTRA_DATA, (ArrayList) (args.getRadarsData()));
        SgsApplication.getContext().sendBroadcast(intent);
        Log.d(TAG, "broadcastRadarsEvent");
    }
}
