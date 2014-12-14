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

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static android.text.format.DateUtils.FORMAT_NUMERIC_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_YEAR;
import static android.text.format.DateUtils.MINUTE_IN_MILLIS;
import android.text.format.DateUtils;

public class SgsDateTimeUtils {
    static final DateFormat sDefaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        return sDefaultDateFormat.format(cal.getTime());
    }

    public static Date parseDate(String date, DateFormat format) {
        if (!SgsStringUtils.isNullOrEmpty(date)) {
            try {
                return format == null ? sDefaultDateFormat.parse(date) : format.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new Date();
    }

    public static Date parseDate(String date) {
        return parseDate(date, null);
    }

    public static boolean isSameDay(Date d1, Date d2) {
        return d1.getTime() == d2.getTime();
    }

    public static String getDate(String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat(dateFormat, Locale.getDefault())
                .format(calendar.getTime());
    }

    public static String getDate(String dateFormat, long currentTimeMillis) {
        return new SimpleDateFormat(dateFormat, Locale.getDefault())
                .format(currentTimeMillis);
    }

    public static long getBuildDate(Context context) {

        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), 0);
            ZipFile zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("classes.dex");
            long time = ze.getTime();

            return time;

        } catch (Exception e) {
        }

        return 0l;
    }

    public static long getInstallDate(Context context) {

        try {
            PackageInfo pi = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            long time = pi.lastUpdateTime;

            return time;

        } catch (Exception e) {
        }

        return 0l;
    }

    /**
     * Get relative time for date
     *
     * @param date
     * @return relative time
     */
    public static CharSequence getRelativeTime(final Date date) {
        long now = System.currentTimeMillis();
        if (Math.abs(now - date.getTime()) > 60000)
            return DateUtils.getRelativeTimeSpanString(date.getTime(), now,
                    MINUTE_IN_MILLIS, FORMAT_SHOW_DATE | FORMAT_SHOW_YEAR
                            | FORMAT_NUMERIC_DATE);
        else
            return "just now";
    }
}
