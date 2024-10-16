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

    // Phương thức gửi yêu cầu tới API Google Books và trả về chuối JSon
    public static String getJsonString(String query) throws URISyntaxException, IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI uri = new URIBuilder(API_URL)
                    .addParameter("q", query)
                    .addParameter("maxResults", String.valueOf(20)) // set gioi han ket qua tra ve la 20
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
