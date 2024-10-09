package com.jmc.libsystem;

//import com.jmc.libsystem.Models.Model;

//import javafx.application.Application;
//import javafx.stage.Stage;
//
//public class App extends Application {
//    @Override
//    public void start(Stage stage) {
//        Model.getInstance().getViewFactory().showLoginWindow();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class App extends Application {
    private static final String API_KEY = "AIzaSyCKOGfs7ka80DGw5YVuPoMzbGkgqKWZ4hI";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private ListView<BookItem> resultList;

    @Override
    public void start(Stage primaryStage) {
        TextField searchField = new TextField();
        searchField.setPromptText("Enter title of books");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchBooks(searchField.getText())); // Khi nhấn nút, gọi hàm tìm kiếm sách

        resultList = new ListView<>();
        resultList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Mở liên kết khi nhấp đúp
                BookItem selectedItem = resultList.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedItem.volumeInfo != null) {
                    openWebpage(selectedItem.volumeInfo.canonicalVolumeLink);
                }
            }
        });

        VBox root = new VBox(10); // Giao diện đơn giản với VBox, khoảng cách giữa các thành phần là 10
        root.setPadding(new Insets(10));
        root.getChildren().addAll(searchField, searchButton, resultList);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("Google Books Search");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Phương thức tìm kiếm sách từ Google Books API
    private void searchBooks(String query) {
        // 1. Tạo URL từ khóa tìm kiếm và API Key
        String url = buildUrl(query);

        // 2. Tạo yêu cầu HTTP
        HttpRequest request = createHttpRequest(url);

        // 3. Gửi yêu cầu HTTP không đồng bộ và xử lý phản hồi
        sendHttpRequest(request);
    }

    // 1. Tạo URL từ từ khóa tìm kiếm
    private String buildUrl(String query) {
        // Thay dấu cách trong từ khóa tìm kiếm bằng dấu '+' và nối với API Key
        return BASE_URL + query.replace(" ", "+") + "&maxResults=20&key=" + API_KEY;
    }

    // 2. Tạo yêu cầu HTTP từ URL
    private HttpRequest createHttpRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url)) // Tạo URI từ URL
                .build(); // Xây dựng yêu cầu HTTP
    }

    // 3. Gửi yêu cầu HTTP không đồng bộ và xử lý kết quả
    private void sendHttpRequest(HttpRequest request) {
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                .thenApply(HttpResponse::body)         // lấy chuối Json
                .thenAccept(this::processResponse)   // gọi hàm để xử lý chuối, hàm process có tham số truyền vào là chuối json vừa lấy
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                }); // Xử lý lỗi nếu có
    }

    // Xử lý kết quả phản hồi từ Google Books API
    private void processResponse(String responseBody) {
        javafx.application.Platform.runLater(() -> {
            resultList.getItems().clear();
            try {
                // Đảm bảo chuỗi được mã hóa UTF-8
                String utf8Response = new String(responseBody.getBytes(), Charset.forName("UTF-8"));

                // Chuyển chuỗi JSON sang đối tượng BookResponse
                BookResponse bookResponse = gson.fromJson(utf8Response, BookResponse.class);

                if (bookResponse != null && bookResponse.items != null) {
                    for (BookItem item : bookResponse.items) {
                        String title = item.volumeInfo.title;
                        String authors = item.volumeInfo.authors != null ? String.join(", ", item.volumeInfo.authors)
                                : "Unknown Author";
                        resultList.getItems().add(item); // Thay vì thêm chuỗi, thêm BookItem
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Mở liên kết trong trình duyệt mặc định
    private void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URI(urlString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Các lớp giúp Gson ánh xạ JSON phản hồi sang đối tượng Java
    private static class BookResponse {
        List<BookItem> items;
    }

    private static class BookItem {
        VolumeInfo volumeInfo;

        @Override
        public String toString() {
            return volumeInfo.title + " by "
                    + (volumeInfo.authors != null ? String.join(", ", volumeInfo.authors) : "Unknown Author");
        }
    }

    private static class VolumeInfo {
        String title;
        List<String> authors;
        String canonicalVolumeLink; // Thêm trường để lưu liên kết đến sách
    }

    public static void main(String[] args) {
        launch(args);
    }
}
