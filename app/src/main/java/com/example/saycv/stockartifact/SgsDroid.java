package com.example.saycv.stockartifact;

import org.saycv.logger.Log;
import org.saycv.logger.LogConfiguration;
import org.saycv.sgs.SgsApplication;

public class SgsDroid extends SgsApplication {
    private final static String TAG = SgsDroid.class.getCanonicalName();

    private static final String DATA_FOLDER = String.format("/data/data/%s", MainActivity.class.getPackage().getName());

    public SgsDroid() {
        // Start log to file from here
        LogConfiguration.getInstance().setLoggerName(SgsDroid.class.getCanonicalName());
        LogConfiguration.getInstance().setFileName(String.format("%s/%s", SgsDroid.DATA_FOLDER, "SgsDroid.log"));
        LogConfiguration.getInstance().setInternalDebugging(true);
        //LogConfiguration.getInstance().setFilePattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        LogConfiguration.getInstance().setFilePattern("%msg%n");
        LogConfiguration.getInstance().configure();

        Log.d(TAG, "SgsDroid()");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
