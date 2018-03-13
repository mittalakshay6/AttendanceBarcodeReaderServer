package com.example.akshay.attendancebarcodereaderserver;

import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatabaseQueries {

    private final String TAG = "DatabaseQueries";

    private String tableName;
    private boolean isTableNameSet=false;


    //get today's date for column name

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/mm/dd");
    LocalDate COL_DATE = LocalDate.now();


    //get reg number of current student connection

    Connection connection;
    String REG_NO = connection.getRegNo();

    
    //Queries

    private final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + this.tableName + " (" +
                    DatabaseContract.DatabaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DatabaseContract.DatabaseEntry.COL_REGNO + " INTEGER)";
    private final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + this.tableName;

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

    public String getSQL_CREATE_TODAY_COL() {
        if(isTableNameSet()) {
            return SQL_CREATE_TODAY_COL;
        }
        else{
            Log.e(TAG, "New today column not created");
            return null;
        }
    }

    public String getSQL_MARK_P() {
        if(isTableNameSet()) {
            return SQL_MARK_P;
        }
        else{
            Log.e(TAG, "Attendence not marked");
            return null;
        }
    }
}
