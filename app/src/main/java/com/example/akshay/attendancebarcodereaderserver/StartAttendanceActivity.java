package com.example.akshay.attendancebarcodereaderserver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshay.attendancebarcodereaderserver.Connection.ConnectionManager;
import com.example.akshay.attendancebarcodereaderserver.Connection.DataExchangeHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class StartAttendanceActivity extends AppCompatActivity {
    private final String TAG = "StartAttendanceActivity";
    private ListView lv;
    private ArrayList<String> regNoData;
    private ConnectionManager connectionManager;
    private DataExchangeHelper dataExchangeHelper;
    private DatabaseHelper databaseHelper;
    private DatabaseQueries databaseQueries;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startattendance);

        Intent intent = getIntent();
        String dbname = intent.getStringExtra(AttendanceActivity.INTENT_DBNAME);
        String tableName = intent.getStringExtra(AttendanceActivity.INTENT_TABLENAME);

        databaseQueries=DatabaseQueries.getInstance();
        databaseHelper = new DatabaseHelper(this, dbname);
        databaseHelper.setTableName(tableName);
        Toast.makeText(this, "Database loading started", Toast.LENGTH_SHORT);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        try {
            sqLiteDatabase.execSQL(databaseQueries.getSQL_CREATE_TODAY_COL(databaseQueries.getTableName()));
        }
        catch (RuntimeException e){
            Log.e(TAG, e.getMessage());
        }
        Toast.makeText(this, "Database loading Finished", Toast.LENGTH_SHORT);

        lv = findViewById(R.id.regNoList);
        regNoData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, 0,regNoData){
            private View row;
            private LayoutInflater inflater = getLayoutInflater();
            private TextView tv;
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                row = inflater.inflate(android.R.layout.simple_list_item_1,parent,false);
                tv = row.findViewById(android.R.id.text1);
                tv.setText(regNoData.get(position));
                return row;
            }
        };
        lv.setAdapter(adapter);

        this.connectionManager = new ConnectionManager();
        if(connectionManager.createServerSocket()){
            Toast.makeText(getApplicationContext(), "ServerSocket Created", Toast.LENGTH_SHORT).show();
            connectionManager.startAccepting();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    servePendingConnections();
                }
            }.start();
        }
    }
    public void servePendingConnections(){
        while(connectionManager.isAccepting()){
            if(connectionManager.areAnyPendingConnections()){
                Socket socket = connectionManager.getSocket();
                try {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    final String showData = dataInputStream.readUTF();
                    final String data = showData+".0";
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    Cursor cursor = sqLiteDatabase.rawQuery(databaseQueries.getIS_REG_NUM_AVAILABLE(databaseQueries.getTableName(), data), null);
                    if(cursor.getCount()==0){
                        dataOutputStream.writeBoolean(false);
                        StartAttendanceActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                regNoData.add(showData+"_invalid");
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                    else {
                        sqLiteDatabase.execSQL(databaseQueries.getSQL_MARK_P(databaseQueries.getTableName(), data));
                        dataOutputStream.writeBoolean(true);

                        StartAttendanceActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                regNoData.add(showData);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public void onClickStopAttendance(View view){
        connectionManager.stopAccepting();
        sqLiteDatabase.close();
        finish();
    }
}