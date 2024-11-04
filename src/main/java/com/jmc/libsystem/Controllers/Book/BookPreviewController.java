package com.jmc.libsystem.Controllers.Book;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.Controllers.User.MyBookController;
import com.jmc.libsystem.Controllers.User.UserController;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import com.jmc.libsystem.QueryDatabase.QueryFavoriteBook;
import com.jmc.libsystem.Views.ShowListBookFound;
import com.jmc.libsystem.Views.UserMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class BookPreviewController implements Initializable {

    private static BookPreviewController instance;

    public Button back_btn;
    public Label author_lbl;
    public Label publishDate_lbl;
    public Label quantity_lbl;
    public Label pages_lbl;
    public Text description_text;
    public ImageView image;
    public Label title;
    public Label sumRating_lbl;
    public Button borrow_btn;
    public Button like_btn;
    public Button return_btn;
    public Button unlike_btn;
    public Label totalLoan_lbl;
    public Button comment_btn;


    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public static BookPreviewController getInstance() {
        return instance;
    }

    public BookPreviewController() {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back_btn.setOnAction(event -> moveMenuCurrent());
        return_btn.setOnAction(event -> toBorrowBook());
        borrow_btn.setOnAction(event -> toReturnBook());
        like_btn.setOnAction(event -> toUnlikeBook());
        unlike_btn.setOnAction(event -> toLikeBook());
    }

    private void toReturnBook() {
        return_btn.setVisible(true);
        borrow_btn.setVisible(false);

        return_btn.setDisable(false);
        borrow_btn.setDisable(true);
        // truy van va them vao csdl bookLoan
        // can chinh
        if (!QueryBookLoans.isBorrowing(book.getId()))
            QueryBookLoans.insertNewRecord(book.getId());
    }

    private void toBorrowBook() {
        return_btn.setVisible(false);
        borrow_btn.setVisible(true);

        return_btn.setDisable(true);
        borrow_btn.setDisable(false);

        // truy van va cap nhat csdl o column return_date
        QueryBookLoans.updateReturnBook(book.getId());
    }

    private void toLikeBook() {
        like_btn.setVisible(true);
        unlike_btn.setVisible(false);

        like_btn.setDisable(false);
        unlike_btn.setDisable(true);

        //truy van va xoa khoi bang favorite
        QueryFavoriteBook.deleteRecord(book.getId());
    }

    private void toUnlikeBook() {
        like_btn.setVisible(false);
        unlike_btn.setVisible(true);

        like_btn.setDisable(true);
        unlike_btn.setDisable(false);

        // truy van va them vao bang favorite
        // can chinh
        if (!QueryFavoriteBook.isFavorite(book.getId()))
            QueryFavoriteBook.insertNewRecord(book.getId());
    }

    private void moveMenuCurrent() {
        UserMenuOptions menuCurrent = Model.getInstance().getViewFactory().getUserSelectedMenuItem().getValue();
        switch (menuCurrent) {
            case DASHBOARD -> {
                UserController.getInstance().user_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                // de cho tiet kiem, chi reset phan reading
                DashboardController.getInstance().resetReading();
            }
            case MYBOOK -> {
                UserController.getInstance().user_parent.setCenter(Model.getInstance().getViewFactory().getMyBookView());
                MyBookController.getInstance().refreshData();
            }
            case SEARCH ->
                    UserController.getInstance().user_parent.setCenter(Model.getInstance().getViewFactory().getSearchView());
        }
    }


    public void setUpInfo(Book book) {
        title.setText(book.getTitle());

        try {
            Image bookCoverImage = new Image(book.getThumbnailUrl(), true);
            image.setImage(bookCoverImage);
        } catch (Exception e) {
            image.setImage(ShowListBookFound.DEFAULT_BOOK_COVER);
        }
        image.setFitHeight(190);
        image.setFitWidth(160);
        image.setPreserveRatio(false);
    }

    public void modifyButton() {
        if (QueryBookLoans.isBorrowing(book.getId())) toReturnBook();
        else toBorrowBook();

        if (QueryFavoriteBook.isFavorite(book.getId())) toUnlikeBook();
        else toLikeBook();
    }
}
