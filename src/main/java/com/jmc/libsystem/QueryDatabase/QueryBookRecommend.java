package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.DatabaseDriver;
import com.jmc.libsystem.Models.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryBookRecommend {
    public static ResultSet getAllPropse() {
        ResultSet resultSet = null;
        String query = "SELECT bookrcm.id, book.google_book_id, title, authors, bookrcm.state FROM bookrcm\n" +
                "join book on bookrcm.google_book_id = book.google_book_id" +
                " order by bookrcm.id desc\n";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void updateStatePropose(String id, String state) {
        String query = "UPDATE bookrcm SET state = ? WHERE google_book_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, state);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getProposeForFilter(String type) {
        ResultSet resultSet = null;
        // no limit num of book displayed
        String query = "SELECT book.google_book_id, title, authors, bookrcm.state FROM bookrcm\n" +
                "join book on bookrcm.google_book_id = book.google_book_id\n" +
                "where bookrcm.state = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, type);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getNumberofState(String state) {
        ResultSet resultSet = null;
        // no limit num of book displayed
        String query = "select count(*) as count from bookrcm\n" +
                "where state = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, state);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getCountAllPropose() {
        ResultSet resultSet = null;
        // no limit num of book displayed
        String query = "select count(*) as count from bookrcm";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void insertNewRecommend(String book_id, String state) {
        String query = "INSERT INTO bookrcm (google_book_id, user_id, state) VALUE (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, book_id);
            preparedStatement.setString(2, Model.getInstance().getMyUser().getId());
            preparedStatement.setString(3, state);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Book> getPreSuggestBooks(String userId) {
        String query = """
                SELECT b.google_book_id, b.title, b.authors, b.publishDate, b.description, b.thumbnail, b.page_count, b.language, b.category, br.state
                FROM bookrcm br
                JOIN book b ON br.google_book_id = b.google_book_id
                WHERE br.user_id = ?;""";

        List<Book> bookList = new ArrayList<>();
        try (Connection conn = DatabaseDriver.getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = Book.createBookProposeFromResultSet(resultSet);
                    bookList.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }
}
