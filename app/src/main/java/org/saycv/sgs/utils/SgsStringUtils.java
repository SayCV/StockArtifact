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

import java.math.BigInteger;
import java.security.MessageDigest;

public class SgsStringUtils {
    private static MessageDigest sMD5Digest;

    public static String emptyValue() {
        return "";
    }

    public static String nullValue() {
        return "(null)";
    }

    public static boolean isNullOrEmpty(String s) {
        return ((s == null) || ("".equals(s)));
    }

    public static boolean startsWith(String s, String prefix, boolean ignoreCase) {
        if (s != null && prefix != null) {
            if (ignoreCase) {
                return s.toLowerCase().startsWith(prefix.toLowerCase());
            } else {
                return s.startsWith(prefix);
            }
        }
        return s == null && prefix == null;
    }

    public static boolean equals(String s1, String s2, boolean ignoreCase) {
        if (s1 != null && s2 != null) {
            if (ignoreCase) {
                return s1.equalsIgnoreCase(s2);
            } else {
                return s1.equals(s2);
            }
        } else {
            return ((s1 == null && s2 == null) ? true : false);
        }
    }

    public static String unquote(String s, String quote) {
        if (!SgsStringUtils.isNullOrEmpty(s) && !SgsStringUtils.isNullOrEmpty(quote)) {
            if (s.startsWith(quote) && s.endsWith(quote)) {
                return s.substring(1, s.length() - quote.length());
            }
        }
        return s;
    }

    public static String quote(String s, String quote) {
        if (!SgsStringUtils.isNullOrEmpty(s) && !SgsStringUtils.isNullOrEmpty(quote)) {
            return quote.concat(s).concat(quote);
        }
        return s;
    }

    public static long parseLong(String value, long defaultValue) {
        try {
            if (SgsStringUtils.isNullOrEmpty(value)) {
                return defaultValue;
            }
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static int parseInt(String value, int defaultValue) {
        try {
            if (SgsStringUtils.isNullOrEmpty(value)) {
                return defaultValue;
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static String getMD5(String str) {
        if (str != null) {
            try {
                final BigInteger bigInt;
                if (sMD5Digest == null) {
                    sMD5Digest = MessageDigest.getInstance("MD5");
                }
                synchronized (sMD5Digest) {
                    sMD5Digest.reset();
                    bigInt = new BigInteger(1, sMD5Digest.digest(str.getBytes("UTF-8")));
                }
                String hash = bigInt.toString(16);
                while (hash.length() < 32) {
                    hash = "0" + hash;
                }
                return hash;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    public static byte[] getMD5Digest(String str) {
        if (str != null) {
            try {
                if (sMD5Digest == null) {
                    sMD5Digest = MessageDigest.getInstance("MD5");
                }
                synchronized (sMD5Digest) {
                    sMD5Digest.reset();
                    return sMD5Digest.digest(str.getBytes("UTF-8"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    public static boolean isValidMD5String(String md5String) {
        if (md5String != null) {
            return md5String.length() == 32;
        }
        return false;
    }
}
