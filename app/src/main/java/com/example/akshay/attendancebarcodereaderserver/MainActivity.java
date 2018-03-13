package com.example.akshay.attendancebarcodereaderserver;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ajts.androidmads.library.ExcelToSQLite;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    TextView pathView;
    Button importBtn;
    Button takeAttendenceBtn;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;
    ExcelToSQLite excelToSQLite;
    String path;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}

