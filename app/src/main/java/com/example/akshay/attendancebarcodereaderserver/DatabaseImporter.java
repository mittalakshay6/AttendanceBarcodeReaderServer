package com.example.akshay.attendancebarcodereaderserver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ajts.androidmads.library.ExcelToSQLite;

import java.io.File;

public class DatabaseImporter {
    private ExcelToSQLite excelToSQLite;
    private boolean isImported;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private DatabaseHelper databaseHelper;
    private File file;
    private String databaseName;
    private DatabaseImporterListener listener;

    private final String TAG = "DatabaseImporter";

    public DatabaseImporter(Context context, String databaseName, DatabaseImporterListener listener) {
        this.context=context;
        this.databaseName = databaseName;
        this.listener=listener;
    }

    public boolean isImported() {
        return isImported;
    }

    public void importDatabase(String path){
        isImported=false;
        file = new File(path);
        if(!file.exists() && !file.canRead()){
            Toast toast = Toast.makeText(context, "File not exists, or cannot read file", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        else{
            excelToSQLite = new ExcelToSQLite(context, databaseName, false);
            excelToSQLite.importFromFile(file.getPath(), new ExcelToSQLite.ImportListener() {
                @Override
                public void onStart() {
                    listener.onStart();
                }

                @Override
                public void onCompleted(String dbName) {
                    Toast.makeText(context, "Database imported successfully", Toast.LENGTH_SHORT).show();
                    isImported=true;
                    listener.onCompleted();
                }

                @Override
                public void onError(Exception e) {
                    listener.onError();
                    Toast.makeText(context, "Database import failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public interface DatabaseImporterListener{
        void onStart();
        void onCompleted();
        void onError();
    }
}
