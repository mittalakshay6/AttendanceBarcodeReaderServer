package com.example.akshay.attendancebarcodereaderserver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ajts.androidmads.library.ExcelToSQLite;

import java.io.File;

public class DatabaseImporter {
    private DatabaseHelper databaseHelper;
    private ExcelToSQLite excelToSQLite;
    private boolean isImported;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private File file;

    private final String TAG = "DatabaseImporter";

    public DatabaseImporter(DatabaseHelper databaseHelper, Context context) {
        this.databaseHelper = databaseHelper;
        this.context=context;
    }

    public boolean isImported() {
        return isImported;
    }

    //TODO: SQLite2Excel library itself takes care of multithreading, hence may not be needed here.

    public void importDatabase(String path){
        file = new File(path);
        if(!file.exists() && !file.canRead()){
            Toast toast = Toast.makeText(context, "File not exists, or cannot read file", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        else{
            new DatabaseRetriever().execute(databaseHelper);
        }

    }
    class DatabaseRetriever extends AsyncTask<DatabaseHelper, Void, Boolean>{

        @Override
        protected Boolean doInBackground(DatabaseHelper... databaseHelpers) {
            sqLiteDatabase=databaseHelpers[0].getWritableDatabase();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            excelToSQLite = new ExcelToSQLite(context, String.valueOf(R.string.DATABASE_NAME), false);
            excelToSQLite.importFromFile(file.getPath(), new ExcelToSQLite.ImportListener() {
                @Override
                public void onStart() {
                    Toast.makeText(context, "Database import started", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCompleted(String dbName) {
                    Toast.makeText(context, "Database imported successfully", Toast.LENGTH_SHORT).show();
                    isImported=true;
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(context, "Database import failed", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
