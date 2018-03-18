package com.example.akshay.attendancebarcodereaderserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;

public class DeleteDatabaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner deleteSpinner;
    private ProgressBar progressBar;
    private final String TAG = "DeleteDatabaseActivity";
    private String selected_dbName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_database);

        deleteSpinner = findViewById(R.id.selectDatabaseNameSpinner_delete);
        progressBar = findViewById(R.id.pb_loading_indicator);
        progressBar.setVisibility(View.INVISIBLE);

        try{
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
            deleteSpinner.setAdapter(fileArrayAdapter);
            deleteSpinner.setOnItemSelectedListener(this);
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }

    }

    public void onClickDeleteDbBtn(View view){

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_dbName = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
