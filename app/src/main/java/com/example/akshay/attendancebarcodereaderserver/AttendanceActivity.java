package com.example.akshay.attendancebarcodereaderserver;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = "AttendanceActivity";
    private LayoutInflater cursorInflator;
    private Spinner spinner_databaseName;
    private Spinner spinner_tableName;
    private String selected_dbName;
    private String selected_tableName;

    private boolean isDbNameSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        spinner_databaseName = findViewById(R.id.selectDatabaseNameSpinner);
        spinner_tableName = findViewById(R.id.selectTableSpinner);

        String dir = null;
        dir = this.getDatabasePath("a").getParent();
        File file = new File(dir);
        File[] files = file.listFiles();
        ArrayList<CharSequence> fileArrayList = new ArrayList<>();
        ArrayAdapter<CharSequence> fileArrayAdapter;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith("db")) {
                fileArrayList.add(files[i].getName());
            }
        }
        fileArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_text_layout, fileArrayList);
        spinner_databaseName.setAdapter(fileArrayAdapter);
        spinner_databaseName.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == spinner_databaseName.getId()) {
            selected_dbName = (String) parent.getItemAtPosition(position);
            Log.d(TAG, selected_dbName);
            Toast.makeText(this, selected_dbName, Toast.LENGTH_SHORT).show();
            DatabaseHelper databaseHelper = new DatabaseHelper(this, selected_dbName);
        }
        if(parent.getId() == spinner_tableName.getId()){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}