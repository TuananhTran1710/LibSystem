package com.jmc.libsystem.Controllers.Book;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.Controllers.User.MyBookController;
import com.jmc.libsystem.Controllers.User.UserController;
import com.jmc.libsystem.HandleResultSet.HandleFeedback;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Information.Comment;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import com.jmc.libsystem.QueryDatabase.QueryFavoriteBook;
import com.jmc.libsystem.QueryDatabase.QueryFeedback;
import com.jmc.libsystem.Views.ShowListBookFound;
import com.jmc.libsystem.Views.ShowListComment;
import com.jmc.libsystem.Views.UserMenuOptions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class BookDetailController implements Initializable {

    private static BookDetailController instance;

    public Button back_btn;
    public Label author_lbl;
    public Label publishDate_lbl;
    public Label quantity_lbl;
    public Label pages_lbl;
    public ImageView imageView;
    public Label title;
    public Button borrow_btn;
    public Button like_btn;
    public Button return_btn;
    public Button unlike_btn;
    public Label totalLoan_lbl;
    public Label description_lbl;
    public Label available_lbl;
    public Label categories_lbl;
    public FontAwesomeIconView star1;
    public FontAwesomeIconView star2;
    public FontAwesomeIconView star3;
    public FontAwesomeIconView star4;
    public FontAwesomeIconView star5;
    //phan cmt
    public VBox comment_list;
    public FontAwesomeIconView star_cmt1;
    public FontAwesomeIconView star_cmt2;
    public FontAwesomeIconView star_cmt3;
    public FontAwesomeIconView star_cmt4;
    public FontAwesomeIconView star_cmt5;
    public TextArea text_cmt;
    public Button edit_btn;
    public Button comment_btn;
    public Button deleted_btn;
    public Button save_btn;
    public ToggleButton overview_btn;
    public ToggleButton commentToggle_btn;
    public Label id_lbl;
    public Label lan_lbl;
    public VBox overview_vbox;
    public VBox comment_vbox;

    private List<Comment> feedbacks;

    private List<FontAwesomeIconView> starsAverage;
    private List<FontAwesomeIconView> starsComment;
    private int currentRating = 0; // Rating ban đầu

    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public static BookDetailController getInstance() {
        return instance;
    }

    public BookDetailController() {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borrow_btn.setFocusTraversable(false);
        like_btn.setFocusTraversable(false);
        return_btn.setFocusTraversable(false);
        unlike_btn.setFocusTraversable(false);

        starsAverage = List.of(star1, star2, star3, star4, star5);
        starsComment = List.of(star_cmt1, star_cmt2, star_cmt3, star_cmt4, star_cmt5);
        for (int i = 0; i < starsComment.size(); i++) {
            int index = i;
            starsComment.get(i).setOnMouseEntered(event -> highlightStars(index + 1));
            starsComment.get(i).setOnMouseExited(event -> resetStars());
            starsComment.get(i).setOnMouseClicked(event -> setRating(starsComment, index + 1));
        }


        back_btn.setOnAction(event -> moveMenuCurrent());
        return_btn.setOnAction(event -> {
            toBorrowButton();
            QueryBookLoans.updateReturnBook(book.getId());
            book.setNumBorrowing(book.getNumBorrowing() - 1);
            available_lbl.setText("Available: " + String.valueOf(book.getQuantity() - book.getNumBorrowing()) + " copies");
            available_lbl.setTextFill(Color.BLACK);
        });
        borrow_btn.setOnAction(event -> {
            toReturnButton();
            QueryBookLoans.insertNewRecord(book.getId());
            book.setNumBorrowing(book.getNumBorrowing() + 1);
            book.setTotalLoan(book.getTotalLoan() + 1);

            int availableNumber = book.getQuantity() - book.getNumBorrowing();
            if (availableNumber == 0) {
                available_lbl.setText("Out of stock");
                available_lbl.setTextFill(Color.RED);
            } else {
                available_lbl.setText("Available: " + availableNumber + " copies");
                available_lbl.setTextFill(Color.BLACK);
            }

            totalLoan_lbl.setText("Borrowed: " + String.valueOf(book.getTotalLoan()) + " times");
        });
        like_btn.setOnAction(event -> {
            toUnlikeButton();
            QueryFavoriteBook.insertNewRecord(book.getId());
        });
        unlike_btn.setOnAction(event -> {
            toLikeButton();
            QueryFavoriteBook.deleteRecord(book.getId());
        });

        overview_btn.setOnAction(event -> {
            moveToOverviewVBox();
        });

        commentToggle_btn.setOnAction(event -> {
            moveToCommentVbox();
        });

        edit_btn.setOnAction(event -> {
            onEditComment();
        });

        comment_btn.setOnAction(event -> {
            if (currentRating > 0 && !text_cmt.getText().trim().isEmpty()) {
                moveToEditComment();
                QueryFeedback.insertNewComment(book.getId(), text_cmt.getText(), currentRating);
                showFeedback();
            } else {

            }
        });

        save_btn.setOnAction(event ->
        {
            if (!text_cmt.getText().trim().isEmpty()) {
                moveToEditComment();
                QueryFeedback.modifyComment(book.getId(), text_cmt.getText(), currentRating);
                showFeedback();
            } else {

            }
        });
        deleted_btn.setOnAction(event -> {
            moveToSendComment();
            QueryFeedback.deleteComment(book.getId());
            showFeedback();
        });
    }


    // Đặt lại các ngôi sao về trạng thái đánh giá hiện tại khi rời chuột
    private void resetStars() {
        setRating(starsComment, currentRating);
    }

    // Làm sáng các ngôi sao đến vị trí chỉ định
    private void highlightStars(int rating) {
        for (int i = 0; i < starsComment.size(); i++) {
            if (i < rating) {
                starsComment.get(i).setFill((Color.web("gold")));
            } else {
                starsComment.get(i).setFill((Color.web("#FFFFFF"))); // Default color
            }
        }
    }

    public void setRating(List<FontAwesomeIconView> stars, int rating) {
        if (stars == starsComment) currentRating = rating;
        for (int i = 0; i < stars.size(); i++) {
            if (i < rating) {
                stars.get(i).setFill((Color.web("gold")));
            } else {
                stars.get(i).setFill((Color.web("#FFFFFF"))); // Default color
            }
        }
    }

    private void toReturnButton() {
        return_btn.setVisible(true);
        borrow_btn.setVisible(false);

        return_btn.setDisable(false);
        borrow_btn.setDisable(true);
    }

    private void toBorrowButton() {
        return_btn.setVisible(false);
        borrow_btn.setVisible(true);

        return_btn.setDisable(true);
        borrow_btn.setDisable(false);

    }

    private void toLikeButton() {
        like_btn.setVisible(true);
        unlike_btn.setVisible(false);

        like_btn.setDisable(false);
        unlike_btn.setDisable(true);
    }

    private void toUnlikeButton() {
        like_btn.setVisible(false);
        unlike_btn.setVisible(true);

        like_btn.setDisable(true);
        unlike_btn.setDisable(false);
    }

    private void moveToCommentVbox() {
        commentToggle_btn.setSelected(true);
        overview_btn.setSelected(false);

        overview_vbox.setDisable(true);
        overview_vbox.setVisible(false);

        comment_vbox.setDisable(false);
        comment_vbox.setVisible(true);
    }

    private void moveToOverviewVBox() {
        commentToggle_btn.setSelected(false);
        overview_btn.setSelected(true);

        overview_vbox.setDisable(false);
        overview_vbox.setVisible(true);

        comment_vbox.setDisable(true);
        comment_vbox.setVisible(false);
    }

    private void moveToEditComment() {
        deleted_btn.setVisible(false);
        deleted_btn.setDisable(true);

        save_btn.setVisible(false);
        save_btn.setDisable(true);

        edit_btn.setVisible(true);
        edit_btn.setDisable(false);

        comment_btn.setVisible(false);
        comment_btn.setDisable(true);

        for (int i = 0; i < 5; i++) {
            starsComment.get(i).setDisable(true);
        }

        text_cmt.setEditable(false);
    }

    private void moveToSendComment() {
        deleted_btn.setVisible(false);
        deleted_btn.setDisable(true);

        save_btn.setVisible(false);
        save_btn.setDisable(true);

        edit_btn.setVisible(false);
        edit_btn.setDisable(true);

        comment_btn.setDisable(false);
        comment_btn.setVisible(true);

        for (int i = 0; i < 5; i++) {
            starsComment.get(i).setDisable(false);
        }

        setRating(starsComment, 0);

        text_cmt.setEditable(true);
        text_cmt.clear();
    }

    private void onEditComment() {
        deleted_btn.setVisible(true);
        deleted_btn.setDisable(false);

        save_btn.setVisible(true);
        save_btn.setDisable(false);

        edit_btn.setVisible(false);
        edit_btn.setDisable(true);

        comment_btn.setDisable(true);
        comment_btn.setVisible(false);

        for (int i = 0; i < 5; i++) {
            starsComment.get(i).setDisable(false);
        }

        text_cmt.setEditable(true);
    }


    public void setDisableBorrowButton() {
        if (book.getQuantity() == book.getNumBorrowing()) {
            borrow_btn.setDisable(true);
        }
    }

    private void moveMenuCurrent() {
        UserMenuOptions menuCurrent = Model.getInstance().getViewFactory().getUserSelectedMenuItem().getValue();
        switch (menuCurrent) {
            case DASHBOARD -> {
                UserController.getInstance().user_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                // de cho tiet kiem, chi reset phan reading
                DashboardController.getInstance().resetReading();
            }
            case MYBOOK -> {
                UserController.getInstance().user_parent.setCenter(Model.getInstance().getViewFactory().getMyBookView());
                MyBookController.getInstance().refreshData();

            }
            case SEARCH ->
                    UserController.getInstance().user_parent.setCenter(Model.getInstance().getViewFactory().getSearchView());
        }
    }

    private void showFeedback() {
        feedbacks = HandleFeedback.getListComment(book.getId());
        ShowListComment.show(comment_list, feedbacks);
    }

    public void setUpInfo(Book book) {

        title.setText(book.getTitle());
        author_lbl.setText(book.getAuthors());
        categories_lbl.setText(book.getCategory());
        publishDate_lbl.setText(book.getPublishDate().toString());
        quantity_lbl.setText(String.valueOf(book.getQuantity()));
        pages_lbl.setText(String.valueOf(book.getPageCount()));
        description_lbl.setText(book.getDescription());
        totalLoan_lbl.setText("Borrowed: " + String.valueOf(book.getTotalLoan()) + " times");
        id_lbl.setText(book.getId());
        lan_lbl.setText(book.getLanguage());

        setRating(starsAverage, book.getSumRatingStar() / book.getCountRating());

        showFeedback();


        int availableNumber = book.getQuantity() - book.getNumBorrowing();
        if (availableNumber == 0) {
            available_lbl.setText("Out of stock");
            available_lbl.setTextFill(Color.RED);
        } else {
            available_lbl.setText("Available: " + String.valueOf(book.getQuantity() - book.getNumBorrowing()) + " copies");
            available_lbl.setTextFill(Color.BLACK);
        }

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


        imageView.setFitHeight(190);
        imageView.setFitWidth(160);
        imageView.setPreserveRatio(false);
    }


    public void modifyButton() {
        moveToOverviewVBox();
        showMyComment();

        if (QueryBookLoans.isBorrowing(book.getId())) toReturnButton();
        else toBorrowButton();

        if (QueryFavoriteBook.isFavorite(book.getId())) toUnlikeButton();
        else toLikeButton();

        setDisableBorrowButton();
    }

    public void showMyComment() {
        ResultSet resultSet = QueryFeedback.isCommented(book.getId());
        try {
            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                String text = resultSet.getString("comment");
                String user_id = Model.getInstance().getMyUser().getId();
                int rating = resultSet.getInt("rating");
                setRating(starsComment, rating);
                text_cmt.setText(text);

                moveToEditComment();
            } else {
                moveToSendComment();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
