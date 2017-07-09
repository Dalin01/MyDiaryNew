package com.example.darlington.mydiary;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryHelper;
/**
 * Created by DARLINGTON on 7/8/2017.
 */

public class DialogSize extends DialogFragment {

    final CharSequence[] fonts = {"DEFAULT", "MEDIUM", "LARGE"};
    int my_font_size = 17;
    String font;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Text Size")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(fonts, 1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int item) {
                                font = fonts[item].toString();
                            }
                        })
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        getFontSize();
                    }
                })
                .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    public void getFontSize(){
        if (font.equals("DEFAULT")){
            my_font_size = 17;
        }
        else if (font.equals("MEDIUM")){
            my_font_size = 19;
        }
        else if (font.equals("LARGE")){
            my_font_size = 21;
        }
        DiaryHelper mDbHelper = new DiaryHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DiaryContract.DiaryEntry.COLUMN_FONT_SIZE, my_font_size);

        int count = db.update(
                DiaryContract.DiaryEntry.TABLE_NAME,
                values,
                null,
                null
        );

        if (count != -1){
            Toast.makeText(getContext(), "Font size changed successfully", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            startActivity(getActivity().getIntent());
        }
        else{
            Toast.makeText(getContext(), "Failed. Please try again", Toast.LENGTH_SHORT).show();
        }
    }
}
