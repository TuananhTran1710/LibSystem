package com.jmc.libsystem.Information;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Book {
    private String id;
    private final String title;
    private final String authors;
    private LocalDate publishDate;
    private String description;
    private final String thumbnailUrl;
    private int pageCount;
    private String language;
    private int quantity;
    private String category;
    private int countRating;
    private int sumRatingStar;
    private int totalLoan;
    private int numBorrowing;


    public Book(String id, String title, String authors, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Book(String title, String authors, String thumbnailUrl) {
        this.title = title;
        this.authors = authors;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Book(String id, String title, String authors, LocalDate publishDate, String description, String thumbnailUrl, int pageCount, String language, int quantity, String category, int countRating, int sumRatingStar, int totalLoan, int numBorrowing) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.publishDate = publishDate;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.pageCount = pageCount;
        this.language = language;
        this.quantity = quantity;
        this.category = category;
        this.countRating = countRating;
        this.sumRatingStar = sumRatingStar;
        this.totalLoan = totalLoan;
        this.numBorrowing = numBorrowing;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getLanguage() {
        return language;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public int getCountRating() {
        return countRating;
    }

    public int getSumRatingStar() {
        return sumRatingStar;
    }

    public int getNumBorrowing() {
        return numBorrowing;
    }

    public int getTotalLoan() {
        return totalLoan;
    }

    public static Book createBookFromResultSet(ResultSet rs) {
        try {
            String id = rs.getString("google_book_id");
            String title = rs.getString("title");
            String authors = rs.getString("authors");
            LocalDate publishedDate = rs.getDate("publishDate").toLocalDate();
            String description = rs.getString("description");
            String thumbnailUrl = rs.getString("thumbnail_url");
            int pageCount = rs.getInt("page_count");
            String language = rs.getString("language");
            int quantity = rs.getInt("quantity");
            String category = rs.getString("category");
            int countRating = rs.getInt("countRating");
            int sumRatingStar = rs.getInt("sumRatingStar");
            int totalLoan = rs.getInt("totalLoan");
            int numBorrowing = rs.getInt("numBorrowing");

            return new Book(id, title, authors, publishedDate, description, thumbnailUrl,
                    pageCount, language, quantity, category, countRating,
                    sumRatingStar, totalLoan, numBorrowing);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Book from ResultSet", e);
        }
    }
}
