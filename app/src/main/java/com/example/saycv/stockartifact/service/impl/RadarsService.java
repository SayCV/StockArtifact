package com.example.saycv.stockartifact.service.impl;

import android.content.Context;

import com.example.saycv.stockartifact.service.IRadarsService;

import org.saycv.sgs.services.ISgsBaseService;

import org.saycv.logger.Log;
import org.saycv.sgs.services.impl.SgsBaseService;

public class RadarsService extends SgsBaseService
        implements IRadarsService {
    private final static String TAG = RadarsService.class.getCanonicalName();

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
}
