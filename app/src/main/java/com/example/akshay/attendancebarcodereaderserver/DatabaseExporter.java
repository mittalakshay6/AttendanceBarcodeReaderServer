package com.example.akshay.attendancebarcodereaderserver;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;

import java.io.File;

public class DatabaseExporter {
    private Context context;
    private final String EXPORT_PATH= Environment.getExternalStorageDirectory().toString()+File.separator+"AttendanceExcelSheets"+File.separator;
    private String databaseName;
    private SQLiteToExcel sqLiteToExcel;
    private String fileName;
    private String tableName;
    private final String TAG = "DatabaseExporter";

    public DatabaseExporter(Context context, String databaseName, String tableName, String fileName) {
        this.context = context;
        this.databaseName = databaseName;
        this.fileName = fileName;
        this.tableName = tableName;
        sqLiteToExcel = new SQLiteToExcel(context, databaseName, EXPORT_PATH);
    }

    public void exportTable(){
        sqLiteToExcel.exportSingleTable(tableName, fileName, new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {
                Toast.makeText(context, "Database Export Started", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Database export started");
            }

            @Override
            public void onCompleted(String filePath) {
                Toast.makeText(context, "Database export completed successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Database Export Completed");
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context, "Database export failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Database Export failed " + e.getMessage());
            }
        });
    }
}
