package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.BookSearchModel;
import com.jmc.libsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public TextField search_tf;
    public Button search_btn;
    public Label name_lbl;
    public HBox resultList_hb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name_lbl.setText("Hi " + Model.getInstance().getMyUser().getFullName() + "!");
        search_btn.setOnAction(event -> searhBooks());
    }

    public void searhBooks() {
        String keyWord = search_tf.getText();
        keyWord = keyWord.trim();
        if (!keyWord.isEmpty()) {

            try {
                //gọi model để lấy kết quả từ api
                List<Book> books = BookSearchModel.searchBook(keyWord);

                //đẩy lên giao diện
                displayBooks(books);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayBooks(List<Book> books) {
        //xóa kết quả cũ trước khi hiển thị
        resultList_hb.getChildren().clear();

        for (Book book : books) {
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
                Image bookCoverImage = new Image(getClass().getResource("/Images/library_icon.png").toString(), true);
                bookCoverImageView.setImage(bookCoverImage);
                bookCoverImageView.setFitHeight(80);  // Chiều cao của ảnh
                bookCoverImageView.setFitWidth(80); // Chiểu rộng
            }

            // Tạo Label cho tên sách
            String shortTitle = book.getTitle();
            if (shortTitle.length() > 12) {
                shortTitle = shortTitle.substring(0, 12) + "..."; // Giới hạn tiêu đề dài hơn 12 ký tự
            }
            Label titleLabel = new Label(shortTitle);
            titleLabel.setStyle("-fx-font-weight: bold;");

            //Tạo Label cho tác giả
            String shortAuthor = book.getAuthor();
            if (shortAuthor.length() > 12) {
                shortAuthor = shortAuthor.substring(0, 12) + "...";
            }
            Label authors = new Label(shortAuthor);
            authors.setStyle("-fx-font-family: 'Calibri Light'; -fx-font-weight: bold; -fx-font-size: 14px;");


            // Thêm các thành phần vào VBox
            bookBox.getChildren().addAll(bookCoverImageView, titleLabel, authors);

            // Thêm VBox vào HBox chính
            resultList_hb.getChildren().add(bookBox);
        }
    }
}