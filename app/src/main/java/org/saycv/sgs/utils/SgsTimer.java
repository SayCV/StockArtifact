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

package org.saycv.sgs.utils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.saycv.logger.Log;

public class SgsTimer extends Timer{
	private final static String TAG = SgsTimer.class.getCanonicalName();
	
	public SgsTimer(){
		super();
	}

	@Override
	public void cancel() {
		try{
			super.cancel();
		}
		catch(IllegalStateException ise){
			Log.w(TAG, ise.toString());
		}
	}

	@Override
	public void schedule(TimerTask task, Date when, long period) {
		try{
			super.schedule(task, when, period);
		}
		catch(IllegalStateException ise){
			Log.w(TAG, ise.toString());
		}
	}

	@Override
	public void schedule(TimerTask task, Date when) {
		try{
			super.schedule(task, when);
		}
		catch(IllegalStateException ise){
			Log.w(TAG, ise.toString());
		}
	}

	@Override
	public void schedule(TimerTask task, long delay, long period) {
		try{
			super.schedule(task, delay, period);
		}
		catch(IllegalStateException ise){
			Log.w(TAG, ise.toString());
		}
	}

	@Override
	public void schedule(TimerTask task, long delay) {
		try{
			super.schedule(task, delay);
		}
		catch(IllegalStateException ise){
			Log.w(TAG, ise.toString());
		}
	}
}
