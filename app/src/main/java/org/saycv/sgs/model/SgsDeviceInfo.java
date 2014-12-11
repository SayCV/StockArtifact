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

public class SgsDeviceInfo {
	private String mLang;
	private String mCountry;
	private Date mDate;
	private Orientation mOrientation;

	public enum Orientation {
		PORTRAIT,
		LANDSCAPE
	};
	
	public SgsDeviceInfo(String lang, String country, Date date, Orientation orientation){
		mLang = lang;
		mCountry = country;
		mDate = date;
		mOrientation = orientation;
	}
	
	public SgsDeviceInfo(){
		this(null, null, null, Orientation.PORTRAIT);
	}
	
	public String getLang(){
		return mLang;
	}
	
	public void setLang(String lang){
		mLang = lang;
	}
	
	public String getCountry(){
		return mCountry;
	}
	
	public void setCountry(String country){
		mCountry = country;
	}
	
	public Date getDate(){
		return mDate;
	}
	
	public void setDate(Date date){
		mDate = date;
	}
	
	public Orientation getOrientation(){
		return mOrientation;
	}
	
	public void setOrientation(Orientation orientation){
		mOrientation = orientation;
	}
}
