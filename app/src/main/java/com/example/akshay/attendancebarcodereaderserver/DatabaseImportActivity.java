package com.example.akshay.attendancebarcodereaderserver;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith("db")) {
                fileArrayList.add(files[i].getName());
            }
        }
        if(fileArrayList.contains(fileName+".db")){
            Toast.makeText(this, "Database already exists", Toast.LENGTH_SHORT).show();
            return;
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
                String path = getPath(this, uri);
                pathView.setText(path);
                String extension = path.substring(path.lastIndexOf("."));
                Log.d(TAG, extension);

                if(extension.equals(".xls")){
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
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}

