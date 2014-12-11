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

import org.saydroid.logger.Log;
import org.saydroid.logger.LogConfiguration;
import org.saydroid.sgs.SgsApplication;

public class SRTDroid extends SgsApplication{
	private final static String TAG = SRTDroid.class.getCanonicalName();

    private static final String DATA_FOLDER = String.format("/data/data/%s", MainActivity.class.getPackage().getName());

	public SRTDroid() {
        // Start log to file from here
        LogConfiguration.getInstance().setLoggerName(SRTDroid.class.getCanonicalName());
        LogConfiguration.getInstance().setFileName(String.format("%s/%s", SRTDroid.DATA_FOLDER,"SRTDroid.log"));
        LogConfiguration.getInstance().setInternalDebugging(true);
        //LogConfiguration.getInstance().setFilePattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        LogConfiguration.getInstance().setFilePattern("%msg%n");
        LogConfiguration.getInstance().configure();

    	Log.d(TAG, "SRTDroid()");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
