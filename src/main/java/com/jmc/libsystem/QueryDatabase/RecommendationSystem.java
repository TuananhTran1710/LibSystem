package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.DatabaseDriver;
import com.jmc.libsystem.Models.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecommendationSystem {

    // Trọng số cho từng tiêu chí
    private static final double WEIGHT_FAVORITE = 0.4;
    private static final double WEIGHT_FEEDBACK = 0.4;
    private static final double WEIGHT_LOANS = 0.4;
    private static final double WEIGHT_RECOMMEND = 0.3;
    private static final double WEIGHT_SIMILAR_AUTHOR = 0.5;
    private static final double WEIGHT_SIMILAR_GENRE = 0.5;
    public static HashMap<String, Double> bookRcm = new HashMap<>();

    public static void calScoreBasedFavorite(String userId) {
        String query = "SELECT google_book_id FROM favorite WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String bookId = resultSet.getString("google_book_id");
                bookRcm.put(bookId, bookRcm.getOrDefault(bookId, 0.0) + WEIGHT_FAVORITE);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void calScoreBasedFeedback() {
        String query = "SELECT google_book_id, sumRatingStar, countRating FROM book GROUP BY google_book_id";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String bookId = resultSet.getString("google_book_id");
                int sumStar = resultSet.getInt("sumRatingStar");
                int countRating = resultSet.getInt("countRating");

                bookRcm.put(bookId, bookRcm.getOrDefault(bookId, 0.0) + WEIGHT_FEEDBACK * sumStar / countRating);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void calScoreBasedNumLoan() {
        String query = "SELECT google_book_id, COUNT(*) AS loan_count FROM BookLoans GROUP BY google_book_id";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String bookId = resultSet.getString("google_book_id");
                int loanCount = resultSet.getInt("loan_count");

                bookRcm.put(bookId, bookRcm.getOrDefault(bookId, 0.0) + WEIGHT_LOANS * loanCount);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // dua tren so luot de xuat
    public static void calScoreBasedRecommend() {
        String query = "SELECT google_book_id, count(*) as totalRcm FROM BookRcm group by google_book_id";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String bookId = resultSet.getString("google_book_id");
                int countRcm = resultSet.getInt("totalRcm");

                bookRcm.put(bookId, bookRcm.getOrDefault(bookId, 0.0) + WEIGHT_RECOMMEND * countRcm);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void calScoreBasedAuthorAndGenre(String user_id) {
        String query = "SELECT DISTINCT b.category, b.authors FROM BookLoans bl natural JOIN Book b WHERE bl.user_id = ?";
        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String[] categories = resultSet.getString("category").split(", ");
                // Tìm sách có bất kỳ thể loại hoặc tác giả nào khớp với danh sách đã mượn
                for (String category : categories) {
                    query = "SELECT google_book_id FROM Book WHERE (category COLLATE utf8mb4_general_ci LIKE ?)";
                    try {
                        preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
                        preparedStatement.setString(1, "%" + category + "%");
                        resultSet = preparedStatement.executeQuery();

                        while (resultSet.next()) {
                            String bookId = resultSet.getString("google_book_id");
                            bookRcm.put(bookId, bookRcm.getOrDefault(bookId, 0.0) + WEIGHT_SIMILAR_GENRE);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }


                String[] authors = resultSet.getString("authors").split(", ");
                // Tìm sách có bất kỳ thể loại hoặc tác giả nào khớp với danh sách đã mượn
                for (String author : authors) {
                    query = "SELECT google_book_id FROM Book WHERE (authors COLLATE utf8mb4_general_ci LIKE ?)";
                    try {
                        preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
                        preparedStatement.setString(1, "%" + author + "%");
                        resultSet = preparedStatement.executeQuery();

                        while (resultSet.next()) {
                            String bookId = resultSet.getString("google_book_id");
                            bookRcm.put(bookId, bookRcm.getOrDefault(bookId, 0.0) + WEIGHT_SIMILAR_AUTHOR);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Hàm lấy ResultSet chứa all in4 sách từ HashMap bookRcm
    public static ResultSet getResultSetFromMap() {
        bookRcm = new HashMap<>();

        calScoreBasedFavorite(Model.getInstance().getMyUser().getId());
        calScoreBasedFeedback();
        calScoreBasedNumLoan();
        calScoreBasedRecommend();
        calScoreBasedAuthorAndGenre(Model.getInstance().getMyUser().getId());


        Set<String> bookIds = bookRcm.keySet();
        String placeholders = bookIds.stream().map(id -> "?").collect(Collectors.joining(", "));
        String query = "SELECT * FROM Book WHERE google_book_id IN (" + placeholders + ")";

        try {
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            int index = 1;
            for (String id : bookIds) {
                preparedStatement.setString(index++, id);
            }
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static List<Book> getListRecommend() {
        List<Book> res = SearchBookDatabase.getBookFromResultSet(getResultSetFromMap());
        //sort giam dan theo value trong hashmap
        res.sort((a, b) -> (int) (bookRcm.get(b.getId()) - bookRcm.get(a.getId())));
        return res;
    }
}
