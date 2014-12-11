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

package org.saycv.logger;

import android.app.Activity;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

public final class Log {
    protected static Log sInstance = null;
	// private final static String TAG = Log.class.getCanonicalName();
    private final static String TAG_TOKEN = ":";

    public static org.slf4j.Logger sLogger = null;

    protected static Log getInstance(){
        if(sInstance == null){
            sInstance = new Log();
        }
        return sInstance;
    }

    protected Log() {
        sLogger = LogConfiguration.getInstance().getLogger();
    }

    protected static boolean checkDebuggingEnabled(){
        if(sLogger==null)
            sLogger = LogConfiguration.getInstance().getLogger();

        if(sLogger!=null && LogConfiguration.getInstance().isInternalDebugging())
            return true;

        return false;
    }

    public static void v(String tag, String msg) {
        if (checkDebuggingEnabled()) {
            sLogger.debug(tag + TAG_TOKEN +msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (checkDebuggingEnabled()) {
            sLogger.debug(tag + TAG_TOKEN +msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (checkDebuggingEnabled()) {
            sLogger.debug(tag + TAG_TOKEN +msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (checkDebuggingEnabled()) {
            sLogger.debug(tag + TAG_TOKEN +msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (checkDebuggingEnabled()) {
            sLogger.info(tag + TAG_TOKEN +msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (checkDebuggingEnabled()) {
            sLogger.info(tag + TAG_TOKEN +msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        sLogger.warn(tag + TAG_TOKEN +msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        sLogger.warn(tag + TAG_TOKEN +msg, tr);
    }

    public static void w(String tag, Throwable tr) {
        sLogger.warn(tag, tr);
    }

    public static void e(String tag, String msg) {
        sLogger.error(tag + TAG_TOKEN +msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        sLogger.error(tag + TAG_TOKEN +msg, tr);
    }
}
