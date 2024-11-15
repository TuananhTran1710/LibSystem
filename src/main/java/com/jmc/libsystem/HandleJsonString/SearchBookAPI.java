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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchBookAPI {

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

        //------------------------------------- chia luong

        //Các task này được quản lý bởi ExecutorService và chạy đồng thời trên các lõi CPU có sẵn.
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<?>> futures = new ArrayList<>();

        if (items != null) {
            for (JsonNode item : items) {
                // Submit each item as a separate task
                Future<?> future = executorService.submit(() -> {
                    JsonNode volumeInfo = item.get("volumeInfo");
                    String id = item.get("id").asText();

                    if (QueryBookData.isExist(id)) return;

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
                            try {
                                publishDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy"))
                                        .withMonth(1).withDayOfMonth(1);
                            } catch (Exception ex) {
                                System.err.println("Unrecognized date format: " + dateStr);
                                return;
                            }
                        }
                    } else {
                        return;
                    }

                    String description = volumeInfo.has("description") ? volumeInfo.get("description").asText() : "N/A";

                    //-------------------- chia luong o day ne
                    byte[] thumbnailImage = null;
                    if (volumeInfo.has("imageLinks") && volumeInfo.get("imageLinks").has("thumbnail")) {
                        String thumbnailUrl = volumeInfo.get("imageLinks").get("thumbnail").asText();
                        thumbnailImage = fetchImageBytes(thumbnailUrl);
                    }

                    //------------------------------------
                    int pageCount = volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").asInt() : 0;
                    String language = volumeInfo.has("language") ? volumeInfo.get("language").asText() : "N/A";

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

                    int countRating = 0, sumRatingStar = 0, totalLoan = 0, numBorrowing = 0, quantity = 0;


                    //synchronized (bookList) có nghĩa là khi một luồng vào khối mã này,
                    // nó sẽ "khóa" đối tượng bookList, ngăn không cho
                    // các luồng khác truy cập vào bookList trong khi nó thực hiện thao tác
                    // thêm sách vào danh sách. Điều này giúp tránh việc thay đổi
                    // đồng thời bookList từ nhiều luồng, gây ra lỗi hoặc bất ổn dữ liệu.
                    synchronized (bookList) {
                        bookList.add(new Book(id, title, authors, publishDate, description, thumbnailImage, pageCount, language, quantity, cats, countRating, sumRatingStar, totalLoan, numBorrowing));
                    }
                });
                futures.add(future);
            }
        }

        // Đợi tất cả các nhiệm vụ hoàn thành
        // future.get() là một cách để chặn luồng chính (main thread) cho đến khi các task hoàn thành
        // vi muc dich la return ve bookList, neu khong chan luong chinh thi luong chinh se ngay lap tuc return ve booklist ma chua
        // co day du ket qua

        // => day la ly do ta can List<Future>
        for (Future<?> future : futures) {
            try {
                future.get(); // Chờ mỗi task hoàn thành
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
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
                 // luu du lieu anh duoi dang byte
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

                // bo dem buffer
                byte[] buffer = new byte[4096];
                int bytesRead;    // luu so byte da doc duoc
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