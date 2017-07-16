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

import java.util.ArrayList;

/**
 * Created by DARLINGTON on 7/3/2017.
 */

public class DialogFonts extends DialogFragment {

    final CharSequence[] fonts = {"DEFAULT", "ANGELINA", "PETBONE", "COOTER CANDY", "KBERRY", "ROMANS", "TACO BELL", "TWIN MARKER"};
    String my_font = "fonts/roboto_regular.0tf";
    String font;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Fonts")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(fonts, 1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int item) {
                                    if(fonts[item] != null)
                                    {
                                        font = fonts[item].toString();
                                    }
                                    else{
                                        font = "ANGELINA";
                                    }
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

    public void getFont(){
        if (font != null) {
            if (font.equals("DEFAULT")){
                my_font = "fonts/roboto_regular.otf";
            }
            else if (font.equals("ANGELINA")){
                my_font = "fonts/angelina.TTF";
            }
            else if (font.equals("PETBONE")){
                my_font = "fonts/petbone.ttf";
            }
            else if (font.equals("COOTER CANDY")){
                my_font = "fonts/cooter_candy.ttf";
            }
            else if (font.equals("KBERRY")){
                my_font = "fonts/kberry.ttf";
            }
            else if (font.equals("ROMANS")){
                my_font = "fonts/romans.ttf";
            }
            else if (font.equals("TACO BELL")){
                my_font = "fonts/taco_bell.ttf";
            }
            else if (font.equals("TWIN MARKER")){
                my_font = "fonts/twinmarker.ttf";
            }

            DiaryHelper mDbHelper = new DiaryHelper(getActivity());
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DiaryContract.DiaryEntry.COLUMN_FONT, my_font);

            int count = db.update(
                    DiaryContract.DiaryEntry.TABLE_NAME,
                    values,
                    null,
                    null
            );

            if (count != -1){
                Toast.makeText(getContext(), "Font updated successfully", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
            else{
                Toast.makeText(getContext(), "Failed. Please try again", Toast.LENGTH_SHORT).show();
            }
        }

        else{
            Toast.makeText(getContext(), "Select a font then click OK", Toast.LENGTH_SHORT).show();
        }


    }
}





