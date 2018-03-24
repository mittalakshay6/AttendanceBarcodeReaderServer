package com.pyenoma.android.markp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
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
    private SQLiteDatabase sqLiteDatabase;
    private final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takeAttendanceBtn = findViewById(R.id.takeAttendanceBtn);
        importDb = findViewById(R.id.importBtn);
        exportDb = findViewById(R.id.exportBtn);
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE};

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 0);
        } else {
            // Permission has already been granted
            File dir = new File(Environment.getExternalStorageDirectory().toString()+File.separator+"AttendanceExcelSheets");
            dir.mkdir();
            DatabaseHelper databaseHelper = new DatabaseHelper(this, "start.db");
            databaseHelper.setTableName("testTable");
            sqLiteDatabase = databaseHelper.getWritableDatabase();
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
    public void onClickDeleteDbBtn(View view){
        Intent intent = new Intent(this, DeleteDatabaseActivity.class);
        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    File dir = new File(Environment.getExternalStorageDirectory().toString()+File.separator+"AttendanceExcelSheets");
                    dir.mkdir();
                    DatabaseHelper databaseHelper = new DatabaseHelper(this, "start.db");
                    databaseHelper.setTableName("testTable");
                    sqLiteDatabase = databaseHelper.getWritableDatabase();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}