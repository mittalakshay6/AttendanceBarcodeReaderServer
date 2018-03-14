package com.example.akshay.attendancebarcodereaderserver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    DatabaseQueries dbQueries;

    private static final String TAG = "DatabaseHelper";

    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context, String tableName) {
        super(context, String.valueOf(R.string.DATABASE_NAME), null, DATABASE_VERSION);
        dbQueries = new DatabaseQueries();
        dbQueries.setTableName(tableName);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(dbQueries.getSQL_CREATE_ENTRIES());
        Log.d(TAG, "SQL Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dbQueries.getSQL_DELETE_ENTRIES());
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void all_tables(SQLiteDatabase db){
        db.execSQL(dbQueries.getSQL_RETURN_ALL_TABLES());
        Log.d(TAG,"All table names returned");
    }
    public void createCol(SQLiteDatabase db){
        db.execSQL(dbQueries.getSQL_CREATE_TODAY_COL());
        Log.d(TAG,"New column for today created");
    }

    public void mark_P(SQLiteDatabase db){
        //db.execSQL(dbQueries.getSQL_MARK_P());
        Log.d(TAG,"Attendence for current student marked");
    }
}