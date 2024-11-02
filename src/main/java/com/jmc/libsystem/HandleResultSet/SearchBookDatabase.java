package com.jmc.libsystem.HandleResultSet;

import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.QueryDatabase.QueryBookData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchBookDatabase {
    public static List<Book> getBookFromResultSet(String keyword) {
        ResultSet resultSet = QueryBookData.getBookForSearch(keyword);
        List<Book> listBook = new ArrayList<>();

        try {
            if (!resultSet.isBeforeFirst()) {

                System.out.println("There isn't any book found through this keyword");
                // return empty list
                return listBook;
            } else {
                while (resultSet.next()) {
                    String id = resultSet.getString("google_book_id");
                    String authors = resultSet.getString("authors");
                    String title = resultSet.getString("title");
                    String imageBook = resultSet.getString("thumbnail_url");
                    listBook.add(new Book(id, title, authors, imageBook));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listBook;
    }


    public static List<Book> getBookFromResultSet(ResultSet BookData) {
        List<Book> listBook = new ArrayList<>();

        try {
            if (!BookData.isBeforeFirst()) {

                System.out.println("There isn't any book found into result set");
                // return empty list
                return listBook;
            } else {
                while (BookData.next()) {
                    String id = BookData.getString("google_book_id");
                    String authors = BookData.getString("authors");
                    String title = BookData.getString("title");
                    String imageBook = BookData.getString("thumbnail_url");
                    listBook.add(new Book(id, title, authors, imageBook));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listBook;
    }
}
