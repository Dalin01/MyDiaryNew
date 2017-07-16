package com.example.darlington.mydiary;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.darlington.mydiary.diary.DiaryContract;
import com.example.darlington.mydiary.diary.DiaryInboxHelper;

/**
 * Created by DARLINGTON on 6/30/2017.
 */

public class DeleteCurrentDialog extends DialogFragment {

    String message = "You are about to delete this current message." +
            "\n\n\nIt can't be revoked";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Note:");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete();
            }
        });
        builder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    //delete the message required using the specified where clause as guide.
    public void delete(){
        DiaryInboxHelper mDbHelper = new DiaryInboxHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = DiaryContract.DiaryEntry._ID_MESSAGE + " LIKE ?"; // where clause
        String[] selectionArgs = { String.valueOf(Home.my_id) }; // value of clause

        try{
            int feedback = db.delete(
                    DiaryContract.DiaryEntry.TABLE_NAME_INBOX,
                    selection,
                    selectionArgs);

            if (feedback != -1) {
                Inbox.getInstanceInbox().finish();
                Home.getInstanceHome().finish();
                Intent i = new Intent(getActivity(), Inbox.class);
                startActivity(i);
                Toast.makeText(getActivity(), "Message deleted successfully", Toast.LENGTH_SHORT).show();

            } else {
                Snackbar.make(getActivity().findViewById(R.id.main), "Failed, please try again.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
        finally {
            db.close();
        }

    }
}
