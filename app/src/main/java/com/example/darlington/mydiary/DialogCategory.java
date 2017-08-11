package com.example.darlington.mydiary;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by DARLINGTON on 7/9/2017.
 */

public class DialogCategory extends DialogFragment {

    final CharSequence[] categories = {"UNCATEGORIZED", "WORK", "SCHOOL", "PERSONAL", "FAMILY", "STUDY", "RESEARCH"};
    String category;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Category")
                .setSingleChoiceItems(categories, 1,
            new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialogInterface, int item) {
            category = categories[item].toString();

        }
    })
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            getCategory();
        }
    })
            .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {

        }
    });

        return builder.create();
}

    private void getCategory() {
        Intent i = new Intent(getActivity(), Sort.class);
        if(category != null){
            i.putExtra("sort", category);
            startActivity(i);
        }
        else{
            Toast.makeText(getContext(), "Select a category then click OK", Toast.LENGTH_SHORT).show();
        }


    }

}
