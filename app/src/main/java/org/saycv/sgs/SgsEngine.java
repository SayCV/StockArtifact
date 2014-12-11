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

import org.saydroid.sgs.media.SgsProxyPluginMgr;
import org.saydroid.sgs.services.ISgsConfigurationService;
import org.saydroid.sgs.services.ISgsContactService;
import org.saydroid.sgs.services.ISgsHistoryService;
import org.saydroid.sgs.services.ISgsHttpClientService;
import org.saydroid.sgs.services.ISgsNetworkService;
import org.saydroid.sgs.services.ISgsSipService;
import org.saydroid.sgs.services.ISgsSoundService;
import org.saydroid.sgs.services.ISgsStorageService;
import org.saydroid.sgs.services.impl.SgsConfigurationService;
import org.saydroid.sgs.services.impl.SgsContactService;
import org.saydroid.sgs.services.impl.SgsHistoryService;
import org.saydroid.sgs.services.impl.SgsHttpClientService;
import org.saydroid.sgs.services.impl.SgsNetworkService;
import org.saydroid.sgs.services.impl.SgsSipService;
import org.saydroid.sgs.services.impl.SgsSoundService;
import org.saydroid.sgs.services.impl.SgsStorageService;
import org.saydroid.sgs.utils.SgsConfigurationEntry;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import org.saydroid.logger.Log;

/**
 * Next Generation Network Engine.
 * This is the main entry point to have access to all services (SIP, XCAP, MSRP, History, ...).
 * Anywhere in the code you can get an instance of the engine by calling @ref getInstance() function.
 */
public class SgsEngine {
	private final static String TAG = SgsEngine.class.getCanonicalName();
	
	protected static SgsEngine sInstance;
	
	protected boolean mStarted;
	protected Activity mMainActivity;
	
	protected final NotificationManager mNotifManager;
	protected final Vibrator mVibrator;
	
	protected ISgsConfigurationService mConfigurationService;
	protected ISgsStorageService mStorageService;
	protected ISgsNetworkService mNetworkService;
	protected ISgsHttpClientService mHttpClientService;
	protected ISgsContactService mContactService;
	protected ISgsHistoryService mHistoryService;
	protected ISgsSipService mSipService;
	protected ISgsSoundService mSoundService;
	
	public static void initialize(){

	}
	
	/**
	 * Gets an instance of the NGN engine. You can call this function as many as you need and it will always return th
	 * same instance.
	 * @return An instance of the NGN engine.
	 */
	public static SgsEngine getInstance(){
		if(sInstance == null){
			sInstance = new SgsEngine();
		}
		return sInstance;
	}
	
	/**
	 * Default constructor for the NGN engine. You should never call this function from your code. Instead you should
	 * use @ref getInstance().
	 * @sa @ref getInstance()
	 */
	protected SgsEngine(){
		final Context applicationContext = SgsApplication.getContext();
		final ISgsConfigurationService configurationService = getConfigurationService();
		if(applicationContext != null){
			mNotifManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		else{ 
			mNotifManager = null;
		}
		mVibrator = null;
		

	}
	
	/**
	 * Starts the engine. This function will start all underlying services (SIP, XCAP, MSRP, History, ...).
	 * You must call this function before trying to use any of the underlying services.
	 * @return true if all services have been successfully started and false otherwise
	 */
	public synchronized boolean start() {
		if(mStarted){
			return true;
		}
		
		boolean success = true;
		
		success &= getConfigurationService().start();
		success &= getStorageService().start();
		success &= getNetworkService().start();
		success &= getHttpClientService().start();
		success &= getHistoryService().start();
        //success &= getContactService().start();
        //success &= getSipService().start();
        //success &= getSoundService().start();
		
		if(success) {
			success &= getHistoryService().load();
			/* success &=*/ //getContactService().load();
			
			SgsApplication.getContext().startService(
					new Intent(SgsApplication.getContext(), getNativeServiceClass()));
		} else {
			Log.e(TAG, "Failed to start services");
		}
		
		mStarted = true;
		return success;
	}
	
	/**
	 * Stops the engine. This function will stop all underlying services (SIP, XCAP, MSRP, History, ...).
	 * @return true if all services have been successfully stopped and false otherwise
	 */
	public synchronized boolean stop() {
		if(!mStarted){
			return true;
		}
		
		boolean success = true;

        success &= getConfigurationService().stop();
        success &= getHttpClientService().stop();
        success &= getHistoryService().stop();
        success &= getStorageService().stop();
        //success &= getContactService().stop();
        //success &= getSipService().stop();
        //success &= getSoundService().stop();
        success &= getNetworkService().stop();
		
		if(!success){
			Log.e(TAG, "Failed to stop services");
		}
		
		SgsApplication.getContext().stopService(
				new Intent(SgsApplication.getContext(), getNativeServiceClass()));
		
		// Cancel the persistent notifications.
		if(mNotifManager != null){
			mNotifManager.cancelAll();
		}
		
		mStarted = false;
		return success;
	}
	
	/**
	 * Checks whether the engine is started.
	 * @return true is the engine is running and false otherwise.
	 * @sa @ref start() @ref stop()
	 */
	public synchronized boolean isStarted(){
		return mStarted;
	}
	
	/**
	 * Sets the main activity to use as context in order to query some native resources.
	 * It's up to you to call this function in order to retrieve the contacts for the ContactService.
	 * @param mainActivity The activity
	 * @sa @ref getMainActivity()
	 */
	public void setMainActivity(Activity mainActivity){
		mMainActivity = mainActivity;
	}
	
	/**
	 * Gets the main activity.
	 * @return the main activity
	 * @sa @ref setMainActivity()
	 */
	public Activity getMainActivity(){
		return mMainActivity;
	}
	
	/**
	 * Gets the configuration service.
	 * @return the configuration service.
	 */
	public ISgsConfigurationService getConfigurationService(){
		if(mConfigurationService == null){
			mConfigurationService = new SgsConfigurationService();
		}
		return mConfigurationService;
	}
	
	/**
	 * Gets the storage service.
	 * @return the storage service.
	 */
	public ISgsStorageService getStorageService(){
		if(mStorageService == null){
			mStorageService = new SgsStorageService();
		}
		return mStorageService;
	}
	
	/**
	 * Gets the network service
	 * @return the network service
	 */
	public ISgsNetworkService getNetworkService(){
		if(mNetworkService == null){
			mNetworkService = new SgsNetworkService();
		}
		return mNetworkService;
	}
	
	/**
	 * Gets the HTTP service
	 * @return the HTTP service
	 */
	public ISgsHttpClientService getHttpClientService(){
		if(mHttpClientService == null){
			mHttpClientService = new SgsHttpClientService();
		}
		return mHttpClientService;
	}
	
	/**
	 * Gets the contact service
	 * @return the contact service
	 */
	public ISgsContactService getContactService(){
		if(mContactService == null){
			mContactService = new SgsContactService();
		}
		return mContactService;
	}
	
	/**
	 * Gets the history service
	 * @return the history service
	 */
	public ISgsHistoryService getHistoryService(){
		if(mHistoryService == null){
			mHistoryService = new SgsHistoryService();
		}
		return mHistoryService;
	}
	
	/**
	 * Gets the SIP service
	 * @return the sip service
	 */
	public ISgsSipService getSipService(){
		if(mSipService == null){
			mSipService = new SgsSipService();
		}
		return mSipService;
	}
	
	/**
	 * Gets the sound service
	 * @return the sound service
	 */
	public ISgsSoundService getSoundService(){
		if(mSoundService == null){
			mSoundService = new SgsSoundService();
		}
		return mSoundService;
	}
	
	/**
	 * Gets the native service class
	 * @return the native service class
	 */
	public Class<? extends SgsNativeService> getNativeServiceClass(){
		return SgsNativeService.class;
	}
}
