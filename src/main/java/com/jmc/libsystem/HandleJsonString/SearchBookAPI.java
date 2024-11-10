package com.jmc.libsystem.HandleJsonString;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.APIDriver;
import com.jmc.libsystem.QueryDatabase.QueryBookData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SearchBookAPI {

    // Method to parse JSON string and retrieve a list of Book objects
    public static List<Book> getListBookFromJson(String keyword, String type) throws URISyntaxException, IOException {
        // Fetch JSON data from Google API
        String jsonResponse = APIDriver.getJsonString(keyword, type);
        List<Book> bookList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);

        if (rootNode.has("error")) {
            System.out.println("Error from Google API: " + rootNode.get("error").get("message").asText());
            return bookList; // Return an empty list if there is an error
        }

        JsonNode items = rootNode.get("items");

        if (items != null) {
            for (JsonNode item : items) {
                JsonNode volumeInfo = item.get("volumeInfo");

                // Retrieve necessary information from JSON response
                String id = item.get("id").asText();
                if (QueryBookData.isExist(id)) continue;
                String title = volumeInfo.has("title") ? volumeInfo.get("title").asText() : "N/A";

                // Authors
                String authors;
                JsonNode authorsNode = volumeInfo.get("authors");
                if (authorsNode != null && authorsNode.isArray()) {
                    List<String> authorsList = new ArrayList<>();
                    for (JsonNode author : authorsNode) {
                        authorsList.add(author.asText());
                    }
                    authors = String.join(", ", authorsList);
                } else {
                    authors = "N/A";
                }

                // Publish date
                LocalDate publishDate = null;
                if (volumeInfo.has("publishedDate")) {
                    String dateStr = volumeInfo.get("publishedDate").asText();
                    try {
                        publishDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (Exception e) {
                        // Default to January 1st of the year if date format is only "yyyy"
                        publishDate = LocalDate.parse(dateStr + "-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                }

                // Description
                String description = volumeInfo.has("description") ? volumeInfo.get("description").asText() : "N/A";

                // Book cover image as byte array
                byte[] thumbnailImage = null;
                if (volumeInfo.has("imageLinks") && volumeInfo.get("imageLinks").has("thumbnail")) {
                    String thumbnailUrl = volumeInfo.get("imageLinks").get("thumbnail").asText();
                    thumbnailImage = fetchImageBytes(thumbnailUrl);
                }

                // Page count
                int pageCount = volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").asInt() : 0;

                // Language
                String language = volumeInfo.has("language") ? volumeInfo.get("language").asText() : "N/A";

                // Categories
                String cats;
                JsonNode catsNode = volumeInfo.get("categories");
                if (catsNode != null && catsNode.isArray()) {
                    List<String> catsList = new ArrayList<>();
                    for (JsonNode cat : catsNode) {
                        catsList.add(cat.asText());
                    }
                    cats = String.join(", ", catsList);
                } else {
                    cats = "N/A";
                }

                // Ratings and loan info (default values)
                int countRating = 0;
                int sumRatingStar = 0;
                int totalLoan = 0;
                int numBorrowing = 0;
                int quantity = 0;

                // Add book to list with state = null (that isn't added to database)
                bookList.add(new Book(id, title, authors, publishDate, description, thumbnailImage, pageCount, language, quantity, cats, countRating, sumRatingStar, totalLoan, numBorrowing));
            }
        }
        return bookList;
    }

    // Method to fetch image as byte array from URL
    private static byte[] fetchImageBytes(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (InputStream inputStream = connection.getInputStream();
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException e) {
            System.out.println("Error fetching image: " + e.getMessage());
            return null; // Return null if there is an error fetching the image
        }
    }
}
