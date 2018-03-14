package com.example.akshay.attendancebarcodereaderserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DatabaseExportActivity extends AppCompatActivity {

    private EditText fileNameView;
    private Button exportBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_export);

        fileNameView=findViewById(R.id.fileNameView);
        exportBtn=findViewById(R.id.exportBtn);
    }
    public void onClickExportBtn(View view){
    }
}