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

package org.saycv.sgs.events;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Base class for all events
 */
public abstract class SgsEventArgs implements Parcelable {
	public static final String EXTRA_EMBEDDED = "EXTRA_" + SgsEventArgs.class.getCanonicalName();
	
	public SgsEventArgs(){
		super();
	}

	 protected SgsEventArgs(Parcel in) {
	      readFromParcel(in);
	 }
	 
    abstract protected void readFromParcel(Parcel in);
	    
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	abstract public void writeToParcel(Parcel dest, int flags);
}
