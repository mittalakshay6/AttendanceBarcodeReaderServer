package com.example.akshay.attendancebarcodereaderserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DatabaseImportActivity extends AppCompatActivity {

    TextView pathView;
    Button browseBtn;
    Button importDatabaseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_import);

        pathView = findViewById(R.id.pathView);
        browseBtn = findViewById(R.id.browseBtn);
        importDatabaseBtn = findViewById(R.id.importBtn);

        importDatabaseBtn.setVisibility(View.INVISIBLE);
    }

    public void onClickBrowseBtn(View view){
    }

    public void onClickImportDatabaseBtn(View View){
    }
}
