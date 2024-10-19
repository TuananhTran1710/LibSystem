package com.jmc.libsystem.HandleResultSet;

import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.QueryDatabase.HandleBookDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchBookDatabase {
    public static List<Book> getBookFromResultSet(String keyword) {
        ResultSet resultSet = HandleBookDatabase.getBookData(keyword);
        List<Book> listBook = new ArrayList<>();

        try {
            if (!resultSet.isBeforeFirst()) {

                System.out.println("There isn't any book found");
                // return empty list
                return listBook;
            } else {
                while (resultSet.next()) {
                    String authors = resultSet.getString("authors");
                    String title = resultSet.getString("title");
                    String imageBook = resultSet.getString("thumbnail_url");
                    listBook.add(new Book(title, authors, imageBook));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listBook;
    }
}
