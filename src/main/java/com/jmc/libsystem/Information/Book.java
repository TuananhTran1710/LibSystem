package com.jmc.libsystem.Information;

import java.time.LocalDate;

public class Book {
    private String id;
    private String title;
    private String author;
    private LocalDate publishedDate;
    private String description;
    private String thumbnailUrl;
    private double avgRate;
    private int ratingCount;
    private int pageCount;
    private String language;
    private String infoLink;


    public Book(String title, String author, String thumbnailUrl, String infoLink) {
        this.title = title;
        this.author = author;
        this.thumbnailUrl = thumbnailUrl;
        this.infoLink = infoLink;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getInfoLink(){
        return infoLink;
    }
}
