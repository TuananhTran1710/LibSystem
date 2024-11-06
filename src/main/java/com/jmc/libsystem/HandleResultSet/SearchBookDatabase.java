package com.jmc.libsystem.HandleResultSet;

import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.QueryDatabase.QueryBookData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchBookDatabase {
    public static List<Book> getBookFromResultSet(String keyword) {
        List<Book> listBook = new ArrayList<>();

        ResultSet resultSet = QueryBookData.getBookForSearch(keyword);

        try {
            if (!resultSet.isBeforeFirst()) {

                System.out.println("There isn't any book found through this keyword");
                // return empty list
                return listBook;
            } else {
                while (resultSet.next()) {
                    listBook.add(Book.createBookFromResultSet(resultSet));
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
                    listBook.add(Book.createBookFromResultSet(BookData));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listBook;
    }
}
