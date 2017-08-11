package com.example.darlington.mydiary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;


import java.text.DateFormat;
import java.util.Date;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow.OnDismissListener;
import com.example.darlington.mydiary.emoji.Emojicon;
import com.example.darlington.mydiary.emoji.EmojiconEditText;
import com.example.darlington.mydiary.emoji.EmojiconGridView;
import com.example.darlington.mydiary.emoji.EmojiconsPopup;

public class Message extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    EditText subject;
    EditText location;
    EditText message;
    String category = "Uncategorized";

    private static final String TAG = MainActivity.class.getSimpleName();


    String font;
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
            } finally {
                c.close();
            }
        }
        if (colour.equals("colour 1")) {
            setTheme(R.style.ThemeOne);
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_one));
                getWindow().setStatusBarColor(getResources().getColor(R.color.theme_one));
            }
        } else if (colour.equals("colour 2")) {
            setTheme(R.style.ThemeTwo);
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_two));
                getWindow().setStatusBarColor(getResources().getColor(R.color.theme_two));
            }
        } else if (colour.equals("colour 3")) {
            setTheme(R.style.ThemeThree);
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_three));
                getWindow().setStatusBarColor(getResources().getColor(R.color.theme_three));
            }
        }


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

        final View rootView = findViewById(R.id.root_view);

        subject = (EditText) findViewById(R.id.subject);
        message = (EditText) findViewById(R.id.body);
        location = (EditText) findViewById(R.id.location);
        {
            DiaryHelper mDbHelper1 = new DiaryHelper(this);
            SQLiteDatabase db1 = mDbHelper1.getReadableDatabase();

            String[] projection1 = {
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
                font = c.getString(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_FONT));
                int text_size = c.getInt(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_FONT_SIZE));
                Typeface typeface = Typeface.createFromAsset(getAssets(), font);
                subject.setTypeface(typeface);
                subject.setTextSize(text_size);
                location.setTypeface(typeface);
                location.setTextSize(text_size);
                message.setTypeface(typeface);
                message.setTextSize(text_size);
            } finally {
                c.close();
            }
        }

        final EmojiconEditText emojiconEditText = (EmojiconEditText) findViewById(R.id.body);
        final ImageView emojiButton = (ImageView) findViewById(R.id.emoji_btn);
        // Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
        final EmojiconsPopup popup = new EmojiconsPopup(rootView, this);
        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();

        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                changeEmojiKeyboardIcon(emojiButton, R.drawable.smiley);
            }
        });

        //If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if (popup.isShowing())
                    popup.dismiss();
            }
        });

        //On emoji clicked, add it to edittext
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (emojiconEditText == null || emojicon == null) {
                    return;
                }

                int start = emojiconEditText.getSelectionStart();
                int end = emojiconEditText.getSelectionEnd();
                if (start < 0) {
                    emojiconEditText.append(emojicon.getEmoji());
                } else {
                    emojiconEditText.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                emojiconEditText.dispatchKeyEvent(event);
            }
        });

        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        emojiButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if (!popup.isShowing()) {

                    //If keyboard is visible, simply show the emoji popup
                    if (popup.isKeyBoardOpen()) {
                        popup.showAtBottom();
                        changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
                    }

                    //else, open the text keyboard first and immediately after that show the emoji popup
                    else {
                        emojiconEditText.setFocusableInTouchMode(true);
                        emojiconEditText.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(emojiconEditText, InputMethodManager.SHOW_IMPLICIT);
                        changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
                    }
                }

                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else {
                    popup.dismiss();
                }
            }
        });
    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId) {
        iconToBeChanged.setImageResource(drawableResourceId);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        category = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        category = "Uncategorized";
    }

    public void save(View view) {
        subject = (EditText) findViewById(R.id.subject);
        message = (EditText) findViewById(R.id.body);
        location = (EditText) findViewById(R.id.location);


        String my_subject = subject.getText().toString();
        String my_location = location.getText().toString();
        String my_message = message.getText().toString();


        if (my_subject.trim().equals("") || my_location.trim().equals("") || my_message.trim().equals("")) {
            if (my_subject.trim().equals("")) {
                subject.setError("Please write a Subject");//it gives user to info message
            } else if (my_location.trim().equals("")) {
                location.setError("Location cannot be empty");//it gives user to info message
            } else if (my_message.trim().equals("")) {
                message.setError("Please write your message");//it gives user to info message
            }
        } else {
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
            values.put(DiaryContract.DiaryEntry.COLUMN_STAR, "unchecked");
            values.put(DiaryContract.DiaryEntry.COLUMN_DATE_TIME, current_date_and_time);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(DiaryContract.DiaryEntry.TABLE_NAME_INBOX, null, values);

            if (newRowId != -1) {
                Inbox.getInstanceInbox().finish();
                Intent i = new Intent(this, Inbox.class);
                startActivity(i);
                finish();
                Toast.makeText(this, "Message saved successfully", Toast.LENGTH_SHORT).show();

            } else {
                Snackbar.make(view, "Failed, please try again.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

}
