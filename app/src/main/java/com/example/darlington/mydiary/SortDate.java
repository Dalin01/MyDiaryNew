package com.example.darlington.mydiary;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;


public class SortDate extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private String date_required;
    String font;
    int text_size;
    String colour;
    String name;
    ListView my_list;
    ArrayList<MyInbox> details = new ArrayList<MyInbox>();
    CustomDiaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        {
            DiaryHelper mDbHelper1 = new DiaryHelper(this);
            SQLiteDatabase db1 = mDbHelper1.getReadableDatabase();

            Cursor c = db1.query(
                    DiaryContract.DiaryEntry.TABLE_NAME,
                    null,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null
            );

            try {
                c.moveToFirst();
                colour = c.getString(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_COLOUR));
                name = c.getString(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_NAME));
                font = c.getString(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_FONT));
                text_size = c.getInt(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_FONT_SIZE));
            } finally {
                c.close();
            }
            if (colour.equals("colour 1")) {
                setTheme(R.style.ThemeOne);
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_one));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.theme_one));
                }
            } else if (colour.equals("colour 2")) {
                setTheme(R.style.ThemeTwo);
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_two));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.theme_two));
                }
            } else if (colour.equals("colour 3")) {
                setTheme(R.style.ThemeThree);
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_three));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.theme_three));
                }
            }

            setContentView(R.layout.activity_sort_date);

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);

            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            askDate(null);

        }
    }

    @SuppressWarnings("deprecation")
    public void askDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    appendDate(arg1, arg2 + 1, arg3);
                }
            };


    private void appendDate(int year, int month, int day) {
        String my_year_pre = String.valueOf(year);
        String my_year = my_year_pre.substring(2);
        String my_month = String.valueOf(month);
        String my_day = String.valueOf(day);
        date_required = my_month + "/" + my_day + "/" + my_year;
        checkDb();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.date_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.date_menu) {
            finish();
            startActivity(getIntent());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkDb() {
        DiaryInboxHelper mDbHelper = new DiaryInboxHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        my_list = (ListView) findViewById(R.id.my_list);
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DiaryContract.DiaryEntry._ID_MESSAGE,
                DiaryContract.DiaryEntry.COLUMN_SUBJECT,
                DiaryContract.DiaryEntry.COLUMN_LOCATION,
                DiaryContract.DiaryEntry.COLUMN_MESSAGE,
                DiaryContract.DiaryEntry.COLUMN_DATE_TIME,
                DiaryContract.DiaryEntry.COLUMN_CATEGORY,
                DiaryContract.DiaryEntry.COLUMN_STAR,
        };
        Cursor cursor = db.query(
                DiaryContract.DiaryEntry.TABLE_NAME_INBOX,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                               // The sort order
        );

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry._ID_MESSAGE));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_SUBJECT));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_LOCATION));
                String message = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_MESSAGE));
                String date_time = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_DATE_TIME));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_CATEGORY));
                String star = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_STAR));

                String[] date_time_new = date_time.split(" ");
                String required_date = date_time_new[0];
                if(required_date.equals(date_required)){
                    MyInbox details1 = new MyInbox(subject, location, message, date_time, category, id, font, text_size, colour, star);
                    details.add(details1);
                    adapter = new CustomDiaryAdapter(this, details);
                    my_list.setAdapter(adapter);
                    my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            MyInbox myInbox = adapter.getItem(position);
                            String my_subject = myInbox.getSubject();
                            String my_location = myInbox.getLocation();
                            String my_message = myInbox.getMessage();
                            String my_category = myInbox.getCategory();
                            String my_time_date = myInbox.getDate();
                            int my_id = myInbox.getId();
                            String font = myInbox.getFont();
                            String star = myInbox.getStar();

                            Intent i = new Intent(getApplicationContext(), Home.class);
                            i.putExtra("Sub", my_subject);
                            i.putExtra("Loc", my_location);
                            i.putExtra("Mes", my_message);
                            i.putExtra("Cat", my_category);
                            i.putExtra("date", my_time_date);
                            i.putExtra("id", my_id);
                            i.putExtra("font", font);
                            i.putExtra("text_size", text_size);
                            i.putExtra("star", star);

                            // Send the intent to launch a new activity
                            startActivity(i);
                        }
                    });
                }
            }
        } finally {
            int array_length = details.size();
            if (array_length == 0){
                View empty = (View) findViewById(R.id.my_view);
                View good = (View) findViewById(R.id.my_list);
                empty.setVisibility(View.VISIBLE);
                good.setVisibility(View.GONE);
            }
            cursor.close();
        }

    }
}

