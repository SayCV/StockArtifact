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

package org.saycv.sgs.services.impl;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.saycv.sgs.services.ISgsHttpClientService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**@page SgsHttpClientService_page HTTP/HTTPS Service
 * The HTTP/HTTPS service is used to send and retrieve data to/from remote server using HTTP/HTTPS protocol.
 */

/**
 * HTTP/HTTPS service.
 */
public class SgsHttpClientService extends SgsBaseService implements ISgsHttpClientService {
    private static final String TAG = SgsHttpClientService.class.getCanonicalName();

    private static final int sTimeoutConnection = 3000;
    private static final int sTimeoutSocket = 5000;

    private HttpClient mClient;

    public SgsHttpClientService() {
        super();
    }

    public static String getResponseAsString(HttpResponse resp) {
        String result = "";
        try {
            InputStream in = resp.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                str.append(line + "\n");
            }
            in.close();
            result = str.toString();
        } catch (Exception ex) {
            result = null;
        }
        return result;
    }

    @Override
    public boolean start() {
        Log.d(TAG, "Starting...");

        if (mClient == null) {
            mClient = new DefaultHttpClient();
            final HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, sTimeoutConnection);
            HttpConnectionParams.setSoTimeout(params, sTimeoutSocket);
            ((DefaultHttpClient) mClient).setParams(params);
            return true;
        }
        Log.e(TAG, "Already started");
        return false;
    }

    @Override
    public boolean stop() {
        if (mClient != null) {
            mClient.getConnectionManager().shutdown();
        }
        mClient = null;
        return true;
    }

    @Override
    public String get(String uri) {
        try {
            HttpGet getRequest = new HttpGet(uri);
            final HttpResponse resp = mClient.execute(getRequest);
            if (resp != null) {
                return getResponseAsString(resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String post(String uri, String contentUTF8, String contentType) {
        String result = null;
        try {
            HttpPost postRequest = new HttpPost(uri);
            final StringEntity entity = new StringEntity(contentUTF8, "UTF-8");
            if (contentType != null) {
                entity.setContentType(contentType);
            }
            postRequest.setEntity(entity);
            final HttpResponse resp = mClient.execute(postRequest);
            if (resp != null) {
                return getResponseAsString(resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public InputStream getBinary(String uri) {
        try {
            HttpGet getRequest = new HttpGet(uri);
            final HttpResponse resp = mClient.execute(getRequest);
            if (resp != null) {
                return resp.getEntity().getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
