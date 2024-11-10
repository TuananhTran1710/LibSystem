package com.jmc.libsystem.Controllers.Book;

import com.jmc.libsystem.Controllers.Admin.AdminController;
import com.jmc.libsystem.Controllers.Admin.ManageBookController;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BookDetailAPI implements Initializable {


    public Button back_btn;
    public ImageView imageView;
    public Label title;
    public Button add_btn;
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
    public TextField quantity_btn;
    public Button subtract_btn;
    public Button plus_btn;
    private Book book;

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
        back_btn.setOnAction(event -> moveMenuCurrent());
    }

    private void moveMenuCurrent() {
        AdminController.getInstance().admin_parent.setCenter(Model.getInstance().getViewFactory().getManageBookView());
        ManageBookController.getInstance().resetBookLibrary();
    }

    public void setUpInfo(Book book) {

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
}
