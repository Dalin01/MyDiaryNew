package com.example.darlington.mydiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();
        String subject = extras.getString("Sub");
        String location = extras.getString("Loc");
        String message = extras.getString("Mes");
        String category = extras.getString("Cat");
        String date_time = extras.getString("date");

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
}
