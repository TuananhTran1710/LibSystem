package com.jmc.libsystem.Controllers.Book;

import com.jmc.libsystem.Controllers.User.UserController;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.ShowListBookFound;
import com.jmc.libsystem.Views.UserMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class BookPreviewController implements Initializable {

    private static BookPreviewController instance;

    public BookPreviewController() {
        instance = this;
    }

    public static BookPreviewController getInstance() {
        return instance;
    }

    public Button back_btn;
    public Button borrow_btn;
    public Button comment_btn;
    public ImageView image_book;
    public Label title;
    public Button like_btn;
    public Label name_lbl;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back_btn.setOnAction(event -> moveMenuCurrent());
    }

    private void moveMenuCurrent() {
        UserMenuOptions menuCurrent = Model.getInstance().getViewFactory().getUserSelectedMenuItem().getValue();
        switch (menuCurrent) {
            case DASHBOARD ->
                    UserController.getInstance().user_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
            case MYBOOK ->
                    UserController.getInstance().user_parent.setCenter(Model.getInstance().getViewFactory().getMyBookView());
            case SEARCH ->
                    UserController.getInstance().user_parent.setCenter(Model.getInstance().getViewFactory().getSearchView());
        }
    }


    public void setUpInfo(Book book) {

        title.setText(book.getTitle());

        try {
            Image bookCoverImage = new Image(book.getThumbnailUrl(), true);
            image_book.setImage(bookCoverImage);
        } catch (Exception e) {
            image_book.setImage(ShowListBookFound.DEFAULT_BOOK_COVER);
        }
        image_book.setFitHeight(190);
        image_book.setFitWidth(160);
        image_book.setPreserveRatio(false);
    }

}
