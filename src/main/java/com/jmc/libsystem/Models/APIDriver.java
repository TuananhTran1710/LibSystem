package com.jmc.libsystem.Models;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class APIDriver {
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes";

    // Phương thức gửi yêu cầu tới API Google Books và trả về chuỗi JSON
    public static String getJsonString(String query, String type) throws URISyntaxException, IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Điều chỉnh cú pháp của query theo type
            String formattedQuery;
            switch (type.toLowerCase()) {
                case "authors":
                    formattedQuery = "inauthor:" + query;
                    break;
                case "title":
                    formattedQuery = "intitle:" + query;
                    break;
                case "category":
                    formattedQuery = "subject:" + query;
                    break;
                default:
                    formattedQuery = query; // Mặc định không có từ khóa đặc biệt nếu type không hợp lệ
                    break;
            }

            URI uri = new URIBuilder(API_URL)
                    .addParameter("q", formattedQuery)
                    .addParameter("maxResults", "20")
                    .build();

            HttpGet request = new HttpGet(uri);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

}