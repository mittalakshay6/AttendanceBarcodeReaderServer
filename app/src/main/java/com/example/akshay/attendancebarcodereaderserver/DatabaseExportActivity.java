package com.example.akshay.attendancebarcodereaderserver;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class DatabaseExportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner_dbname_export;
    private Spinner spinner_tableName_export;
    private String selected_dbName;
    private String TAG = "DatabaseExportActivity";
    private String selected_tableName;
    private Button startBtn;
    private DatabaseQueries databaseQueries;
    private DatabaseExporter databaseExporter;
    private String fileName;
    private EditText fileNameView_export;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_export);

        progressBar = findViewById(R.id.pb_loading_indicator);
        progressBar.setVisibility(View.INVISIBLE);
        spinner_dbname_export = findViewById(R.id.selectDatabaseNameSpinner_export);
        spinner_tableName_export = findViewById(R.id.selectTableSpinner_export);
        startBtn = findViewById(R.id.startBtn_export);
        databaseQueries = DatabaseQueries.getInstance();
        fileNameView_export = findViewById(R.id.fileNameView_export);

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
            spinner_dbname_export.setAdapter(fileArrayAdapter);
            spinner_dbname_export.setOnItemSelectedListener(this);
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }

    }
    public void onClickStartBtn_export(View view){
        if(selected_dbName == null){
            Toast.makeText(this, "No Database to export", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(selected_tableName == null){
            Toast.makeText(this, "No Table to import", Toast.LENGTH_SHORT).show();
            return;
        }
        fileName = fileNameView_export.getText().toString();
        if(fileName.isEmpty()){
            Toast.makeText(this, "Enter a file name", Toast.LENGTH_SHORT).show();
            return;
        }
        fileName=fileName+".xls";
        Log.d(TAG, selected_dbName+selected_tableName);
        databaseExporter = new DatabaseExporter(this, selected_dbName, selected_tableName, fileName);
        progressBar.setVisibility(View.VISIBLE);
        databaseExporter.exportTable();
        progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == spinner_dbname_export.getId()) {
            selected_dbName = (String) parent.getItemAtPosition(position);
            Log.i(TAG, selected_dbName);
//            Toast.makeText(this, selected_dbName, Toast.LENGTH_SHORT).show();
            DatabaseHelper databaseHelper = new DatabaseHelper(this, selected_dbName);
            databaseHelper.setTableName("sqlite_master");
            SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(databaseQueries.getSQL_RETURN_ALL_TABLES(), null);
            String[] strings = cursor.getColumnNames();
            ArrayList<CharSequence> table_names = new ArrayList<>();
            ArrayAdapter<CharSequence> table_names_adapter;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String table = cursor.getString(cursor.getColumnIndex(strings[0]));
                if(table.equals("android_metadata")){
                    continue;
                }
                table_names.add(table);
            }
            table_names_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_text_layout, table_names);
            spinner_tableName_export.setAdapter(table_names_adapter);
            spinner_tableName_export.setOnItemSelectedListener(this);
        }
        if(parent.getId() == spinner_tableName_export.getId()){
            selected_tableName =(String) parent.getItemAtPosition(position);
            Log.i(TAG, selected_tableName);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}