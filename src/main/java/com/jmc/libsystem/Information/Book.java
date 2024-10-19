package com.jmc.libsystem.Information;

import java.time.LocalDate;

public class Book {
    private String id;
    private String title;
    private String authors;
    private LocalDate publishedDate;
    private String description;
    private String thumbnailUrl;
    private double avgRate;
    private int ratingCount;
    private int pageCount;
    private String language;
    private String infoLink;
    private int quantity;
    private int numberOfIssued;
    private String category;

    public Book(String title, String authors, String thumbnailUrl) {
        this.title = title;
        this.authors = authors;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getInfoLink() {
        return infoLink;
    }
}
