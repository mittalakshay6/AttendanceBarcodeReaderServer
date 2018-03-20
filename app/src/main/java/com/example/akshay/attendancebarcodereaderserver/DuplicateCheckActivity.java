package com.example.akshay.attendancebarcodereaderserver;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DuplicateCheckActivity extends AppCompatActivity {

    private final String TAG = "DuplicateCheckActivity";

    private ArrayList<String> proxies;
    private ProgressBar progressBar;
    private ListView lv;
    private ArrayAdapter<String> proxyListAdapter;
    private ArrayList<String> confirmedProxies;
    private String tableName;
    private String dbname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duplicate_check);

        final Intent intent = getIntent();
        confirmedProxies = new ArrayList<>();
        proxies = intent.getStringArrayListExtra(StartAttendanceActivity.PROXIES_LIST);
        tableName = intent.getStringExtra(StartAttendanceActivity.TABLE_NAME);
        dbname = intent.getStringExtra(StartAttendanceActivity.DB_NAME);

        progressBar = findViewById(R.id.pb_loading_indicator);

        lv= findViewById(R.id.proxyRegNoList);
        proxyListAdapter = new ArrayAdapter<String>(this, 0, proxies){
            private View row;
            private LayoutInflater inflater = getLayoutInflater();
            private TextView tv;

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                row = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
                tv = row.findViewById(android.R.id.text1);
                tv.setText(proxies.get(position));
                return row;
            }
        };
        lv.setAdapter(proxyListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = lv.getItemAtPosition(position).toString();
                if(confirmedProxies.contains(item)){
                    confirmedProxies.remove(item);
                }
                else{
                    confirmedProxies.add(item);
                }
            }
        });
    }
    public void onClickDoneBtn(View view){
        finish();
    }
    public void onClickMarkAbsentBtn(View view){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseQueries databaseQueries = DatabaseQueries.getInstance();
        DatabaseHelper databaseHelper = new DatabaseHelper(this, dbname);
        databaseHelper.setTableName(tableName);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        for(int i = 0; i<confirmedProxies.size();i++){
            sqLiteDatabase.execSQL(databaseQueries.getSQL_MARK_A(tableName, confirmedProxies.remove(i)+".0"));
        }
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "Selected students have been marked as absent", Toast.LENGTH_SHORT).show();
    }
}
