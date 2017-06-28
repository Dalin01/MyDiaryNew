package com.example.darlington.mydiary.diary;

import android.provider.BaseColumns;

/**
 * Created by Darlington on 6/17/2017.
 */

public final class DiaryContract {
    DiaryContract(){}

    public static class DiaryEntry implements BaseColumns
    {
        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUES_ANS = "answer";
        public static final String COLUMN_PASS = "password";

        public static final String _ID_MESSAGE = BaseColumns._ID;
        public static final String TABLE_NAME_INBOX = "inbox";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_DATE_TIME = "dateTime";
        public static final String COLUMN_CATEGORY = "category";
    }
}




