package com.pyenoma.android.markp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = "AttendanceActivity";
    private SQLiteDatabase sqLiteDatabase;
    private Spinner spinner_databaseName;
    private Spinner spinner_tableName;
    private String selected_dbName;
    private DatabaseQueries databaseQueries;
    private TextView note2;
    private String selected_tableName;
    private Button startBtn;
    public static final String INTENT_DBNAME="selected_database_name";
    public static final String INTENT_TABLENAME="selected_table_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_attendance);

            spinner_databaseName = findViewById(R.id.selectDatabaseNameSpinner);
            spinner_tableName = findViewById(R.id.selectTableSpinner);
            databaseQueries = DatabaseQueries.getInstance();
            note2 = findViewById(R.id.note2);
            startBtn = findViewById(R.id.startBtn);
            try {
                String dir = null;
                dir = this.getDatabasePath("a").getParent();
                File file = new File(dir);
                File[] files = file.listFiles();
                if(files.length == 0){
                    Toast.makeText(this, "No database is imported", Toast.LENGTH_SHORT).show();
                }
                ArrayList<CharSequence> fileArrayList = new ArrayList<>();
                ArrayAdapter<CharSequence> fileArrayAdapter;
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().endsWith("db") && !files[i].getName().equals("start.db")) {
                        fileArrayList.add(files[i].getName());
                    }
                }


                fileArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_text_layout, fileArrayList);
                spinner_databaseName.setAdapter(fileArrayAdapter);
                spinner_databaseName.setOnItemSelectedListener(this);
            }
            catch(NullPointerException e){
                    e.printStackTrace();
            }
    }

    public void onClickStartBtn(View View){
        Log.d(TAG, selected_dbName + " " + selected_tableName);
        if(selected_dbName == null) {
            Toast.makeText(this, "No Database selected", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(selected_tableName == null){
            Toast.makeText(this, "No Table selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, StartAttendanceActivity.class);
        intent.putExtra(INTENT_DBNAME, selected_dbName);
        intent.putExtra(INTENT_TABLENAME, selected_tableName);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == spinner_databaseName.getId()) {
            selected_dbName = (String) parent.getItemAtPosition(position);
            Log.i(TAG, selected_dbName);
//            Toast.makeText(this, selected_dbName, Toast.LENGTH_SHORT).show();

            DatabaseHelper databaseHelper = new DatabaseHelper(this, selected_dbName);
            databaseHelper.setTableName("sqlite_master");
            sqLiteDatabase = databaseHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(databaseQueries.getSQL_RETURN_ALL_TABLES(), null);
            String[] strings = cursor.getColumnNames();
            ArrayList<CharSequence> table_names = new ArrayList<>();
            ArrayAdapter<CharSequence> table_names_adapter;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String table=cursor.getString(cursor.getColumnIndex(strings[0]));
                if(table.equals("android_metadata")){
                    continue;
                }
                table_names.add(table);
            }
            table_names_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_text_layout, table_names);
            spinner_tableName.setAdapter(table_names_adapter);
            spinner_tableName.setOnItemSelectedListener(this);
        }
        if(parent.getId() == spinner_tableName.getId()){
            selected_tableName =(String) parent.getItemAtPosition(position);
            Log.i(TAG, selected_tableName);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onClickDoneBtn(View view){
        finish();
    }
}