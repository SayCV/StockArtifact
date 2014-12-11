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

import org.saydroid.rootcommands.RootCommands;
import org.saydroid.sgs.utils.SgsConfigurationEntry;
import org.saydroid.sgs.utils.SgsFileUtils;
import org.saydroid.tether.usb.Services.IScreenService;
import org.saydroid.tether.usb.Services.ITetheringNetworkService;
import org.saydroid.tether.usb.Services.ITetheringService;
import org.saydroid.tether.usb.Services.Impl.ScreenService;
import org.saydroid.sgs.SgsApplication;
import org.saydroid.sgs.SgsEngine;
import org.saydroid.sgs.SgsNativeService;
import org.saydroid.sgs.media.SgsMediaType;
import org.saydroid.sgs.sip.SgsAVSession;
import org.saydroid.sgs.sip.SgsMsrpSession;
import org.saydroid.sgs.utils.SgsPredicate;
import org.saydroid.tether.usb.Services.Impl.TetheringNetworkService;
import org.saydroid.tether.usb.Services.Impl.TetheringService;
import org.saydroid.utils.AndroidUtils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.saydroid.logger.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Engine extends SgsEngine{
	private final static String TAG = Engine.class.getCanonicalName();
	
	private static final String CONTENT_TITLE = "SRTDroid";
	
	private static final int NOTIF_AVCALL_ID = 19833892;
	private static final int NOTIF_SMS_ID = 19833893;
	private static final int NOTIF_APP_ID = 19833894;
	private static final int NOTIF_CONTSHARE_ID = 19833895;
	private static final int NOTIF_CHAT_ID = 19833896;
	private static final String DATA_FOLDER = String.format("/data/data/%s", MainActivity.class.getPackage().getName());
	private static final String LIBS_FOLDER = String.format("%s/lib", Engine.DATA_FOLDER);

    private static final String SETTING_DB_PATH = "/data/data/com.android.providers.settings/databases/";
    private static final String mGlobalSetting_tether_supported = "tether_supported"; //valid setting is 1
    private static final String mGlobalSetting_tether_dun_required = "tether_dun_required"; //valid setting is 0

	
	private IScreenService mScreenService;
    protected ITetheringNetworkService mTetheringNetworkService;
    private ITetheringService mTetheringService;

	static {
		// See 'http://code.google.com/p/imsdroid/issues/detail?id=197' for more information
		// Load Android utils library (required to detect CPU features)
		System.load(String.format("%s/%s", Engine.LIBS_FOLDER, "libutils_armv5te.so"));
		Log.d(TAG,"CPU_Feature="+AndroidUtils.getCpuFeatures());
		/*if(SgsApplication.isCpuNeon()){
			Log.d(TAG,"isCpuNeon()=YES");
			System.load(String.format("%s/%s", Engine.LIBS_FOLDER, "libtinyWRAP_armv7-a.so"));
		}
		else{
			Log.d(TAG,"isCpuNeon()=NO");
			System.load(String.format("%s/%s", Engine.LIBS_FOLDER, "libtinyWRAP_armv5te.so"));
		}*/
		// Initialize the engine
		SgsEngine.initialize();
	}

	public static SgsEngine getInstance(){
		if(sInstance == null){
			sInstance = new Engine();
		}
		return sInstance;
	}
	
	public Engine(){
		super();
	}
	
	@Override
	public boolean start() {
        boolean result;
        result = super.start();
        // we can use getConfigurationService() after call SgsEngine.Start()
        if(getConfigurationService().getBoolean(SgsConfigurationEntry.GENERAL_STARTUP_CHECK,
                SgsConfigurationEntry.DEFAULT_GENERAL_STARTUP_CHECK)) {
            this.startupCheck();
        } else {
            if (!this.hasRootPermission()){
                Log.d(TAG, "No Root Permission!!!");
            }
        }
		return result;
	}
	
	@Override
	public boolean stop() {
		return super.stop();
	}

    public Handler displayMessageHandler = new Handler(){
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                showAppMessage((String) msg.obj);
            }
            super.handleMessage(msg);
        }
    };

    // Display Toast-Message
    public void showAppMessage(String message) {
        Toast.makeText(SRTDroid.getContext(), message, Toast.LENGTH_LONG).show();
    }

	private void showNotification(int notifId, int drawableId, String tickerText) {
		if(!mStarted){
			return;
		}
        //final Notification.Builder notificationBuilder = new Notification.Builder(SRTDroid.getContext());
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(SRTDroid.getContext())
                .setWhen(System.currentTimeMillis());
        notificationBuilder.setSmallIcon(drawableId);
        // Set the icon, scrolling text and timestamp
        //final Notification notification = new Notification(drawableId, "", System.currentTimeMillis());
        
        Intent intent = new Intent(SRTDroid.getContext(), MainActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP  | Intent.FLAG_ACTIVITY_NEW_TASK);
        
        switch(notifId){
        	case NOTIF_APP_ID:
        		//notification.flags |= Notification.FLAG_ONGOING_EVENT;
                notificationBuilder.setOngoing(true);
        		intent.putExtra("notif-type", "reg");
        		break;
        		
        	case NOTIF_CONTSHARE_ID:
                intent.putExtra("action", MainActivity.ACTION_SHOW_CONTSHARE_SCREEN);
                //notification.defaults |= Notification.DEFAULT_SOUND;
                notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                break;
                
        	case NOTIF_SMS_ID:
                //notification.flags |= Notification.FLAG_AUTO_CANCEL;
                //notification.defaults |= Notification.DEFAULT_SOUND;
                //notification.tickerText = tickerText;
                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                intent.putExtra("action", MainActivity.ACTION_SHOW_SMS);
                break;
                
        	case NOTIF_AVCALL_ID:
        		tickerText = String.format("%s (%d)", tickerText, SgsAVSession.getSize());
        		intent.putExtra("action", MainActivity.ACTION_SHOW_AVSCREEN);
        		break;
        		
        	case NOTIF_CHAT_ID:
        		//notification.defaults |= Notification.DEFAULT_SOUND;
                notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        		tickerText = String.format("%s (%d)", tickerText, SgsMsrpSession.getSize(new SgsPredicate<SgsMsrpSession>() {
					@Override
					public boolean apply(SgsMsrpSession session) {
						return session != null && SgsMediaType.isChat(session.getMediaType());
					}
				}));
        		intent.putExtra("action", MainActivity.ACTION_SHOW_CHAT_SCREEN);
        		break;
        		
       		default:
       			
       			break;
        }
        
        PendingIntent contentIntent = PendingIntent.getActivity(SRTDroid.getContext(), notifId/*requestCode*/, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the info for the views that show in the notification panel.
        //notification.setLatestEventInfo(SRTDroid.getContext(), CONTENT_TITLE, tickerText, contentIntent);
        notificationBuilder.setContentTitle(CONTENT_TITLE);
        notificationBuilder.setTicker(tickerText);
        notificationBuilder.setContentIntent(contentIntent);

        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNotifManager.notify(notifId, notificationBuilder.build());
    }
	
	public void showAppNotif(int drawableId, String tickerText){
    	Log.d(TAG, "showAppNotif");
    	showNotification(NOTIF_APP_ID, drawableId, tickerText);
    }
	
	public void showAVCallNotif(int drawableId, String tickerText){
    	showNotification(NOTIF_AVCALL_ID, drawableId, tickerText);
    }
	
	public void cancelAVCallNotif(){
    	if(!SgsAVSession.hasActiveSession()){
    		mNotifManager.cancel(NOTIF_AVCALL_ID);
    	}
    }
	
	public void refreshAVCallNotif(int drawableId){
		if(!SgsAVSession.hasActiveSession()){
    		mNotifManager.cancel(NOTIF_AVCALL_ID);
    	}
    	else{
    		showNotification(NOTIF_AVCALL_ID, drawableId, "In Call");
    	}
    }
	
	public void showContentShareNotif(int drawableId, String tickerText){
    	showNotification(NOTIF_CONTSHARE_ID, drawableId, tickerText);
    }
	
	public void cancelContentShareNotif(){
    	if(!SgsMsrpSession.hasActiveSession(new SgsPredicate<SgsMsrpSession>() {
			@Override
			public boolean apply(SgsMsrpSession session) {
				return session != null && SgsMediaType.isFileTransfer(session.getMediaType());
			}}))
    	{
    		mNotifManager.cancel(NOTIF_CONTSHARE_ID);
    	}
    }
    
	public void refreshContentShareNotif(int drawableId){
		if(!SgsMsrpSession.hasActiveSession(new SgsPredicate<SgsMsrpSession>() {
			@Override
			public boolean apply(SgsMsrpSession session) {
				return session != null && SgsMediaType.isFileTransfer(session.getMediaType());
			}}))
    	{
    		mNotifManager.cancel(NOTIF_CONTSHARE_ID);
    	}
    	else{
    		showNotification(NOTIF_CONTSHARE_ID, drawableId, "Content sharing");
    	}
    }
	
	public void showContentChatNotif(int drawableId, String tickerText){
    	showNotification(NOTIF_CHAT_ID, drawableId, tickerText);
    }
	
	public void cancelChatNotif(){
    	if(!SgsMsrpSession.hasActiveSession(new SgsPredicate<SgsMsrpSession>() {
			@Override
			public boolean apply(SgsMsrpSession session) {
				return session != null && SgsMediaType.isChat(session.getMediaType());
			}}))
    	{
    		mNotifManager.cancel(NOTIF_CHAT_ID);
    	}
    }
    
	public void refreshChatNotif(int drawableId){
		if(!SgsMsrpSession.hasActiveSession(new SgsPredicate<SgsMsrpSession>() {
			@Override
			public boolean apply(SgsMsrpSession session) {
				return session != null && SgsMediaType.isChat(session.getMediaType());
			}}))
    	{
    		mNotifManager.cancel(NOTIF_CHAT_ID);
    	}
    	else{
    		showNotification(NOTIF_CHAT_ID, drawableId, "Chat");
    	}
    }
	
	public void showSMSNotif(int drawableId, String tickerText){
    	showNotification(NOTIF_SMS_ID, drawableId, tickerText);
    }

	public IScreenService getScreenService(){
		if(mScreenService == null){
			mScreenService = new ScreenService();
		}
		return mScreenService;
	}

    public ITetheringNetworkService getTetheringNetworkService(){
        if(mTetheringNetworkService == null){
            mTetheringNetworkService = new TetheringNetworkService();
        }
        return mTetheringNetworkService;
    }

    public ITetheringService getTetheringService(){
        if(mTetheringService == null){
            mTetheringService = new TetheringService();
        }
        return mTetheringService;
    }

	@Override
	public Class<? extends SgsNativeService> getNativeServiceClass(){
		return NativeService.class;
	}

    public boolean hasRootPermission() {
        return RootCommands.hasRootPermission();
    }

    private void checkDirs() {
        File dir = new File(this.DATA_FOLDER);
        if (dir.exists() == false) {
            Log.d(TAG, "Application data-dir does not exist!");
        } else {
            String[] dirs = {
                    "/bin",
                    //"/var",
                    "/conf",
                    //"/library"
            };
            for (String dirname : dirs) {
                dir = new File(this.DATA_FOLDER + dirname);
                if (dir.exists() == false) {
                    if (!dir.mkdir()) {
                        Log.d(TAG, "Couldn't create " + dirname + " directory!");
                    }
                }
                else {
                    Log.d(TAG, "Directory '" + dir.getAbsolutePath() + "' already exists!");
                }
            }
        }
    }
      
    public boolean binariesExists() {
        File file_ifconfig = new File(this.DATA_FOLDER + "/bin/ifconfig");
        File file_route = new File(this.DATA_FOLDER + "/bin/route");
        return (file_ifconfig.exists() && file_route.exists());
    }

    private String copyBinary(String filename, int resource) {
        File outFile = new File(filename);
        Log.d(TAG, "Copying file '" + filename + "' ...");
        InputStream is = SgsApplication.getContext().getResources().openRawResource(resource);
        byte buf[]=new byte[1024];
        int len;
        try {
            OutputStream out = new FileOutputStream(outFile);
            while((len = is.read(buf))>0) {
                out.write(buf,0,len);
            }
            out.close();
            is.close();
        } catch (IOException e) {
            return "Couldn't install file - "+filename+"!";
        }
        return null;
    }

    private boolean chmodBin() {
        return RootCommands.run("chmod 0755 " + Engine.DATA_FOLDER + "/bin/*");
    }

    private void installFiles() {
        new Thread(new Runnable(){
            public void run(){
                String message = null;

                // tether
                if (message == null) {
                    message = ((Engine)Engine.getInstance()).copyBinary(Engine.DATA_FOLDER + "/bin/tether", R.raw.tether);
                }
                // iptables
                if (message == null) {
                    message = ((Engine)Engine.getInstance()).copyBinary(Engine.DATA_FOLDER + "/bin/iptables", R.raw.iptables);
                }
                // dnsmasq
                if (message == null) {
                    message = ((Engine)Engine.getInstance()).copyBinary(Engine.DATA_FOLDER + "/bin/dnsmasq", R.raw.dnsmasq);
                }
                // ifconfig
                if (message == null) {
                    message = ((Engine)Engine.getInstance()).copyBinary(Engine.DATA_FOLDER + "/bin/ifconfig", R.raw.ifconfig);
                }
                // sqlite3
                if (message == null) {
                    message = ((Engine)Engine.getInstance()).copyBinary(Engine.DATA_FOLDER + "/bin/sqlite3", R.raw.sqlite3);
                }
                //add route command from busybox to serve the setup gw purpose.
                if (message == null) {
                    message = ((Engine)Engine.getInstance()).copyBinary(Engine.DATA_FOLDER + "/bin/route", R.raw.route);
                }

                try {
                    ((Engine)Engine.getInstance()).chmodBin();
                } catch (Exception e) {
                    message = "Unable to change permission on binary files!";
                    Log.e(TAG, message, e);
                }

                // version
                if (message == null) {
                    ((Engine)Engine.getInstance()).copyBinary(Engine.DATA_FOLDER + "/conf/version", R.raw.version);
                }
                // text
                if (message == null) {
                    //((Engine)Engine.getInstance()).copyBinary(Engine.DATA_FOLDER + "/setting.txt", R.raw.setting);
                    //((Engine)Engine.getInstance()).copyBinary(Engine.DATA_FOLDER + "/maxid.txt", R.raw.maxid);
                    //((Engine)Engine.getInstance()).copyBinary(Engine.DATA_FOLDER + "/maxid_exit.txt", R.raw.maxid_exit);
                    ((Engine)Engine.getInstance()).copyBinary(Engine.DATA_FOLDER + "/conf/route.conf", R.raw.route_conf);
                }
                if (message == null) {
                    message = "Binaries and config-files installed!";
                    Log.d(TAG, message);
                }

                // Removing ols lan-config-file
                File lanConfFile = new File(Engine.DATA_FOLDER + "/conf/lan_network.conf");
                if (lanConfFile.exists()) {
                    lanConfFile.delete();
                }

                // Sending message
                //Message msg = new Message();
                //msg.obj = message;
                //((Engine)Engine.getInstance()).displayMessageHandler.sendMessage(msg);
            }
        }).start();
    }

    /*
         * this function dumps the securesetting to a txt file. If success, return true, otherwise false
         * do not use new thread to run it now.
         */
    private boolean dumpGlobalSettings(){
        String dumpGlobalSettings = "echo \'.dump global\' | " + this.DATA_FOLDER + "/bin/sqlite3 " +
                this.SETTING_DB_PATH + "settings.db  > " +
                this.DATA_FOLDER + "/setting.txt";
        Log.d(TAG, "command for dumping the GlobalSettings is: " + dumpGlobalSettings);
        if(RootCommands.run(20000, dumpGlobalSettings)==false){
            Log.e(TAG, "Unable to dump the GlobalSettings to " + this.DATA_FOLDER + "/setting.txt");
            File file = new File(Engine.DATA_FOLDER + "/setting.txt");
            if(!file.exists())
                return false;
        }
        if(RootCommands.run("chmod 666 " + Engine.DATA_FOLDER + "/setting.txt")==false){
            Log.e(TAG, "Unable to add permission to " + this.DATA_FOLDER + "/setting.txt");
            return false;
        }
        return true;
    }

    private boolean dumpGlobalSettingsMaxID(){
        String dumpGlobalSettingsMaxId = this.DATA_FOLDER + "/bin/sqlite3 " + this.SETTING_DB_PATH +
                "settings.db \'select max(_id) from global\'"+  " > " +
                this.DATA_FOLDER + "/maxid_exit.txt";
        Log.d(TAG, "command for dumping the max id is: " + dumpGlobalSettingsMaxId);
        if(RootCommands.run(dumpGlobalSettingsMaxId)==false){
            Log.e(TAG, "Unable to dump the global setting maxid to" + this.DATA_FOLDER + "/maxid_exit.txt");
            File file = new File(Engine.DATA_FOLDER + "/maxid_exit.txt");
            if(!file.exists())
                return false;
        }
        if(RootCommands.run("chmod 666 " + Engine.DATA_FOLDER + "/maxid_exit.txt")==false){
            Log.e(TAG, "Unable to add permission to " + this.DATA_FOLDER + "/maxid_exit.txt");
            return false;
        }
        return true;
    }

    /*
     * Check the setting.txt for particular stringSetting, if it is there, return true, otherwise false
     */
    private boolean checkGlobalSetting(String stringSetting) {
        String secureSettingDumpedFile = this.DATA_FOLDER+"/setting.txt"; //this is the dumped secure table
        boolean settingIsDefined = false;
        for (String line : SgsFileUtils.readLinesFromFile(secureSettingDumpedFile)) {
            if (line.contains(stringSetting) == true)
                settingIsDefined = true;

        }
        Log.d(TAG, stringSetting + " inside global table is defined: " + settingIsDefined);
        return settingIsDefined;
    }

    /*
     * check if particular stringSetting is enabled or not but without updating the settings.db
     * this function will run with the assumption that the checkSecureSetting is true.
     */
    public boolean globalSettingIsEnabled(String stringSetting, int iSetValue) {
        boolean enabled = false;
        String sSetValue = String.valueOf(iSetValue);
        String settingLine = null;
        String secureSettingDumpedFile = this.DATA_FOLDER + "/setting.txt"; //this is the dumped secure table
        for (String line : SgsFileUtils.readLinesFromFile(secureSettingDumpedFile)) {
            if (line.contains(stringSetting) && line.contains("\'"+sSetValue+"\'")){
                enabled = true;
                settingLine = line;
            }
        }
        Log.d(TAG, stringSetting + " inside global table is: " + settingLine);
        return enabled;
    }

    /*
     * parameter: setting to be enabled (has to pass the check setting first to use this function)
     * if enabled successfully, return true, otherwise return false
     */
    public synchronized boolean globalSettingEnable(String stringSetting, int iValue) {
        boolean enabled = false;
        String command;
        String secureSettingFile = this.SETTING_DB_PATH + "settings.db"; //this is the dumped secure table
        command = this.DATA_FOLDER + "/bin/sqlite3 "+ this.SETTING_DB_PATH + "settings.db "+ "\"update global set value ="+ String.valueOf(iValue) + "where name = " + "\'"+stringSetting +"\'"+"\"" ;
        Log.d(TAG, "command for enable globalSetting is : " + command);
        if(RootCommands.run(command)==false){
            Log.e(TAG, "Unable to enable the global Setting for the setting of " + stringSetting);
            return enabled;
        };
        enabled = true;
        return enabled;
    }

    /*
     * need to define another function to get max(_id).
     * sqlite3 settings.db "select max(_id) from secure" >> maxid.txt
     * so the function will read from maxid.txt and return maxid
     * return -1 if fail otherwise return maxid.
     */
    public synchronized int globalSettingMaxId() {
        int intMaxId = -1;
        int intTmpId;
        String command;
        String secureSettingFile = this.SETTING_DB_PATH+"settings.db"; //this is the dumped secure table
        command = this.DATA_FOLDER + "/bin/sqlite3 "+ this.SETTING_DB_PATH + "settings.db "+ "\'select max(_id) from global\'  > " + this.DATA_FOLDER + "/maxid.txt"; ;
        Log.d(TAG, "command for dump the max is: " + command);
        if(RootCommands.run(command)==false){
            Log.d(TAG, "Unable to get the global setting maxid from" + this.SETTING_DB_PATH + "/settings.db");
            File file = new File(Engine.DATA_FOLDER + "/maxid.txt");
            if(!file.exists())
                return -1;
        }
        if(RootCommands.run("chmod 666 " + Engine.DATA_FOLDER + "/maxid.txt")==false){
            Log.e(TAG, "Unable to add permission to " + this.DATA_FOLDER + "/maxid.txt");
            return -1;
        }

        String secureSettingMaxIdDumpedFile = this.DATA_FOLDER + "/maxid.txt"; //this is the dumped secure table
        for (String line : SgsFileUtils.readLinesFromFile(secureSettingMaxIdDumpedFile)) {
            intTmpId = Integer.parseInt(line);
            if (intTmpId > intMaxId)
                intMaxId = intTmpId;

        }
        Log.d(TAG, "max Id in global setting table is " + intMaxId);
        return intMaxId;
    }

    /*
     * parameters: setting to be inserted and intId to be used
     * return trun if succss, otherwise false
     * sqlite3 settings.db "insert into secure values(51,'test_insert',1 or 0)"
     */
    public synchronized boolean globalSettingInsertAndEnable(String stringSetting, int intId, int iValue) {
        boolean insertSuccess = false;
        String command;

        //String secureSettingFile = this.SETTING_DB_PATH+"settings.db"; //this is the dumped secure table
        command = this.DATA_FOLDER + "/bin/sqlite3 "+ this.SETTING_DB_PATH + "settings.db "+ "\'insert into global values(" +String.valueOf(intId)+","+ "\""+ stringSetting +"\","+ String.valueOf(iValue) + ")\'" ;
        Log.d(TAG, "command to insert" + stringSetting + "is :" + command);
        if(RootCommands.run(command)==false){
            Log.e(TAG, "Unable to insert " + stringSetting + " global Setting for the setting of" + stringSetting);
            return false;
        }
        insertSuccess = true;
        return insertSuccess;
    }

    public boolean startupCheck() {
        boolean startupCheckSuccess = false;

        boolean mSupportedKernel = false;

        this.checkDirs();

        if (!this.hasRootPermission()){
            mSupportedKernel = false;
        }

        if (this.binariesExists() == false) {
            if (this.hasRootPermission()) {
                this.installFiles();
            }
        }

        /*
	         * Add a function for get setting from settings.db to see if there is tether_supported
	         * need to write a function to dump the settings.db secure table into a file and
	         * read from this file to see if contains tether_supported. This function is defined
	         * inside CoreTask.java
	         */
        boolean dumpSettingSuccess = false;
        boolean setGlobalSettingFail = false;
        if (this.hasRootPermission()) {
            if ((this.dumpGlobalSettings()==false)||(this.dumpGlobalSettingsMaxID()==false)){
                //this.openFailToDumpSecureSettingDialog();
                dumpSettingSuccess = false;
            } else dumpSettingSuccess = true;
        }
        int iMaxId = -1;
        if(!this.checkGlobalSetting(this.mGlobalSetting_tether_supported)){
            iMaxId = this.globalSettingMaxId();
            if (iMaxId < 0){   //error occurs during retrieve maxId of secure table, return -1 if error
                Log.d(TAG, "cannot read system setting's max id during checking tether_supported...");
            } else {	//if there is no error to retrieve maxId, then insert with new name field and maxid
                iMaxId = iMaxId + 1;
                if(!this.globalSettingInsertAndEnable(this.mGlobalSetting_tether_supported, iMaxId, 1)){
                    setGlobalSettingFail = true;
                    Log.d(TAG, "cannot insert and enable " + this.mGlobalSetting_tether_supported );
                }
            }
        } else if(!this.globalSettingIsEnabled(this.mGlobalSetting_tether_supported, 1)){
            if(!this.globalSettingEnable(this.mGlobalSetting_tether_supported, 1)){
                setGlobalSettingFail = true;
                Log.d(TAG, "cannot enable " + this.mGlobalSetting_tether_supported);
            }
        } else {
            Log.d(TAG, this.mGlobalSetting_tether_supported + " has been enabled already");
        }

        if(!this.checkGlobalSetting(this.mGlobalSetting_tether_dun_required)){
            iMaxId = this.globalSettingMaxId();
            if (iMaxId < 0){   //error occurs during retrieve maxId of secure table, return -1 if error
                Log.d(TAG, "cannot read system setting's max id during checking tether_dun_required...");
            } else {	//if there is no error to retrieve maxId, then insert with new name field and maxid
                iMaxId = iMaxId + 1;
                if(!this.globalSettingInsertAndEnable(this.mGlobalSetting_tether_dun_required, iMaxId, 0)){
                    setGlobalSettingFail = true;
                    Log.d(TAG, "cannot insert and enable " + this.mGlobalSetting_tether_dun_required );
                }
            }
        } else if(!this.globalSettingIsEnabled(this.mGlobalSetting_tether_dun_required, 0)){
            if(!this.globalSettingEnable(this.mGlobalSetting_tether_dun_required, 0)){
                setGlobalSettingFail = true;
                Log.d(TAG, "cannot enable " + this.mGlobalSetting_tether_dun_required);
            }
        } else {
            Log.d(TAG, this.mGlobalSetting_tether_dun_required + " has been enabled already");
        }

        Log.d(TAG, "dumpSettingSuccess is:" + dumpSettingSuccess);
        Log.d(TAG, "setSecureSettingFail is:" + setGlobalSettingFail);
        if(dumpSettingSuccess && !setGlobalSettingFail){

            //Use reflection to retrieve the isTetheringSupported method from connnectivityManager
            ConnectivityManager cm = SgsApplication.getConnectivityManager();
            Method isTetheringSupportedLocal = null;
            Method getTetherableUsbRegexsLocal = null;  //added temp
            String [] tetherRegex;
            try {
                isTetheringSupportedLocal = cm.getClass().getMethod("isTetheringSupported");
                getTetherableUsbRegexsLocal = cm.getClass().getMethod("getTetherableUsbRegexs");
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //Use this high level general method to make judgment
            try {
                mSupportedKernel = (Boolean) isTetheringSupportedLocal.invoke(cm);
                tetherRegex = (String []) getTetherableUsbRegexsLocal.invoke(cm);
                Log.d(TAG, "supportedKernel value is:" + mSupportedKernel);

                //note: cannot use (boolean) isTetheringSupportedLocal.invoke(cm);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!mSupportedKernel) {
                //this.openUnsupportedKernelDialog();
            }
        }

        //TODO: those lines below for debug only
        int tetherEnabledInSettings = 0;
        try {
            tetherEnabledInSettings = Settings.Global.getInt(SgsApplication.getInstance().getContentResolver(),
                    "tether_supported");
        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d(TAG, "tetherEnabledInSettings is ..." + tetherEnabledInSettings);

        //SgsApplication.getInstance().checkForUpdate();

        //this.toggleStartStop();

        startupCheckSuccess = true;
        getConfigurationService().putBoolean(SgsConfigurationEntry.GENERAL_STARTUP_CHECK,
                !startupCheckSuccess);
        getConfigurationService().commit();

        return startupCheckSuccess;
    }
}
