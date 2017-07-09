package com.example.darlington.mydiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;

public class MainActivity extends AppCompatActivity {

    // declare variables required
    DiaryHelper mDbHelper;
    SQLiteDatabase db;
    String password = "darlington";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the text view and sex the font to angelina.
        TextView myTextView = (TextView) findViewById(R.id.text);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/angelina.TTF");
        myTextView.setTypeface(typeface);

        mDbHelper = new DiaryHelper(this);  //create an instance of DiaryHelper
        db = mDbHelper.getReadableDatabase(); // get the readable database method for accessing data from the db

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DiaryContract.DiaryEntry.COLUMN_PASS,
        };

        Cursor cursor = db.query(
                DiaryContract.DiaryEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                               // The sort order
        );

        try {
            cursor.moveToFirst(); // move to the first row in the db
            password = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_PASS)); //get the value/string and save it in the variable password
        }
        catch (CursorIndexOutOfBoundsException e) { // catch the error and display a toast message instead
            Toast.makeText(this, "Register to gain access", Toast.LENGTH_LONG).show();
        }
        finally {
            cursor.close(); // close the cursor
        }

    }

    // This method is called when the register button is clicked
    // and it launches a new activity (Register)
    public void register(View view) {
        Intent i = new Intent(this, Register.class);
        i.putExtra("Register", "register");
        startActivity(i);
    }


    // Method that checks if the provided password is same as the given password
    // and if they are same, the user is granted access into his/her account.
    public void login(View view) {
        EditText text_box = (EditText) findViewById(R.id.pin); // get the id for the pin edit text box
        String pin_provided = text_box.getText().toString().trim(); // get the pin provided.
        // check if pin equals password, if true open account else provide feedback using a snack bar
        if (password.equals(pin_provided)) {
            Intent i = new Intent(this, Inbox.class);
            startActivity(i);
            finish();
        } else {
            Snackbar.make(view, "Wrong Pin.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            text_box.setText("");
        }

    }

    // method the appends a string to the existing string in the edit text field and checks
    // if the current string matches the password
    public void buttonClicked(View view) {
        Button b = (Button) view; // get the button
        String buttonText = b.getText().toString(); //get text written on button and save it in button text
        setQuestion(buttonText); // call set question method!, and pass the text from the button as argument
    }

    /**
     * A method that gets the text written on the button clicked and displays it on the text view.
     * It also checks the current pin with the password.
     */
    public void setQuestion(String btnText) {
        EditText pin_typed = (EditText) findViewById(R.id.pin);
        String current_pin = pin_typed.getText().toString();
        String new_pin = current_pin + btnText;
        pin_typed.setText(new_pin);
        EditText text_box = (EditText) findViewById(R.id.pin); // get the id for the pin edit text box
        String pin_provided = text_box.getText().toString().trim(); // get the pin provided.
        // check if pin equals password, if true open account else provide feedback using a snack bar
        if (password.equals(pin_provided)) {
            Intent i = new Intent(this, Inbox.class);
            startActivity(i);
            finish();
        }

    }

    // method that clears up the edit text field
    public void del(View view) {
        EditText pin = (EditText) findViewById(R.id.pin);
        pin.setText("");
    }

    // opens up an activity for forgot pin
    public void forgotPin(View view) {
        Intent i = new Intent(this, Register.class);
        i.putExtra("Register", "not used");
        startActivity(i);
    }
}



