package com.jmc.libsystem.HandleJsonString;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.APIDriver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class SearchBookAPI {

    // method để phân tích chuỗi JSon để lấy ra list Book
    public static List<Book> getListBookFromJson(String keyword) throws URISyntaxException, IOException {
        // gọi gg service để lấy dữ liệu api
        String jsonReponse = APIDriver.getJsonString(keyword);
        //tạo danh sách sách được trả về
        List<Book> bookList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonReponse);

        if (rootNode.has("error")) {
            System.out.println("Lỗi từ Google API: " + rootNode.get("error").get("message").asText());
            return bookList; // Trả về danh sách trống
        }

        JsonNode items = rootNode.get("items");

        if (items != null) {
            for (JsonNode item : items) {
                // lay volumeInfo cua moi quyen sach
                JsonNode volumeInfo = item.get("volumeInfo");
                //lay title
                String title = volumeInfo.get("title").asText();
                //lay tac gia
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

                // lay link anh bia cua sach
                String thumbnailUrl = volumeInfo.has("imageLinks") ? volumeInfo.get("imageLinks").get("thumbnail").asText() : "";

                //thêm cái book vừa mới lôi ra vào danh sách kết quả
                bookList.add(new Book(title, authors, thumbnailUrl));
            }
        }
        return bookList;
    }
}
