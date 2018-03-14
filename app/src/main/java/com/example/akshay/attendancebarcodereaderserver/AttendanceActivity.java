package com.example.akshay.attendancebarcodereaderserver;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class AttendanceActivity extends AppCompatActivity {
    private ListView lv;
    private ArrayList<String> regNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        lv = findViewById(R.id.regNoList);

        regNoData = new ArrayList<String>();
        for(int i=0;i<100;i++) {
            regNoData.add("20158012");
            //   regNoData.add("20158040");
           // regNoData.add("20158026");
        }

        lv.setAdapter(new ArrayAdapter<String>(this, 0,regNoData){

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
        });
    }
}
