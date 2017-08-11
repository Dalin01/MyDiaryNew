package com.example.darlington.mydiary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;

public class Register extends AppCompatActivity {

    //declare required variables
    EditText name;
    EditText password;
    EditText answer;
    EditText fav_food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //get the intent and recover the message sent along with it
        Bundle extras = getIntent().getExtras();
        String register = extras.getString("Register");

        //get the layout for registration and password recovery
        View v1 = findViewById(R.id.register_layout);
        View v2 = findViewById(R.id.forgot_password__layout);

        //set visibility accordingly
        if (register.equals("register")) {
            v1.setVisibility(View.VISIBLE);
        } else {
            v2.setVisibility(View.VISIBLE);
        }

        //Get the text view and sex the font to angelina.
        TextView myTextView = (TextView) findViewById(R.id.fname);
        TextView myTextView1 = (TextView) findViewById(R.id.pin);
        TextView myTextView2 = (TextView) findViewById(R.id.sec_ques);
        TextView myTextView3 = (TextView) findViewById(R.id.ques);
        TextView myTextView4 = (TextView) findViewById(R.id.rec_ques);
        Button button_one = (Button) findViewById(R.id.login);
        Button button_two = (Button) findViewById(R.id.login1);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/roboto_regular.otf");
        myTextView.setTypeface(typeface);
        myTextView1.setTypeface(typeface);
        myTextView2.setTypeface(typeface);
        myTextView3.setTypeface(typeface);
        myTextView4.setTypeface(typeface);
        button_one.setTypeface(typeface);
        button_two.setTypeface(typeface);
    }

    //method that gets the values from the input fields and saves them in the db
    public void reg(View view) {

        DiaryHelper mDbHelper = new DiaryHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DiaryContract.DiaryEntry.TABLE_NAME,                     // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                               // The sort order
        );

        double count = cursor.getCount();
        int count_int = (int) count;

        if (count_int == 0) {
            //get the IDs required
            name = (EditText) findViewById(R.id.my_name);
            password = (EditText) findViewById(R.id.passwrd);
            answer = (EditText) findViewById(R.id.answer_to_question);

            //get the values in them in string format
            String my_string = name.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String my_answer = answer.getText().toString().trim();

            if (my_string.equals("") || pass.equals("") || my_answer.equals("")) {
                if (my_string.equals("")) {
                    name.setError("Name cannot be empty");//it gives user to info message
                } else if (pass.equals("")) {
                    password.setError("Password cannot be empty");//it gives user to info message
                } else if (my_answer.equals("")) {
                    answer.setError("Answer to security question cannot be empty");//it gives user to info message
                }
            } else {
                SQLiteDatabase db1 = mDbHelper.getWritableDatabase();
                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(DiaryContract.DiaryEntry.COLUMN_NAME, my_string);
                values.put(DiaryContract.DiaryEntry.COLUMN_QUES_ANS, my_answer);
                values.put(DiaryContract.DiaryEntry.COLUMN_PASS, pass);
                values.put(DiaryContract.DiaryEntry.COLUMN_FONT_SIZE, 17);
                values.put(DiaryContract.DiaryEntry.COLUMN_COLOUR, "default");
                values.put(DiaryContract.DiaryEntry.COLUMN_FONT, "fonts/roboto_regular.otf");

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db1.insert(DiaryContract.DiaryEntry.TABLE_NAME, null, values);

                if (newRowId != -1) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();//if successful say it through a toast message
                    finish();// close the current activity
                    MainActivity.getInstance().finish();
                    Intent i = new Intent(this, Inbox.class);// create an instance of Intent while passing this current activity and the Inbox as arguments
                    startActivity(i);//start new activity
                } else {
                    //registration failed, tell the user through a toast and set the Edit Texts to empty
                    Snackbar.make(view, "Failed to register, try again.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    name.setText("");
                    password.setText("");
                    answer.setText("");
                }
            }
        } else {
            Snackbar.make(view, "A user already exist, you can not register again.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }


    //method for password recovery
    //get the text in the view,
    //get the value of password in the db
    //make comparison, if same, provide the necessary feedback
    //else, provide the right feedback also
    public void verify(View view) {

        fav_food = (EditText) findViewById(R.id.fav_food);

        String my_fav_food = fav_food.getText().toString().trim();

        DiaryHelper mDbHelper = new DiaryHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DiaryContract.DiaryEntry._ID,
                DiaryContract.DiaryEntry.COLUMN_QUES_ANS,
                DiaryContract.DiaryEntry.COLUMN_PASS,
        };

        String selection = DiaryContract.DiaryEntry._ID + " = ?";
        String[] selectionArgs = {"1"};

        Cursor c = db.query(
                DiaryContract.DiaryEntry.TABLE_NAME,
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null
        );

        try {
            c.moveToFirst();
            String answer = c.getString(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_QUES_ANS));
            if (my_fav_food.equals(answer)) {
                String password = c.getString(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_PASS));
                Snackbar.make(view, "Correct. Your pin is " + password, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fav_food.setText("");
            } else {
                Snackbar.make(view, "Failed. Incorrect answer to question", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fav_food.setText("");
            }
        } catch (CursorIndexOutOfBoundsException e) { // catch the error and display a toast message instead
            Toast.makeText(this, "You can't recover your password since you are yet to register. Please register.", Toast.LENGTH_LONG).show();
        } finally {
            c.close();
        }
    }
}
