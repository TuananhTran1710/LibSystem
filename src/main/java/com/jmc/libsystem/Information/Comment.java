package com.jmc.libsystem.Information;

public class Comment {
    String user_id;
    String text;
    int rating;

    public Comment(String user_id, String text, int rating) {
        this.user_id = user_id;
        this.text = text;
        this.rating = rating;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
