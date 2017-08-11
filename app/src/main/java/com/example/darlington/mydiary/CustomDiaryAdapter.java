package com.example.darlington.mydiary;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Darlington on 6/17/2017.
 * Customize adapter for populating the custom list view in the Inbox.
 */

public class CustomDiaryAdapter extends ArrayAdapter<MyInbox> {

    private Context context;

    public CustomDiaryAdapter(Context context, ArrayList<MyInbox> myInbox) {
        super(context, 0, myInbox);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView; //view passed to it

        if (listItemView == null) { // if the view is null create a new view
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list, parent, false);
        }

        MyInbox current_message = getItem(position); // get the next item for the db through the class

        //get the views required from the list view
        TextView sub = (TextView) listItemView.findViewById(R.id.subject);
        TextView my_message = (TextView) listItemView.findViewById(R.id.message);
        TextView location = (TextView) listItemView.findViewById(R.id.location);
        TextView my_time = (TextView) listItemView.findViewById(R.id.time);
        TextView my_date = (TextView) listItemView.findViewById(R.id.date);
        TextView my_category = (TextView) listItemView.findViewById(R.id.my_category);
        ImageView my_category_image = (ImageView) listItemView.findViewById(R.id.my_category_image);

        //call the get methods in the instance of the class current_message and
        //save the results in corresponding variables
        String subject = current_message.getSubject();
        String message = current_message.getMessage();
        String date = current_message.getDate();
        String my_location = current_message.getLocation();
        String category = current_message.getCategory();
        String font = current_message.getFont();
        int text_size = current_message.getText_size();

        if (category.equals("Work") || category.equals("School") || category.equals("Personal") || category.equals("Family") || category.equals("Study") ||
                category.equals("Research") || category.equals("Uncategorized")) {
            my_category_image.setVisibility(View.GONE);
            my_category.setVisibility(View.VISIBLE);
            // set the drawable (circle) in the view- color also
            GradientDrawable magnitudeCircle = (GradientDrawable) my_category.getBackground();
            int magnitudeColor = getCategoryColor(category);
            magnitudeCircle.setColor(magnitudeColor);
        } else {
            my_category_image.setVisibility(View.VISIBLE);
            my_category.setVisibility(View.GONE);
            setFullImageFromFilePath(category, my_category_image);
        }

        //using split method to generate an array of the item in the date_time variable
        //and then save the items in the array in corresponding variables.
        String[] date_time = date.split(" ");
        String required_date = date_time[0];
        String required_time = date_time[1];

        //set the views to the appropriate values
        sub.setText(subject);
        my_message.setText(message);
        my_date.setText(required_date);
        my_time.setText(required_time);
        location.setText(my_location);

        //set the fonts
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), font);
        sub.setTypeface(typeface);
        sub.setTextSize(text_size);
        my_date.setTypeface(typeface);
        my_date.setTextSize(text_size);
        my_message.setTypeface(typeface);
        my_message.setTextSize(text_size);
        location.setTypeface(typeface);
        location.setTextSize(text_size);
        my_time.setTypeface(typeface);
        my_time.setTextSize(text_size);

        //return the list item.
        return listItemView;

    }

    /**
     * Scale the photo down and fit it to our image views.
     *
     * "Drastically increases performance" to set images using this technique.
     * Read more:http://developer.android.com/training/camera/photobasics.html
     */
    private void setFullImageFromFilePath(String imagePath, ImageView imageView) {
        //I used glide library to display the profile image
        Glide.with(context)
                .load(imagePath)
                .asBitmap()
                .placeholder(R.drawable.bck)
                .into(imageView);
    }


    // method that set the color for the drawable based on the category
    //selected and displays on the view
    private int getCategoryColor(String cat) {
        int magnitudeColorResourceId;
        switch (cat) {
            case "Work":
                magnitudeColorResourceId = R.color.work;
                break;
            case "School":
                magnitudeColorResourceId = R.color.school;
                break;
            case "Personal":
                magnitudeColorResourceId = R.color.personal;
                break;
            case "Family":
                magnitudeColorResourceId = R.color.family;
                break;
            case "Study":
                magnitudeColorResourceId = R.color.study;
                break;
            case "Research":
                magnitudeColorResourceId = R.color.research;
                break;
            default:
                magnitudeColorResourceId = R.color.uncategory;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);

    }
}
