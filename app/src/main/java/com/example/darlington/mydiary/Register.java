package com.example.darlington.mydiary;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;

public class Register extends AppCompatActivity {

    EditText name;
    EditText passwrd;
    EditText answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    public void reg(View view) {

        name = (EditText) findViewById(R.id.my_name);
        passwrd = (EditText) findViewById(R.id.passwrd);
        answer = (EditText) findViewById(R.id.answer_to_question);

        String my_string = name.getText().toString().trim();
        String pass = passwrd.getText().toString().trim();
        String my_answer = answer.getText().toString().trim();

        DiaryHelper mDbHelper = new DiaryHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DiaryContract.DiaryEntry.COLUMN_NAME, my_string);
        values.put(DiaryContract.DiaryEntry.COLUMN_QUES_ANS, my_answer);
        values.put(DiaryContract.DiaryEntry.COLUMN_PASS, pass);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DiaryContract.DiaryEntry.TABLE_NAME, null, values);

        if (newRowId != -1) {
            Snackbar.make(view, "You have registered successfullly.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            name.setText("");
            passwrd.setText("");
            answer.setText("");

        } else {
            Snackbar.make(view, "Failed to register, try again.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            name.setText("");
            passwrd.setText("");
            answer.setText("");
        }

    }
}
