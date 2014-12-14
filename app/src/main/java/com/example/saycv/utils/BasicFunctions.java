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

package com.example.saycv.utils;

import com.example.saycv.android.activities.SgsFragmentActivity;
import com.example.saycv.stockartifact.Engine;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class BasicFunctions {

    private static final String IMAGE_REQUEST_HASH = "http://www.gravatar.com/avatar/%s?s=40";

    public static String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; ++i) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            BasicFunctions.showException(e);
        }
        return "";
    }

    public static String buildGravatarURL(String email) {
        String hash = md5(email);
        String url = String.format(Locale.getDefault(), IMAGE_REQUEST_HASH,
                hash);
        return url;
    }

    public static ImageLoader getImageLoader() {
        return ((SgsFragmentActivity)Engine.getInstance().getMainActivity()).getImageLoader();
    }

    public static void showException(Throwable t) {
        ((Engine)Engine.getInstance()).showAppMessage(t.getMessage());
        t.printStackTrace();
    }

    public static void showException(Throwable t, int res) {
        ((Engine)Engine.getInstance()).showAppMessage(Engine.getInstance().getMainActivity().getString(res));
        t.printStackTrace();
    }

}
