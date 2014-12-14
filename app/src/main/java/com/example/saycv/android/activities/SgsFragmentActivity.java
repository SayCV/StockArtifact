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

package com.example.saycv.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.saycv.stockartifact.R;
import com.example.saycv.utils.BasicFunctions;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.saycv.logger.Log;
import org.saycv.logger.LogConfiguration;

import java.io.File;

public class SgsFragmentActivity extends Activity {
		
		private final static String TAG = SgsFragmentActivity.class.getCanonicalName();
		
		private final static String myAppCanonicalName =
                SgsFragmentActivity.class.getCanonicalName();
		private final static String myAppPackageName =
                SgsFragmentActivity.class.getPackage().getName();
    
    public static interface OnBackClickListener {
        public boolean onClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "onCreate()");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mImageLoader != null && mImageLoader.isInited()) {
            mImageLoader.destroy();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }

    /* Switch Actvity Animation Start */
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        forwardTransition();
    }

    public void finish() {
        super.finish();
        backTransition();
    }

    public void rawfinish() {
        super.finish();
    }

    public void forwardTransition() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public void backTransition() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    /* Switch Actvity Animation End */

    /* ImageCache Start */

    private static final int SIZE = 100 << 20;
    private ImageLoader mImageLoader;

    private void setupImageLoader() {
        DisplayImageOptions mDiskCacheOption = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true).build();
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(mDiskCacheOption)
                .discCache(new TotalSizeLimitedDiscCache(cacheDir, SIZE))
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(configuration);
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader == null || !mImageLoader.isInited()) {
            setupImageLoader();
        }
        return mImageLoader;
    }
    /* ImageCache End */
}
