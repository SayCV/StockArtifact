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

import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;

import org.saycv.logger.Log;
import org.saycv.sgs.utils.SgsStringUtils;

import java.lang.reflect.Field;

//import org.saycv.utils.AndroidUtils;
//import org.saycv.utils.CpuFeatures_t;

/**
 * @mainpage Foreword
 * <p/>
 * <h1>Foreword</h1>
 * <b>android-sgs-stack</b> is a <a href="http://en.wikipedia.org/wiki/Next_generation_network">NGN</a> (Next Generation Network) stack for Android 2.x (or later) devices. <br />
 * The Stack is based on <a href="http://saycv.org">saycv</a> framework. <a href="http://saycv.org">saycv</a> is the world's most advanced open source
 * 3GPP IMS/RCS framework for both embedded and desktop systems. <br />
 * The main purpose is to provide an open source stack for the developers to build their own VoIP applications. <br />
 * This framework offers a unique set of features ranging from audio/video calls, content sharing, messaging, conferencing, enhanced address book to social presence.
 * All these features are implemented in accordance with the standards: GSMA RCS, 3GPP IMS or VoLTE.<br />
 * @page Introduction
 * This document has been written by us (Doubango Telecom) to help developers to quickly create innovative multimedia applications
 * for the Android OS. If you are a developer and is looking for the best way to develop a NGN (VoIP, Messaging, Video Conferencing, ...) or rich application for Android
 * then your are at the right place. <br />
 * If you want to get help or have some feedbacks then please visit our website: <a href="http://code.google.com/p/imsdroid/">http://code.google.com/p/imsdroid/</a>
 * <p/>
 * <h2>Doubango Solution</h2>
 * <b>android-sgs-stack</b> is part of Doubango Solution which include many components such as:
 * <p/>
 * <h3>Client-side components</h3>
 * - <a href="http://code.google.com/p/boghe/">Boghe</a>: IMS/RCS Client for Windows
 * - <a href="http://code.google.com/p/imsdroid/">IMSDroid</a>: IMS/RCS Client for Android using <b>android-sgs-stack</b>
 * - <a href="http://code.google.com/p/idoubs/">iDoubs</a>: IMS/RCS Client for iOS (iPhone, iPad and iPod Touch)
 * <p/>
 * <h3>Server-side components</h3>
 * - <a href="http://code.google.com/p/openvcs/">OpenVCS</a>: OpenVCS stands for Open Source Video Conferencing Server and is used to manage Multipoint Control Units (MCU). Each MCU (a.k.a Bridge) can handle up to 64 participants
 * - <a href="http://code.google.com/p/flash2ims/">Flash2IMS</a>: Adobe� Flash� to SIP/IMS Gateway.
 * <p/>
 * <h2>Highlights</h2>
 * <p/>
 * - SIP(RFC 3261, 3GPP TS 24.229 Rel-9)
 * - TCP and UDP over IPv4 or IPv6
 * - Signaling Compression, SigComp(RFC 3320, 3485, 4077, 4464, 4465, 4896, 5049, 5112 and 1951)
 * <p/>
 * - Enhanced Address Book (XCAP storage, authorizations, presence, ...)
 * - GSMA Rich Communication Suite release 3
 * - Partial supports for One Voice Profile V1.0.0 (GSMA VoLTE)
 * - Partial supports for MMTel UNI (used by GSMA RCS and GSMA VoLTE)
 * <p/>
 * - IMS-AKA registration (both AKA-v1 and AKA-v2), Digest MD5, Basic
 * - 3GPP Early IMS Security (3GPP TS 33.978)
 * - Proxy-CSCF discovery using DNS NAPTR+SRV
 * - Private extension headers for 3GPP
 * - Service Route discovery
 * - Subscription to reg event package (Honoring network initiated (re/de/un)-registration events)
 * <p/>
 * - 3GPP SMS Over IP (3GPP TS 23.038, 24.040, 24.011, 24.341 and 24.451)
 * - Voice Call (G729AB1, AMR-NB, iLBC, GSM, PCMA, PCMU, Speex-NB)
 * - Video Call (H264, MP4V-ES, Theora, H.263, H.263-1998, H.261)
 * - DTMF (RFC 4733)
 * - QoS negotiation using Preconditions (RFC 3312, 4032 and 5027)
 * - SIP Session Timers (RFC 4028)
 * - Provisional Response Acknowledgments (PRACK)
 * - Communication Hold (3GPP TS 24.610)
 * - Message Waiting Indication (3GPP TS 24.606)
 * - Calling E.164 numbers by using ENUM protocol (RFC 3761)
 * - NAT Traversal using STUN2 (RFC 5389) with possibilities to automatically discover the server by using DNS SRV (TURN already implemented and ICE is under tests)
 * <p/>
 * - One2One and Group Chat
 * - File Transfer and Content sharing
 * @page page__Setting_Up_NGN_project Setting up NGN project
 * @anchor anchor_Setting_Up_NGN_project
 * This section explain how to setup a NGN project using Eclipse.<br />
 * <p/>
 * <h2>Checking out the source code</h2>
 * To check out the source code of the NGN library you will need a SVN client.<br />
 * Use this command to anonymously check out the last project source:
 * @code svn checkout http://imsdroid.googlecode.com/svn imsdroid
 * @endcode The source code of the library is under:
 * @code imsdroid/branches/2.0/android-sgs-stack
 * @endcode <h2>Importing the NGN project into Eclipse</h2>
 * The NGN project is the Next Generation Network library.
 * - Open eclipse
 * - Go to File -> Import -> General -> Existing Project into workspace
 * - Select <b> android-sgs-stack </b> folder and click <b>Finish</b>
 * @image html sgs_eclipse_import.png "Importing project into your workspace"
 * <p/>
 * <h2>Creating you first NGN application using Eclipse</h2>
 * - Open Eclipse and select File -> New -> Android Project
 * - From the next window (<b>"New Android Project"</b>) fill the text fields like this:<br />
 * - Project name: <b>myFirstApp</b><br />
 * - Location: < set any path ><br />
 * - Build Target: <b>Android 2.0</b> (at least)<br />
 * - Application name: <b>myFirstApp</b><br />
 * - Package name: <b>org.saycv.test</b><br />
 * - Check <b>"Create Activity"</b> and name it <b>"Main"</b><br />
 * @image html sgs_eclipse_newproj.png "Create your first NGN application"
 * - Click on Finish to create the project
 * - From the Eclipse package explorer, right click on <b>myFirstApp</b> and select <b>"Properties"</b> then "Android" from the left<br/><br/>
 * @image html sgs_eclipse_properties_1.png "NGN application properties"
 * - From the properties window, select <b>"Add"</b> button then select <b>android-sgs-stack</b> from the list of the available libraries<br/><br/>
 * @image html sgs_eclipse_properties_2.png "Add dependencies"
 * - Select <b>"Java Compiler"</b> from the left and change the version from 1.5 to 1.6<br/><br/>
 * @image html sgs_eclipse_jdk_version.png "Java Compiler version"
 * - Select <b>"Java Build Path"</b> from the left, then <b>"Libraries"</b><br/><br/>
 * @image html sgs_eclipse_java_buil_path_1.png "Java Build Path 1/2"
 * - From "Java Build Path 1/2", select <b>"Add JARs..."</b> then <b>android-sgs-stack/libs/simple-xml-2.3.4.jar</b>, then <b>"OK"</b> to close the window<br/><br/>
 * @image html sgs_eclipse_java_buil_path_2.png "Java Build Path 2/2"
 * - Click on <b>"OK"</b> to close the window
 * <p/>
 * <h2>Setting up Android Permissions</h2>
 * In order to use the framework you must enable some user-permission in your Android manifest. <br />
 * Open <b>myFirstApp/AndroidManifest.xml</b>, then add this:
 * @code <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
 * <p/>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.CAMERA" />
 * <uses-permission android:name="android.permission.WAKE_LOCK" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 * <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
 * <uses-permission android:name="android.permission.VIBRATE" />
 * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 * <p/>
 * <uses-permission android:name="android.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
 * <uses-permission android:name="android.permission.READ_CONTACTS"/>
 * <uses-permission android:name="android.permission.WRITE_CONTACTS"/>
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 * <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
 * <uses-permission android:name="android.permission.CALL_PHONE" />
 * <uses-permission android:name="android.permission.RAISED_THREAD_PRIORITY"/>
 * @endcode ... just before @code </manifest> @endcode
 * <p/>
 * <h2>Loading native libraries</h2>
 * The NGN library contain native (C/C++) libraries from Doubango Framework. These libraries contain the signaling protocols (sip, sdp, rtp, xcap, msrp,...),
 * codecs (h264,theora,speex,gsm,g729,...), ...<br />
 * You must load these libraries before calling any function from the NGN library. We recommend using a static block in your main activity like this:<br>
 * @code // Load native libraries (the shared libraries are from 'android-sgs-stack' project)
 * static {
 * System.load(String.format("/data/data/%s/lib/libtinyWRAP.so", Main.class.getPackage().getName()));
 * SgsEngine.initialize();
 * }
 * @endcode <h2>Declaring your app as NGN</h2>
 * Decalaring your app as NGN is recommended if your are programming at <b>high</b> level. <br />
 * - From the Eclipse package explorer, open <b>AndroidManifest.xml</b> and select <b>Application</b> tab from below
 * - Click on <b>browse</b> (on the right of <b>Name</b>) then, select <b>"SgsApplication"</b> from the list<br/><br/>
 * @image html sgs_eclipse_declaring_sgs_app.png "Declaring your app as NGN"
 * @page Architecture
 * The stack offers three levels of programming: <b>Low</b>, <b>Medium</b> and <b>High</b>.<br />
 * Before building and running your project, you should take a look at the section @ref anchor_Setting_Up_NGN_project "explaining how to setup a NGN project".
 * <h2>Low level</h2>
 * This level allow you to directly have access to saycv functions through JNI.
 * This level is the most flexible one but is out of scoop because it's too difficult to manage. <br />
 * All functions used in this level are in one single package: <b>org.saycv.tinyWRAP</b><br />
 * For example, the code below shows how to register to a SIP/IMS server:
 * @code final String realm = "sip:saycv.org";
 * final String privateIdentity = "001";
 * final String publicIdentity = "sip:001@saycv.org";
 * final String password = "my secret";
 * final String proxyHost = "192.168.0.1";
 * RegistrationSession registrationSession;
 * // Sip Callback
 * final SipCallback callback = new SipCallback(){
 * @Override public int OnDialogEvent(DialogEvent e) {
 * final SipSession sipSession = e.getBaseSession();
 * final long sipSessionId = sipSession.getId();
 * final short code = e.getCode();
 * switch (code){
 * case tinyWRAPConstants.tsip_event_code_dialog_connecting:
 * if(registrationSession != null && registrationSession.getId() == sipSessionId){
 * // Registration in progress
 * }
 * break;
 * case tinyWRAPConstants.tsip_event_code_dialog_connected:
 * if(registrationSession != null && registrationSession.getId() == sipSessionId){
 * // You are registered
 * }
 * break;
 * case tinyWRAPConstants.tsip_event_code_dialog_terminating:
 * if(registrationSession != null && registrationSession.getId() == sipSessionId){
 * // You are unregistering
 * }
 * break;
 * case tinyWRAPConstants.tsip_event_code_dialog_terminated:
 * if(registrationSession != null && registrationSession.getId() == sipSessionId){
 * // You are unregistered
 * }
 * break;
 * }
 * <p/>
 * return 0;
 * }
 * @Override public int OnRegistrationEvent(RegistrationEvent e) {
 * // low level events
 * return 0;
 * }
 * };
 * // Create the SipStack
 * SipStack sipStack = new SipStack(callback, realm, privateIdentity, publicIdentity);
 * // Set Proxy Host and port
 * sipStack.setProxyCSCF(proxyHost, 5060, "UDP", "IPv4");
 * // Set password
 * sipStack.setPassword(password);
 * if(sipStack.isValid()){
 * if(sipStack.start()){
 * registrationSession = new RegistrationSession(sipStack);
 * registrationSession.setFromUri(publicIdentity);
 * // Send SIP register request
 * registrationSession.register_();
 * }
 * }
 * @endcode <h2>Medium level</h2>
 * This level is built on of the <b>low</b> level. The main advantage of this level is that it's flexible without
 * being too complicated as all low level functions are wrapped into comprehensive API.
 * For example, if you want to implement a multi-stack (multi-account) application this is the right level.
 * <p/>
 * <h2>High level</h2>
 * This level is built in top of the <b>low</b> level and is much easier than the later.
 * The High level is composed of a set of Services managed by a single NGN engine instance. Each service is responsible for
 * a particular task. For example, you have one service for SIP, one for contact management, one for networking etc etc <br />
 * <p/>
 * <h3>NGN Engine</h3>
 * The engine is a black box containing all the services. You must always retrieve the services through the engine. <br />
 * You must also start/stop the services through the NGN engine.<br />
 * The code below shows how to get an instance of the engine:
 * @code // Gets an instance of the engine. This function will always returns the same instance
 * // which means that you can call it as many as you want from anywhere in your code
 * final SgsEngine mEngine = SgsEngine.getInstance();
 * @endcode The code below shows how to get some services from the engine:
 * @code // Gets the configuration service
 * ISgsConfigurationService mConfigurationService = mEngine.getConfigurationService();
 * // Gets the SIP/IMS service
 * ISgsSipService mSipService = mEngine.getSipService();
 * // etc etc
 * @endocde The code below shows how to start/stop the engine.
 * @code // Starts the engine
 * mEngine.start();
 * // Stops the engine
 * mEngine.stop();
 * @endcode Starting/Stopping the engine will start/stop all underlying services.
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * /**
 * Global object defining the application. You should extends this class in your own
 * Android application.
 */
public class SgsApplication extends Application {
    private final static String TAG = SgsApplication.class.getCanonicalName();

    private static SgsApplication sInstance;
    private static PackageManager sPackageManager;
    private static String sPackageName;
    private static String sDeviceURN;
    private static String sDeviceIMEI;
    private static int sSdkVersion;
    private static int sVersionCode;
    private static AudioManager sAudioManager;
    private static SensorManager sSensorManager;
    private static KeyguardManager sKeyguardManager;
    private static ConnectivityManager sConnectivityManager;
    private static PowerManager sPowerManager;
    private static PowerManager.WakeLock sPowerManagerLock;


    public SgsApplication() {
        sInstance = this;
    }

    public static SgsApplication getInstance() {
        return sInstance;
    }

    /**
     * Retrieve application's context
     *
     * @return Android context
     */
    public static Context getContext() {
        return getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sPackageManager = sInstance.getPackageManager();
        sPackageName = sInstance.getPackageName();

        Log.d(TAG,"Build.MODEL="+Build.MODEL);
        Log.d(TAG,"Build.VERSION.SDK="+Build.VERSION.SDK_INT);
    }

    /**
     * Gets Android SDK version
     *
     * @return Android SDK version used to build the project
     */
    public static int getSDKVersion() {
        if (sSdkVersion == 0) {
            sSdkVersion = Build.VERSION.SDK_INT;
        }
        return sSdkVersion;
    }

    /**
     * Whether we need special hack for buggy speaker. For example, all Samsung devices
     * need to be hacked.
     *
     * @return true if we need to apply the hack and false otherwise
     */
    public static boolean useSetModeToHackSpeaker() {
        final String model = Build.MODEL;
        return (isSamsung() && !isSamsungGalaxyMini() && getSDKVersion() <= 7) ||

                model.equalsIgnoreCase("blade") ||        // ZTE Blade

                model.equalsIgnoreCase("htc_supersonic") || //HTC EVO

                model.equalsIgnoreCase("U8110") || // Huawei U8110
                model.equalsIgnoreCase("U8150")  // Huawei U8110

                ;
    }

    /**
     * Whether the stack is running on a Samsung device
     *
     * @return true if the stack is running on a Samsung device and false otherwise
     */

    public static boolean isSamsungGalaxyMini() {
        final String model = Build.MODEL.toLowerCase();
        return model.equalsIgnoreCase("gt-i5800");
    }


    // Requires 'System.load("libutils.so");' to be called first
    //public static boolean isARMv7WithoutNeon(){
    //	final CpuFamily_t family = AndroidUtils.getCpuFamily();
    //	if(family == CpuFamily_t.ARM){
    //		final CpuFeatures_t features =  AndroidUtils.getCpuFeatures();
    //		return ((features.swigValue() & CpuFeatures_t.ARMv7.swigValue()) != 0) && ((features.swigValue() & CpuFeatures_t.NEON.swigValue()) == 0);
    //	}
    //	return false;
    //}
/*   
    public static boolean isARMv7WithoutNeon(){
        return (isCpuARMv7() && !isCpuNeon());
    }
    
    public static boolean isCpuARMv7(){
    	return ((AndroidUtils.getCpuFeatures().intValue() & CpuFeatures_t.ARMv7.swigValue()) != 0);
    }
    
    public static boolean isCpuNeon(){
    	return ((AndroidUtils.getCpuFeatures().intValue() & CpuFeatures_t.NEON.swigValue()) != 0);
    }
*/

    public static boolean isSamsung() {
        final String model = Build.MODEL.toLowerCase();
        return model.startsWith("gt-")
                || model.contains("samsung")
                || model.startsWith("sgh-")
                || model.startsWith("sph-")
                || model.startsWith("sch-");
    }

    /**
     * Whether the stack is running on a HTC device
     *
     * @return true if the stack is running on a HTC device and false otherwise
     */
    public static boolean isHTC() {
        final String model = Build.MODEL.toLowerCase();
        return model.startsWith("htc");
    }

    public static boolean isZTE() {
        final String model = Build.MODEL.toLowerCase();
        return model.startsWith("zte");
    }

    public static boolean isLG() {
        final String model = Build.MODEL.toLowerCase();
        return model.startsWith("lg-");
    }

    public static boolean isToshiba() {
        final String model = Build.MODEL.toLowerCase();
        return model.startsWith("toshiba");
    }

    public static boolean isAudioRecreateRequired() {
        return false;
    }

    public static boolean isSetModeAllowed() {
        return isZTE() || isLG();
    }

    public static boolean isBuggyProximitySensor() {
        return isZTE();
    }

    public static boolean isAGCSupported() {
        return isSamsung() || isHTC();
    }

    public static int getVersionCode() {
        if (sVersionCode == 0 && sPackageManager != null) {
            try {
                sVersionCode = sPackageManager.getPackageInfo(sPackageName, 0).versionCode;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sVersionCode;
    }

    public static String getVersionName() {
        if (sPackageManager != null) {
            try {
                return sPackageManager.getPackageInfo(sPackageName, 0).versionName;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "0.0";
    }

    public static String getDeviceURN() {
        if (SgsStringUtils.isNullOrEmpty(sDeviceURN)) {
            try {
                final TelephonyManager telephonyMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                final String msisdn = telephonyMgr.getLine1Number();
                if (SgsStringUtils.isNullOrEmpty(msisdn)) {
                    sDeviceURN = String.format("urn:imei:%s", telephonyMgr.getDeviceId());
                } else {
                    sDeviceURN = String.format("urn:tel:%s", msisdn);
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                sDeviceURN = "urn:uuid:3ca50bcb-7a67-44f1-afd0-994a55f930f4";
            }
        }
        return sDeviceURN;
    }

    public static String getDeviceIMEI() {
        if (SgsStringUtils.isNullOrEmpty(sDeviceIMEI)) {
            final TelephonyManager telephonyMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            sDeviceIMEI = telephonyMgr.getDeviceId();
        }
        return sDeviceIMEI;
    }

    public static AudioManager getAudioManager() {
        if (sAudioManager == null) {
            sAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        }
        return sAudioManager;
    }

    public static SensorManager getSensorManager() {
        if (sSensorManager == null) {
            sSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        }
        return sSensorManager;
    }

    public static KeyguardManager getKeyguardManager() {
        if (sKeyguardManager == null) {
            sKeyguardManager = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
        }
        return sKeyguardManager;
    }

    public static ConnectivityManager getConnectivityManager() {
        if (sConnectivityManager == null) {
            sConnectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        }
        return sConnectivityManager;
    }

    public static PowerManager getPowerManager() {
        if (sPowerManager == null) {
            sPowerManager = (PowerManager) getContext().getSystemService(POWER_SERVICE);
        }
        return sPowerManager;
    }

    public static Display getDefaultDisplay() {
        return ((WindowManager) getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
    }

    public static String getABI() {
        try {
            Field field = android.os.Build.class.getField("CPU_ABI");
            String abi = field.get(null).toString();
            if (abi == null) {
                return "unknown";
            }
            return abi;
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    public static boolean acquirePowerLock() {
        if (sPowerManagerLock == null) {
            final PowerManager powerManager = getPowerManager();
            if (powerManager == null) {
                Log.e(TAG, "Null Power manager from the system");
                return false;
            }

            if ((sPowerManagerLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG)) == null) {
                Log.e(TAG, "Null Power manager lock from the system");
                return false;
            }
            sPowerManagerLock.setReferenceCounted(false);
        }

        synchronized (sPowerManagerLock) {
            if (!sPowerManagerLock.isHeld()) {
                Log.d(TAG, "acquirePowerLock()");
                sPowerManagerLock.acquire();
            }
        }
        return true;
    }

    public static boolean releasePowerLock() {
        if (sPowerManagerLock != null) {
            synchronized (sPowerManagerLock) {
                if (sPowerManagerLock.isHeld()) {
                    Log.d(TAG, "releasePowerLock()");
                    sPowerManagerLock.release();
                }
            }
        }
        return true;
    }

    public static boolean acquireWakeLock() {
        if (sPowerManagerLock == null) {
            final PowerManager powerManager = getPowerManager();
            if (powerManager == null) {
                Log.e(TAG, "Null Power manager from the system");
                return false;
            }

            if ((sPowerManagerLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG)) == null) {
                Log.e(TAG, "Null Power manager lock from the system");
                return false;
            }
            sPowerManagerLock.setReferenceCounted(false);
        }

        synchronized (sPowerManagerLock) {
            if (!sPowerManagerLock.isHeld()) {
                Log.d(TAG, "acquirePowerLock()");
                sPowerManagerLock.acquire();
            }
        }
        return true;
    }

    public static boolean releaseWakeLock() {
        if (sPowerManagerLock != null) {
            synchronized (sPowerManagerLock) {
                if (sPowerManagerLock.isHeld()) {
                    Log.d(TAG, "releasePowerLock()");
                    sPowerManagerLock.release();
                }
            }
        }
        return true;
    }
}
