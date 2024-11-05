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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class BookDetailController implements Initializable {

    private static BookDetailController instance;

    public Button back_btn;
    public Label author_lbl;
    public Label publishDate_lbl;
    public Label quantity_lbl;
    public Label pages_lbl;
    public ImageView image;
    public Label title;
    public Label sumRating_lbl;
    public Button borrow_btn;
    public Button like_btn;
    public Button return_btn;
    public Button unlike_btn;
    public Label totalLoan_lbl;
    public Button comment_btn;
    public Label description_lbl;
    public Label available_lbl;
    public Label categories_lbl;
    public ScrollPane scrollPane;


    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public static BookDetailController getInstance() {
        return instance;
    }

    public BookDetailController() {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borrow_btn.setFocusTraversable(false);
        like_btn.setFocusTraversable(false);
        return_btn.setFocusTraversable(false);
        unlike_btn.setFocusTraversable(false);

        back_btn.setOnAction(event -> moveMenuCurrent());
        return_btn.setOnAction(event -> {
            toBorrowButton();
            QueryBookLoans.updateReturnBook(book.getId());
            book.setNumBorrowing(book.getNumBorrowing() - 1);
            available_lbl.setText("Available: " + String.valueOf(book.getQuantity() - book.getNumBorrowing()) + " copies");
            available_lbl.setTextFill(Color.BLACK);
        });
        borrow_btn.setOnAction(event -> {
            toReturnButton();
            QueryBookLoans.insertNewRecord(book.getId());
            book.setNumBorrowing(book.getNumBorrowing() + 1);
            book.setTotalLoan(book.getTotalLoan() + 1);

            int availableNumber = book.getQuantity() - book.getNumBorrowing();
            if (availableNumber == 0) {
                available_lbl.setText("Out of stock");
                available_lbl.setTextFill(Color.RED);
            } else {
                available_lbl.setText("Available: " + availableNumber + " copies");
                available_lbl.setTextFill(Color.BLACK);
            }

            totalLoan_lbl.setText("Borrowed: " + String.valueOf(book.getTotalLoan()) + " times");
        });
        like_btn.setOnAction(event -> {
            toUnlikeButton();
            QueryFavoriteBook.insertNewRecord(book.getId());
        });
        unlike_btn.setOnAction(event -> {
            toLikeButton();
            QueryFavoriteBook.deleteRecord(book.getId());
        });
    }

    private void toReturnButton() {
        return_btn.setVisible(true);
        borrow_btn.setVisible(false);

        return_btn.setDisable(false);
        borrow_btn.setDisable(true);
    }

    private void toBorrowButton() {
        return_btn.setVisible(false);
        borrow_btn.setVisible(true);

        return_btn.setDisable(true);
        borrow_btn.setDisable(false);

    }

    private void toLikeButton() {
        like_btn.setVisible(true);
        unlike_btn.setVisible(false);

        like_btn.setDisable(false);
        unlike_btn.setDisable(true);
    }

    private void toUnlikeButton() {
        like_btn.setVisible(false);
        unlike_btn.setVisible(true);

        like_btn.setDisable(true);
        unlike_btn.setDisable(false);
    }

    public void setDisableBorrowButton() {
        if (book.getQuantity() == book.getNumBorrowing()) {
            borrow_btn.setDisable(true);
        }
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
        scrollPane.setVvalue(0.0);

        title.setText(book.getTitle());
        author_lbl.setText(book.getAuthors());
        categories_lbl.setText(book.getCategory());
        publishDate_lbl.setText(book.getPublishDate().toString());
        quantity_lbl.setText(String.valueOf(book.getQuantity()));
        pages_lbl.setText(String.valueOf(book.getPageCount()));
        description_lbl.setText(book.getDescription());
        totalLoan_lbl.setText("Borrowed: " + String.valueOf(book.getTotalLoan()) + " times");

        int availableNumber = book.getQuantity() - book.getNumBorrowing();
        if (availableNumber == 0) {
            available_lbl.setText("Out of stock");
            available_lbl.setTextFill(Color.RED);
        } else {
            available_lbl.setText("Available: " + String.valueOf(book.getQuantity() - book.getNumBorrowing()) + " copies");
            available_lbl.setTextFill(Color.BLACK);
        }


        if (book.getCountRating() > 0) {
            sumRating_lbl.setText("(" + String.valueOf(book.getSumRatingStar()) + " ratings)");
        } else {
            sumRating_lbl.setText("Nothing rating");
        }

        // Thiết lập hình ảnh bìa sách
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

        if (QueryBookLoans.isBorrowing(book.getId())) toReturnButton();
        else toBorrowButton();

        if (QueryFavoriteBook.isFavorite(book.getId())) toUnlikeButton();
        else toLikeButton();

        setDisableBorrowButton();
    }
}
