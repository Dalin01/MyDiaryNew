package com.example.darlington.mydiary;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by DARLINGTON on 8/11/2017.
 */

public class DialogUpdatePin extends DialogFragment {

    View textEntryView;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        textEntryView = inflater.inflate(R.layout.dialog_update_pin, null);
        builder.setView(textEntryView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        verify();

                    }
                });

        return builder.create();
    }

    public void verify() {

        EditText old_pin = (EditText) textEntryView.findViewById(R.id.old_pin);
        String my_old_pin = old_pin.getText().toString().trim();

        DiaryHelper mDbHelper = new DiaryHelper(getActivity());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DiaryContract.DiaryEntry._ID,
                DiaryContract.DiaryEntry.COLUMN_PASS,
        };

        String selection = DiaryContract.DiaryEntry._ID + " = ?";
        String[] selectionArgs = {"1"};

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
            String answer = c.getString(c.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_PASS));
            if (my_old_pin.equals(answer)) {
                update();
            } else {
                Toast.makeText(getActivity(), "Incorrect Pin", Toast.LENGTH_SHORT).show();
            }
        } catch (CursorIndexOutOfBoundsException e) { // catch the error and display a toast message instead
            Toast.makeText(getActivity(), "Failed. Try again", Toast.LENGTH_LONG).show();
        } finally {
            c.close();
        }
    }

    public void update() {
        EditText new_pin = (EditText) textEntryView.findViewById(R.id.new_pin);
        String my_new_pin = new_pin.getText().toString();
        if (my_new_pin.trim().equals("")) {
            Toast.makeText(getActivity(), "Pin cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            DiaryHelper mDbHelper = new DiaryHelper(getActivity());
            // Gets the data repository in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DiaryContract.DiaryEntry.COLUMN_PASS, my_new_pin);
            String selection = DiaryContract.DiaryEntry._ID + " = ?";
            String[] selectionArgs = {"1"};
            try{
                // Insert the new row, returning the primary key value of the new row
                int newRowId = db.update(DiaryContract.DiaryEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                if (newRowId != -1) {
                    getActivity().finish();
                    Intent i = new Intent(getActivity(), Inbox.class);
                    startActivity(i);
                } else {
                    Snackbar.make(getActivity().findViewById(R.id.main), "Failed, please try again.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
            catch (SQLiteException e){
                Toast.makeText(getActivity(), "Error, please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
