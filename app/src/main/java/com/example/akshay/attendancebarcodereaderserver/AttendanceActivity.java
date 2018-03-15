package com.example.akshay.attendancebarcodereaderserver;

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

import java.io.File;
import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = "AttendanceActivity";
    private int count1=0;
    private int count2=0;
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

    public void onClickStartBtn(View View){
        Intent intent = new Intent(this, StartAttendanceActivity.class);
        intent.putExtra(INTENT_DBNAME, selected_dbName);
        intent.putExtra(INTENT_TABLENAME, selected_tableName);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == spinner_databaseName.getId()) {
            count1++;
            if(count1<2){
                return;
            }
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
                table_names.add(cursor.getString(cursor.getColumnIndex(strings[0])));
            }
            table_names_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_text_layout, table_names);
            spinner_tableName.setAdapter(table_names_adapter);
            spinner_tableName.setOnItemSelectedListener(this);
        }
        if(parent.getId() == spinner_tableName.getId()){
            count2++;
            if(count2<2){
                return;
            }
            selected_tableName =(String) parent.getItemAtPosition(position);
            Log.i(TAG, selected_tableName);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}