package com.example.saycv.stockartifact;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.saycv.stockartifact.service.IRadarsService;
import com.example.saycv.stockartifact.service.impl.RadarsService;

import org.saycv.logger.Log;
import org.saycv.sgs.SgsEngine;
import org.saycv.sgs.SgsNativeService;

public class Engine extends SgsEngine {
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
    public Handler displayMessageHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                showAppMessage((String) msg.obj);
            }
            super.handleMessage(msg);
        }
    };
    //private ITetheringService mTetheringService;
    //private IScreenService mScreenService;
    protected IRadarsService mRadarsService;
    static {
        // See 'http://code.google.com/p/imsdroid/issues/detail?id=197' for more information
        // Load Android utils library (required to detect CPU features)
        //System.load(String.format("%s/%s", Engine.LIBS_FOLDER, "libutils_armv5te.so"));
        //Log.d(TAG,"CPU_Feature="+AndroidUtils.getCpuFeatures());
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

    public Engine() {
        super();
    }

    public static SgsEngine getInstance() {
        if (sInstance == null) {
            sInstance = new Engine();
        }
        return sInstance;
    }

    @Override
    public boolean start() {
        boolean result;
        result = super.start();
        // we can use getConfigurationService() after call SgsEngine.Start()

        return result;
    }

    @Override
    public boolean stop() {
        return super.stop();
    }

    // Display Toast-Message
    public void showAppMessage(String message) {
        Toast.makeText(SgsDroid.getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void showNotification(int notifId, int drawableId, String tickerText) {
        if (!mStarted) {
            return;
        }

    }

    public void showAppNotif(int drawableId, String tickerText) {
        Log.d(TAG, "showAppNotif");
        showNotification(NOTIF_APP_ID, drawableId, tickerText);
    }


    public void showSMSNotif(int drawableId, String tickerText) {
        showNotification(NOTIF_SMS_ID, drawableId, tickerText);
    }

    public IRadarsService getRadarsService() {
        if (mRadarsService == null) {
            mRadarsService = new RadarsService();
        }
        return mRadarsService;
    }

    @Override
    public Class<? extends SgsNativeService> getNativeServiceClass() {
        return NativeService.class;
    }

}
