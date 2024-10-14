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

    public Book(String title, String author, String thumbnailUrl) {
        this.title = title;
        this.author = author;
        this.thumbnailUrl = thumbnailUrl;
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
}
