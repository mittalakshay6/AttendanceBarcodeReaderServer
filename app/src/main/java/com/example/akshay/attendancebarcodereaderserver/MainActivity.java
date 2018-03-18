package com.example.akshay.attendancebarcodereaderserver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Button takeAttendanceBtn;
    Button importDb;
    Button exportDb;
    private final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takeAttendanceBtn = findViewById(R.id.takeAttendanceBtn);
        importDb = findViewById(R.id.importBtn);
        exportDb = findViewById(R.id.exportBtn);
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};

        ActivityCompat.requestPermissions(this, permissions, 0);


        File dir = new File(Environment.getExternalStorageDirectory().toString()+File.separator+"AttendanceExcelSheets");
        if(!dir.exists()) {
            dir.mkdirs();
            Log.d(TAG, "Directory created");
        }
        else{
            Log.d(TAG, "Directory Created");
        }
    }
    public void onClickTakeAttendance(View view){
        Intent intent = new Intent(this, AttendanceActivity.class);
        startActivity(intent);
    }
    public void onClickImportDb(View View){
        Intent intent = new Intent(this, DatabaseImportActivity.class);
        startActivity(intent);
    }
    public void onClickExportDb(View View){
        Intent intent = new Intent(this, DatabaseExportActivity.class);
        startActivity(intent);
    }
}