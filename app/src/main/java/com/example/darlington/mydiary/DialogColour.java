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

public class DialogColour extends DialogFragment {

    final CharSequence[] fonts = {"DEFAULT", "PURPLE", "GREEN", "PINK"};
    String my_font_colour = "default";
    String font;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Colour")
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
                        getFont();
                    }
                })
                .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    private void getFont(){
        if(font != null){
            if (font.equals("DEFAULT")){
                my_font_colour = "default";
            }
            else if (font.equals("PURPLE")){
                my_font_colour = "colour 1";
            }
            else if (font.equals("GREEN")){
                my_font_colour = "colour 2";
            }
            else if (font.equals("PINK")){
                my_font_colour = "colour 3";
            }

            DiaryHelper mDbHelper = new DiaryHelper(getActivity());
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DiaryContract.DiaryEntry.COLUMN_COLOUR, my_font_colour);

            int count = db.update(
                    DiaryContract.DiaryEntry.TABLE_NAME,
                    values,
                    null,
                    null
            );

            if (count != -1){
                Toast.makeText(getContext(), "Colour changed successfully", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
            else{
                Toast.makeText(getContext(), "Failed. Please try again", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getContext(), "Pick a colour and click OK", Toast.LENGTH_SHORT).show();
        }

    }
}
