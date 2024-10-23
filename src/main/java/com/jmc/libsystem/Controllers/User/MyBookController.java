package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MyBookController implements Initializable {

    public Label NumberBorrow;
    public Label NumberReturn;
    public Label NumberFavorite;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NumberFavorite.setText("0");
        String user_id = Model.getInstance().getMyUser().getId();
        try {
            int borrow = getBorrowBook(user_id);
            int returns = getReturnBook(user_id);
            NumberBorrow.setText(Integer.toString(borrow));
            NumberReturn.setText(Integer.toString(returns));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getBorrowBook(String userId) throws SQLException {
        try (ResultSet resultSet = QueryBookLoans.Borrow(userId)) {
            if (resultSet.next()) {
                int totalBorrows = resultSet.getInt("total_borrows");
                return totalBorrows;
            } else {
                System.out.println("User not found.");
                return 0;
            }
        }
    }

    private int getReturnBook(String userId) throws SQLException {
        try (ResultSet resultSet = QueryBookLoans.Return(userId)) {
            if (resultSet.next()) {
                int totalReturn = resultSet.getInt("total_returns");
                return totalReturn;
            } else {
                System.out.println("User not found.");
                return 0;
            }
        }
    }
}