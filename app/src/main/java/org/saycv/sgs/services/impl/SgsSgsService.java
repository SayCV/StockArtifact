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

package org.saycv.sgs.services.impl;

import android.content.Context;
import android.util.Log;

import org.saycv.sgs.SgsEngine;
import org.saycv.sgs.services.ISgsConfigurationService;
import org.saycv.sgs.services.ISgsNetworkService;
import org.saycv.sgs.services.ISgsSgsService;


public class SgsSgsService extends SgsBaseService
        implements ISgsSgsService {
    private final static String TAG = SgsSgsService.class.getCanonicalName();

    private final ISgsConfigurationService mConfigurationService;
    private final ISgsNetworkService mNetworkService;

    public SgsSgsService() {
        super();

        mConfigurationService = SgsEngine.getInstance().getConfigurationService();
        mNetworkService = SgsEngine.getInstance().getNetworkService();
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

    @Override
    public boolean isRegistered() {
        return false;
    }

    @Override
    public boolean register(Context context) {
        Log.d(TAG, "register()");
        return true;
    }

    @Override
    public boolean unRegister() {
        if (isRegistered()) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
        }
        return true;
    }

    @Override
    public boolean PresencePublish() {
        return false;
    }

//	private void broadcastRegistrationEvent(SgsRegistrationEventArgs args){
//		final Intent intent = new Intent(SgsRegistrationEventArgs.ACTION_REGISTRATION_EVENT);
//		intent.putExtra(SgsRegistrationEventArgs.EXTRA_EMBEDDED, args);
//		SgsApplication.getContext().sendBroadcast(intent);
//	}


}
