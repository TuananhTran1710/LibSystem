package com.jmc.libsystem.Controllers;

import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.QueryDatabase.QueryUserBook;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class MyBookController implements Initializable {

    public Label NumberBorrow;
    public Label NumberReturn;
    public Label NumberFavorite;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NumberFavorite.setText("0");
        String user_id = Model.getInstance().getMyUser().getId();
        int borrow = 0;
        int returns = 0;
        try {
            borrow = getBorrowBook(user_id);
            returns = getReturnBook(user_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        NumberBorrow.setText(Integer.toString(borrow));
        NumberReturn.setText(Integer.toString(returns));
    }

    private int getBorrowBook(String userId) throws SQLException {
        ResultSet resultSet = QueryUserBook.Borrow(userId);
        if (resultSet.next()) {
            int totalBorrows = resultSet.getInt("total_borrows");
            return totalBorrows;
        } else {
            System.out.println("User not found.");
            return 0;
        }
    }

    private int getReturnBook(String userId) throws SQLException {
        ResultSet resultSet = QueryUserBook.Return(userId);
        if (resultSet.next()) {
            int totalReturn = resultSet.getInt("total_return");
            return totalReturn;
        } else {
            System.out.println("User not found.");
            return 0;
        }
    }
}
