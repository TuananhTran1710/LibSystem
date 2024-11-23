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
import com.jmc.libsystem.Views.ShowListComment;
import com.jmc.libsystem.Views.UserMenuOptions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class BookDetailController extends BaseBookDetailController {

    private static BookDetailController instance;

    public Button borrow_btn;
    public Button like_btn;
    public Button return_btn;
    public Button unlike_btn;
    public Label totalLoan_lbl;
    public Label available_lbl;
    public Label quantity_lbl;

    //phan cmt
    public VBox comment_list;
    public FontAwesomeIconView star_cmt1;
    public FontAwesomeIconView star_cmt2;
    public FontAwesomeIconView star_cmt3;
    public FontAwesomeIconView star_cmt4;
    public FontAwesomeIconView star_cmt5;
    public TextArea text_cmt;
    public Button edit_btn;      //edit my comment
    public Button comment_btn;   // send comment
    public Button deleted_btn;  // delete my comment
    public Button save_btn;   // save my comment

    public ToggleButton overview_btn;
    public ToggleButton commentToggle_btn;

    public VBox overview_vbox;
    public VBox comment_vbox;

    public Label time_lbl;
    public HBox state_hbox;
    public Label state_lbl;
    public AnchorPane available_hbox;
    public HBox time_hBox;
    public ScrollPane scrollPane_cmt;


    private List<Comment> feedbacks;

    private List<FontAwesomeIconView> starsComment;
    private int currentRating = 0; // Rating ban đầu


    public static BookDetailController getInstance() {
        return instance;
    }

    public BookDetailController() {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        borrow_btn.setFocusTraversable(false);
        like_btn.setFocusTraversable(false);
        return_btn.setFocusTraversable(false);
        unlike_btn.setFocusTraversable(false);


        starsComment = List.of(star_cmt1, star_cmt2, star_cmt3, star_cmt4, star_cmt5);
        for (int i = 0; i < starsComment.size(); i++) {
            int index = i;
            starsComment.get(i).setOnMouseEntered(event -> highlightStars(index + 1));
            starsComment.get(i).setOnMouseExited(event -> resetStars());
            starsComment.get(i).setOnMouseClicked(event -> setRating(starsComment, index + 1));
        }

        return_btn.setOnAction(event -> {
            QueryBookLoans.updateReturnBook(book.getId());
            book.setNumBorrowing(book.getNumBorrowing() - 1);
            time_hBox.setVisible(false);
            toBorrowButton();
            available_lbl.setText("Available: " + String.valueOf(book.getQuantity() - book.getNumBorrowing()) + " copies");
            available_lbl.setStyle("-fx-text-fill: #FFFFFF;");
            if (book.getState().equals("deleted")) {
                borrow_btn.setDisable(true);
                available_hbox.setVisible(false);

                state_lbl.setText("Deleted");
            }

            notice_lbl.setText("Return successfully");
            notice_lbl.setTextFill(Color.web("#32CD32"));
            createTimeLine();
        });

        borrow_btn.setOnAction(event -> {
            time_hBox.setVisible(true);
            toReturnButton();
            QueryBookLoans.insertNewRecord(book.getId());
            book.setNumBorrowing(book.getNumBorrowing() + 1);
            book.setTotalLoan(book.getTotalLoan() + 1);
            QueryBookLoans.setRemainingTime(time_lbl, book.getId(), Model.getInstance().getMyUser().getId());

            notice_lbl.setText("Borrow successfully");
            notice_lbl.setTextFill(Color.web("#32CD32"));
            createTimeLine();

            int availableNumber = book.getQuantity() - book.getNumBorrowing();
            if (availableNumber == 0) {
                available_lbl.setText("Out of stock");
                available_lbl.setTextFill(Color.RED);
            } else {
                available_lbl.setText("Available: " + availableNumber + " copies");
                available_lbl.setStyle("-fx-text-fill: #FFFFFF;");
            }

            totalLoan_lbl.setText(String.valueOf(book.getTotalLoan()) + " times");
        });
        like_btn.setOnAction(event -> {
            toUnlikeButton();
            QueryFavoriteBook.insertNewRecord(book.getId());

            notice_lbl.setText("Added to your favorite list");
            notice_lbl.setTextFill(Color.web("#32CD32"));
            createTimeLine();
        });
        unlike_btn.setOnAction(event -> {
            toLikeButton();
            QueryFavoriteBook.deleteRecord(book.getId());

            notice_lbl.setText("Removed from your favorite list");
            notice_lbl.setTextFill(Color.web("#32CD32"));
            createTimeLine();
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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please rate and write your comment");
                alert.showAndWait();
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

    @Override
    public void setRating(List<FontAwesomeIconView> stars, int rating) {
        if (stars == starsComment) currentRating = rating;
        super.setRating(stars, rating);
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
        scrollPane_cmt.setVvalue(0.0);

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
        text_cmt.positionCaret(text_cmt.getText().length());
    }


    public void setDisableBorrowButton() {
        if (book.getQuantity() == book.getNumBorrowing()) {
            borrow_btn.setDisable(true);
        }
    }

    @Override
    public void moveMenuCurrent() {
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
        }
    }

    private void showFeedback() {
        feedbacks = HandleFeedback.getListComment(book.getId());
        ShowListComment.show(comment_list, feedbacks);
    }

    @Override
    public void setUpInfo(Book book) {
        super.setUpInfo(book);

        quantity_lbl.setText(String.valueOf(book.getQuantity()));
        totalLoan_lbl.setText(String.valueOf(book.getTotalLoan()) + " times");

        showFeedback();

        int availableNumber = book.getQuantity() - book.getNumBorrowing();
        if (availableNumber == 0) {
            available_lbl.setText("Out of stock");
            available_lbl.setTextFill(Color.RED);
        } else {
            available_lbl.setText("Available: " + String.valueOf(book.getQuantity() - book.getNumBorrowing()) + " copies");
            available_lbl.setStyle("-fx-text-fill: #FFFFFF;");
        }
    }

    @Override
    public void modifyButton() {
        moveToOverviewVBox();
        showMyComment();
        if (QueryBookLoans.isBorrowing(book.getId())) {
            time_hBox.setVisible(true);
            QueryBookLoans.setRemainingTime(time_lbl, book.getId(), Model.getInstance().getMyUser().getId());
            toReturnButton();
        } else {
            time_hBox.setVisible(false);
            toBorrowButton(); // Chức năng khi sách chưa mượn
        }
        if (QueryFavoriteBook.isFavorite(book.getId())) {
            toUnlikeButton();
        } else {
            toLikeButton();
        }

        setDisableBorrowButton();
        setState();
    }

    public void setState() {
        if (book.getState().equals("deleted")) {
            available_hbox.setVisible(false);
            borrow_btn.setDisable(true);
            state_lbl.setText("Deleted");
            state_hbox.getStyleClass().removeAll();
            state_hbox.getStyleClass().remove("published");
            state_hbox.getStyleClass().add("deleted");
            if (QueryBookLoans.isBorrowing(book.getId())) time_hBox.setVisible(true);
            else time_hBox.setVisible(false);
        } else {
            available_hbox.setVisible(true);
            state_lbl.setText("Publishing");
            state_hbox.getStyleClass().removeAll();
            state_hbox.getStyleClass().remove("deleted");
            state_hbox.getStyleClass().add("published");
        }
    }

    public void showMyComment() {
        ResultSet resultSet = QueryFeedback.isCommented(book.getId());
        try {
            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                String text = resultSet.getString("comment");
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
