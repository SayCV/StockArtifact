/*
 * Copyright (C) 2013, sayDroid.
 *
 * Copyright 2013 The sayDroid Project
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

package org.saydroid.tether.usb;


import org.saydroid.sgs.SgsApplication;
import org.saydroid.sgs.SgsEngine;
import org.saydroid.sgs.SgsNativeService;
import org.saydroid.sgs.events.SgsEventArgs;
import org.saydroid.sgs.events.SgsInviteEventArgs;
import org.saydroid.sgs.events.SgsMessagingEventArgs;
import org.saydroid.sgs.events.SgsMsrpEventArgs;
import org.saydroid.sgs.events.SgsRegistrationEventArgs;
import org.saydroid.sgs.events.SgsRegistrationEventTypes;
import org.saydroid.sgs.media.SgsMediaType;
import org.saydroid.sgs.model.SgsHistorySMSEvent;
import org.saydroid.sgs.model.SgsHistoryEvent.StatusType;
import org.saydroid.sgs.sip.SgsAVSession;
import org.saydroid.sgs.sip.SgsMsrpSession;
import org.saydroid.sgs.utils.SgsConfigurationEntry;
import org.saydroid.sgs.utils.SgsDateTimeUtils;
import org.saydroid.sgs.utils.SgsStringUtils;
import org.saydroid.sgs.utils.SgsUriUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

import org.saydroid.logger.Log;
import org.saydroid.tether.usb.Events.TrafficCountEventArgs;
import org.saydroid.tether.usb.Events.TrafficCountEventTypes;
import org.saydroid.tether.usb.Model.HistoryTrafficCountEvent;
import org.saydroid.tether.usb.Services.Impl.TetheringService;
import org.saydroid.tether.usb.Tethering.TetheringSession;

public class NativeService extends SgsNativeService {
	private final static String TAG = NativeService.class.getCanonicalName();
	public static final String ACTION_STATE_EVENT = TAG + ".ACTION_STATE_EVENT";
	
	private PowerManager.WakeLock mWakeLock;
	private BroadcastReceiver mBroadcastReceiver;
	private Engine mEngine;
	
	public NativeService(){
		super();
		mEngine = (Engine)Engine.getInstance();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate()");
		
		final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		if(powerManager != null && mWakeLock == null){
			mWakeLock = powerManager.newWakeLock(PowerManager.ON_AFTER_RELEASE 
					| PowerManager.SCREEN_BRIGHT_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
		}
        /*final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if(mEngine != null){
            mEngine.getInstance().getMainActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }*/
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d(TAG, "onStart()");
		
		// register()
		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();
				
				// Registration Events
				if(SgsRegistrationEventArgs.ACTION_REGISTRATION_EVENT.equals(action)){
                    SgsRegistrationEventArgs args = intent.getParcelableExtra(SgsEventArgs.EXTRA_EMBEDDED);
                    final SgsRegistrationEventTypes type;
                    if(args == null){
                        Log.e(TAG, "Invalid event args");
                        return;
                    }
                    switch((type = args.getEventType())){
                        case REGISTRATION_OK:
                        case REGISTRATION_NOK:
                        case REGISTRATION_INPROGRESS:
                        case UNREGISTRATION_INPROGRESS:
                        case UNREGISTRATION_OK:
                        case UNREGISTRATION_NOK:
                        default:
                            final boolean bTrying = (type == SgsRegistrationEventTypes.REGISTRATION_INPROGRESS || type == SgsRegistrationEventTypes.UNREGISTRATION_INPROGRESS);
                            if(mEngine.getTetheringService().isRegistered()){
                                mEngine.showAppNotif(bTrying ?R.drawable.bullet_ball_glass_grey_16 : R.drawable.bullet_ball_glass_green_16, null);
                                SRTDroid.acquirePowerLock();
                            }
                            else{
                                mEngine.showAppNotif(bTrying ?R.drawable.bullet_ball_glass_grey_16 : R.drawable.bullet_ball_glass_red_16, null);
                                SRTDroid.releasePowerLock();
                            }
                            break;
                    }
                }
                if(TrafficCountEventArgs.ACTION_TRAFFIC_COUNT_EVENT.equals(action)){
                    TrafficCountEventArgs args = intent.getParcelableExtra(SgsEventArgs.EXTRA_EMBEDDED);
                    final TrafficCountEventTypes type;
                    if(args == null){
                        Log.e(TAG, "Invalid event args");
                        return;
                    }
                    String dateString = intent.getStringExtra(TrafficCountEventArgs.EXTRA_DATE);
                    String remoteParty = intent.getStringExtra(TrafficCountEventArgs.EXTRA_REMOTE_PARTY);
                    if(SgsStringUtils.isNullOrEmpty(remoteParty)){
                        remoteParty = SgsStringUtils.nullValue();
                    }
                    remoteParty = "HistoryTrafficCountEvent";//SgsUriUtils.getUserName(remoteParty);
                    HistoryTrafficCountEvent event;
                    switch((type = args.getEventType())){
                        case COUNTING:
                            /*event = new HistoryTrafficCountEvent(remoteParty, StatusType.Incoming);
                            event.setContent(new String("Start Tethering -- TotalUpload: " +
                                    Long.toString(args.getTotalUpload()) + ", TotalDownload: " + Long.toString(args.getTotalDownload())));
                            event.setStartTime(SgsDateTimeUtils.parseDate(dateString).getTime());
                            mEngine.getHistoryService().addEvent(event);*/
                            //mEngine.showSMSNotif(R.drawable.sms_25, "New message");
                            break;
                        case END:
                            event = new HistoryTrafficCountEvent(remoteParty, StatusType.Missed);
                            /*event.setContent(new String("Start Tethering -- TotalUpload: " +
                                    Long.toString(args.getTotalUpload()) + ", TotalDownload: " + Long.toString(args.getTotalDownload())));
                            */
                            //Log.d(TAG, "Traffic Count getTotalUpload = " + args.getTotalUpload());
                            //Log.d(TAG, "Traffic Count getTotalDownload = " + args.getTotalDownload());
                            //Log.d(TAG, "Traffic Count Rx End date = " + dateString);
                            event.setTotalUpload(Long.toString(args.getTotalUpload()));
                            event.setTotalDownload(Long.toString(args.getTotalDownload()));
                            event.setStartTime(SgsDateTimeUtils.parseDate(dateString).getTime());
                            if(mEngine.getHistoryService().getEvents().size() != 0 && mEngine.getHistoryService().getEvents().get(0).compareTo(event) == 0 ) {
                                Log.d(TAG, "Found Redundant Traffic Count Event, will avoid this");
                                break;
                            }
                            mEngine.getHistoryService().addEvent(event);
                            break;
                        default:
                            Log.d(TAG, "Invalid event args.getEventType().");
                            break;
                    }
                }
				// PagerMode Messaging Events
				//if(SgsMessagingEventArgs.ACTION_MESSAGING_EVENT.equals(action)){ }
				
				// MSRP chat Events
				// For performance reasons, file transfer events will be handled by the owner of the context
				if(SgsMsrpEventArgs.ACTION_MSRP_EVENT.equals(action)){ }
				
				// Invite Events
				else if(SgsInviteEventArgs.ACTION_INVITE_EVENT.equals(action)){ }
			}
		};
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SgsRegistrationEventArgs.ACTION_REGISTRATION_EVENT);
		intentFilter.addAction(SgsInviteEventArgs.ACTION_INVITE_EVENT);
		intentFilter.addAction(SgsMessagingEventArgs.ACTION_MESSAGING_EVENT);
		intentFilter.addAction(SgsMsrpEventArgs.ACTION_MSRP_EVENT);
        intentFilter.addAction(TrafficCountEventArgs.ACTION_TRAFFIC_COUNT_EVENT);
		registerReceiver(mBroadcastReceiver, intentFilter);
		
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if (bundle != null && bundle.getBoolean("autostarted")) {
				if (mEngine.start()) {
					mEngine.getTetheringService().register(null);
				}
			}
		}
		
		// alert()
		final Intent i = new Intent(ACTION_STATE_EVENT);
		i.putExtra("started", true);
		sendBroadcast(i);
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy()");
		if(mBroadcastReceiver != null){
			unregisterReceiver(mBroadcastReceiver);
			mBroadcastReceiver = null;
		}
		if(mWakeLock != null){
			if(mWakeLock.isHeld()){
				mWakeLock.release();
				mWakeLock = null;
			}
		}
		super.onDestroy();
	}
}
