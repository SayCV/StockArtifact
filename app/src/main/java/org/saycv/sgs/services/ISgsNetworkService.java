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

package org.saycv.sgs.services;

import org.saydroid.sgs.model.SgsAccessPoint;
import org.saydroid.sgs.services.impl.SgsNetworkService.DNS_TYPE;
import org.saydroid.sgs.utils.SgsObservableList;

public interface ISgsNetworkService extends ISgsBaseService{
	String getDnsServer(DNS_TYPE type);
	String getLocalIP(boolean ipv6);
	boolean isScanning();
	boolean setNetworkEnabledAndRegister();
	boolean setNetworkEnabled(String SSID, boolean enabled, boolean force);
	boolean setNetworkEnabled(int networkId, boolean enabled, boolean force);
	boolean forceConnectToNetwork();
	SgsObservableList<SgsAccessPoint> getAccessPoints();
	int configure(SgsAccessPoint ap, String password, boolean bHex);
	boolean scan();
	boolean acquire();
	boolean release();
}
