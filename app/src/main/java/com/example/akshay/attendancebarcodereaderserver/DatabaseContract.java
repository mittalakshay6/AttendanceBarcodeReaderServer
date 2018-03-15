package com.example.akshay.attendancebarcodereaderserver;

import android.provider.BaseColumns;

public class DatabaseContract {
    private DatabaseContract(){
    }
    public static class DatabaseEntry implements BaseColumns{
        public static final String COL_REGNO="RegNum";
    }
}
