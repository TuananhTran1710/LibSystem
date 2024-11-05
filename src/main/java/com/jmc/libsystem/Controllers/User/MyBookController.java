package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import com.jmc.libsystem.QueryDatabase.QueryFavoriteBook;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MyBookController implements Initializable {

    private static MyBookController instance;
    public Label NumberBorrow;
    public Label NumberReturn;
    public Label NumberFavorite;
    public HBox Borrow_HB;
    public HBox Favorite_HB;
    public ScrollPane scrollPane_fav;
    public ScrollPane scrollPane_borrow;
    public ChoiceBox<Integer> num_show_fav;
    public ChoiceBox<Integer> num_show_borrow;

    public static int limitBookBorrowing;
    public static int limitBookFavorite;

    private List<Book> borrowingList;
    private List<Book> favList;

    public MyBookController() {
        instance = this;
    }

    public static MyBookController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        num_show_borrow.setItems(FXCollections.observableArrayList(20, 50, 100));
        num_show_borrow.setValue(20);

        num_show_fav.setItems(FXCollections.observableArrayList(20, 50, 100));
        num_show_fav.setValue(20);

        limitBookBorrowing = 20;
        limitBookFavorite = 20;

        borrowingList = new ArrayList<>();
        favList = new ArrayList<>();

        num_show_borrow.valueProperty().addListener(observable -> modifyShowBookBorrow());
        num_show_fav.valueProperty().addListener(observable -> modifyShowBookFavorite());
        refreshData();
    }

    private void modifyShowBookFavorite() {
        limitBookFavorite = num_show_fav.getValue();
        ShowListBookFound.show(favList, Favorite_HB, limitBookFavorite);
    }

    private void modifyShowBookBorrow() {
        limitBookBorrowing = num_show_borrow.getValue();
        ShowListBookFound.show(borrowingList, Borrow_HB, limitBookBorrowing);
    }

    /*---------------------- refresh --------------------*/

    public void refreshData() {
        String user_id = Model.getInstance().getMyUser().getId();
        int borrow = 0;
        int returns = 0;
        int favorite = 0;

        try {
            borrow = getBorrowBook(user_id);
            returns = getReturnBook(user_id);
            favorite = getFavoriteBook(user_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        NumberBorrow.setText(Integer.toString(borrow));
        NumberReturn.setText(Integer.toString(returns));
        NumberFavorite.setText(Integer.toString(favorite));
        showBorrowBook(user_id, Borrow_HB);
        showFavoriteBook(user_id, Favorite_HB);

        scrollPane_borrow.setHvalue(0.0);
        scrollPane_fav.setHvalue(0.0);
    }

    /*-----------------------get data -----------------------*/

    private static int getBorrowBook(String userId) throws SQLException {
        try (ResultSet resultSet = QueryBookLoans.getTotalLoaned(userId)) {
            if (resultSet.next()) {
                int totalBorrows = resultSet.getInt("total_borrows");
                return totalBorrows;
            } else {
                System.out.println("You haven't ever borrowed any book");
                return 0;
            }
        }
    }

    private static int getReturnBook(String userId) throws SQLException {
        try (ResultSet resultSet = QueryBookLoans.getTotalReturned(userId)) {
            if (resultSet.next()) {
                int totalReturn = resultSet.getInt("total_returns");
                return totalReturn;
            } else {
                System.out.println("There wasn't any books returned");
                return 0;
            }
        }
    }

    private static int getFavoriteBook(String userId) throws SQLException {
        ResultSet resultSet = QueryFavoriteBook.getTotalFavorite(userId);
        if (resultSet.next()) {
            int totalFavorite = resultSet.getInt("total_favorite");
            return totalFavorite;
        } else {
            System.out.println("Your favorite book list is empty!");
            return 0;
        }
    }

    /*----------------------- show listbook ------------------*/

    private void showBorrowBook(String userId, HBox borrow_HB) {
        ResultSet resultSet = QueryBookLoans.getListBorrowing(userId);
        borrowingList = SearchBookDatabase.getBookFromResultSet(resultSet);
        ShowListBookFound.show(borrowingList, borrow_HB, limitBookBorrowing);
    }

    private void showFavoriteBook(String userId, HBox favorite_HB) {
        ResultSet resultSet = QueryFavoriteBook.getListFavorite(userId);
        favList = SearchBookDatabase.getBookFromResultSet(resultSet);
        ShowListBookFound.show(favList, favorite_HB, limitBookFavorite);
    }
}
