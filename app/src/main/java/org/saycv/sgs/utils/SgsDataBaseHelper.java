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
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SgsDataBaseHelper {
    protected static final String TAG = SgsDataBaseHelper.class.getCanonicalName();

    private final Context mContext;
    private final String mDataBaseName;
    private final int mDataBaseVersion;
    private final SgsDataBaseOpenHelper mDataBaseOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public SgsDataBaseHelper(Context context, String dataBaseName, int dataBaseVersion, String[][] createTableSt) {
        mContext = context;
        mDataBaseName = dataBaseName;
        mDataBaseVersion = dataBaseVersion;

        mDataBaseOpenHelper = new SgsDataBaseOpenHelper(mContext, mDataBaseName, mDataBaseVersion, createTableSt);
        mSQLiteDatabase = mDataBaseOpenHelper.getWritableDatabase();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public boolean close() {
        try {
            if (mSQLiteDatabase != null) {
                mSQLiteDatabase.close();
                mSQLiteDatabase = null;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isFreshDataBase() {
        return mDataBaseOpenHelper.isFreshDataBase();
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return mSQLiteDatabase;
    }

    public boolean deleteAll(String table, String whereClause, String[] whereArgs) {
        try {
            mSQLiteDatabase.delete(table, whereClause, whereArgs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAll(String table) {
        return deleteAll(table, null, null);
    }


    static class SgsDataBaseOpenHelper extends SQLiteOpenHelper {
        private final String[][] mCreateTableSt;
        boolean mFreshDataBase;

        SgsDataBaseOpenHelper(Context context, String dataBaseName, int dataBaseVersion, String[][] createTableSt) {
            super(context, dataBaseName, null, dataBaseVersion);
            mCreateTableSt = createTableSt;
        }

        private boolean isFreshDataBase() {
            return mFreshDataBase;
        }

        private boolean createDataBase(SQLiteDatabase db) {
            mFreshDataBase = true;
            if (mCreateTableSt != null) {
                for (String st[] : mCreateTableSt) {
                    try {
                        db.execSQL(String.format("CREATE TABLE %s(%s)", st[0], st[1]));
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "SgsDataBaseOpenHelper.onCreate()");
            createDataBase(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "SgsDataBaseOpenHelper.onUpgrade(" + oldVersion + "," + newVersion + ")");
            if (mCreateTableSt != null) {
                for (String st[] : mCreateTableSt) {
                    try {
                        db.execSQL(String.format("DROP TABLE IF EXISTS %s", st[0]));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            createDataBase(db);
        }
    }
}
