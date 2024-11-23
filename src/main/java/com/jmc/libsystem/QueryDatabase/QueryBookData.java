package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.DatabaseDriver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryBookData {
    public static ResultSet getBookForSearch(String keyWord, String typeSearch) {
        ResultSet resultSet = null;
        // no limit num of book displayed
        String query = "SELECT * FROM book WHERE " + typeSearch + " COLLATE utf8mb4_general_ci LIKE ? and state = 'publishing' ";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            // Thêm % vào từ khóa tìm kiếm cho LIKE
            preparedStatement.setString(1, "%" + keyWord + "%");
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getBookForSearchV2(String keyWord, String typeSearch) {
        ResultSet resultSet = null;
        // no limit num of book displayed
        String query = "SELECT * FROM book WHERE " + typeSearch + " COLLATE utf8mb4_general_ci LIKE ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            // Thêm % vào từ khóa tìm kiếm cho LIKE
            preparedStatement.setString(1, "%" + keyWord + "%");
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    public static ResultSet getBookForSearchAllBook(String keyWord, String typeSearch) {
        ResultSet resultSet = null;
        // no limit num of book displayed
        String query = "SELECT * FROM book WHERE " + typeSearch + " COLLATE utf8mb4_general_ci LIKE ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            // Thêm % vào từ khóa tìm kiếm cho LIKE
            preparedStatement.setString(1, "%" + keyWord + "%");
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getBook(String book_id) {
        ResultSet resultSet = null;
        String query = "SELECT * FROM book WHERE google_book_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, book_id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getDataForSuggest() {
        ResultSet resultSet = null;
        String query = "SELECT distinct title, authors, category FROM book where state = 'publishing'";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // truy van co van de ?
    public static ResultSet getBookStatistic() {
        ResultSet resultSet = null;
        String query = "SELECT b.google_book_id, b.thumbnail, b.title, b.authors, " +
                "    b.quantity AS total_books, " +
                "    b.totalLoan AS total_borrowed_books, " +
                "    CASE " +
                "        WHEN b.quantity > b.numBorrowing THEN 'Available' " +
                "        ELSE 'Out of stock' " +
                "    END AS status " +
                "FROM " +
                "    Book b " +
                "where b.state = 'publishing'";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getCountBook() {
        ResultSet resultSet = null;
        try {
            String query = "SELECT COUNT(*) as count FROM book where state = 'publishing'";

            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getCountBookLoan() {
        ResultSet resultSet = null;
        try {
            String query = "SELECT sum(totalLoan) as count FROM book;";

            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getCategorySort3() {
        ResultSet resultSet = null;
        try {
            String query = "WITH category_split AS (\n" +
                    "    SELECT \n" +
                    "        TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(category, ',', numbers.n), ',', -1)) AS genre,\n" +
                    "        quantity\n" +
                    "    FROM \n" +
                    "        book\n" +
                    "    JOIN \n" +
                    "        (SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 \n" +
                    "         UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10) numbers\n" +
                    "        ON CHAR_LENGTH(category) - CHAR_LENGTH(REPLACE(category, ',', '')) >= numbers.n - 1\n" +
                    "    WHERE \n" +
                    "        state != 'In queue'\n" +
                    ")\n" +
                    "SELECT \n" +
                    "    genre AS category,\n" +
                    "    SUM(quantity) AS total_books\n" +
                    "FROM \n" +
                    "    category_split\n" +
                    "GROUP BY \n" +
                    "    genre\n" +
                    "ORDER BY \n" +
                    "    total_books DESC\n" +
                    "LIMIT 3;\n";

            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getAllBook() {
        ResultSet resultSet = null;
        String query = "SELECT * FROM book";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static boolean isExist(String book_id) {
        String query = "SELECT EXISTS (SELECT 1 FROM book WHERE google_book_id = ?)";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, book_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getBoolean(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static void addBook(Book book, int num) {
        String query = "INSERT INTO book (google_book_id, title, authors, category, publishDate, page_count, description, language, thumbnail, quantity, state) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, book.getId());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthors());
            preparedStatement.setString(4, book.getCategory());

            preparedStatement.setDate(5, java.sql.Date.valueOf(book.getPublishDate()));

            preparedStatement.setInt(6, book.getPageCount());
            preparedStatement.setString(7, book.getDescription());
            preparedStatement.setString(8, book.getLanguage());
            preparedStatement.setBytes(9, book.getThumbnailImage());
            preparedStatement.setInt(10, num);
            preparedStatement.setString(11, "publishing");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addBookRcm(Book book) {
        String query = "INSERT INTO book (google_book_id, title, authors, category, publishDate, page_count, description, language, thumbnail, quantity, state) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, book.getId());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthors());
            preparedStatement.setString(4, book.getCategory());

            preparedStatement.setDate(5, java.sql.Date.valueOf(book.getPublishDate()));

            preparedStatement.setInt(6, book.getPageCount());
            preparedStatement.setString(7, book.getDescription());
            preparedStatement.setString(8, book.getLanguage());
            preparedStatement.setBytes(9, book.getThumbnailImage());
            preparedStatement.setInt(10, 1);
            preparedStatement.setString(11, "In queue");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBook(String book_id, int num) {
        String query = "UPDATE book SET quantity = ? WHERE google_book_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setInt(1, num);
            preparedStatement.setString(2, book_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBook(String book_id) {
        String query = "DELETE FROM book WHERE google_book_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, book_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateState(String book_id, String state) {
        String query = "UPDATE book SET state = ? WHERE google_book_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, state);
            preparedStatement.setString(2, book_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateQuantity(String id, int quantity) {
        String query = "UPDATE book SET quantity = ? WHERE google_book_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

