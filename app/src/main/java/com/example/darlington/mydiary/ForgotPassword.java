package com.example.darlington.mydiary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;

public class ForgotPassword extends AppCompatActivity {

    EditText fav_food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

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
        String[] selectionArgs = { "1" };

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
            if (my_fav_food.equals(answer)){
                String password = c.getString(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_PASS));
                Snackbar.make(view, "Correct. Your pin is "+password, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fav_food.setText("");
            }
            else{
                Snackbar.make(view, "Failed. Incorrect answer to question", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fav_food.setText("");
            }
        }
        finally {
            c.close();
        }
    }
}
