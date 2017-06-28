package com.example.darlington.mydiary;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Darlington on 6/17/2017.
 */

public class CustomDiaryAdapter extends ArrayAdapter<MyInbox> {

    public CustomDiaryAdapter(Context context, ArrayList<MyInbox> myInbox) {
        super(context, 0, myInbox);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list, parent, false);
        }

        MyInbox current_message = getItem(position);

        TextView sub = (TextView) listItemView.findViewById(R.id.subject);
        TextView my_message = (TextView) listItemView.findViewById(R.id.message);
        TextView location = (TextView) listItemView.findViewById(R.id.location);
        TextView my_time = (TextView) listItemView.findViewById(R.id.time);
        TextView my_date = (TextView) listItemView.findViewById(R.id.date);
        TextView my_category = (TextView) listItemView.findViewById(R.id.my_category);

        String subject = current_message.getSubject();
        String message = current_message.getMessage();
        String date = current_message.getDate();
        String my_location = current_message.getLocation();
        String category = current_message.getCategory();

        GradientDrawable magnitudeCircle = (GradientDrawable) my_category.getBackground();

        int magnitudeColor = getCategoryColor(category);
        magnitudeCircle.setColor(magnitudeColor);

        String[] date_time = date.split(" ");
        String required_date = date_time[0];
        String required_time = date_time[1];

        sub.setText(subject);
        my_message.setText(message);
        my_date.setText(required_date);
        my_time.setText(required_time);
        location.setText(my_location);


        return listItemView;

    }

    public int getCategoryColor(String cat){
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
