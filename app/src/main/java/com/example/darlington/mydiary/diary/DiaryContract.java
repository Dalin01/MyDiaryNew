package com.example.darlington.mydiary.diary;

import android.provider.BaseColumns;
import android.text.Editable;
import android.text.Spanned;

/**
 * Created by Darlington on 6/17/2017.
 * A contract class to define the constants in the db.
 * I used one contract class for both dbs
 */

public final class DiaryContract {


    public static class DiaryEntry implements BaseColumns
    {

        //Table one column names
        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUES_ANS = "answer";
        public static final String COLUMN_PASS = "password";
        public static final String COLUMN_FONT = "font";
        public static final String COLUMN_FONT_SIZE = "size";
        public static final String COLUMN_COLOUR = "colour";

        //Table two column names
        public static final String _ID_MESSAGE = BaseColumns._ID;
        public static final String TABLE_NAME_INBOX = "inbox";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_DATE_TIME = "dateTime";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_STAR = "star";
    }
}




