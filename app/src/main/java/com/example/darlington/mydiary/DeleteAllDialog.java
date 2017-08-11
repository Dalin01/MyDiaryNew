package com.example.darlington.mydiary;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;

/**
 * Created by DARLINGTON on 6/28/2017.
 */

public class DeleteAllDialog extends DialogFragment {

    //pops up a dialog that ensures and asks if the user is certain of the implications
    //if he/she clicks "ok" then it proceeds else return back
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = "You are about to delete all your saved messages." +
                "\n\n\nIt can't be revoked";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Note:");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DiaryInboxHelper mDbHelper = new DiaryInboxHelper(getActivity());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                try {
                    int feedback = db.delete(
                            DiaryContract.DiaryEntry.TABLE_NAME_INBOX,
                            null,
                            null);

                    if (feedback != -1) {
                        getActivity().finish();
                        Intent i = new Intent(getActivity(), Inbox.class);
                        startActivity(i);

                    } else {
                        Snackbar.make(getActivity().findViewById(R.id.main), "Failed, please try again.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } finally {
                    db.close();
                }
            }
        });
        builder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

}
