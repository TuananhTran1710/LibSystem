package com.jmc.libsystem.Views;

import com.jmc.libsystem.Information.Comment;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class ShowListComment {
    public static void show(VBox container, List<Comment> listComment) {
        container.getChildren().clear();
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
        userIdLabel.setFont(new Font("Arial", 14));
        userIdLabel.setStyle("-fx-font-weight: bold;"); // Chữ đậm cho tên người dùng
        userIdLabel.setMaxWidth(Double.MAX_VALUE); // Đảm bảo Label dãn theo VBox
        userIdLabel.setMaxHeight(Double.MAX_VALUE);

        // Tạo HBox chứa các ngôi sao
        HBox ratingBox = new HBox();

        ratingBox.getChildren().add(userIdLabel);
        ratingBox.setSpacing(2); // Khoảng cách giữa các ngôi sao

        // Thêm các ngôi sao vào HBox (có thể thay thế bằng hình ảnh ngôi sao)
        for (int i = 0; i < comment.getRating(); i++) {
            Label star = new Label("★"); // Sử dụng ký tự ngôi sao
            star.setStyle("-fx-font-size: 12; -fx-text-fill: gold;"); // Định dạng ngôi sao
            ratingBox.getChildren().add(star);
        }

        // Tạo Label cho nội dung bình luận
        Text textLabel = new Text(comment.getText());
        textLabel.setFont(new Font("Arial", 12));
        textLabel.setWrappingWidth(300); // Điều chỉnh độ rộng của dòng comment
        textLabel.setStyle("-fx-wrap-text: true;"); // Tự động xuống dòng khi vượt quá kích thước

        // Thêm các thành phần vào VBox
        commentBox.getChildren().addAll(ratingBox, textLabel);
        return commentBox;
    }


}
