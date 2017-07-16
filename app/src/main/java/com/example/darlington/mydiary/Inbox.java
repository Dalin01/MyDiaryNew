package com.example.darlington.mydiary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;

import java.util.ArrayList;

import static android.support.design.R.attr.headerLayout;

public class Inbox extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    ArrayList<MyInbox> details = new ArrayList<MyInbox>();
    CustomDiaryAdapter adapter;

    String font;
    int text_size;
    String colour;

    static Activity activityInbox;

    String message_to_share = "I use this android app and it's pretty amazing. Do download and enjoy!";
    private String mActivityTitle;


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
            }
            finally {
                c.close();
            }
        }
        if(colour.equals("colour 1")){
            setTheme(R.style.ThemeOne);
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_one));
                getWindow().setStatusBarColor(getResources().getColor(R.color.theme_one));
            }
        }
        else if(colour.equals("colour 2")){
            setTheme(R.style.ThemeTwo);
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_two));
                getWindow().setStatusBarColor(getResources().getColor(R.color.theme_two));
            }
        }
        else if(colour.equals("colour 3")){
            setTheme(R.style.ThemeThree);
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_three));
                getWindow().setStatusBarColor(getResources().getColor(R.color.theme_three));
            }
        }
        setContentView(R.layout.activity_inbox);

        activityInbox = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView my_list = (ListView) findViewById(R.id.my_list);

        {
            DiaryHelper mDbHelper1 = new DiaryHelper(this);
            SQLiteDatabase db1 = mDbHelper1.getReadableDatabase();

            String[] projection1 = {
                    DiaryContract.DiaryEntry.COLUMN_NAME,
                    DiaryContract.DiaryEntry.COLUMN_FONT,
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
                font = c.getString(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_FONT));
                text_size = c.getInt(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_FONT_SIZE));
                colour = c.getString(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_COLOUR));
                TextView v = (TextView) findViewById(R.id.name);
                TextView copyright = (TextView) findViewById(R.id.copyright);
                v.setText(name);
                Typeface typeface = Typeface.createFromAsset(getAssets(), font);
                v.setTypeface(typeface);
                copyright.setTypeface(typeface);
                v.setTextSize(text_size);
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
                    DiaryContract.DiaryEntry.COLUMN_STAR,
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
            if(count == 0){
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
            }
            finally {
                cursor.close();
            }

        }

        mActivityTitle = getTitle().toString();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.float_widget) {
            chatHead();
            return true;
        } else if (id == R.id.fonts) {
            DialogFragment fragment = new DialogFonts();
            fragment.show(getSupportFragmentManager(), "note");
            return true;
        } else if (id == R.id.size) {
            DialogFragment fragment = new DialogSize();
            fragment.show(getSupportFragmentManager(), "note");
            return true;
        } else if (id == R.id.colour) {
            DialogFragment fragment = new DialogColour();
            fragment.show(getSupportFragmentManager(), "note");
            return true;
        } else if (id == R.id.starred) {
            Intent i = new Intent(this, SortStar.class);
            i.putExtra("star", "star");
            startActivity(i);
            return true;
        } else if (id == R.id.date_sort) {
            Intent i = new Intent(this, SortDate.class);
            startActivity(i);
            return true;
        }else if (id == R.id.category_sort) {
            DialogFragment fragment = new DialogCategory();
            fragment.show(getSupportFragmentManager(), "note");
            return true;
        } else if (id == R.id.share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, message_to_share);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            return true;
        } else if (id == R.id.contact) {
            Intent mail = new Intent(Intent.ACTION_SENDTO);
            mail.setData(Uri.parse("mailto:somtodarlington@yahoo.com"));
            mail.putExtra(Intent.EXTRA_SUBJECT, "Awesome Darlington");
            mail.putExtra(Intent.EXTRA_TEXT, "");
            // Verify that the intent will resolve to an activity
            if (mail.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(mail, "Choose an email client from..."));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public static Activity getInstanceInbox(){
        return activityInbox;
    }



}
