package com.example.darlington.mydiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // This method is called when the register button is clicked
    // and it launches a new activity (Register)
    public void register(View view) {
        Intent i = new Intent(this, Register.class);
        startActivity(i);
    }


    public void login(View view) {

        EditText text_box = (EditText) findViewById(R.id.pin);
        String pin_provided = text_box.getText().toString().trim();

        DiaryHelper mDbHelper = new DiaryHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DiaryContract.DiaryEntry.COLUMN_PASS,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = DiaryContract.DiaryEntry.COLUMN_PASS + " = ?";

        Cursor cursor = db.query(
                DiaryContract.DiaryEntry.TABLE_NAME,                     // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                               // The sort order
        );

        try {
            cursor.moveToFirst();
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_PASS));
            if(password.equals(pin_provided)){
                Intent i = new Intent(this, Inbox.class);
                startActivity(i);
                text_box.setText("");
                finish();
            }
            else{
                Snackbar.make(view, "Wrong Pin.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                text_box.setText("");
            }
        }
        finally {
            cursor.close();
        }
    }

    public void buttonClicked(View view) {
        Button b = (Button) view; // get the button
        String buttonText = b.getText().toString(); //get text written on button and save it in button text
        setQuestion(buttonText); // call set question method!, and pass the text from the button as argument
    }

    /**
     * A method that gets the text written on the button clicked and displays it on the text view.
     * It also checks if the length of the text in the view is equal or greater than 13, the font should be reduced.
     *
     * @param btnText
     */
    public void setQuestion(String btnText) {
        EditText pin_typed = (EditText) findViewById(R.id.pin);
        String current_pin = pin_typed.getText().toString();
        String new_pin = current_pin + btnText;
        pin_typed.setText(new_pin);
    }

    public void del(View view){
        EditText pin = (EditText) findViewById(R.id.pin);
        pin.setText("");
    }

    public void forgotPin(View view){
        Intent i = new Intent(this, ForgotPassword.class);
        startActivity(i);
    }
}



