package com.jmc.libsystem.Views;

import com.jmc.libsystem.Information.Book;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ShowListBookFound {

    public static void show(List<Book> bookList, HBox resultList_hb) {
        resultList_hb.getChildren().clear();

        for (Book book : bookList) {
            VBox bookBox = new VBox();
            bookBox.setSpacing(5);
            bookBox.setPadding(new Insets(5));

            // Tạo ImageView cho ảnh bìa
            ImageView bookCoverImageView = new ImageView();
            try {
                Image bookCoverImage = new Image(book.getThumbnailUrl(), true);
                bookCoverImageView.setImage(bookCoverImage);
                // Đặt chiều cao và chiều rộng cố định
                bookCoverImageView.setFitHeight(80);  // Chiều cao của ảnh
                bookCoverImageView.setFitWidth(80);   // Chiều rộng của ảnh

                // Không bảo toàn tỉ lệ để ảnh có kích thước đồng nhất
                bookCoverImageView.setPreserveRatio(false);
            } catch (Exception e) {
                System.out.println("Lỗi khi tải ảnh bìa: " + e.getMessage());
                Image bookCoverImage = new Image(ShowListBookFound.class.getResource("/Images/library_icon.png").toString(), true);
                bookCoverImageView.setImage(bookCoverImage);
                bookCoverImageView.setFitHeight(80);  // Chiều cao của ảnh
                bookCoverImageView.setFitWidth(80); // Chiểu rộng
            }

            // Thêm sự kiện nhấn vào ảnh bìa để mở đường link
            bookCoverImageView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    try {
                        Desktop.getDesktop().browse(new URI(book.getInfoLink()));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Tạo Label cho tên sách
            String shortTitle = book.getTitle();
            if (shortTitle.length() > 12) {
                shortTitle = shortTitle.substring(0, 12) + "..."; // Giới hạn tiêu đề dài hơn 12 ký tự
            }
            javafx.scene.control.Label titleLabel = new javafx.scene.control.Label(shortTitle);
            titleLabel.setStyle("-fx-font-weight: bold;");

            //Tạo Label cho tác giả
            String shortAuthor = book.getAuthors();
            if (shortAuthor.length() > 12) {
                shortAuthor = shortAuthor.substring(0, 12) + "...";
            }
            javafx.scene.control.Label authors = new Label(shortAuthor);
            authors.setStyle("-fx-font-family: 'Calibri Light'; -fx-font-weight: bold; -fx-font-size: 14px;");


            // Thêm các thành phần vào VBox
            bookBox.getChildren().addAll(bookCoverImageView, titleLabel, authors);

            // Thêm VBox vào HBox chính
            resultList_hb.getChildren().add(bookBox);
        }
    }
}
