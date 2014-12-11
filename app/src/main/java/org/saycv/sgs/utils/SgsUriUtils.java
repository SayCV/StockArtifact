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

import org.saydroid.sgs.SgsEngine;
import org.saydroid.sgs.model.SgsContact;
import org.saydroid.tinyWRAP.SipUri;

//FIXME: THIS IS THE WORST CLASS YOU WILL FIND IN THE PROJECT ;)
public class SgsUriUtils {

	private final static long MAX_PHONE_NUMBER = 1000000000000L;
	private final static String INVALID_SIP_URI = "sip:invalid@open-ims.test";
	
	public static String getDisplayName(String uri){
		String displayname = null;
		if(!SgsStringUtils.isNullOrEmpty(uri)){
			SgsContact contact = SgsEngine.getInstance().getContactService().getContactByUri(uri);
			if(contact != null  && (displayname = contact.getDisplayName()) != null){
				return displayname;
			}
			
			final SipUri sipUri = new SipUri(uri);
			if(sipUri.isValid()){
				displayname = sipUri.getUserName();
				contact = SgsEngine.getInstance().getContactService().getContactByPhoneNumber(displayname);
				if(contact != null && !SgsStringUtils.isNullOrEmpty(contact.getDisplayName())){
					displayname = contact.getDisplayName();
				}
			}
			sipUri.delete();
		}
		
		return displayname == null ? uri : displayname;
	}
	
	public static String getUserName(String validUri){
		final SipUri sipUri = new SipUri(validUri);
		String userName = validUri;
		if(sipUri.isValid()){
			userName = sipUri.getUserName();
		}
		sipUri.delete();
		return userName;
	}
	
	public static boolean isValidSipUri(String uri){
		return SipUri.isValid(uri);
	}
	
	// Very very basic
	public static String makeValidSipUri(String uri){
		if(SgsStringUtils.isNullOrEmpty(uri)){
			return SgsUriUtils.INVALID_SIP_URI;
		}
		if(uri.startsWith("sip:") || uri.startsWith("sips:")){
			return uri.replace("#", "%23");
		}
		else if(uri.startsWith("tel:")){
			return uri;
		}
		else{
			if(uri.contains("@")){
				return String.format("sip:%s", uri);
			}
			else{
				String realm = SgsEngine.getInstance().getConfigurationService().getString(SgsConfigurationEntry.NETWORK_REALM, 
						SgsConfigurationEntry.DEFAULT_NETWORK_REALM);
				if(realm.contains(":")){
					realm = realm.substring(realm.indexOf(":")+1);
				}
				// FIXME: Should be done by saydroid
				return String.format("sip:%s@%s", 
						uri.replace("(", "").replace(")", "").replace("-", "").replace("#", "%23"), 
						realm);
			}
		}
	}
	
	public static SipUri makeValidSipUriObj(String uri){
		SipUri sipUri = new SipUri(makeValidSipUri(uri));
		if(sipUri.isValid()){
			return sipUri;
		}
		sipUri.delete();
		return null;
	}
	
	public static String getValidPhoneNumber(String uri){
		if(uri != null && (uri.startsWith("sip:") || uri.startsWith("sip:") || uri.startsWith("tel:"))){
			SipUri sipUri = new SipUri(uri);
			if(sipUri.isValid()){
				String userName = sipUri.getUserName();
				if(userName != null){
					try{
						String scheme = sipUri.getScheme();
						if(scheme != null && scheme.equals("tel")){
							userName = userName.replace("-", "");
						}
						long result = Long.parseLong(userName.startsWith("+") ? userName.substring(1) : userName);
						if(result < SgsUriUtils.MAX_PHONE_NUMBER){
							return userName;
						}
					}
					catch(NumberFormatException ne){ }
					catch (Exception e){
						e.printStackTrace();
					}
				}
			}
			sipUri.delete();
		}
		else{
			try{
				uri = uri.replace("-", "");
				long result = Long.parseLong(uri.startsWith("+") ? uri.substring(1) : uri);
				if(result < SgsUriUtils.MAX_PHONE_NUMBER){
					return uri;
				}
			}
			catch(NumberFormatException ne){ }
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
}
