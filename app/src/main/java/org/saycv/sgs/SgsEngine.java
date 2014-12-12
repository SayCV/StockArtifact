/*
 * Copyright (C) 2014, sayCV.
 *
 * Copyright 2014 The sayCV's Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.saycv.sgs;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import org.saycv.logger.Log;
import org.saycv.sgs.services.ISgsConfigurationService;
import org.saycv.sgs.services.ISgsHistoryService;
import org.saycv.sgs.services.ISgsHttpClientService;
import org.saycv.sgs.services.ISgsNetworkService;
import org.saycv.sgs.services.ISgsSgsService;
import org.saycv.sgs.services.ISgsStorageService;
import org.saycv.sgs.services.impl.SgsConfigurationService;
import org.saycv.sgs.services.impl.SgsHistoryService;
import org.saycv.sgs.services.impl.SgsHttpClientService;
import org.saycv.sgs.services.impl.SgsNetworkService;
import org.saycv.sgs.services.impl.SgsSgsService;
import org.saycv.sgs.services.impl.SgsStorageService;

/**
 * Next Generation Network Engine.
 * This is the main entry point to have access to all services (SIP, XCAP, MSRP, History, ...).
 * Anywhere in the code you can get an instance of the engine by calling @ref getInstance() function.
 */
public class SgsEngine {
    private final static String TAG = SgsEngine.class.getCanonicalName();

    protected static SgsEngine sInstance;
    protected final NotificationManager mNotifManager;
    protected final Vibrator mVibrator;
    protected boolean mStarted;
    protected Activity mMainActivity;
    protected ISgsSgsService mSgsService;
    protected ISgsConfigurationService mConfigurationService;
    protected ISgsStorageService mStorageService;
    protected ISgsNetworkService mNetworkService;
    protected ISgsHttpClientService mHttpClientService;
    protected ISgsHistoryService mHistoryService;

    /**
     * Default constructor for the NGN engine. You should never call this function from your code. Instead you should
     * use @ref getInstance().
     *
     * @sa @ref getInstance()
     */
    protected SgsEngine() {
        final Context applicationContext = SgsApplication.getContext();
        final ISgsConfigurationService configurationService = getConfigurationService();
        if (applicationContext != null) {
            mNotifManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        } else {
            mNotifManager = null;
        }
        mVibrator = null;


    }

    public static void initialize() {

    }

    /**
     * Gets an instance of the NGN engine. You can call this function as many as you need and it will always return th
     * same instance.
     *
     * @return An instance of the NGN engine.
     */
    public static SgsEngine getInstance() {
        if (sInstance == null) {
            sInstance = new SgsEngine();
        }
        return sInstance;
    }

    /**
     * Starts the engine. This function will start all underlying services (SIP, XCAP, MSRP, History, ...).
     * You must call this function before trying to use any of the underlying services.
     *
     * @return true if all services have been successfully started and false otherwise
     */
    public synchronized boolean start() {
        if (mStarted) {
            return true;
        }

        boolean success = true;

        success &= getConfigurationService().start();
        success &= getStorageService().start();
        success &= getNetworkService().start();
        success &= getHttpClientService().start();
        success &= getHistoryService().start();

        if (success) {
            success &= getHistoryService().load();

            SgsApplication.getContext().startService(
                    new Intent(SgsApplication.getContext(), getNativeServiceClass()));
        } else {
            Log.e(TAG, "Failed to start services");
        }

        mStarted = true;

        Log.e(TAG, "start services");

        return success;
    }

    /**
     * Stops the engine. This function will stop all underlying services (SIP, XCAP, MSRP, History, ...).
     *
     * @return true if all services have been successfully stopped and false otherwise
     */
    public synchronized boolean stop() {
        if (!mStarted) {
            return true;
        }

        boolean success = true;

        success &= getConfigurationService().stop();
        success &= getHttpClientService().stop();
        success &= getHistoryService().stop();
        success &= getStorageService().stop();
        success &= getNetworkService().stop();

        if (!success) {
            Log.e(TAG, "Failed to stop services");
        }

        SgsApplication.getContext().stopService(
                new Intent(SgsApplication.getContext(), getNativeServiceClass()));

        // Cancel the persistent notifications.
        if (mNotifManager != null) {
            mNotifManager.cancelAll();
        }

        mStarted = false;
        return success;
    }

    /**
     * Checks whether the engine is started.
     *
     * @return true is the engine is running and false otherwise.
     * @sa @ref start() @ref stop()
     */
    public synchronized boolean isStarted() {
        return mStarted;
    }

    /**
     * Gets the main activity.
     *
     * @return the main activity
     * @sa @ref setMainActivity()
     */
    public Activity getMainActivity() {
        return mMainActivity;
    }

    /**
     * Sets the main activity to use as context in order to query some native resources.
     * It's up to you to call this function in order to retrieve the contacts for the ContactService.
     *
     * @param mainActivity The activity
     * @sa @ref getMainActivity()
     */
    public void setMainActivity(Activity mainActivity) {
        mMainActivity = mainActivity;
    }

    public ISgsSgsService getSgsService() {
        if (mSgsService == null) {
            mSgsService = new SgsSgsService();
        }
        return mSgsService;
    }

    /**
     * Gets the configuration service.
     *
     * @return the configuration service.
     */
    public ISgsConfigurationService getConfigurationService() {
        if (mConfigurationService == null) {
            mConfigurationService = new SgsConfigurationService();
        }
        return mConfigurationService;
    }

    /**
     * Gets the storage service.
     *
     * @return the storage service.
     */
    public ISgsStorageService getStorageService() {
        if (mStorageService == null) {
            mStorageService = new SgsStorageService();
        }
        return mStorageService;
    }

    /**
     * Gets the network service
     *
     * @return the network service
     */
    public ISgsNetworkService getNetworkService() {
        if (mNetworkService == null) {
            mNetworkService = new SgsNetworkService();
        }
        return mNetworkService;
    }

    /**
     * Gets the HTTP service
     *
     * @return the HTTP service
     */
    public ISgsHttpClientService getHttpClientService() {
        if (mHttpClientService == null) {
            mHttpClientService = new SgsHttpClientService();
        }
        return mHttpClientService;
    }

    /**
     * Gets the history service
     *
     * @return the history service
     */
    public ISgsHistoryService getHistoryService() {
        if (mHistoryService == null) {
            mHistoryService = new SgsHistoryService();
        }
        return mHistoryService;
    }

    /**
     * Gets the native service class
     *
     * @return the native service class
     */
    public Class<? extends SgsNativeService> getNativeServiceClass() {
        return SgsNativeService.class;
    }
}
