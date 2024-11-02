package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.DatabaseDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.swing.text.TableView;
import java.beans.Statement;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminDashboardController {
    @FXML
    private TableView listBook;
    @FXML
    private Label numberUser;
    @FXML
    private Label numberBook;

//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        refreshData();
//    }

//    private void refreshData() {
//
//    }
//
//    public static ObservableList<Book> getBooks() {
//        ObservableList<Book> books = FXCollections.observableArrayList();
//        String query = "SELECT id, title, author FROM Book";
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery(query);
//
//        while (rs.next()) {
//            books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author")));
//        }
//    }
//
//    public static ResultSet getTotalReturned(String userId) {
//        ResultSet resultSet = null;
//        String query = "SELECT COUNT(DISTINCT google_book_id) AS total_returns " +
//                "FROM bookloans " +
//                "WHERE user_id = ? AND return_date IS NOT NULL";
//        try {
//            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
//            preparedStatement.setString(1, userId);
//            resultSet = preparedStatement.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return resultSet;
//    }

}
