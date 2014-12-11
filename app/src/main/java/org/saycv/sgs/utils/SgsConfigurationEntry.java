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
package org.saycv.sgs.utils;

import org.saycv.sgs.sip.SgsPresenceStatus;


public class SgsConfigurationEntry {
	private static final String TAG = SgsConfigurationEntry.class.getCanonicalName();
	
	public final static String  SHARED_PREF_NAME = TAG;
	public static final String PCSCF_DISCOVERY_DNS_SRV = "DNS NAPTR+SRV";
	
	// General
    public static final String GENERAL_STARTUP_CHECK = "GENERAL_STARTUP_CHECK." + TAG;
	public static final String GENERAL_AUTOSTART = "GENERAL_AUTOSTART." + TAG;
	public static final String GENERAL_SHOW_WELCOME_SCREEN = "GENERAL_SHOW_WELCOME_SCREEN." + TAG;
	public static final String GENERAL_FULL_SCREEN_VIDEO = "GENERAL_FULL_SCREEN_VIDEO." + TAG;
	public static final String GENERAL_USE_FFC = "GENERAL_USE_FFC." + TAG;
	public static final String GENERAL_INTERCEPT_OUTGOING_CALLS = "GENERAL_INTERCEPT_OUTGOING_CALLS." + TAG;
	public static final String GENERAL_AUDIO_PLAY_LEVEL = "GENERAL_AUDIO_PLAY_LEVEL." + TAG;
	public static final String GENERAL_ENUM_DOMAIN = "GENERAL_ENUM_DOMAIN." + TAG;
	public static final String GENERAL_AEC = "GENERAL_AEC."+ TAG ;
	public static final String GENERAL_VAD = "GENERAL_VAD."+ TAG ;	
	public static final String GENERAL_NR = "GENERAL_NR."+ TAG ;	
	public static final String GENERAL_ECHO_TAIL = "GENERAL_ECHO_TAIL." + TAG ;
	public static final String GENERAL_SEND_DEVICE_INFO = "GENERAL_SEND_DEVICE_INFO" + TAG;

    public static final String GENERAL_VOC = "GENERAL_VOC."+ TAG ;
    public static final String GENERAL_DUC = "GENERAL_DUC."+ TAG ;
    public static final String GENERAL_DWL = "GENERAL_DWL."+ TAG ;
    public static final String GENERAL_DSO = "GENERAL_DSO."+ TAG ;

    // Identity
	public static final String IDENTITY_DISPLAY_NAME = "IDENTITY_DISPLAY_NAME." + TAG;
	public static final String IDENTITY_IMPU = "IDENTITY_IMPU." + TAG;
	public static final String IDENTITY_IMPI = "IDENTITY_IMPI." + TAG;
	public static final String IDENTITY_PASSWORD = "IDENTITY_PASSWORD." + TAG;
	
	// Network
	public static final String NETWORK_USE_WIFI = "NETWORK_USE_WIFI." + TAG;
	public static final String NETWORK_USE_MOBILE = "NETWORK_USE_MOBILE." + TAG;
	
	//
	//	Default values
	//
	// Network
	public static final int DEFAULT_NETWORK_REGISTRATION_TIMEOUT = 1700;
	public static final String DEFAULT_NETWORK_REALM = "sayCV.org";
	public static final boolean DEFAULT_NETWORK_USE_WIFI = false;
	public static final boolean DEFAULT_NETWORK_USE_MOBILE = false;
}
