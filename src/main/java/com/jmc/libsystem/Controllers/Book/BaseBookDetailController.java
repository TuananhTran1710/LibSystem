package com.jmc.libsystem.Controllers.Book;

import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Views.ShowListBookFound;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BaseBookDetailController implements Initializable {
    public Button back_btn;
    public Label publishDate_lbl;
    public Label pages_lbl;
    public ImageView imageView;
    public Label title;
    public Label id_lbl;
    public Label lan_lbl;
    public Label description_lbl;

    public FontAwesomeIconView star1;
    public FontAwesomeIconView star2;
    public FontAwesomeIconView star3;
    public FontAwesomeIconView star4;
    public FontAwesomeIconView star5;

    public Label notice_lbl;
    protected Timeline noticeTimeline;

    public VBox authorsContainer;
    public VBox categoriesContainer;

    protected List<FontAwesomeIconView> starsAverage;
    protected Book book;

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        starsAverage = List.of(star1, star2, star3, star4, star5);
        back_btn.setOnAction(event -> moveMenuCurrent());
    }

    public void createTimeLine() {
        // Chỉ tạo Timeline nếu nó chưa được khởi tạo
        if (noticeTimeline == null) {
            noticeTimeline = new Timeline(new KeyFrame(
                    Duration.seconds(1.5),
                    evt -> notice_lbl.setText("")
            ));
            noticeTimeline.setCycleCount(1);
        }

        // Dừng và chạy lại Timeline để đảm bảo nhãn sẽ biến mất sau 3 giây
        noticeTimeline.stop();
        noticeTimeline.playFromStart();
    }

    // khong dinh nghia o day vi deu duoc overide o lop con
    public void moveMenuCurrent() {
    }

    public void setRating(List<FontAwesomeIconView> stars, int rating) {
        for (int i = 0; i < stars.size(); i++) {
            if (i < rating) {
                stars.get(i).setFill((Color.web("gold")));
            } else {
                stars.get(i).setFill((Color.web("#FFFFFF"))); // Default color
            }
        }
    }


    public void setUpInfo(Book book) {
        title.setText(book.getTitle());
        publishDate_lbl.setText(book.getPublishDate().toString());
        pages_lbl.setText(String.valueOf(book.getPageCount()));
        description_lbl.setText(book.getDescription());
        id_lbl.setText(book.getId());
        lan_lbl.setText(book.getLanguage());

        if (book.getSumRatingStar() == 0) {
            setRating(starsAverage, 0);
        } else
            setRating(starsAverage, book.getSumRatingStar() / book.getCountRating());

        // set anh
        try {
            Image bookCoverImage;
            if (book.getThumbnailImage() != null) {
                // Create Image from byte array
                bookCoverImage = new Image(new ByteArrayInputStream(book.getThumbnailImage()));
            } else {
                bookCoverImage = ShowListBookFound.DEFAULT_BOOK_COVER;
            }
            imageView.setImage(bookCoverImage);
        } catch (Exception e) {
            imageView.setImage(ShowListBookFound.DEFAULT_BOOK_COVER);
        }


        imageView.setFitHeight(280);
        imageView.setFitWidth(225);
        imageView.setPreserveRatio(false);

        // Hiển thị danh sách tác giả dưới dạng các ô, mỗi dòng chứa 2 ô
        authorsContainer.getChildren().clear();
        List<String> authors = List.of(book.getAuthors().split(", "));
        for (int i = 0; i < Math.min(authors.size(), 4); i += 2) {
            HBox row = new HBox(5);
            for (int j = i; j < i + 2 && j < authors.size(); j++) {
                String author = authors.get(j);
                author = author.length() > 20 ? author.substring(0, 20) + "..." : author;
                Label authorLabel = new Label(author);
                authorLabel.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 5 10; -fx-border-radius: 15; -fx-background-radius: 15;");
                row.getChildren().add(authorLabel);
            }
            authorsContainer.getChildren().add(row);
        }

        // Hiển thị danh mục sách dưới dạng các ô, mỗi dòng chứa 2 ô
        categoriesContainer.getChildren().clear();
        List<String> categories = List.of(book.getCategory().split(", "));
        for (int i = 0; i < categories.size(); i += 3) {
            HBox row = new HBox(5);
            for (int j = i; j < i + 3 && j < categories.size(); j++) {
                String cate = categories.get(j);
                cate = cate.length() > 20 ? cate.substring(0, 20) + "..." : cate;
                Label categoryLabel = new Label(cate);
                categoryLabel.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 5 10; -fx-border-radius: 15; -fx-background-radius: 15;");
                row.getChildren().add(categoryLabel);
            }
            categoriesContainer.getChildren().add(row);
        }
    }

    //luon phai Override o lop con
    public void modifyButton() {
    }
}
