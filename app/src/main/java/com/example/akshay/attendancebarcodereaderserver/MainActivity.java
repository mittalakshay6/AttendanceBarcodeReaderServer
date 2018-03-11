package com.example.akshay.attendancebarcodereaderserver;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.ajts.androidmads.library.ExcelToSQLite;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    TextView pathView;
    Button importBtn;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;
    ExcelToSQLite excelToSQLite;
    String path;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        path = Environment.getExternalStorageDirectory().getPath()+"/excelSheets/test.xls";
        file = new File(path);
        if(!file.exists() && !file.canRead()){
            Log.d("FILEERROR", "File path is wrong");
        }
        else{
            Log.d("FILEERROR", "File exists");
        }

        databaseHelper = new DatabaseHelper(this);
        new DatabaseTask().execute();
    }

    class DatabaseTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            excelToSQLite = new ExcelToSQLite(getApplicationContext(), DatabaseHelper.DATABASE_NAME, false);
            excelToSQLite.importFromFile(file.getPath(), new ExcelToSQLite.ImportListener() {
                @Override
                public void onStart() {
                    Log.d("IMPORT", "Database import started");
                }

                @Override
                public void onCompleted(String dbName) {
                    Log.d("IMPORT", "Database import completed");
                }

                @Override
                public void onError(Exception e) {
                    Log.d("IMPORT", "Database import failed"+e.getMessage());
                }
            });
            databaseHelper.close();
        }
    }
}
