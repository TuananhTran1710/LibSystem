package com.jmc.libsystem.Information;

public class User extends Person{
    private int attendance_score;
    private int reputation_score;
    private int max_books;
    private String state;
    private String borrow_table_name;
    private String fav_table_name;


    public User(String id, String fullName, String email, String password, int attendance_score, int reputation_score, int max_books, String state, String borrow_table_name, String fav_table_name) {
        super(id, fullName, email, password);
        this.attendance_score = attendance_score;
        this.reputation_score = reputation_score;
        this.max_books = max_books;
        this.state = state;
        this.borrow_table_name = borrow_table_name;
        this.fav_table_name = fav_table_name;
    }

    public int getAttendance_score() {
        return attendance_score;
    }

    public int getReputation_score() {
        return reputation_score;
    }

    public int getMax_books() {
        return max_books;
    }

    public String getState() {
        return state;
    }

    public String getBorrow_table_name() {
        return borrow_table_name;
    }

    public String getFav_table_name() {
        return fav_table_name;
    }

    public void setAttendance_score(int attendance_score) {
        this.attendance_score = attendance_score;
    }

    public void setReputation_score(int reputation_score) {
        this.reputation_score = reputation_score;
    }

    public void setMax_books(int max_books) {
        this.max_books = max_books;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setBorrow_table_name(String borrow_table_name) {
        this.borrow_table_name = borrow_table_name;
    }

    public void setFav_table_name(String fav_table_name) {
        this.fav_table_name = fav_table_name;
    }
}
