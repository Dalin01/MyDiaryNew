package com.example.darlington.mydiary.diary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Darlington on 6/17/2017.
 */

public class DiaryHelper extends SQLiteOpenHelper {

    // Schema to create the db if non exists or create a table
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyDiary.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DiaryContract.DiaryEntry.TABLE_NAME + " (" +
                    DiaryContract.DiaryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DiaryContract.DiaryEntry.COLUMN_NAME + " TEXT," +
                    DiaryContract.DiaryEntry.COLUMN_QUES_ANS + " TEXT," +
                    DiaryContract.DiaryEntry.COLUMN_PASS + " TEXT," +
                    DiaryContract.DiaryEntry.COLUMN_FONT_SIZE + " INTEGER," +
                    DiaryContract.DiaryEntry.COLUMN_COLOUR + " TEXT," +
                    DiaryContract.DiaryEntry.COLUMN_FONT + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DiaryContract.DiaryEntry.TABLE_NAME;

    public DiaryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db); // check on this before launch.
    }
}
