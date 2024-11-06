package com.jmc.libsystem.Views;

import com.jmc.libsystem.Information.Comment;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class ShowListComment {
    public static void show(VBox container, List<Comment> listComment, int limit) {
        container.getChildren().clear();
        if (listComment.isEmpty()) {
            //do something
            return;
        }

        for (int i = 0; i < Math.min(listComment.size(), limit); i++) {
            Comment comment = listComment.get(i);
            VBox commentBox = createEachVbox(comment);
            container.getChildren().add(commentBox);
        }
    }


    private static VBox createEachVbox(Comment comment) {
        VBox commentBox = new VBox();
        commentBox.setSpacing(5); // Khoảng cách giữa các phần tử trong VBox

        // Tạo Label cho user_id
        Label userIdLabel = new Label(comment.getUser_id());
        userIdLabel.setFont(new Font("Arial", 14));
        userIdLabel.setStyle("-fx-font-weight: bold;"); // Chữ đậm cho tên người dùng

        // Tạo HBox chứa các ngôi sao
        HBox ratingBox = new HBox();
        ratingBox.setSpacing(2); // Khoảng cách giữa các ngôi sao

//        int rating = comment.getRating();
//        for (int i = 1; i <= 5; i++) {
//            FontIcon starIcon = new FontIcon("fas-star");
//            starIcon.setIconSize(16);
//            if (i <= rating) {
//                starIcon.setIconColor(Color.BLACK); // Ngôi sao đã đánh giá
//            } else {
//                starIcon.setIconColor(Color.GREY); // Ngôi sao chưa đánh giá
//            }
//            ratingBox.getChildren().add(starIcon);
//        }

        // Tạo Label cho nội dung bình luận
        Text textLabel = new Text(comment.getText());
        textLabel.setFont(new Font("Arial", 12));
        textLabel.setWrappingWidth(300); // Điều chỉnh độ rộng của dòng comment

        // Thêm các thành phần vào VBox
        commentBox.getChildren().addAll(userIdLabel, ratingBox, textLabel);

        return commentBox;
    }

}
