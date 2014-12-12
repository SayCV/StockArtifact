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

import java.io.File;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

/**
 * Configures the Log4j logging framework.
 * See <a href="http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html">Patterns</a> for pattern layout.
 *
 * @author Rolf Kulemann
 */
public final class LogConfiguration {
    protected static LogConfiguration sInstance = null;
    private static LoggerContext mLoggerContext = null;
    private static ch.qos.logback.classic.Logger mLoggerRoot = null;
    private static org.slf4j.Logger mLogger = null;
    private static Activity mMainActivity = null;
    private static String mLoggerName = "ROOT";
    private static String mLogFileName = "sgsLog.log";
    // private String mFilePattern = "%d - [%p::%c::%C] - %m%n";
    // private String mLogCatPattern = "%m%n";
    private String mFilePattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
    private String mLogcatPattern = "[%thread] %msg%n";
    private int mMaxBackupSize = 5;
    private long mMaxFileSize = 512 * 1024;

    private boolean mImmediateFlush = true;
    private boolean mUseLogcatAppender = true;
    private boolean mUseFileAppender = true;
    private boolean mResetConfiguration = true;
    private boolean mInternalDebugging = false;

    public LogConfiguration() {
    }

    /**
     * @param loggerName Name of the log file
     */
    public LogConfiguration(final String loggerName) {
        this();
        setLoggerName(loggerName);
    }

    /**
     * @param loggerName Name of the logger
     * @param fileName   Name of the log file
     */
    public LogConfiguration(final String loggerName, final String fileName) {
        this(loggerName);
        setFileName(loggerName);
    }

    /**
     * @param loggerName  Name of the logger
     * @param fileName    Name of the log file
     * @param filePattern Log pattern for the file appender
     */
    public LogConfiguration(final String loggerName, final String fileName, final String filePattern) {
        this(loggerName, fileName);
        setFilePattern(filePattern);
    }

    /**
     * @param loggerName    Name of the logger
     * @param fileName      Name of the log file
     * @param maxBackupSize Maximum number of backed up log files
     * @param maxFileSize   Maximum size of log file until rolling
     * @param filePattern   Log pattern for the file appender
     */
    public LogConfiguration(final String loggerName, final String fileName,
                            final int maxBackupSize, final long maxFileSize, final String filePattern) {
        this(loggerName, fileName, filePattern);
        setMaxBackupSize(maxBackupSize);
        setMaxFileSize(maxFileSize);
    }

    public static LogConfiguration getInstance() {
        if (sInstance == null) {
            sInstance = new LogConfiguration();
        }
        return sInstance;
    }

    public void configure() {
        mLogger = LoggerFactory.getLogger(getLoggerName());
        mLoggerRoot = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(mLogger.ROOT_LOGGER_NAME);
        mLoggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // reset the default context (which may already have been initialized)
        // since we want to reconfigure it
        if (isResetConfiguration()) {
            getLoggerContext().reset();

            File logFile = new File(getFileName());
            if (logFile.exists() == true) {
                if (logFile.length() > 20 * 1024) // 20KB
                    logFile.delete();
            }
        }

        // setInternalDebugging(isInternalDebugging());

        // setup FileAppender
        if (isUseFileAppender()) {
            configureFileAppender();
        }

        // setup LogcatAppender
        if (isUseLogcatAppender()) {
            configureLogcatAppender();
        }
    }

    private void configureFileAppender() {
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
        PatternLayoutEncoder fileAppenderEncoder = new PatternLayoutEncoder();

        fileAppenderEncoder.setContext(getLoggerContext());
        fileAppenderEncoder.setPattern(getFilePattern());
        fileAppenderEncoder.start();

        fileAppender.setContext(getLoggerContext());
        fileAppender.setFile(getFileName());
        fileAppender.setEncoder(fileAppenderEncoder);
        fileAppender.start();

        mLoggerRoot.addAppender(fileAppender);
    }

    private void configureLogcatAppender() {
        LogcatAppender logcatAppender = new LogcatAppender();
        PatternLayoutEncoder logcatAppenderEncoder = new PatternLayoutEncoder();

        logcatAppenderEncoder.setContext(getLoggerContext());
        logcatAppenderEncoder.setPattern(getLogcatPattern());
        logcatAppenderEncoder.start();

        logcatAppender.setContext(getLoggerContext());
        logcatAppender.setEncoder(logcatAppenderEncoder);
        logcatAppender.start();

        mLoggerRoot.addAppender(logcatAppender);
    }

    public org.slf4j.Logger getLogger() {
        return mLogger;
    }

    public LoggerContext getLoggerContext() {
        return mLoggerContext;
    }

    public String getFilePattern() {
        return mFilePattern;
    }

    public void setFilePattern(final String filePattern) {
        this.mFilePattern = filePattern;
    }

    public String getLogcatPattern() {
        return mLogcatPattern;
    }

    public void setLogcatPattern(final String logCatPattern) {
        this.mLogcatPattern = logCatPattern;
    }

    public String getLoggerName() {
        return mLoggerName;
    }

    public void setLoggerName(final String loggerName) {
        mLoggerName = loggerName;
    }

    /**
     * Returns the name of the log file
     *
     * @return the name of the log file
     */
    public String getFileName() {
        return mLogFileName;
    }

    /**
     * Sets the name of the log file
     *
     * @param fileName Name of the log file
     */
    public void setFileName(final String fileName) {
        this.mLogFileName = fileName;
    }

    /**
     * Returns the maximum number of backed up log files
     *
     * @return Maximum number of backed up log files
     */
    public int getMaxBackupSize() {
        return mMaxBackupSize;
    }

    /**
     * Sets the maximum number of backed up log files
     *
     * @param maxBackupSize Maximum number of backed up log files
     */
    public void setMaxBackupSize(final int maxBackupSize) {
        this.mMaxBackupSize = maxBackupSize;
    }

    /**
     * Returns the maximum size of log file until rolling
     *
     * @return Maximum size of log file until rolling
     */
    public long getMaxFileSize() {
        return mMaxFileSize;
    }

    /**
     * Sets the maximum size of log file until rolling
     *
     * @param maxFileSize Maximum size of log file until rolling
     */
    public void setMaxFileSize(final long maxFileSize) {
        this.mMaxFileSize = maxFileSize;
    }

    public boolean isImmediateFlush() {
        return mImmediateFlush;
    }

    public void setImmediateFlush(final boolean immediateFlush) {
        this.mImmediateFlush = immediateFlush;
    }

    /**
     * Returns true, if FileAppender is used for logging
     *
     * @return True, if FileAppender is used for logging
     */
    public boolean isUseFileAppender() {
        return mUseFileAppender;
    }

    /**
     * @param useFileAppender the useFileAppender to set
     */
    public void setUseFileAppender(final boolean useFileAppender) {
        this.mUseFileAppender = mUseFileAppender;
    }

    /**
     * Returns true, if LogcatAppender should be used
     *
     * @return True, if LogcatAppender should be used
     */
    public boolean isUseLogcatAppender() {
        return mUseLogcatAppender;
    }

    /**
     * If set to true, LogcatAppender will be used for logging
     *
     * @param useLogcatAppender If true, LogcatAppender will be used for logging
     */
    public void setUseLogcatAppender(final boolean useLogcatAppender) {
        this.mUseLogcatAppender = useLogcatAppender;
    }

    /**
     * Resets the log4j configuration before applying this configuration. Default is true.
     *
     * @return True, if the log4j configuration should be reset before applying this configuration.
     */
    public boolean isResetConfiguration() {
        return mResetConfiguration;
    }

    public void setResetConfiguration(boolean resetConfiguration) {
        this.mResetConfiguration = resetConfiguration;
    }

    public boolean isInternalDebugging() {
        return mInternalDebugging;
    }

    public void setInternalDebugging(boolean internalDebugging) {
        this.mInternalDebugging = internalDebugging;
    }
}