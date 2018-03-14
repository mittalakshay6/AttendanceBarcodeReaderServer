package com.example.akshay.attendancebarcodereaderserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class AttendanceActivity extends AppCompatActivity {

    Button starttakeAttendanceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        starttakeAttendanceBtn = findViewById(R.id.starttakeAttendanceBtn);
       /* Spinner selectbatch_spinner = findViewById(R.id.selectbatch_spinner);
        ArrayAdapter<CharSequence> selectbatch_adapter =ArrayAdapter.createFromResource(this,
                R.array.selectbatch,android.R.layout.simple_spinner_item);
        selectbatch_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectbatch_spinner.setAdapter(selectbatch_adapter);
    */

    }
    public void onClickStartTakeAttendance(View view){
        Intent intent = new Intent(this, StartAttendanceActivity.class);
        startActivity(intent);
    }


}
