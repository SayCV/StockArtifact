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

import android.util.Log;

import org.saycv.sgs.SgsApplication;
import org.saycv.sgs.services.ISgsStorageService;

/**@page SgsStorageService_page Storage Service
 * This service is used to manage storage functions.
 */

/**
 * Storage service.
 */
public class SgsStorageService extends SgsBaseService implements ISgsStorageService {
    private final static String TAG = SgsStorageService.class.getCanonicalName();

    private final String mCurrentDir;
    private final String mContentShareDir;

    public SgsStorageService() {
        mCurrentDir = String.format("/data/data/%s", SgsApplication.getContext().getPackageName());
        mContentShareDir = "/sdcard/sgsCommon";
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
    public String getCurrentDir() {
        return this.mCurrentDir;
    }

    @Override
    public String getContentShareDir() {
        return this.mContentShareDir;
    }
}
