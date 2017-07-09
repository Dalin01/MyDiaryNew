package com.example.darlington.mydiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;

public class Home extends AppCompatActivity {

    public static int my_id;
    public String subject;
    public String location;
    public String message;
    public String category;
    public String date_time;
    public String message_to_share;
    public String font;
    String colour;

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

        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();
        subject = extras.getString("Sub");
        location = extras.getString("Loc");
        message = extras.getString("Mes");
        category = extras.getString("Cat");
        date_time = extras.getString("date");
        my_id = extras.getInt("id");
        font = extras.getString("font");
        int text_size = extras.getInt("text_size");


        TextView sub = (TextView) findViewById(R.id.home_sub);
        TextView loc = (TextView) findViewById(R.id.home_location);
        TextView mess = (TextView) findViewById(R.id.home_message);
        TextView cat = (TextView) findViewById(R.id.home_cat);
        TextView date = (TextView) findViewById(R.id.home_date_time);

        sub.setText(subject);
        loc.setText(location);
        mess.setText(message);
        cat.setText(category);
        date.setText(date_time);

        Typeface typeface = Typeface.createFromAsset(getAssets(), font);
        sub.setTypeface(typeface);
        sub.setTextSize(text_size);
        loc.setTypeface(typeface);
        loc.setTextSize(text_size);
        mess.setTypeface(typeface);
        mess.setTextSize(text_size);
        cat.setTypeface(typeface);
        cat.setTextSize(text_size);
        date.setTypeface(typeface);
        date.setTextSize(text_size);

        message_to_share = getString(R.string.subject) + subject + "\n\n" + getString(R.string.message) +"\n" + message;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.edit) {
            edit();
        }
        else if(id == R.id.share){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, message_to_share);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            return true;
        }
        else if(id == R.id.delete){
            DialogFragment fragment = new DeleteCurrentDialog();
            fragment.show(getSupportFragmentManager(), "note");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void edit(){
       Intent edit_message = new Intent(this, EditMessage.class);
       edit_message.putExtra("id", my_id);
       edit_message.putExtra("subject", subject);
       edit_message.putExtra("location", location);
       edit_message.putExtra("message", message);
       edit_message.putExtra("category", category);
       edit_message.putExtra("time", date_time);
       startActivity(edit_message);
    }
}
