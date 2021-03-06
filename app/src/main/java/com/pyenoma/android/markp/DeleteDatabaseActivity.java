package com.pyenoma.android.markp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class DeleteDatabaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner deleteSpinner;
    private ProgressBar progressBar;
    private final String TAG = "DeleteDatabaseActivity";
    private String selected_dbName;
    private ArrayAdapter<CharSequence> fileArrayAdapter;
    private ArrayList<CharSequence> fileArrayList;

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
            fileArrayList = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith("db") && !files[i].getName().equals("start.db")) {
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
        if(selected_dbName==null){
            Toast.makeText(this, "No database selected", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, selected_dbName);
        boolean result = this.deleteDatabase(selected_dbName);
        if(result){
            Log.d(TAG, "File Deleted successfully");
            fileArrayList.remove(selected_dbName);
            fileArrayAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Database removed", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.e(TAG, "Unable to delete file");
            Toast.makeText(this, "Cannot delete database", Toast.LENGTH_SHORT).show();
        }
        if(fileArrayList.size()>0) {
            selected_dbName = (String) fileArrayList.get(0);
            Log.d(TAG, selected_dbName);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_dbName = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onClickDoneBtn(View view){
        finish();
    }
}
