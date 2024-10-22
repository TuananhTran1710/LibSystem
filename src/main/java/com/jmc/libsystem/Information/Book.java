package com.jmc.libsystem.Information;

import java.time.LocalDate;

public class Book {
    private String id;
    private final String title;
    private final String authors;
    private LocalDate publishedDate;
    private String description;
    private final String thumbnailUrl;
    private int pageCount;
    private String language;
    private String infoLink;
    private int quantity;
    private String category;
    private String state;

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
