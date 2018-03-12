package com.example.akshay.attendancebarcodereaderserver;

import android.util.Log;

public class DatabaseQueries {

    private String TAG = "DatabaseQueries";

    private String tableName;
    private boolean isTableNameSet=false;

    private String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + this.tableName + " (" +
                    DatabaseContract.DatabaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DatabaseContract.DatabaseEntry.COL_REGNO + " INTEGER)";
    private String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + this.tableName;

    public void setTableName(String tableName) {
        this.tableName = tableName;
        isTableNameSet=true;
    }
    public boolean isTableNameSet() {
        return isTableNameSet;
    }

    public String getSQL_CREATE_ENTRIES() {
        if(isTableNameSet()) {
            return SQL_CREATE_ENTRIES;
        }
        else{
            Log.e(TAG, "Table Name not set");
            return null;
        }
    }

    public String getSQL_DELETE_ENTRIES() {
        if(isTableNameSet()) {
            return SQL_DELETE_ENTRIES;
        }
        else{
            Log.e(TAG, "Table name not set");
            return null;
        }
    }
}
