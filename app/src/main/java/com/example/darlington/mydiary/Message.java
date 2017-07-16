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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;

import java.text.DateFormat;
import java.util.Date;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.provider.Settings;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

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

    String font;

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
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
            }
            finally {
                c.close();
            }
        }


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


    public void cam(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (getFromPref(this, ALLOW_KEY)) {
                showSettingsAlert();
            } else if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)

                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {
            openCamera();
        }
    }

    public static void saveToPreferences(Context context, String key, Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.commit();
    }

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(Message.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(Message.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(Message.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(Message.this);
                    }
                });

        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];

                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean
                                showRationale =
                                ActivityCompat.shouldShowRequestPermissionRationale(
                                        this, permission);

                        if (showRationale) {
                            showAlert();
                        } else if (!showRationale) {
                            // user denied flagging NEVER ASK AGAIN
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting
                            saveToPreferences(Message.this, ALLOW_KEY, true);
                        }
                    }
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }

}
