package com.jmc.libsystem.Views;

import com.jmc.libsystem.Information.Comment;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class ShowListComment {
    public static void show(VBox container, List<Comment> listComment) {
        container.getChildren().clear();
        container.setSpacing(10);
        if (listComment.isEmpty()) {
            //do something
            return;
        }

        for (int i = 0; i < listComment.size(); i++) {
            Comment comment = listComment.get(i);
            VBox commentBox = createEachVbox(comment);
            container.getChildren().add(commentBox);
        }
    }


    private static VBox createEachVbox(Comment comment) {
        VBox commentBox = new VBox();
        commentBox.setSpacing(2); // Khoảng cách giữa các phần tử trong VBox

        // Tạo Label cho user_id
        Label userIdLabel = new Label(comment.getUser_id());
        userIdLabel.setPrefWidth(285);
        userIdLabel.setStyle("-fx-font-weight: bold;" +
                "-fx-font-size: 1.3em"); // Chữ đậm cho tên người dùng

        userIdLabel.setMaxWidth(Double.MAX_VALUE); // Đảm bảo Label dãn theo VBox
        userIdLabel.setMaxHeight(Double.MAX_VALUE);

        // Tạo HBox chứa các ngôi sao
        HBox rating_id = new HBox();
        rating_id.getChildren().add(userIdLabel);
        rating_id.setSpacing(0.0);

        HBox ratingBox = new HBox();
        ratingBox.setSpacing(2); // Khoảng cách giữa các ngôi sao

        // Thêm các ngôi sao vào HBox (có thể thay thế bằng hình ảnh ngôi sao)
        for (int i = 0; i < 5; i++) {
            Label star = new Label("★"); // Sử dụng ký tự ngôi sao
            if (i < comment.getRating()) {
                star.setStyle("-fx-font-size: 15; -fx-text-fill: gold;"); // Định dạng ngôi sao
            } else {
                star.setStyle("-fx-font-size: 15; -fx-text-fill: white;");
            }
            ratingBox.getChildren().add(star);

        }

        rating_id.getChildren().add(ratingBox);

        // Tạo Label cho nội dung bình luận
        Text textLabel = new Text(comment.getText());
        textLabel.setWrappingWidth(300); // Điều chỉnh độ rộng của dòng comment
        textLabel.setStyle("-fx-wrap-text: true;" +
                "-fx-font-size: 1.25em"); // Tự động xuống dòng khi vượt quá kích thước

        // Thêm các thành phần vào VBox
        commentBox.getChildren().addAll(rating_id, textLabel);
        commentBox.setStyle("-fx-background-color: #EEEEEE;" +
                "-fx-border-radius: 5px;" +
                "-fx-padding: 3px 10px 10px 10px;" +
                "-fx-border-color: #132A13;" +
                "-fx-border-weight: 2px;");
        commentBox.setPrefWidth(400);
        commentBox.setPrefHeight(55);

        return commentBox;
    }


}
