package com.example.akshay.attendancebarcodereaderserver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatabaseQueries {

    private final String TAG = "DatabaseQueries";

    private static DatabaseQueries databaseQueries;
    private String tableName;
    private boolean isTableNameSet=false;
    private String COL_DATE="d";

    private DatabaseQueries(){
    }

    public static DatabaseQueries getInstance(){
        if(databaseQueries!=null){
            return databaseQueries;
        }
        else{
            databaseQueries=new DatabaseQueries();
            return databaseQueries;
        }
    }

    //Queries

    public String getSQL_CREATE_ENTRIES(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                DatabaseContract.DatabaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.DatabaseEntry.COL_REGNO + " INTEGER)";
    }
    public String getSQL_DELETE_ENTRIES(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    public String getSQL_RETURN_ALL_TABLES() {
        return "SELECT name FROM sqlite_master WHERE type='table'";
    }

    public String getSQL_CREATE_TODAY_COL(String tableName) {
        return "ALTER TABLE " + tableName + " ADD COLUMN " + COL_DATE +
                " TEXT NOT NULL DEFAULT A";
    }

    public String getSQL_MARK_P(String tableName, String regNum) {
        String date = getDate();
        return "UPDATE " + tableName + " SET " + date + " = 'P' " +
                "WHERE " + DatabaseContract.DatabaseEntry.COL_REGNO + " == " + regNum;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        isTableNameSet=true;
    }

    public String getTableName() {
        return tableName;
    }

    private String getDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }

    public boolean isTableNameSet() {
        return isTableNameSet;
    }
//
//    public String getSQL_CREATE_ENTRIES() {
//        if(isTableNameSet()) {
//            return SQL_CREATE_ENTRIES;
//        }
//        else{
//            Log.e(TAG, "Table Name not set");
//            return null;
//        }
//    }
//
//    public String getSQL_DELETE_ENTRIES() {
//        if(isTableNameSet()) {
//            return SQL_DELETE_ENTRIES;
//        }
//        else{
//            Log.e(TAG, "Table name not set");
//            return null;
//        }
//    }
//
//    public String getSQL_RETURN_ALL_TABLES() {
//        return  SQL_RETURN_ALL_TABLES;
//    }
//
//    public String getSQL_CREATE_TODAY_COL() {
//        if(isTableNameSet()) {
//            return SQL_CREATE_TODAY_COL;
//        }
//        else{
//            Log.e(TAG, "New today column not created");
//            return null;
//        }
//    }
//
//    public String getSQL_MARK_P(String regNum) {
//        if(isTableNameSet() && setREG_NO(regNum)) {
//            return SQL_MARK_P;
//        }
//        else{
//            Log.e(TAG, "Attendance not marked");
//            return null;
//        }
//    }
}
