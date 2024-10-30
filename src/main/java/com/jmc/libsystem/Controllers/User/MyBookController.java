package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import com.jmc.libsystem.QueryDatabase.QueryFavoriteBook;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MyBookController implements Initializable {

    public Label NumberBorrow;
    public Label NumberReturn;
    public Label NumberFavorite;
    public HBox Borrow_HB;
    public HBox Favorite_HB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NumberFavorite.setText("0");
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
    }

    private int getBorrowBook(String userId) throws SQLException {
        try (ResultSet resultSet = QueryBookLoans.getTotalLoaned(userId)) {
            if (resultSet.next()) {
                int totalBorrows = resultSet.getInt("total_borrows");
                return totalBorrows;
            } else {
                System.out.println("You haven't ever loaned any book");
                return 0;
            }
        }
    }

    private int getReturnBook(String userId) throws SQLException {
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

    private int getFavoriteBook(String userId) throws SQLException {
        ResultSet resultSet = QueryFavoriteBook.getTotalFavorite(userId);
        if (resultSet.next()) {
            int totalFavorite = resultSet.getInt("total_favorite");
            return totalFavorite;
        } else {
            System.out.println("Your favorite book list is empty!");
            return 0;
        }
    }

    private void showBorrowBook(String userId, HBox borrow_HB) {
        ResultSet resultSet = QueryBookLoans.getListBorrow(userId);
        List<Book> bookList = SearchBookDatabase.getBookFromResultSet(resultSet);
        ShowListBookFound.show(bookList, borrow_HB, 20);
    }

    private void showFavoriteBook(String userId, HBox favorite_HB) {
        ResultSet resultSet = QueryFavoriteBook.getListFavorite(userId);
        List<Book> bookList = SearchBookDatabase.getBookFromResultSet(resultSet);
        ShowListBookFound.show(bookList, favorite_HB, 20);
    }
}
