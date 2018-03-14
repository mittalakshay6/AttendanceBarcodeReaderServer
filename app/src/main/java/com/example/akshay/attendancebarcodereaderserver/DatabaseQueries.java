package com.example.akshay.attendancebarcodereaderserver;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatabaseQueries {

    private final String TAG = "DatabaseQueries";

    private String tableName;
    private boolean isTableNameSet=false;
    private String REG_NO;
    private String COL_DATE;

    //Queries

    private final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + this.tableName + " (" +
                    DatabaseContract.DatabaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DatabaseContract.DatabaseEntry.COL_REGNO + " INTEGER)";
    private final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + this.tableName;

    private final String SQL_RETURN_ALL_TABLES =
            "SELECT name FROM sqlite_master WHERE type='table'";

    private final String SQL_CREATE_TODAY_COL =
            "ALTER TABLE " + this.tableName + " ADD COLUMN " + COL_DATE +
                    " TEXT NOT NULL DEFAULT(A)";

    private final String SQL_MARK_P =
            "UPDATE " + this.tableName + " SET " + COL_DATE + " = 'p' " +
                    "WHERE " + DatabaseContract.DatabaseEntry.COL_REGNO + " == " + REG_NO;

    public void setTableName(String tableName) {
        this.tableName = tableName;
        isTableNameSet=true;
    }

    private String getDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }

    public void setCOL_DATE(){
        COL_DATE=getDate();
    }

    public boolean setREG_NO(String REG_NO) {
        this.REG_NO = REG_NO;
        return true;
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

    public String getSQL_RETURN_ALL_TABLES() {
        return  SQL_RETURN_ALL_TABLES;
    }

    public String getSQL_CREATE_TODAY_COL() {
        if(isTableNameSet()) {
            return SQL_CREATE_TODAY_COL;
        }
        else{
            Log.e(TAG, "New today column not created");
            return null;
        }
    }

    public String getSQL_MARK_P(String regNum) {
        if(isTableNameSet() && setREG_NO(regNum)) {
            return SQL_MARK_P;
        }
        else{
            Log.e(TAG, "Attendance not marked");
            return null;
        }
    }
}
