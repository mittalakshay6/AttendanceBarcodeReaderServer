package com.example.akshay.attendancebarcodereaderserver;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class DatabaseImportActivity extends AppCompatActivity {

    private TextView pathView;
    private EditText databaseNameView;
    private Button browseBtn;
    private Button importDatabaseBtn;
    private Button doneBtn;
    private static final int READ_REQUEST_CODE = 42;
    private final String TAG = "DatabaseImportActivity";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_import);

        pathView = findViewById(R.id.pathView);
        browseBtn = findViewById(R.id.browseBtn);
        importDatabaseBtn = findViewById(R.id.importBtn);
        doneBtn = findViewById(R.id.doneBtn);
        databaseNameView = findViewById(R.id.databaseNameView);
        progressBar = findViewById(R.id.pb_loading_indicator);
        progressBar.setVisibility(View.INVISIBLE);

        databaseNameView.setVisibility(View.INVISIBLE);
        importDatabaseBtn.setVisibility(View.INVISIBLE);
    }

    public void onClickDoneBtn(View view){
        finish();
    }

    public void onClickBrowseBtn(View view){
        performFileSearch();
    }

    public void onClickImportDatabaseBtn(View View){
        String fileName = databaseNameView.getText().toString();
        ArrayList<CharSequence> fileArrayList = new ArrayList<>();
        String dir = this.getDatabasePath("a").getParent();
        File file = new File(dir);
        File[] files = file.listFiles();
        if(files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith("db")) {
                    fileArrayList.add(files[i].getName());
                }
            }

            if (fileArrayList.contains(fileName + ".db")) {
                Toast.makeText(this, "Database already exists", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        DatabaseImporter.DatabaseImporterListener listener = new DatabaseImporter.DatabaseImporterListener() {
            @Override
            public void onStart() {
                progressBar.setVisibility(android.view.View.VISIBLE);
            }

            @Override
            public void onCompleted() {
                progressBar.setVisibility(android.view.View.INVISIBLE);
            }

            @Override
            public void onError() {

            }
        };
        DatabaseImporter databaseImporter = new DatabaseImporter(this, databaseNameView.getText().toString()+".db", listener);
        databaseImporter.importDatabase(pathView.getText().toString());
    }

    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
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
                if(uri==null){
                    uri = (Uri)data.getExtras().get("data");
                }
                String path = RealPathUtil.getRealPath(this, uri);
                pathView.setText(path);
                String extension = null;
                if (path != null) {
                    extension = path.substring(path.lastIndexOf("."));
                    Log.d(TAG, extension);
                }


                if(extension!=null && extension.equals(".xls")){
                    databaseNameView.setVisibility(View.VISIBLE);
                    importDatabaseBtn.setVisibility(View.VISIBLE);
                }
                else{
                    databaseNameView.setVisibility(View.INVISIBLE);
                    importDatabaseBtn.setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(this, "Wrong file type selected", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }
}

