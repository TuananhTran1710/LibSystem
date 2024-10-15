package com.jmc.libsystem.Models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmc.libsystem.Information.Book;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class BookSearchModel {
    public static List<Book> searchBook(String keyword) throws URISyntaxException, IOException {
        // gọi gg service để lấy dữ liệu api
        String jsonReponse = GoogleBooksService.searchBooks(keyword);
        //tạo danh sách sách được trả về
        List<Book> bookList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonReponse);

        if (rootNode.has("error")) {
            System.out.println("Lỗi từ Google API: " + rootNode.get("error").get("message").asText());
            return bookList; // Trả về danh sách trống
        }

        JsonNode items = rootNode.get("items");

        if(items != null) {
            for(JsonNode item : items) {
                JsonNode volumeInfo = item.get("volumeInfo");
                String title = volumeInfo.get("title").asText();
                String authors;
                JsonNode authorsNode = volumeInfo.get("authors");
                if (authorsNode != null && authorsNode.isArray()) {
                    List<String> authorsList = new ArrayList<>();
                    for (JsonNode author : authorsNode) {
                        authorsList.add(author.asText());
                    }
                    authors = String.join(", ", authorsList); // Nối các tên tác giả bằng dấu phẩy
                } else {
                    authors = "N/A"; // Nếu không có tác giả
                }

                String thumbnailUrl = volumeInfo.has("imageLinks") ? volumeInfo.get("imageLinks").get("thumbnail").asText() : "";
                String link = volumeInfo.get("infoLink").asText();
                //thêm cái book vừa mới lôi ra vào danh sách kết quả
                bookList.add(new Book(title, authors, thumbnailUrl, link));
            }
        }
        return bookList;
    }
}
