package com.example.akshay.attendancebarcodereaderserver;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseQueries dbQueries = DatabaseQueries.getInstance();

    private static final String TAG = "DatabaseHelper";
    private String databaseName;

    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        this.databaseName=databaseName;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(dbQueries.getSQL_CREATE_ENTRIES(dbQueries.getTableName()));
        Log.d(TAG, "SQL Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dbQueries.getSQL_DELETE_ENTRIES(dbQueries.getTableName()));
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void setTableName(String tableName){
        dbQueries.setTableName(tableName);
    }

    public Cursor all_tables(SQLiteDatabase db){
        return db.rawQuery(dbQueries.getSQL_RETURN_ALL_TABLES(), null);
    }
    public Cursor createCol(SQLiteDatabase db){
        return db.rawQuery(dbQueries.getSQL_CREATE_TODAY_COL(dbQueries.getTableName()), null);
    }

    public void mark_P(SQLiteDatabase db){
        //db.execSQL(dbQueries.getSQL_MARK_P());
        Log.d(TAG,"Attendence for current student marked");
    }
}