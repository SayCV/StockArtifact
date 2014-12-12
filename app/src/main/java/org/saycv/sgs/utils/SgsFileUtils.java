/*
 * Copyright (C) 2013, saycv.
 *
 * Copyright 2013 The saycv Project
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

import org.saycv.logger.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class SgsFileUtils {
    private final static String TAG = SgsFileUtils.class.getCanonicalName();

    public static ArrayList<String> readLinesFromFile(String filename) {
        String line = null;
        BufferedReader br = null;
        InputStream ins = null;
        ArrayList<String> lines = new ArrayList<String>();
        android.util.Log.d(TAG, "Reading lines from file: " + filename);
        try {
            ins = new FileInputStream(new File(filename));
            br = new BufferedReader(new InputStreamReader(ins), 8192);
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (Exception e) {
            Log.d(TAG, "Unexpected error - Here is what I know: " + e.getMessage());
        } finally {
            try {
                ins.close();
                br.close();
            } catch (Exception e) {
                // Nothing.
            }
        }
        return lines;
    }

    public boolean writeLinesToFile(String filename, String lines) {
        OutputStream out = null;
        boolean returnStatus = false;
        Log.d(TAG, "Writing " + lines.length() + " bytes to file: " + filename);
        try {
            out = new FileOutputStream(filename);
            out.write(lines.getBytes());
        } catch (Exception e) {
            Log.d(TAG, "Unexpected error - Here is what I know: " + e.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();
                returnStatus = true;
            } catch (IOException e) {
                returnStatus = false;
            }
        }
        return returnStatus;
    }
}
