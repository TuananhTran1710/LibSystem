package com.jmc.libsystem.Views;

import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;


public class ShowListBookFound {

    private static final Image DEFAULT_BOOK_COVER = new Image(ShowListBookFound.class.getResource("/Images/library_icon.png").toString(), true);

    public static void show(List<Book> bookList, HBox resultList_hb, int limit) {
        resultList_hb.getChildren().clear();

        if (bookList.isEmpty()) {
            ImageView image = new ImageView();
            Image notFoundImage = new Image(ShowListBookFound.class.getResource("/Images/notfound.png").toString());
            image.setImage(notFoundImage);
            image.setFitHeight(142);
            image.setFitWidth(760);
            image.setPreserveRatio(false);

            resultList_hb.getChildren().add(image);
            return;
        }

        for (int i = 0; i < Math.min(bookList.size(), limit); i++) {
            Book book = bookList.get(i);
            VBox bookBox = createBookBox(book);
            resultList_hb.getChildren().add(bookBox);
        }
    }

    private static VBox createBookBox(Book book) {
        VBox bookBox = new VBox();
        bookBox.setSpacing(3);
        bookBox.setPadding(new Insets(5));
        bookBox.setAlignment(Pos.TOP_CENTER);
        bookBox.setPrefHeight(165);

        // Ảnh bìa
        ImageView bookCoverImageView = new ImageView();
        try {
            Image bookCoverImage = new Image(book.getThumbnailUrl(), true);
            bookCoverImageView.setImage(bookCoverImage);
        } catch (Exception e) {
            bookCoverImageView.setImage(DEFAULT_BOOK_COVER);
        }
        bookCoverImageView.setFitHeight(70);
        bookCoverImageView.setFitWidth(70);
        bookCoverImageView.setPreserveRatio(false);

        // Sự kiện click vào ảnh bìa để xem trước sách
        bookCoverImageView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Model.getInstance().getViewFactory().getUserSelectedMenuItem().set(UserMenuOptions.BOOKLOANPREVIEW);
            }
        });

        // Hiển thị tiêu đề sách (giới hạn ký tự)
        String shortTitle = book.getTitle().length() > 15 ? book.getTitle().substring(0, 15) + "..." : book.getTitle();
        Label titleLabel = new Label(shortTitle);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        titleLabel.setTooltip(new Tooltip(book.getTitle()));

        // Hiển thị tên tác giả (giới hạn ký tự)
        String shortAuthor = book.getAuthors().length() > 15 ? book.getAuthors().substring(0, 15) + "..." : book.getAuthors();
        Label authorLabel = new Label(shortAuthor);
        authorLabel.setStyle("-fx-font-family: 'Calibri Light'; -fx-font-size: 12px;");
        authorLabel.setTooltip(new Tooltip(book.getAuthors()));

        // Xếp hạng trung bình
        double averageRating = (book.getCountRating() == 0) ? 0 : 1.0 * book.getSumRatingStar() / book.getCountRating();
        Label ratingLabel = new Label("★" + String.format("%.1f", averageRating));
        ratingLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #FFA500;");

        // Số lượng đã mượn
        Label borrowCountLabel = new Label("Picked:" + book.getNumLoans());
        borrowCountLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #555;");

        // Sắp xếp các thông tin trong hai dòng chính
        HBox ratingAndBorrowBox = new HBox(4, ratingLabel, borrowCountLabel);
        ratingAndBorrowBox.setAlignment(Pos.TOP_LEFT);

        // Thêm các thành phần vào VBox
        bookBox.getChildren().addAll(bookCoverImageView, titleLabel, authorLabel, ratingAndBorrowBox);
        return bookBox;
    }
}




