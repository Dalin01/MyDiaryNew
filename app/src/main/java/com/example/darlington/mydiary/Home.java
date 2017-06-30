package com.example.darlington.mydiary;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;

public class Home extends AppCompatActivity {

    public static int my_id;
    public String subject;
    public String location;
    public String message;
    public String category;
    public String date_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();
        subject = extras.getString("Sub");
        location = extras.getString("Loc");
        message = extras.getString("Mes");
        category = extras.getString("Cat");
        date_time = extras.getString("date");
        my_id = extras.getInt("id");

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
