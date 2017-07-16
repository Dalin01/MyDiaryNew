package com.example.darlington.mydiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;

import java.util.ArrayList;

public class SortStar extends AppCompatActivity {

    ArrayList<MyInbox> details = new ArrayList<MyInbox>();
    CustomDiaryAdapter adapter;

    String font;
    int text_size;
    String colour;
    String name;
    String my_sort;
    ListView my_list;

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
            setContentView(R.layout.activity_sort);

            Bundle extras = getIntent().getExtras();

            my_sort = extras.getString("star");

            my_list = (ListView) findViewById(R.id.my_list);

        }

        {
            TextView v = (TextView) findViewById(R.id.name);
            TextView copyright = (TextView) findViewById(R.id.copyright);
            v.setText(name);
            Typeface typeface = Typeface.createFromAsset(getAssets(), font);
            v.setTypeface(typeface);
            copyright.setTypeface(typeface);
            v.setTextSize(text_size);
        }

        {
            DiaryInboxHelper mDbHelper = new DiaryInboxHelper(this);
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
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

            String sortOrder = DiaryContract.DiaryEntry._ID_MESSAGE + " DESC";
            String selection = DiaryContract.DiaryEntry.COLUMN_STAR + " LIKE ?";
            String[] selectionArgs = {"checked"};


            Cursor cursor = db.query(
                    DiaryContract.DiaryEntry.TABLE_NAME_INBOX,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                               // The sort order
            );

            double count = cursor.getCount();
            int count_int = (int) count;
            if (count == 0) {
                View empty = (View) findViewById(R.id.empty);
                View good = (View) findViewById(R.id.my_list);
                empty.setVisibility(View.VISIBLE);
                good.setVisibility(View.GONE);
            }
            TextView count_messages = (TextView) findViewById(R.id.count);
            count_messages.setText(String.valueOf(count_int));

            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry._ID_MESSAGE));
                    String subject = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_SUBJECT));
                    String location = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_LOCATION));
                    String message = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_MESSAGE));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_DATE_TIME));
                    String category = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_CATEGORY));
                    String star = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_STAR));
                    MyInbox details1 = new MyInbox(subject, location, message, password, category, id, font, text_size, colour, star);
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
            } finally {
                cursor.close();
            }
        }
    }
}
