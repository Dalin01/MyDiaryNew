package com.example.darlington.mydiary;

/**
 * Created by Darlington on 6/17/2017.
 */

public class MyInbox {

    private String Subject;
    private String message;
    private String location;
    private String date;
    private String category;
    private int id;

    public MyInbox(String subject, String location, String message, String date, String category, int id)
    {
        this.Subject = subject;
        this.message = message;
        this.date = date;
        this.location = location;
        this.category = category;
        this.id = id;
    }

    public String getSubject()
    {
        return Subject;
    }

    public String getMessage()
    {
        return message;
    }

    public String getDate()
    {
        return date;
    }

    public String getLocation() {return location; }

    public String getCategory() {return category; }

    public int getId()
    {
        return id;
    }
}
