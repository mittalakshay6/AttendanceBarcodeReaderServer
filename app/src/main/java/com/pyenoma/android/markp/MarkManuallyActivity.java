package com.pyenoma.android.markp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MarkManuallyActivity extends AppCompatActivity {

    private static TextView dateView;
    private static EditText regNoView;
    private static String selectedDate;
    private static final String TAG = "MarkManualActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_manually);

        dateView = findViewById(R.id.dateView);
        regNoView = findViewById(R.id.regNoView);
        dateView.setVisibility(View.INVISIBLE);
        selectedDate=null;
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datePickerDialog;
        }



        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat showDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
            dateView.setText(showDateFormat.format(c.getTime()));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
            selectedDate = simpleDateFormat.format(c.getTime());
            selectedDate="d"+selectedDate;
            dateView.setVisibility(View.VISIBLE);
        }
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onClickMarkPBtn(View view){
        String regNo = regNoView.getText().toString();
        if(regNo.length()!=8){
            Toast.makeText(this, "Set Registration Number first", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(selectedDate==null){
            Toast.makeText(this, "Set date first", Toast.LENGTH_SHORT).show();
            return;
        }
        regNo=regNo+".0";
        DatabaseQueries databaseQueries = DatabaseQueries.getInstance();
        assert DatabaseObjects.isSqliteDatabaseWritableSet();
        SQLiteDatabase sqLiteDatabase = DatabaseObjects.getSqLiteDatabaseWritable();

        Cursor colCursor = sqLiteDatabase.rawQuery("SELECT * FROM "+databaseQueries.getTableName()+ " LIMIT 0,1", null);
        if(colCursor.getColumnIndex(selectedDate)<0){
            Toast.makeText(this, "No attendance took place on the specified date", Toast.LENGTH_SHORT).show();
            colCursor.close();
            return;
        }
        colCursor.close();

        Cursor cursor = sqLiteDatabase.rawQuery(databaseQueries.getIS_REG_NUM_AVAILABLE(databaseQueries.getTableName(), regNo), null);
        if(cursor.getCount()==0){
            Toast.makeText(this, "Registration Number not available", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();

        Cursor c = sqLiteDatabase.rawQuery(databaseQueries.getStatusOfRegNum(databaseQueries.getTableName(), regNo), null);
        c.moveToNext();
        String status = c.getString(0);
        c.close();
        if(status.equals("A")){
            sqLiteDatabase.execSQL(databaseQueries.getSQL_MARK_P(databaseQueries.getTableName(), regNo, selectedDate));
            StartAttendanceActivity.numMarkedP++;
            StartAttendanceActivity.numAttView.setText(String.valueOf(StartAttendanceActivity.numMarkedP));
            Toast.makeText(this, "Marked present", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Already Present", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickMarkABtn(View view){
        String regNo = regNoView.getText().toString();
        if(regNo.isEmpty()){
            Toast.makeText(this, "Set Registration Number first", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(selectedDate==null){
            Toast.makeText(this, "Set date first", Toast.LENGTH_SHORT).show();
            return;
        }
        regNo=regNo+".0";
        DatabaseQueries databaseQueries = DatabaseQueries.getInstance();
        assert DatabaseObjects.isSqliteDatabaseWritableSet();
        SQLiteDatabase sqLiteDatabase = DatabaseObjects.getSqLiteDatabaseWritable();

        Cursor colCursor = sqLiteDatabase.rawQuery("SELECT * FROM "+databaseQueries.getTableName()+ " LIMIT 0,1", null);
        if(colCursor.getColumnIndex(selectedDate)<0){
            Toast.makeText(this, "No attendance took place on the specified date", Toast.LENGTH_SHORT).show();
            colCursor.close();
            return;
        }
        colCursor.close();

        Cursor cursor = sqLiteDatabase.rawQuery(databaseQueries.getIS_REG_NUM_AVAILABLE(databaseQueries.getTableName(), regNo), null);
        if(cursor.getCount()==0){
            Toast.makeText(this, "Registration Number not available", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();

        Cursor c = sqLiteDatabase.rawQuery(databaseQueries.getStatusOfRegNum(databaseQueries.getTableName(), regNo), null);
        c.moveToNext();
        String status = c.getString(0);
        c.close();
        if(status.equals("P")){
            sqLiteDatabase.execSQL(databaseQueries.getSQL_MARK_A(databaseQueries.getTableName(), regNo, selectedDate));
            StartAttendanceActivity.numMarkedP--;
            StartAttendanceActivity.numAttView.setText(String.valueOf(StartAttendanceActivity.numMarkedP));
            Toast.makeText(this, "Marked absent", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Already absent", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickMarkAllABtn(View view){
        if(selectedDate==null){
            Toast.makeText(this, "Set date first", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseQueries databaseQueries = DatabaseQueries.getInstance();
        assert DatabaseObjects.isSqliteDatabaseWritableSet();
        SQLiteDatabase sqLiteDatabase = DatabaseObjects.getSqLiteDatabaseWritable();

        Cursor colCursor = sqLiteDatabase.rawQuery("SELECT * FROM "+databaseQueries.getTableName()+ " LIMIT 0,1", null);
        if(colCursor.getColumnIndex(selectedDate)<0){
            Toast.makeText(this, "No attendance took place on the specified date", Toast.LENGTH_SHORT).show();
            colCursor.close();
            return;
        }
        colCursor.close();
        sqLiteDatabase.execSQL(databaseQueries.getSQL_MARK_All_A(databaseQueries.getTableName(), selectedDate));
        Toast.makeText(this, "All students marked Absent", Toast.LENGTH_SHORT).show();
        StartAttendanceActivity.numMarkedP=0;
        StartAttendanceActivity.numAttView.setText(String.valueOf(StartAttendanceActivity.numMarkedP));
        StartAttendanceActivity.regNoData.clear();
    }
    public void onClickMarkAllPBtn(View view){
        if(selectedDate==null){
            Toast.makeText(this, "Set date first", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseQueries databaseQueries = DatabaseQueries.getInstance();
        assert DatabaseObjects.isSqliteDatabaseWritableSet();
        SQLiteDatabase sqLiteDatabase = DatabaseObjects.getSqLiteDatabaseWritable();

        Cursor colCursor = sqLiteDatabase.rawQuery("SELECT * FROM "+databaseQueries.getTableName()+ " LIMIT 0,1", null);
        if(colCursor.getColumnIndex(selectedDate)<0){
            Toast.makeText(this, "No attendance took place on the specified date", Toast.LENGTH_SHORT).show();
            colCursor.close();
            return;
        }
        colCursor.close();
        sqLiteDatabase.execSQL(databaseQueries.getSQL_MARK_All_P(databaseQueries.getTableName(), selectedDate));
        Toast.makeText(this, "All Students marked present", Toast.LENGTH_SHORT).show();
        StartAttendanceActivity.numMarkedP=0;
        StartAttendanceActivity.numAttView.setText(String.valueOf(StartAttendanceActivity.numMarkedP));
        StartAttendanceActivity.regNoData.clear();
    }
    public void onClickDoneBtn(View view) {
        finish();
    }
}
