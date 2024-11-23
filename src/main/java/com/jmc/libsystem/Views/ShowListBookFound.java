package com.jmc.libsystem.Views;

import com.jmc.libsystem.Controllers.Admin.AdminController;
import com.jmc.libsystem.Controllers.Book.BookDetailAPI;
import com.jmc.libsystem.Controllers.Book.BookDetailController;
import com.jmc.libsystem.Controllers.Book.BookProposeDetailController;
import com.jmc.libsystem.Controllers.User.UserController;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ShowListBookFound {

    public static final Image DEFAULT_BOOK_COVER = new Image(ShowListBookFound.class.getResource("/Images/library_icon.png").toString(), true);

    public static void show(List<Book> bookList, HBox resultList_hb, int limit) {
        resultList_hb.getChildren().clear();

        if (bookList.isEmpty()) {
            VBox emptyBox = new VBox();
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setSpacing(3);

            ImageView image = new ImageView();
            Image notFoundImage = new Image(ShowListBookFound.class.getResource("/Images/empty.png").toString());
            image.setImage(notFoundImage);
            image.setFitHeight(90);
            image.setFitWidth(90);
            image.setPreserveRatio(false);

            Label messageLabel = new Label("No books found");
            messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #A0A0A0;");

            emptyBox.getChildren().addAll(image, messageLabel);
            emptyBox.setPadding(new Insets(10, 0, 0, 0));
            emptyBox.setPrefHeight(120);
            resultList_hb.setPrefWidth(730);
            resultList_hb.setAlignment(Pos.CENTER);
            resultList_hb.getChildren().addAll(emptyBox);
            return;
        }

        resultList_hb.setAlignment(Pos.TOP_LEFT);
        // Khởi chạy một luồng cho mỗi sách trong bookList
        for (int i = 0; i < Math.min(bookList.size(), limit); i++) {
            VBox bookBox = createBookBox(bookList.get(i));
            resultList_hb.getChildren().add(bookBox);
        }
    }

    public static void show(List<Book> bookList, HBox resultList_hb) {
        resultList_hb.getChildren().clear();

        // Nếu danh sách sách trống
        if (bookList.isEmpty()) {
            VBox emptyBox = new VBox();
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setSpacing(3);

            ImageView image = new ImageView();
            Image notFoundImage = new Image(ShowListBookFound.class.getResource("/Images/empty.png").toString());
            image.setImage(notFoundImage);
            image.setFitHeight(90);
            image.setFitWidth(90);
            image.setPreserveRatio(false);

            Label messageLabel = new Label("No books found");
            messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #A0A0A0;");

            emptyBox.getChildren().addAll(image, messageLabel);

            emptyBox.setPadding(new Insets(10, 0, 0, 0));
            emptyBox.setPrefHeight(120);
            resultList_hb.setPrefWidth(730);
            resultList_hb.setAlignment(Pos.CENTER);

            resultList_hb.getChildren().add(emptyBox);
            return;
        }
        resultList_hb.setAlignment(Pos.TOP_LEFT);
        // Khởi chạy một luồng cho mỗi sách trong bookList
        for (Book book : bookList) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    // Tạo VBox cho mỗi sách
                    VBox bookBox = createBookBoxAPI(book);

                    // Cập nhật giao diện trong luồng chính
                    Platform.runLater(() -> resultList_hb.getChildren().add(bookBox));
                    return null;
                }
            };

            // Khởi chạy Task trong một luồng mới
            Thread thread = new Thread(task);
            thread.setDaemon(true); // Đảm bảo luồng sẽ tự động tắt khi ứng dụng JavaFX kết thúc
            thread.start();
        }
    }

    // không cho lích sử đề xuất bấm vào, cái này chỉ có lịch sử dùng nên k phải gắn flags
    public static void showAPIFromUser(List<Book> bookList, HBox resultList_hb, int limit) {
        resultList_hb.getChildren().clear();

        // Nếu danh sách sách trống
        if (bookList.isEmpty()) {
            VBox emptyBox = new VBox();
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setSpacing(3);

            ImageView image = new ImageView();
            Image notFoundImage = new Image(ShowListBookFound.class.getResource("/Images/empty.png").toString());
            image.setImage(notFoundImage);
            image.setFitHeight(90);
            image.setFitWidth(90);
            image.setPreserveRatio(false);

            Label messageLabel = new Label("No books found");
            messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #A0A0A0;");

            emptyBox.getChildren().addAll(image, messageLabel);

            emptyBox.setPadding(new Insets(10, 0, 0, 0));
            emptyBox.setPrefHeight(120);
            resultList_hb.setPrefWidth(730);
            resultList_hb.setAlignment(Pos.CENTER);

            resultList_hb.getChildren().add(emptyBox);
            return;
        }

        resultList_hb.setAlignment(Pos.TOP_LEFT);
        // Hiển thị tối đa `limit` sách từ danh sách
        for (int i = 0; i < Math.min(bookList.size(), limit); i++) {
            VBox bookBox = createBookBoxAPIFromUser(bookList.get(i), false);
            resultList_hb.getChildren().add(bookBox);
        }
    }

    public static void showAPIFromUser(List<Book> bookList, HBox resultList_hb, boolean allowViewDetailFlag) {
        resultList_hb.getChildren().clear();
        // Nếu danh sách sách trống
        if (bookList.isEmpty()) {
            VBox emptyBox = new VBox();
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setSpacing(3);

            ImageView image = new ImageView();
            Image notFoundImage = new Image(ShowListBookFound.class.getResource("/Images/empty.png").toString());
            image.setImage(notFoundImage);
            image.setFitHeight(90);
            image.setFitWidth(90);
            image.setPreserveRatio(false);

            Label messageLabel = new Label("No books found");
            messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #A0A0A0;");

            emptyBox.getChildren().addAll(image, messageLabel);

            emptyBox.setPadding(new Insets(10, 0, 0, 0));
            emptyBox.setPrefHeight(120);
            resultList_hb.setPrefWidth(730);
            resultList_hb.setAlignment(Pos.CENTER);

            resultList_hb.getChildren().add(emptyBox);
            return;
        }
        resultList_hb.setAlignment(Pos.TOP_LEFT);
        for (Book book : bookList) {
            VBox bookBox = createBookBoxAPIFromUser(book, allowViewDetailFlag);
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
            Image bookCoverImage;
            if (book.getThumbnailImage() != null) {
                // Create Image from byte array
                bookCoverImage = new Image(new ByteArrayInputStream(book.getThumbnailImage()));
            } else {
                bookCoverImage = DEFAULT_BOOK_COVER;
            }
            bookCoverImageView.setImage(bookCoverImage);
        } catch (Exception e) {
            bookCoverImageView.setImage(DEFAULT_BOOK_COVER);
        }
        bookCoverImageView.setFitHeight(90);
        bookCoverImageView.setFitWidth(90);
        bookCoverImageView.setPreserveRatio(false);

        // Thêm CSS trực tiếp vào ImageView
        bookCoverImageView.setStyle(
                "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 7, 0.7, 0, 0);"
        );

        // Sự kiện click vào ảnh bìa để xem trước sách
        bookCoverImageView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                UserController.getInstance().user_parent.setCenter(Model.getInstance().getViewFactory().getBookDetail());
                BookDetailController.getInstance().setBook(book);
                BookDetailController.getInstance().modifyButton();
                BookDetailController.getInstance().setUpInfo(book);
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
        Label ratingLabel = new Label("★" + String.format("%.1f", 1.0 * (int) Math.floor(averageRating)));
        ratingLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #FFA500;");

        // Số lượng đã mượn
        String totalLoan;
        if (book.getTotalLoan() >= 1000) {
            if (book.getTotalLoan() % 1000 == 0) totalLoan = book.getTotalLoan() / 1000 + "k";
            else
                totalLoan = String.format("%.1f", 1.0 * book.getTotalLoan() / 1000) + "k";
        } else {
            totalLoan = book.getTotalLoan() + "";
        }
        Label borrowCountLabel = new Label("Picked: " + totalLoan);
        borrowCountLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #555;");

        // Sắp xếp các thông tin trong hai dòng chính
        HBox ratingAndBorrowBox = new HBox(8, ratingLabel, borrowCountLabel);
        ratingAndBorrowBox.setAlignment(Pos.TOP_LEFT);

        // Thêm các thành phần vào VBox
        bookBox.getChildren().addAll(bookCoverImageView, titleLabel, authorLabel, ratingAndBorrowBox);
        return bookBox;
    }

    private static VBox createBookBoxAPI(Book book) {
        VBox bookBox = new VBox();
        bookBox.setSpacing(3);
        bookBox.setPadding(new Insets(5));
        bookBox.setAlignment(Pos.TOP_CENTER);
        bookBox.setPrefHeight(165);

        // Ảnh bìa
        ImageView bookCoverImageView = new ImageView();
        try {
            Image bookCoverImage;
            if (book.getThumbnailImage() != null) {
                // Create Image from byte array
                bookCoverImage = new Image(new ByteArrayInputStream(book.getThumbnailImage()));
            } else {
                bookCoverImage = DEFAULT_BOOK_COVER;
            }
            bookCoverImageView.setImage(bookCoverImage);
        } catch (Exception e) {
            bookCoverImageView.setImage(DEFAULT_BOOK_COVER);
        }
        bookCoverImageView.setFitHeight(90);
        bookCoverImageView.setFitWidth(90);
        bookCoverImageView.setPreserveRatio(false);

        // Thêm CSS trực tiếp vào ImageView
        bookCoverImageView.setStyle(
                "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 7, 0.7, 0, 0);"
        );

        // Sự kiện click vào ảnh bìa để xem trước sách
        bookCoverImageView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                AdminController.getInstance().admin_parent.setCenter(Model.getInstance().getViewFactory().getBookDetailAPI());
                BookDetailAPI.getInstance().setBook(book);
                BookDetailAPI.getInstance().modifyButton();
                BookDetailAPI.getInstance().setUpInfo(book);
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

        // Thêm các thành phần vào VBox
        bookBox.getChildren().addAll(bookCoverImageView, titleLabel, authorLabel);
        return bookBox;
    }

    private static VBox createBookBoxAPIFromUser(Book book, boolean allowViewDetailFlag) {
        VBox bookBox = new VBox();
        bookBox.setSpacing(3);
        bookBox.setPadding(new Insets(5));
        bookBox.setAlignment(Pos.TOP_CENTER);
        bookBox.setPrefHeight(165);

        // Ảnh bìa
        ImageView bookCoverImageView = new ImageView();
        try {
            Image bookCoverImage;
            if (book.getThumbnailImage() != null) {
                // Create Image from byte array
                bookCoverImage = new Image(new ByteArrayInputStream(book.getThumbnailImage()));
            } else {
                bookCoverImage = DEFAULT_BOOK_COVER;
            }
            bookCoverImageView.setImage(bookCoverImage);
        } catch (Exception e) {
            bookCoverImageView.setImage(DEFAULT_BOOK_COVER);
        }
        bookCoverImageView.setFitHeight(90);
        bookCoverImageView.setFitWidth(90);
        bookCoverImageView.setPreserveRatio(false);

        // Thêm CSS trực tiếp vào ImageView
        bookCoverImageView.setStyle(
                "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 7, 0.7, 0, 0);"
        );

        if (allowViewDetailFlag == true) {
            // Sự kiện click vào ảnh bìa để xem trước sách
            bookCoverImageView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    UserController.getInstance().user_parent.setCenter(Model.getInstance().getViewFactory().getBookPropose());
                    BookProposeDetailController.getInstance().setBook(book);
                    BookProposeDetailController.getInstance().modifyButton();
                    BookProposeDetailController.getInstance().setUpInfo(book);
                }
            });
        }

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

        // Thêm các thành phần vào VBox
        bookBox.getChildren().addAll(bookCoverImageView, titleLabel, authorLabel);
        return bookBox;
    }
}
