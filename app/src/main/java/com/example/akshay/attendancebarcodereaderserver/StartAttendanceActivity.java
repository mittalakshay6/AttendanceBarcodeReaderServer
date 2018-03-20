package com.example.akshay.attendancebarcodereaderserver;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class StartAttendanceActivity extends AppCompatActivity {

    private ArrayList<String> markedP;
    private ArrayList<String> proxies;
    private final String TAG = "StartAttendanceActivity";
    public static final String PROXIES_LIST = "DuplicateChecker_Object";
    public static final String DB_NAME="DatabaseName";
    public static final String TABLE_NAME="TableName";
    private ListView lv;
    private ArrayList<String> regNoData;
    private ConnectionManager connectionManager;
    private DataExchangeHelper dataExchangeHelper;
    private DatabaseHelper databaseHelper;
    private DatabaseQueries databaseQueries;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayAdapter<String> adapter;
    private DuplicateChecker duplicateChecker;
    private String dbname;
    private String tableName;
    private TextView numAttView;
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
        markedP = new ArrayList<>();

        duplicateChecker = new DuplicateChecker();
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        ipAddrView.setText(ipAddress);
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    InetAddress inetAddress = InetAddress.getLocalHost();
//                    final String ipAddress = inetAddress.getHostAddress();
//                    StartAttendanceActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ipAddrView.setText(ipAddress);
//                        }
//                    });
//                } catch (UnknownHostException e) {
//                    Log.e(TAG, "UnknownHostException: "+e.getMessage());
//                }
//                return null;
//            }
//        }.execute();

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
                                if(!regNoData.contains(showData+"_invalid")) {
                                    regNoData.add(showData + "_invalid");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                    else {
                        dataOutputStream.writeBoolean(true);
                        boolean isProxy = duplicateChecker.checkThisINetAddress(socket.getInetAddress());
                        Log.d(TAG, String.valueOf(isProxy));
                        if(isProxy){
                            Cursor c = sqLiteDatabase.rawQuery(databaseQueries.getStatusOfRegNum(databaseQueries.getTableName(), data), null);
                            c.moveToNext();
                            String proxy = c.getString(0);
                            c.close();
                            Log.d(TAG, proxy);
                            Log.d(TAG, proxy);

                            if(proxy.equals("A")) {
                                sqLiteDatabase.execSQL(databaseQueries.getSQL_MARK_P(databaseQueries.getTableName(), data));
                                proxies.add(showData);
                                Log.d(TAG, "Proxy(showData) " + showData);
                                StartAttendanceActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(!regNoData.contains(showData + "_suspect")) {
                                            regNoData.add(showData + "_suspect");
                                            adapter.notifyDataSetChanged();
                                            markedP.add(showData);
                                            numAttView.setText(String.valueOf(markedP.size()));
                                        }
                                    }
                                });
                            }
                        }
                        else {
                            sqLiteDatabase.execSQL(databaseQueries.getSQL_MARK_P(databaseQueries.getTableName(), data));
                            StartAttendanceActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    if(!regNoData.contains(showData)) {
                                        regNoData.add(showData);
                                        adapter.notifyDataSetChanged();
                                        markedP.add(showData);
                                        numAttView.setText(String.valueOf(markedP.size()));
                                    }
                                }
                            });
                        }
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
    public void onClickDuplicateCheckBtn(View view){
        Intent intent = new Intent(this, DuplicateCheckActivity.class);
        intent.putExtra(PROXIES_LIST, proxies);
        intent.putExtra(DB_NAME, dbname);
        intent.putExtra(TABLE_NAME, tableName);
        startActivity(intent);
    }
}