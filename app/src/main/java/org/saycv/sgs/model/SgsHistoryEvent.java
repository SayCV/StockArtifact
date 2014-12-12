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

import java.util.Date;

import org.saycv.sgs.media.SgsMediaType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public abstract class SgsHistoryEvent  implements Comparable<SgsHistoryEvent> {
	
	public enum StatusType{
		Outgoing,
		Incoming,
		Missed,
		Failed,
        RADARS_ALL,
        RADARS_LATEST
	}
	
	// For performance reasons, do not use Date() class
	
	@Element(name = "type", required = true)
	protected SgsMediaType mMediaType;
	@Element(name = "start", required = true)
	protected long mStartTime;
	@Element(name = "end", required = true)
	protected long mEndTime;
	@Element(name = "remote", required = true)
	protected String mRemoteParty;
	@Element(name = "seen", required = true)
	protected boolean mSeen;
	@Element(name = "status", required = true)
	protected StatusType mStatus;
	
	private String mDisplayName;
	
	
	protected SgsHistoryEvent(SgsMediaType mediaType, String remoteParty){
		mMediaType = mediaType;
		mStartTime = new Date().getTime();
		mEndTime = mStartTime;
		mRemoteParty = remoteParty;
		mStatus = StatusType.Missed;
	}
	
	public void setStartTime(long time){
		mStartTime = time;
	}
	
	public long getStartTime(){
		return mStartTime;
	}
	
	public long getEndTime(){
		return mEndTime;
	}
	
	public void setEndTime(long time){
		mEndTime = time;
	}
	
	public SgsMediaType getMediaType(){
		return mMediaType;
	}
	
	public String getRemoteParty(){
		return mRemoteParty;
	}
	
	public void setRemoteParty(String remoteParty){
		mRemoteParty = remoteParty;
	}
	
	public boolean isSeen(){
		return mSeen;
	}
	
	public void setSeen(boolean seen){
		mSeen = seen;
	}
	
	public StatusType getStatus(){
		return mStatus;
	}
	
	public void setStatus(StatusType status){
		mStatus = status;
	}
	
	public void setDisplayName(String displayName){
		mDisplayName = displayName;
	}
	
	public String getDisplayName(){
		return mDisplayName;
	}
	
	@Override
	public int compareTo(SgsHistoryEvent another) {
		return (int)(mStartTime - another.mStartTime);
	}
}
