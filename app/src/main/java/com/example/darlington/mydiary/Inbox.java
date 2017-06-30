package com.example.darlington.mydiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;

import java.util.ArrayList;

public class Inbox extends AppCompatActivity {

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    ArrayList<MyInbox> details = new ArrayList<MyInbox>();
    CustomDiaryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView my_list = (ListView) findViewById(R.id.my_list);

        {
            DiaryHelper mDbHelper1 = new DiaryHelper(this);
            SQLiteDatabase db1 = mDbHelper1.getReadableDatabase();

            String[] projection1 = {
                    DiaryContract.DiaryEntry.COLUMN_NAME,
            };

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
                String name = c.getString(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_NAME));
                TextView v = (TextView) findViewById(R.id.name);
                v.setText(name);
            }
            finally {
                c.close();
            }
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
            };

            String sortOrder = DiaryContract.DiaryEntry._ID_MESSAGE + " DESC";


            Cursor cursor = db.query(
                    DiaryContract.DiaryEntry.TABLE_NAME_INBOX,                     // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                               // The sort order
            );

            double count = cursor.getCount();
            int count_int = (int) count;
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


                    MyInbox details1 = new MyInbox(subject, location, message, password, category, id);
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

                            Intent i = new Intent(getApplicationContext(), Home.class);
                            i.putExtra("Sub", my_subject);
                            i.putExtra("Loc", my_location);
                            i.putExtra("Mes", my_message);
                            i.putExtra("Cat", my_category);
                            i.putExtra("date", my_time_date);
                            i.putExtra("id", my_id);

                            // Send the intent to launch a new activity
                            startActivity(i);
                        }
                    });
                }
            }
            finally {
                cursor.close();
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                //initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            finish();
            startActivity(getIntent());
            return true;
        }
        else if(id == R.id.delete_all){
            DialogFragment fragment = new DeleteAllDialog();
            fragment.show(getSupportFragmentManager(), "note");
            return true;
        }
        else if(id == R.id.float_widget){
            chatHead();
            return true;
        }
        else if (id == R.id.exit){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void fab(View view){
        Intent i = new Intent(this, Message.class);
        startActivity(i);
    }

    public void chatHead(){
        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            startService(new Intent(Inbox.this, FloatingViewService.class));
            finish();
        }
    }

}
