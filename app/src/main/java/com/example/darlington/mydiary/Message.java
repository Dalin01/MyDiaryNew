package com.example.darlington.mydiary;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;

import java.text.DateFormat;
import java.util.Date;

public class Message extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EmojiconEditText subject;
    EmojiconEditText location;
    EmojiconEditText message;
    String category = "Uncategorized";

    private static final String TAG = MainActivity.class.getSimpleName();
    EmojiconEditText emojiconEditText;
    EmojiconTextView textView;
    ImageView emojiImageView;
    ImageView emojiImageView1;
    ImageView emojiImageView2;
    View rootView;
    EmojIconActions emojIcon;
    EmojIconActions emojIcon1;
    EmojIconActions emojIcon2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Spinner spinner = (Spinner) findViewById(R.id.category);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        rootView = findViewById(R.id.root_view);

        emojiImageView = (ImageView) findViewById(R.id.emoji_btn);
        emojiImageView1 = (ImageView) findViewById(R.id.emoji_btn1);
        emojiImageView2 = (ImageView) findViewById(R.id.emoji_btn2);

        subject = (EmojiconEditText) findViewById(R.id.subject);
        message = (EmojiconEditText) findViewById(R.id.body);
        location = (EmojiconEditText) findViewById(R.id.location);


        emojIcon = new EmojIconActions(this, rootView, subject, emojiImageView);
        emojIcon1 = new EmojIconActions(this, rootView, location, emojiImageView1);
        emojIcon2 = new EmojIconActions(this, rootView, message, emojiImageView2);


        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }
        });

        emojIcon1.ShowEmojIcon();
        emojIcon1.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon1.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }
        });


        emojIcon2.ShowEmojIcon();
        emojIcon2.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon2.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item_from_spinner = parent.getItemAtPosition(position).toString();
        category = item_from_spinner;
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        category = "Uncategorized";
    }

    public void save(View view) {
        subject = (EmojiconEditText) findViewById(R.id.subject);
        message = (EmojiconEditText) findViewById(R.id.body);
        location = (EmojiconEditText) findViewById(R.id.location);

        String my_subject = subject.getText().toString();
        String my_location = location.getText().toString();
        String my_message = message.getText().toString();


        if (my_subject.trim().equals("") || my_location.trim().equals("") || my_message.trim().equals("")){
            if (my_subject.trim().equals("")){
                subject.setError("Please write a Subject");//it gives user to info message
            }
            else if(my_location.trim().equals("")){
                location.setError("Location cannot be empty");//it gives user to info message
            }
            else if(my_message.trim().equals("")){
                message.setError("Please write your message");//it gives user to info message
            }
        }
        else{
            String current_date_and_time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date());

            DiaryInboxHelper mDbHelper = new DiaryInboxHelper(this);
            // Gets the data repository in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DiaryContract.DiaryEntry.COLUMN_SUBJECT, my_subject);
            values.put(DiaryContract.DiaryEntry.COLUMN_LOCATION, my_location);
            values.put(DiaryContract.DiaryEntry.COLUMN_MESSAGE, my_message);
            values.put(DiaryContract.DiaryEntry.COLUMN_CATEGORY, category);
            values.put(DiaryContract.DiaryEntry.COLUMN_DATE_TIME, current_date_and_time);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(DiaryContract.DiaryEntry.TABLE_NAME_INBOX, null, values);

            if (newRowId != -1) {
                subject.setText("");
                location.setText("");
                message.setText("");

                Snackbar.make(view, "You message has been saved successfully", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            } else {
                Snackbar.make(view, "Failed, please try again.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }


    }
}
