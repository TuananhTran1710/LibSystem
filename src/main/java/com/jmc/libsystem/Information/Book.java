package com.jmc.libsystem.Information;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Book {
    private String id;
    private final String title;
    private final String authors;
    private LocalDate publishDate;
    private String description;
    private final byte[] thumbnailImage; // Store image data as byte array
    private int pageCount;
    private String language;
    private int quantity;
    private String category;
    private int countRating;
    private int sumRatingStar;
    private int totalLoan;
    private int numBorrowing;

    // Constructor with thumbnail image as byte array
    public Book(String id, String title, String authors, byte[] thumbnailImage) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.thumbnailImage = thumbnailImage;
    }

    public Book(String title, String authors, byte[] thumbnailImage) {
        this.title = title;
        this.authors = authors;
        this.thumbnailImage = thumbnailImage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCountRating(int countRating) {
        this.countRating = countRating;
    }

    public void setSumRatingStar(int sumRatingStar) {
        this.sumRatingStar = sumRatingStar;
    }

    public void setTotalLoan(int totalLoan) {
        this.totalLoan = totalLoan;
    }

    public void setNumBorrowing(int numBorrowing) {
        this.numBorrowing = numBorrowing;
    }

    public Book(String id, String title, String authors, LocalDate publishDate, String description, byte[] thumbnailImage, int pageCount, String language, int quantity, String category, int countRating, int sumRatingStar, int totalLoan, int numBorrowing) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.publishDate = publishDate;
        this.description = description;
        this.thumbnailImage = thumbnailImage;
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

    public byte[] getThumbnailImage() {
        return thumbnailImage;
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
            LocalDate publishedDate = rs.getDate("publishDate") != null ? rs.getDate("publishDate").toLocalDate() : null;
            String description = rs.getString("description");
            Blob thumbnailBlob = rs.getBlob("thumbnail"); // Get image as Blob
            byte[] thumbnailImage = thumbnailBlob != null ? thumbnailBlob.getBytes(1, (int) thumbnailBlob.length()) : null;
            int pageCount = rs.getInt("page_count");
            String language = rs.getString("language");
            int quantity = rs.getInt("quantity");
            String category = rs.getString("category");
            int countRating = rs.getInt("countRating");
            int sumRatingStar = rs.getInt("sumRatingStar");
            int totalLoan = rs.getInt("totalLoan");
            int numBorrowing = rs.getInt("numBorrowing");

            return new Book(id, title, authors, publishedDate, description, thumbnailImage,
                    pageCount, language, quantity, category, countRating,
                    sumRatingStar, totalLoan, numBorrowing);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Book from ResultSet", e);
        }
    }
}
