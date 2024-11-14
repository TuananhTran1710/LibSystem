package com.jmc.libsystem.Controllers.Book;

import com.jmc.libsystem.Controllers.Admin.AdminController;
import com.jmc.libsystem.Controllers.Admin.ManageBookController;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.SuggestionBox.SuggestionBook;
import com.jmc.libsystem.Views.SearchCriteria;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class BookDetailAPI implements Initializable {


    public Button back_btn;
    public ImageView imageView;
    public Label title;
    public Button addBook_btn;
    public ToggleButton overview_btn;
    public VBox overview_vbox;
    public VBox authorsContainer;
    public VBox categoriesContainer;
    public Label description_lbl;
    public Label publishDate_lbl;
    public Label pages_lbl;
    public Label id_lbl;
    public Label lan_lbl;

    private static BookDetailAPI instance;
    public TextField quantity_tf;
    public Button subtract_btn;
    public Button plus_btn;
    public Button update_btn;
    public Label notice_lbl;
    public Button delete_btn;
    private Book book;

    private Timeline noticeTimeline;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public static BookDetailAPI getInstance() {
        return instance;
    }

    public BookDetailAPI() {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        quantity_tf.setText("1");
        quantity_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            // Kiểm tra nếu giá trị không phải là số hoặc là số âm
            try {
                int quantity = Integer.parseInt(newValue);
                if (quantity <= 0) {
                    // Nếu là số âm hoặc bằng 0, đặt lại giá trị về giá trị cũ
                    quantity_tf.setText(oldValue);
                }
            } catch (NumberFormatException e) {
                // Nếu không phải là số, đặt lại giá trị về giá trị cũ
                quantity_tf.setText(oldValue);
            }
        });


        addBook_btn.setOnAction(event -> {

            int num = Integer.parseInt(quantity_tf.getText());
            QueryBookData.addBook(book, num);
            book.setQuantity(num);
            toUpdateButton();

            notice_lbl.setText("Add book successfully");
            notice_lbl.setTextFill(Color.web("#32CD32"));
            createTimeLine();

            // add data into suggestion box
            Set<String> typeSuggest;
            if (ManageBookController.typeSearchAddBook == SearchCriteria.TITLE) {
                typeSuggest = SuggestionBook.titleSuggest;
            } else if (ManageBookController.typeSearchAddBook == SearchCriteria.CATEGORY) {
                typeSuggest = SuggestionBook.categorySuggest;
            } else {
                typeSuggest = SuggestionBook.authorSuggest;
            }
            String newWord = ManageBookController.getInstance().getSearchAddBook_tf().getText();
            typeSuggest.add(newWord);
        });

        update_btn.setOnAction(event -> {
            int num = Integer.parseInt(quantity_tf.getText());
            if (num != book.getQuantity()) {
                QueryBookData.updateBook(book.getId(), num);
                notice_lbl.setText("Update book successfully");
                notice_lbl.setTextFill(Color.web("#32CD32"));
            } else {
                notice_lbl.setText("Have no change");
                notice_lbl.setTextFill(Color.RED);
            }
            createTimeLine();
        });

        delete_btn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete book");
            alert.setContentText("Are you sure you want to delete this book?");

            // Hiển thị hộp thoại và chờ người dùng chọn
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Nếu người dùng chọn OK, tiến hành xóa sách
                QueryBookData.deleteBook(book.getId());
                quantity_tf.setText("1");
                toAddButton();

                notice_lbl.setText("Delete book successfully");
                notice_lbl.setTextFill(Color.web("#32CD32"));

                createTimeLine();
            } else {
                // Nếu người dùng chọn Cancel, không làm gì cả
                notice_lbl.setText("Aborted deletion");
                notice_lbl.setTextFill(Color.web("#FF0000"));

                createTimeLine();
            }
        });


        plus_btn.setOnAction(event -> {
            int num = Integer.parseInt(quantity_tf.getText());
            num += 1;
            quantity_tf.setText("" + num);
        });

        subtract_btn.setOnAction(event -> {
            int num = Integer.parseInt(quantity_tf.getText());
            num -= 1;
            quantity_tf.setText("" + num);
        });

        back_btn.setOnAction(event -> moveMenuCurrent());
    }

    private void moveMenuCurrent() {
        AdminController.getInstance().admin_parent.setCenter(Model.getInstance().getViewFactory().getManageBookView());
        ManageBookController.getInstance().resetBookLibrary();
    }

    public void createTimeLine() {
        // Chỉ tạo Timeline nếu nó chưa được khởi tạo
        if (noticeTimeline == null) {
            noticeTimeline = new Timeline(new KeyFrame(
                    Duration.seconds(1.5),
                    evt -> notice_lbl.setText("")
            ));
            noticeTimeline.setCycleCount(1);
        }

        // Dừng và chạy lại Timeline để đảm bảo nhãn sẽ biến mất sau 3 giây
        noticeTimeline.stop();
        noticeTimeline.playFromStart();
    }

    public void setUpInfo(Book book) {
        notice_lbl.setText("");

        title.setText(book.getTitle());
        publishDate_lbl.setText(book.getPublishDate().toString());

        pages_lbl.setText(String.valueOf(book.getPageCount()));
        description_lbl.setText(book.getDescription());
        id_lbl.setText(book.getId());
        lan_lbl.setText(book.getLanguage());

        // set anh
        try {
            Image bookCoverImage;
            if (book.getThumbnailImage() != null) {
                // Create Image from byte array
                bookCoverImage = new Image(new ByteArrayInputStream(book.getThumbnailImage()));
            } else {
                bookCoverImage = ShowListBookFound.DEFAULT_BOOK_COVER;
            }
            imageView.setImage(bookCoverImage);
        } catch (Exception e) {
            imageView.setImage(ShowListBookFound.DEFAULT_BOOK_COVER);
        }


        imageView.setFitHeight(280);
        imageView.setFitWidth(225);
        imageView.setPreserveRatio(false);

        // Hiển thị danh sách tác giả dưới dạng các ô, mỗi dòng chứa 2 ô
        authorsContainer.getChildren().clear();
        List<String> authors = List.of(book.getAuthors().split(", "));
        for (int i = 0; i < authors.size(); i += 2) {
            HBox row = new HBox(5);
            for (int j = i; j < i + 2 && j < authors.size(); j++) {
                Label authorLabel = new Label(authors.get(j));
                authorLabel.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 5 10; -fx-border-radius: 15; -fx-background-radius: 15;");
                row.getChildren().add(authorLabel);
            }
            authorsContainer.getChildren().add(row);
        }

        // Hiển thị danh mục sách dưới dạng các ô, mỗi dòng chứa 2 ô
        categoriesContainer.getChildren().clear();
        List<String> categories = List.of(book.getCategory().split(", "));
        for (int i = 0; i < categories.size(); i += 3) {
            HBox row = new HBox(5);
            for (int j = i; j < i + 3 && j < categories.size(); j++) {
                Label categoryLabel = new Label(categories.get(j).trim());
                categoryLabel.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 5 10; -fx-border-radius: 15; -fx-background-radius: 15;");
                row.getChildren().add(categoryLabel);
            }
            categoriesContainer.getChildren().add(row);
        }
    }

    public void toUpdateButton() {
        addBook_btn.setDisable(true);
        addBook_btn.setVisible(false);

        update_btn.setVisible(true);
        update_btn.setDisable(false);

        delete_btn.setVisible(true);
        delete_btn.setDisable(false);
    }

    public void toAddButton() {
        addBook_btn.setDisable(false);
        addBook_btn.setVisible(true);

        update_btn.setVisible(false);
        update_btn.setDisable(true);

        delete_btn.setVisible(false);
        delete_btn.setDisable(true);
    }

    public void modifyButton() {
        if (QueryBookData.isExist(book.getId())) {
            quantity_tf.setText("" + book.getQuantity());
            toUpdateButton();
        } else {
            quantity_tf.setText("1");
            toAddButton();
        }
    }

}
