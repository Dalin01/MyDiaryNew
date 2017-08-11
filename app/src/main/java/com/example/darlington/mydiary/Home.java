package com.example.darlington.mydiary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;


public class Home extends AppCompatActivity {


    public static int my_id;
    public String subject;
    public String location;
    public String message;
    public String category;
    public String date_time;
    public String message_to_share;
    public String font;
    String colour;
    String my_star;
    static Activity activityHome;


    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

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
        switch (colour) {
            case "colour 1":
                setTheme(R.style.ThemeOne);
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_one));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.theme_one));
                }
                break;
            case "colour 2":
                setTheme(R.style.ThemeTwo);
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_two));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.theme_two));
                }
                break;
            case "colour 3":
                setTheme(R.style.ThemeThree);
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_three));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.theme_three));
                }
                break;
        }

        setContentView(R.layout.activity_home);
        activityHome = this;

        Bundle extras = getIntent().getExtras();
        subject = extras.getString("Sub");
        location = extras.getString("Loc");
        message = extras.getString("Mes");
        category = extras.getString("Cat");
        date_time = extras.getString("date");
        my_id = extras.getInt("id");
        font = extras.getString("font");
        int text_size = extras.getInt("text_size");
        my_star = extras.getString("star");


        TextView sub = (TextView) findViewById(R.id.home_sub);
        TextView loc = (TextView) findViewById(R.id.home_location);
        TextView mess = (TextView) findViewById(R.id.home_message);
        TextView cat = (TextView) findViewById(R.id.home_cat);
        TextView date = (TextView) findViewById(R.id.home_date_time);
        CheckBox checkBox = (CheckBox) findViewById(R.id.star);
        ImageButton my_picture = (ImageButton) findViewById(R.id.my_image);
        CheckBox checkbox1 = (CheckBox) findViewById(R.id.star1);
        TextView sub1 = (TextView) findViewById(R.id.home_sub1);

        LinearLayout no_image_layout = (LinearLayout) findViewById(R.id.no_image);
        LinearLayout yes_image_layout = (LinearLayout) findViewById(R.id.yes_image);

        if (category.equals("Work") || category.equals("School") || category.equals("Personal") || category.equals("Family") || category.equals("Study") ||
                category.equals("Research") || category.equals("Uncategorized")){

            no_image_layout.setVisibility(View.VISIBLE);
            yes_image_layout.setVisibility(View.GONE);

            if (my_star.equals("checked")) {
                checkBox.setChecked(true);
            }
            else{
                checkBox.setChecked(false);
            }

            sub.setText(subject);
            loc.setText(location);
            mess.setText(message);
            cat.setText(category);
            date.setText(date_time);

            Typeface typeface = Typeface.createFromAsset(getAssets(), font);
            sub.setTypeface(typeface);
            sub.setTextSize(text_size);
            loc.setTypeface(typeface);
            loc.setTextSize(text_size);
            mess.setTypeface(typeface);
            mess.setTextSize(text_size);
            cat.setTypeface(typeface);
            cat.setTextSize(text_size);
            date.setTypeface(typeface);
            date.setTextSize(text_size);
        }

        else{
            no_image_layout.setVisibility(View.GONE);
            yes_image_layout.setVisibility(View.VISIBLE);

            if (my_star.equals("checked")) {
                checkbox1.setChecked(true);
            }
            else{
                checkbox1.setChecked(false);
            }


            sub1.setText(subject);
            loc.setText(location);

            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(message);
            mess.setText(builder);

            date.setText(date_time);

            Typeface typeface = Typeface.createFromAsset(getAssets(), font);
            sub1.setTypeface(typeface);
            sub1.setTextSize(text_size);
            loc.setTypeface(typeface);
            loc.setTextSize(text_size);
            mess.setTypeface(typeface);
            mess.setTextSize(text_size);
            date.setTypeface(typeface);
            date.setTextSize(text_size);

            Glide.with(this)
                    .load(category)
                    .asBitmap()
                    .placeholder(R.drawable.bck)
                    .into(my_picture);

            // Hook up clicks on the thumbnail views.

            final View thumb1View = findViewById(R.id.my_image);
            thumb1View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    zoomImageFromThumb(thumb1View);
                }
            });

            // Retrieve and cache the system's default "short" animation time.
            mShortAnimationDuration = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
        }

        message_to_share = getString(R.string.subject) + subject + "\n\n" + getString(R.string.message) +"\n" + message;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.edit) {
            edit();
        }
        else if(id == R.id.share){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, message_to_share);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            return true;
        }
        else if(id == R.id.delete){
            DialogFragment fragment = new DeleteCurrentDialog();
            fragment.show(getSupportFragmentManager(), "note");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void edit(){
       Intent edit_message = new Intent(this, EditMessage.class);
       edit_message.putExtra("id", my_id);
       edit_message.putExtra("subject", subject);
       edit_message.putExtra("location", location);
       edit_message.putExtra("message", message);
       edit_message.putExtra("category", category);
       edit_message.putExtra("time", date_time);
       startActivity(edit_message);
    }

    public void star(View view) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.star);
        DiaryInboxHelper mDbHelper = new DiaryInboxHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (checkBox.isChecked()) {
            ContentValues values = new ContentValues();
            values.put(DiaryContract.DiaryEntry.COLUMN_STAR, "checked");
            String selection = DiaryContract.DiaryEntry._ID_MESSAGE + " LIKE ?";
            String[] selectionArgs = {String.valueOf(my_id)};

            int count = db.update(
                    DiaryContract.DiaryEntry.TABLE_NAME_INBOX,
                    values,
                    selection,
                    selectionArgs);
            if (count != -1){
                Toast.makeText(this, "Message Starred", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Ops!, try again", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            ContentValues values = new ContentValues();
            values.put(DiaryContract.DiaryEntry.COLUMN_STAR, "unchecked");
            String selection = DiaryContract.DiaryEntry._ID_MESSAGE + " LIKE ?";
            String[] selectionArgs = {String.valueOf(my_id)};

            int count = db.update(
                    DiaryContract.DiaryEntry.TABLE_NAME_INBOX,
                    values,
                    selection,
                    selectionArgs);
            if (count != -1){
                Toast.makeText(this, "Message Unstarred", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Ops!, try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void starOne(View view) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.star1);
        DiaryInboxHelper mDbHelper = new DiaryInboxHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (checkBox.isChecked()) {
            ContentValues values = new ContentValues();
            values.put(DiaryContract.DiaryEntry.COLUMN_STAR, "checked");
            String selection = DiaryContract.DiaryEntry._ID_MESSAGE + " LIKE ?";
            String[] selectionArgs = {String.valueOf(my_id)};

            int count = db.update(
                    DiaryContract.DiaryEntry.TABLE_NAME_INBOX,
                    values,
                    selection,
                    selectionArgs);
            if (count != -1){
                Toast.makeText(this, "Message Starred", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Ops!, try again", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            ContentValues values = new ContentValues();
            values.put(DiaryContract.DiaryEntry.COLUMN_STAR, "unchecked");
            String selection = DiaryContract.DiaryEntry._ID_MESSAGE + " LIKE ?";
            String[] selectionArgs = {String.valueOf(my_id)};

            int count = db.update(
                    DiaryContract.DiaryEntry.TABLE_NAME_INBOX,
                    values,
                    selection,
                    selectionArgs);
            if (count != -1){
                Toast.makeText(this, "Message Unstarred", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Ops!, try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Activity getInstanceHome(){
        return activityHome;
    }


    private void zoomImageFromThumb(final View thumbView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);

        Glide.with(this)
                .load(category)
                .asBitmap()
                .into(expandedImageView);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }


}
