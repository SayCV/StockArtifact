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

package com.example.saycv.stockartifact.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.saycv.stockartifact.model.Radar;

import org.saycv.sgs.events.SgsEventArgs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all events
 */
public class RadarsEventArgs extends SgsEventArgs {

    private RadarsEventTypes mEventType;
    // TransactionTooLargeException
    // private List<Radar> mRadarsData;
    private int mRadarsNumber;

    public static final String EXTRA_EMBEDDED = SgsEventArgs.EXTRA_EMBEDDED;

    private final static String TAG = RadarsEventArgs.class.getCanonicalName();

    public static final String ACTION_RADARS_EVENT = TAG + ".ACTION_RADARS_EVENT";
    public static final String ACTION_RADARS_INDEX = TAG + ".ACTION_RADARS_EVENT";

    public static final String EXTRA_REMOTE_PARTY = TAG + "from";
    public static final String EXTRA_DATA = TAG + "data";
    public static final String EXTRA_DATE = TAG + "date";



    public RadarsEventArgs(RadarsEventTypes type) {
        super();
        mEventType = type;
    }

    public RadarsEventArgs(RadarsEventTypes type, int radarsNumber) {
        super();
        mEventType = type;
        //mRadarsData = radarsData;
        mRadarsNumber = radarsNumber;
    }

    public RadarsEventArgs(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<RadarsEventArgs> CREATOR = new Parcelable.Creator<RadarsEventArgs>() {
        public RadarsEventArgs createFromParcel(Parcel in) { return new RadarsEventArgs(in); }

        public RadarsEventArgs[] newArray(int size) {
            return new RadarsEventArgs[size];
        }
    };

    public RadarsEventTypes getEventType() {
        return mEventType;
    }

   public int getRadarsNumber() {
        return mRadarsNumber;
   }

    @Override
    protected void readFromParcel(Parcel in) {
        mEventType = Enum.valueOf(RadarsEventTypes.class, in.readString());
        //mRadarsData = in.readArrayList(Radar.class.getClassLoader());
        mRadarsNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mEventType.toString());
        //dest.writeList( mRadarsData);
        dest.writeInt(mRadarsNumber);
    }
}
