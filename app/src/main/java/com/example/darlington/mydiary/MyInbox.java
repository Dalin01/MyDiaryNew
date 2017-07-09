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
    private String font;
    private int text_size;
    private String colour;

    public MyInbox(String subject, String location, String message, String date, String category, int id, String font, int text_size, String colour)
    {
        this.Subject = subject;
        this.message = message;
        this.date = date;
        this.location = location;
        this.category = category;
        this.id = id;
        this.font = font;
        this.text_size = text_size;
        this.colour = colour;
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

    public String getFont()
    {
        return font;
    }

    public int getText_size()
    {
        return text_size;
    }

    public String getColour()
    {
        return colour;
    }
}
