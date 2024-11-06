package com.jmc.libsystem.HandleResultSet;

import com.jmc.libsystem.Information.Comment;
import com.jmc.libsystem.QueryDatabase.QueryFeedback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HandleFeedback {
    public static List<Comment> getListComment(String book_id) {
        List<Comment> listComment = new ArrayList<>();

        ResultSet resultSet = QueryFeedback.setAllComment(book_id);

        try {
            if (!resultSet.isBeforeFirst()) {
                System.out.println("There isn't any feedback");
                return listComment;
            } else {
                while (resultSet.next()) {
                    String user_id = resultSet.getString("user_id");
                    String text = resultSet.getString("comment");
                    int rating = resultSet.getInt("rating");
                    listComment.add(new Comment(user_id, text, rating));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listComment;
    }

}
