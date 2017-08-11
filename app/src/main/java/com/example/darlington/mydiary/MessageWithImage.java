package com.example.darlington.mydiary;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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



public class MessageWithImage extends AppCompatActivity {

    EditText subject;
    EditText location;
    EditText message;
    String category = "Uncategorized";

    private static final String TAG = MainActivity.class.getSimpleName();

    String font;
    String colour;

    // Activity result key for camera
    static final int REQUEST_TAKE_PHOTO = 11111;

    // Image view for showing our image.
    private ImageView mImageView;
    private ImageView mThumbnailImageView;


    // Storage for camera image URI components
    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    private final static String CAPTURED_PHOTO_URI_KEY = "mCapturedImageURI";

    // Required for camera operations in order to save the image file on resume.
    private String mCurrentPhotoPath = null;
    private Uri mCapturedImageURI = null;

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

        setContentView(R.layout.activity_message_with_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        // Set the image view
        mImageView = (ImageView) findViewById(R.id.imageViewFullSized);
        mThumbnailImageView = (ImageView) findViewById(R.id.imageViewThumbnail);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Message");
                    if (mCurrentPhotoPath != null) {
                        mThumbnailImageView.setVisibility(View.VISIBLE);
                        setFullImageFromFilePath1(getCurrentPhotoPath(), mThumbnailImageView);
                    }
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    mThumbnailImageView.setVisibility(View.GONE);
                    isShow = false;
                }
            }
        });


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

    public void save(View view) {
        subject = (EditText) findViewById(R.id.subject);
        message = (EditText) findViewById(R.id.body);
        location = (EditText) findViewById(R.id.location);

        String my_subject = subject.getText().toString();
        String my_location = location.getText().toString();
        String my_message = message.getText().toString();

        category = getCurrentPhotoPath();

        if (my_subject.trim().equals("") || my_location.trim().equals("") || my_message.trim().equals("") || category == null) {
            if (my_subject.trim().equals("")) {
                subject.setError("Please write a Subject");//it gives user to info message
            } else if (my_location.trim().equals("")) {
                location.setError("Location cannot be empty");//it gives user to info message
            } else if (my_message.trim().equals("")) {
                message.setError("Please write your message");//it gives user to info message
            } else {
                Toast.makeText(this, "You must take a photo.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
        }
        if (mCapturedImageURI != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_URI_KEY, mCapturedImageURI.toString());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_URI_KEY)) {
            mCapturedImageURI = Uri.parse(savedInstanceState.getString(CAPTURED_PHOTO_URI_KEY));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Getters and setters.
     */

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public Uri getCapturedImageURI() {
        return mCapturedImageURI;
    }

    public void setCapturedImageURI(Uri mCapturedImageURI) {
        this.mCapturedImageURI = mCapturedImageURI;
    }


    public void takePhoto(View view) {
        checkCameraPermission();
        //dispatchTakePictureIntent();
    }

    /**
     * Start the camera by dispatching a camera intent.
     */
    protected void dispatchTakePictureIntent() {
        PackageManager packageManager = getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(this, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                setCapturedImageURI(fileUri);
                setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        getCapturedImageURI());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Creates the image file to which the image must be saved.
     *
     * @return
     * @throws IOException
     */
    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        setCurrentPhotoPath("file:" + image.getAbsolutePath());
        return image;
    }

    /**
     * The activity returns with the photo.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
           // addPhotoToGallery();
            // Show the full sized image.
            setFullImageFromFilePath1(getCurrentPhotoPath(), mImageView);

        } else {
            Toast.makeText(this, "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Add the picture to the photo gallery.
     * Must be called on all camera images or they will
     * disappear once taken.
     */
    protected void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private void setFullImageFromFilePath1(String imagePath, ImageView imageView) {
        Glide.with(this)
                .load(imagePath)
                .asBitmap()
                .placeholder(R.drawable.bck)
                .into(imageView);
    }

    public void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_TAKE_PHOTO);
        }
        else{
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();

                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
