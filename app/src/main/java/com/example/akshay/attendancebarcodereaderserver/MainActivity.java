package com.example.akshay.attendancebarcodereaderserver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button takeAttendanceBtn;
    Button importDb;
    Button exportDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takeAttendanceBtn = findViewById(R.id.takeAttendanceBtn);
        importDb = findViewById(R.id.importBtn);
        exportDb = findViewById(R.id.exportBtn);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    4);
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