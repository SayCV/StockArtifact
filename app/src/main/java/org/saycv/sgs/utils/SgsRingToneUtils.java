/*
 * Copyright (C) 2013, saycv.
 *
 * Copyright 2013 The saycv Project
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

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import org.saycv.logger.Log;
import org.saycv.sgs.SgsApplication;

import java.util.ArrayList;
import java.util.List;

public class SgsRingToneUtils {
    private final static String TAG = SgsRingToneUtils.class.getCanonicalName();

    protected static SgsRingToneUtils sInstance;
    private static Context mContext;

    public static SgsRingToneUtils getInstance(){
        if(sInstance == null){
            sInstance = new SgsRingToneUtils(SgsApplication.getContext());
        }
        return sInstance;
    }

    public SgsRingToneUtils(Context context){
        mContext = context;
    }

    public Ringtone getDefaultRingtone(int type){
        return RingtoneManager.getRingtone(mContext, RingtoneManager.getActualDefaultRingtoneUri(mContext, type));
    }

    public Uri getDefaultRingtoneUri(int type){
        return RingtoneManager.getActualDefaultRingtoneUri(mContext, type);
    }

    public List<Ringtone> getRingtoneList(int type){
        List<Ringtone> resArr = new ArrayList<Ringtone>();
        RingtoneManager manager = new RingtoneManager(mContext);
        manager.setType(type);
        Cursor cursor = manager.getCursor();
        int count = cursor.getCount();
        for(int i = 0 ; i < count ; i ++){
            resArr.add(manager.getRingtone(i));
        }
        return resArr;
    }

    public Ringtone getRingtone(int type,int pos){
        RingtoneManager manager = new RingtoneManager(mContext);
        manager.setType(type);
        return manager.getRingtone(pos);
    }

    public List<String> getRingtoneTitleList(int type){
        List<String> resArr = new ArrayList<String>();
        RingtoneManager manager = new RingtoneManager(mContext);
        manager.setType(type);
        Cursor cursor = manager.getCursor();
        if(cursor.moveToFirst()){
            do{
                resArr.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
            }while(cursor.moveToNext());
        }
        return resArr;
    }


    public String getRingtoneUriPath(int type,int pos,String def){
        RingtoneManager manager = new RingtoneManager(mContext);
        manager.setType(type);
        Uri uri = manager.getRingtoneUri(pos);
        return uri==null?def:uri.toString();
    }

    public Ringtone getRingtoneByUriPath(int type ,String uriPath){
        RingtoneManager manager = new RingtoneManager(mContext);
        manager.setType(type);
        Uri uri = Uri.parse(uriPath);
        return manager.getRingtone(mContext, uri);
    }
}
