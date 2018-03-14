package com.example.akshay.attendancebarcodereaderserver;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.net.URI;

public class DatabaseImportActivity extends AppCompatActivity {

    private TextView pathView;
    private Button browseBtn;
    private Button importDatabaseBtn;
    private static final int READ_REQUEST_CODE = 42;
    private final String TAG = "DatabaseImportActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_import);

        pathView = findViewById(R.id.pathView);
        browseBtn = findViewById(R.id.browseBtn);
        importDatabaseBtn = findViewById(R.id.importBtn);

        importDatabaseBtn.setVisibility(View.INVISIBLE);
    }

    public void onClickBrowseBtn(View view){
        performFileSearch();
    }

    public void onClickImportDatabaseBtn(View View){
    }

    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==READ_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri uri;
            if(data!=null){
                uri = data.getData();
                assert uri != null;
                Log.i(TAG, "Uri: "+uri.toString());
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                String fileName = cursor.getString(nameIndex);
                pathView.setText(fileName);
            }
        }
    }
}
