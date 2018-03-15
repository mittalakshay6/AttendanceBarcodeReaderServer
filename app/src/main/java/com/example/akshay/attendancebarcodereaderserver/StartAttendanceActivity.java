package com.example.akshay.attendancebarcodereaderserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class StartAttendanceActivity extends AppCompatActivity {

    TextView regNumView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startattendance);

        regNumView = findViewById(R.id.regNumView);


    }
}
