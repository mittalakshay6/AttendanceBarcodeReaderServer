package com.example.akshay.attendancebarcodereaderserver;

import com.example.akshay.attendancebarcodereaderserver.R;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private Button startBtn;
    private Spinner batchSelectSpinner;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private DatabaseQueries databaseQueries;

    private final String TAG = "AttendanceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        databaseQueries=DatabaseQueries.getInstance();
        startBtn = findViewById(R.id.startBtn);
        batchSelectSpinner = findViewById(R.id.selectTableNameSpinner);
//        databaseHelper = new DatabaseHelper(this);
        List tableNames = null;

        new ReadableDatabaseGet().execute(databaseHelper);

        if(sqLiteDatabase!=null){
            Cursor cursor = sqLiteDatabase.rawQuery(databaseQueries.getSQL_RETURN_ALL_TABLES(), null);
            tableNames = new ArrayList();
            while(cursor.moveToNext()) {
                String tableName = cursor.getString(
                        cursor.getColumnIndexOrThrow("type"));
                tableNames.add(tableName);
            }
            cursor.close();
        }
        if(tableNames==null){
            Log.d(TAG, "Null");
        }
//        Log.d(TAG, tableNames.toString());
    }
    class ReadableDatabaseGet extends AsyncTask<DatabaseHelper, Void, Boolean>{

        @Override
        protected Boolean doInBackground(DatabaseHelper... databaseHelpers) {
            sqLiteDatabase = databaseHelpers[0].getReadableDatabase();
            return true;
        }
    }
}
