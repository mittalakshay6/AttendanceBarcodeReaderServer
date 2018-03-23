package com.pyenoma.android.markp;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseObjects {

    //This is class is made for use only with attendanceActivity class and its further classes and activities

    private static DatabaseQueries databaseQueries;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase sqLiteDatabaseWritable;
    private static SQLiteDatabase sqLiteDatabaseReadable;

    private static boolean databaseQueriesSet;
    private static boolean databaseHelperSet;
    private static boolean sqliteDatabaseWritableSet;
    private static boolean sqliteDatabaseReadableSet;


    public static DatabaseQueries getDatabaseQueries() {
        return databaseQueries;
    }

    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public static SQLiteDatabase getSqLiteDatabaseWritable() {
        return sqLiteDatabaseWritable;
    }

    public static SQLiteDatabase getSqLiteDatabaseReadable() {
        return sqLiteDatabaseReadable;
    }

    public static void setDatabaseQueries(DatabaseQueries databaseQueries) {
        DatabaseObjects.databaseQueries = databaseQueries;
        setDatabaseQueriesSet(true);
    }

    public static void setDatabaseHelper(DatabaseHelper databaseHelper) {
        DatabaseObjects.databaseHelper = databaseHelper;
        setDatabaseHelperSet(true);
    }

    public static void setSqLiteDatabaseWritable(SQLiteDatabase sqLiteDatabaseWritable) {
        DatabaseObjects.sqLiteDatabaseWritable = sqLiteDatabaseWritable;
        setSqliteDatabaseWritableSet(true);
    }

    public static void setSqLiteDatabaseReadable(SQLiteDatabase sqLiteDatabaseReadable) {
        DatabaseObjects.sqLiteDatabaseReadable = sqLiteDatabaseReadable;
        setSqliteDatabaseReadableSet(true);
    }

    public static boolean isDatabaseQueriesSet() {
        return databaseQueriesSet;
    }

    private static void setDatabaseQueriesSet(boolean databaseQueriesSet) {
        DatabaseObjects.databaseQueriesSet = databaseQueriesSet;
    }

    public static boolean isDatabaseHelperSet() {
        return databaseHelperSet;
    }

    private static void setDatabaseHelperSet(boolean databaseHelperSet) {
        DatabaseObjects.databaseHelperSet = databaseHelperSet;
    }

    public static boolean isSqliteDatabaseWritableSet() {
        return sqliteDatabaseWritableSet;
    }

    private static void setSqliteDatabaseWritableSet(boolean sqliteDatabaseWritableSet) {
        DatabaseObjects.sqliteDatabaseWritableSet = sqliteDatabaseWritableSet;
    }

    public static boolean isSqliteDatabaseReadableSet() {
        return sqliteDatabaseReadableSet;
    }

    private static void setSqliteDatabaseReadableSet(boolean sqliteDatabaseReadableSet) {
        DatabaseObjects.sqliteDatabaseReadableSet = sqliteDatabaseReadableSet;
    }
}
