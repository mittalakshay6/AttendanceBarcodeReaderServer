package com.pyenoma.android.markp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pyenoma.android.markp.Connection.ConnectionManager;
import com.pyenoma.android.markp.Connection.DataExchangeHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class StartAttendanceActivity extends AppCompatActivity {

    public static int numMarkedP;
    public static ArrayList<String> proxies;
    private final String TAG = "StartAttendanceActivity";
    public static final String PROXIES_LIST = "DuplicateChecker_Object";
    public static final Character SEND_SUCCESS = 'S';
    public static final Character SEND_FAIL_INVALID = 'I';
    public static final Character SEND_SUCCESS_PROXY = 'P';
    public static final String DB_NAME="DatabaseName";
    public static final String TABLE_NAME="TableName";
    private ListView lv;
    private static ArrayList<String> regNoData;
    private ConnectionManager connectionManager;
    private DataExchangeHelper dataExchangeHelper;
    private DatabaseHelper databaseHelper;
    private DatabaseQueries databaseQueries;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayAdapter<String> adapter;
    private DuplicateChecker duplicateChecker;
    private String dbname;
    private String tableName;
    public static TextView numAttView;
    private TextView ipAddrView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startattendance);

        Intent intent = getIntent();
        dbname = intent.getStringExtra(AttendanceActivity.INTENT_DBNAME);
        tableName = intent.getStringExtra(AttendanceActivity.INTENT_TABLENAME);
        numAttView = findViewById(R.id.numAttText);
        ipAddrView = findViewById(R.id.ipAddressText);
        proxies = new ArrayList<>();
        numMarkedP = 0;

        duplicateChecker = new DuplicateChecker();
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        ipAddrView.setText(ipAddress);

        databaseQueries=DatabaseQueries.getInstance();
        DatabaseObjects.setDatabaseQueries(databaseQueries);

        databaseHelper = new DatabaseHelper(this, dbname);
        databaseHelper.setTableName(tableName);
        DatabaseObjects.setDatabaseHelper(databaseHelper);

        sqLiteDatabase = databaseHelper.getWritableDatabase();
        DatabaseObjects.setSqLiteDatabaseWritable(sqLiteDatabase);

        try {
            sqLiteDatabase.execSQL(databaseQueries.getSQL_CREATE_TODAY_COL(databaseQueries.getTableName()));
        }
        catch (RuntimeException e){
            Log.e(TAG, e.getMessage());
        }

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
            Toast.makeText(this, "Taking attendance...", Toast.LENGTH_SHORT).show();
            connectionManager.startAccepting();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    servePendingConnections();
                }
            }.start();
        }
        else{
            Toast.makeText(this, "Cannot create socket, please restart the application", Toast.LENGTH_LONG).show();
        }
    }
    public void serveSocket(Socket socket){
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            final String showData = dataInputStream.readUTF();
            final String data = showData+".0";
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Cursor cursor = sqLiteDatabase.rawQuery(databaseQueries.getIS_REG_NUM_AVAILABLE(databaseQueries.getTableName(), data), null);
            if(cursor.getCount()==0){
                dataOutputStream.writeChar(SEND_FAIL_INVALID);
                StartAttendanceActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if(!regNoData.contains(showData+"_invalid")) {
                            regNoData.add(showData + "_invalid");
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
            else {
                boolean isProxy = duplicateChecker.checkThisINetAddress(socket.getInetAddress());
                if(isProxy){
                    Cursor c = sqLiteDatabase.rawQuery(databaseQueries.getStatusOfRegNum(databaseQueries.getTableName(), data), null);
                    c.moveToNext();
                    String proxy = c.getString(0);
                    c.close();

                    if(proxy.equals("A")) {
                        sqLiteDatabase.execSQL(databaseQueries.getSQL_MARK_P(databaseQueries.getTableName(), data));
                        StartAttendanceActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                numMarkedP++;
                                numAttView.setText(String.valueOf(numMarkedP));
                                regNoData.add(showData + "_suspect");
                                adapter.notifyDataSetChanged();
                            }
                        });
                        dataOutputStream.writeChar(SEND_SUCCESS_PROXY);
                        if(!proxies.contains(showData)) {
                            proxies.add(showData);
                        }
//                                StartAttendanceActivity.this.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        regNoData.add(showData + "_suspect");
//                                        adapter.notifyDataSetChanged();
//                                    }
//                                });
                    }
                    else{
                        dataOutputStream.writeChar(SEND_SUCCESS);
                    }
                }
                else {
                    sqLiteDatabase.execSQL(databaseQueries.getSQL_MARK_P(databaseQueries.getTableName(), data));
                    dataOutputStream.writeChar(SEND_SUCCESS);
                    StartAttendanceActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            numMarkedP++;
                            numAttView.setText(String.valueOf(numMarkedP));
                        }
                    });
                    StartAttendanceActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            if(!regNoData.contains(showData)) {
                                regNoData.add(showData);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void servePendingConnections(){
        while(connectionManager.isAccepting()){
            if(connectionManager.areAnyPendingConnections()){
                final Socket socket = connectionManager.getSocket();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        serveSocket(socket);
                    }
                }).start();
            }
        }
    }
    public void onClickStopAttendance(View view){
        connectionManager.stopAccepting();
        sqLiteDatabase.close();
        finish();
    }
    public void onClickDuplicateCheckBtn(View view){
        Intent intent = new Intent(this, DuplicateCheckActivity.class);
        intent.putExtra(PROXIES_LIST, proxies);
        intent.putExtra(DB_NAME, dbname);
        intent.putExtra(TABLE_NAME, tableName);
        startActivity(intent);
    }
    public void onClickManualMarkBtn(View view){
        Intent intent = new Intent(this, MarkManuallyActivity.class);
        startActivity(intent);
    }

}