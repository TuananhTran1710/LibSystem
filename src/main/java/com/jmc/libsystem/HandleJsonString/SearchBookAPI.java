package com.jmc.libsystem.HandleJsonString;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.APIDriver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SearchBookAPI {

    // Method để phân tích chuỗi JSON và lấy ra list Book
    public static List<Book> getListBookFromJson(String keyword) throws URISyntaxException, IOException {
        // Gọi Google API để lấy dữ liệu JSON
        String jsonResponse = APIDriver.getJsonString(keyword);
        List<Book> bookList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);

        if (rootNode.has("error")) {
            System.out.println("Lỗi từ Google API: " + rootNode.get("error").get("message").asText());
            return bookList; // Trả về danh sách trống nếu có lỗi
        }

        JsonNode items = rootNode.get("items");

        if (items != null) {
            for (JsonNode item : items) {
                JsonNode volumeInfo = item.get("volumeInfo");

                // Lấy các thông tin cần thiết từ JSON response
                String id = item.get("id").asText();
                String title = volumeInfo.has("title") ? volumeInfo.get("title").asText() : "N/A";

                // Tác giả
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

                // Ngày xuất bản
                LocalDate publishDate = null;
                if (volumeInfo.has("publishedDate")) {
                    String dateStr = volumeInfo.get("publishedDate").asText();
                    try {
                        publishDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (Exception e) {
                        // Nếu không có định dạng chuẩn (chỉ có năm), bạn có thể tùy chỉnh parse
                        publishDate = LocalDate.parse(dateStr + "2024-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                }

                // Mô tả
                String description = volumeInfo.has("description") ? volumeInfo.get("description").asText() : "N/A";

                // Ảnh bìa
                String thumbnailUrl = volumeInfo.has("imageLinks") && volumeInfo.get("imageLinks").has("thumbnail")
                        ? volumeInfo.get("imageLinks").get("thumbnail").asText()
                        : "";

                // Số trang
                int pageCount = volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").asInt() : 0;

                // Ngôn ngữ
                String language = volumeInfo.has("language") ? volumeInfo.get("language").asText() : "N/A";

                // Thể loại

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

                // Đánh giá
                int countRating = 0;
                int sumRatingStar = 0;

                // Số lượng mượn (giá trị mặc định)
                int totalLoan = 0;
                int numBorrowing = 0;
                int quantity = 0; // số lượng mặc định

                // Thêm sách vào danh sách
                bookList.add(new Book(id, title, authors, publishDate, description, thumbnailUrl, pageCount, language, quantity, cats, countRating, sumRatingStar, totalLoan, numBorrowing));
            }
        }
        return bookList;
    }
}
