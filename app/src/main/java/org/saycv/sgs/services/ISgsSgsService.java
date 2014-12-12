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

import android.content.Context;


public interface ISgsSgsService extends ISgsBaseService {
    String getDefaultIdentity();

    void setDefaultIdentity(String identity);

    /**
     * Checks whether we are already registered or not.
     *
     * @return
     */
    boolean isRegistered();

    /**
     * Sends a Sip REGISTER request to the Proxy-CSCF
     *
     * @param context the context associated to this request. Could be null.
     * @return true if succeed and false otherwise
     * @sa @ref unRegister()
     */
    boolean register(Context context);

    /**
     * Deregisters the user by sending a Sip REGISTER request with an expires value equal to zero
     *
     * @return true if succeed and false otherwise
     * @sa register
     */
    boolean unRegister();

    boolean PresencePublish();
}
