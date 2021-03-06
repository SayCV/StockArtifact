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

/**
 * @page SgsConfigurationService_page Configuration Service
 * The configuration service is used to store the user preferences. All preferences saved using this service
 * are persistent which means that you can retrieve them when the application/device restarts. <br />
 * You should never create or start this service by yourself. <br />
 * An instance of this service could be retrieved like this:
 * @code final ISgsConfigurationService mConfigurationService = SgsEngine.getInstance().getConfigurationService();
 * @endcode
 */
public interface ISgsConfigurationService extends ISgsBaseService {
    /**
     * Puts a string value into the user storage space
     *
     * @param entry  the name of the preference to save
     * @param value  the value of the preference
     * @param commit whether to commit the changes
     * @return true if succeed and false otherwise
     * @sa @ref getString()
     */
    boolean putString(final String entry, String value, boolean commit);

    /**
     * Puts a string value into the user storage space without committing the change
     * You must call @ref commit() in order to commit the changes. You should only
     * use this function if you want to put many values before applying the changes.
     *
     * @param entry the name of the preference to save
     * @param value the value of the preference
     * @return true if succeed and false otherwise
     * @sa @ref getString() @ref commit()
     */
    boolean putString(final String entry, String value);

    /**
     * Puts a integer value into the user storage space
     *
     * @param entry  the name of the preference to save
     * @param value  the value of the preference
     * @param commit whether to commit the changes
     * @return true if succeed and false otherwise
     * @sa @ref getInt()
     */
    boolean putInt(final String entry, int value, boolean commit);

    /**
     * Puts an integer value into the user storage space without committing the change
     * You must call @ref commit() in order to commit the changes. You should only
     * use this function if you want to put many values before applying the changes.
     *
     * @param entry the name of the preference to save
     * @param value the value of the preference
     * @return true if succeed and false otherwise
     * @sa @ref getString() @ref commit()
     */
    boolean putInt(final String entry, int value);

    /**
     * Puts a float value into the user storage space
     *
     * @param entry  the name of the preference to save
     * @param value  the value of the preference
     * @param commit whether to commit the changes
     * @return true if succeed and false otherwise
     * @sa @ref getFloat()
     */
    boolean putFloat(final String entry, float value, boolean commit);

    /**
     * Puts a float value into the user storage space without committing the change
     * You must call @ref commit() in order to commit the changes. You should only
     * use this function if you want to put many values before applying the changes.
     *
     * @param entry the name of the preference to save
     * @param value the value of the preference
     * @return true if succeed and false otherwise
     * @sa @ref getString() @ref commit()
     */
    boolean putFloat(final String entry, float value);

    /**
     * Puts a boolean value into the user storage space
     *
     * @param entry  the name of the preference to save
     * @param value  the value of the preference
     * @param commit whether to commit the changes
     * @return true if succeed and false otherwise
     * @sa @ref getBoolean()
     */
    boolean putBoolean(final String entry, boolean value, boolean commit);

    /**
     * Puts a boolean value into the user storage space without committing the change
     * You must call @ref commit() in order to commit the changes. You should only
     * use this function if you want to put many values before applying the changes.
     *
     * @param entry the name of the preference to save
     * @param value the value of the preference
     * @return true if succeed and false otherwise
     * @sa @ref getString() @ref commit()
     */
    boolean putBoolean(final String entry, boolean value);

    /**
     * Gets a string value from the user storage. This value should be previously stored using @ref
     * putString().
     *
     * @param entry        the name of the preference for which to retrieve the value
     * @param defaultValue the default value to return if this function fails or the preference has never
     *                     been stored using @ref putString()
     * @return the value of the preference
     * @sa @ref putString()
     */
    String getString(final String entry, String defaultValue);

    /**
     * Gets an integer value from the user storage. This value should be previously stored using @ref
     * putInt().
     *
     * @param entry        the name of the preference for which to retrieve the value
     * @param defaultValue the default value to return if this function fails or the preference has never
     *                     been stored using @ref putInt()
     * @return the value of the preference
     * @sa @ref putInt()
     */
    int getInt(final String entry, int defaultValue);

    /**
     * Gets a float value from the user storage. This value should be previously stored using @ref
     * putFloat().
     *
     * @param entry        the name of the preference for which to retrieve the value
     * @param defaultValue the default value to return if this function fails or the preference has never
     *                     been strored using @ref putFloat()
     * @return the value of the preference
     * @sa @ref putFloat()
     */
    float getFloat(final String entry, float defaultValue);

    /**
     * Gets a boolean value from the user storage. This value should be previously stored using @ref
     * putBoolean().
     *
     * @param entry        the name of the preference for which to retrieve the value
     * @param defaultValue the default value to return if this function fails or the preference has never
     *                     been stored using @ref putBoolean()
     * @return the value of the preference
     * @sa @ref putBoolean()
     */
    boolean getBoolean(final String entry, boolean defaultValue);

    /**
     * Commits all pending changes
     *
     * @return true if succeed and false otherwise
     */
    boolean commit();
}
