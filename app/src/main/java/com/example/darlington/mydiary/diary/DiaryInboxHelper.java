package com.example.darlington.mydiary.diary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Darlington on 6/19/2017.
 */

public class DiaryInboxHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyDiaryMessage.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DiaryContract.DiaryEntry.TABLE_NAME_INBOX + " (" +
                    DiaryContract.DiaryEntry._ID_MESSAGE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DiaryContract.DiaryEntry.COLUMN_SUBJECT + " TEXT," +
                    DiaryContract.DiaryEntry.COLUMN_LOCATION + " TEXT," +
                    DiaryContract.DiaryEntry.COLUMN_MESSAGE + " TEXT," +
                    DiaryContract.DiaryEntry.COLUMN_CATEGORY + " TEXT," +
                    DiaryContract.DiaryEntry.COLUMN_DATE_TIME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DiaryContract.DiaryEntry.TABLE_NAME_INBOX;

    public DiaryInboxHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
