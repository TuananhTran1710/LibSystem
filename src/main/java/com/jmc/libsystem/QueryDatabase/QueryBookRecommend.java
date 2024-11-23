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

    public static List<Book> getPreSuggestBooks() {
        String query = """ 
                SELECT b.google_book_id, b.title, b.authors, b.publishDate, b.description, b.thumbnail, b.page_count, b.language, b.category, b.state
                FROM bookrcm br
                JOIN book b ON br.google_book_id = b.google_book_id
                WHERE br.state = 'In queue';""";

        List<Book> bookList = new ArrayList<>();
        try (Connection conn = DatabaseDriver.getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Book book = Book.createBookProposeFromResultSet(resultSet);
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }
}
