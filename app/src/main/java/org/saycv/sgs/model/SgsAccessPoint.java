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

package org.saycv.sgs.model;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import org.saycv.sgs.services.impl.SgsNetworkService;
import org.saycv.sgs.utils.SgsObservableObject;
import org.saycv.sgs.utils.SgsStringUtils;

public class SgsAccessPoint extends SgsObservableObject {
    // Constants used for different security types
    public static final String AP_WPA2 = "WPA2";
    public static final String AP_WPA = "WPA";
    public static final String AP_WEP = "WEP";
    public static final String AP_OPEN = "Open";
    // For EAP Enterprise fields
    public static final String AP_WPA_EAP = "WPA-EAP";
    public static final String AP_IEEE8021X = "IEEE8021X";
    public static final String[] AP_SECURITY_MODES = {AP_WEP, AP_WPA, AP_WPA2, AP_WPA_EAP, AP_IEEE8021X};

    public static final int AP_WEP_PASSWORD_AUTO = 0;
    public static final int AP_WEP_PASSWORD_ASCII = 1;
    public static final int AP_WEP_PASSWORD_HEX = 2;

    private int mNetworkId;
    private String mSSID;
    private String mDescription;
    private boolean mConnected;
    private boolean mHasPassword;
    private boolean mOpen;
    private int mLevel;
    private WifiConfiguration mConf;
    private ScanResult mSR;

    public SgsAccessPoint() {
        mNetworkId = -1;
        mSSID = SgsStringUtils.emptyValue();
        mLevel = 0;
        mDescription = SgsStringUtils.emptyValue();
    }

    public SgsAccessPoint(int networkId, String SSID) {
        this();
        mNetworkId = networkId;
        mSSID = SSID;
    }

    public SgsAccessPoint(ScanResult sr) {
        this();
        if ((mSR = sr) != null) {
            mSSID = sr.SSID;
            mLevel = WifiManager.calculateSignalLevel(mSR.level,
                    SgsNetworkService.sWifiSignalValues.length);
            final String rs = getScanResultSecurity(mSR);
            mOpen = SgsStringUtils.equals(rs, AP_OPEN, false);
            mDescription = String.format("%s;%s",
                    rs,
                    mSR.capabilities);
        }
    }

    public SgsAccessPoint(WifiConfiguration conf) {
        this();
        if ((mConf = conf) != null) {
            mSSID = SgsStringUtils.unquote(mConf.SSID, "\"");
            mNetworkId = conf.networkId;
            mHasPassword = !SgsStringUtils.isNullOrEmpty(conf.preSharedKey);
        }
    }

    public static String getScanResultSecurity(ScanResult scanResult) {
        final String cap = scanResult.capabilities;
        for (int i = SgsAccessPoint.AP_SECURITY_MODES.length - 1; i >= 0; i--) {
            if (cap.contains(SgsAccessPoint.AP_SECURITY_MODES[i])) {
                return SgsAccessPoint.AP_SECURITY_MODES[i];
            }
        }

        return SgsAccessPoint.AP_OPEN;
    }

    public WifiConfiguration getConf() {
        return mConf;
    }

    public ScanResult getSR() {
        return mSR;
    }

    public int getNetworkId() {
        return mNetworkId;
    }

    public void setNetworkId(int networkId) {
        mNetworkId = networkId;
    }

    public String getSSID() {
        return mSSID;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        if (mLevel != level) {
            mLevel = level;
            super.setChangedAndNotifyObservers(this);
        }
    }

    public boolean isConfigured() {
        return (mNetworkId >= 0);
    }

    public boolean isConnected() {
        return mConnected;
    }

    public void setConnected(boolean connected) {
        if (mConnected != connected) {
            mConnected = connected;
            super.setChangedAndNotifyObservers(this);
        }
    }

    public boolean hasPassword() {
        return mHasPassword;
    }

    public boolean isOpen() {
        return mOpen;
    }
}
