package com.jmc.libsystem.Controllers.Book;

import com.jmc.libsystem.Controllers.Admin.AdminController;
import com.jmc.libsystem.Controllers.Admin.AdminDashboardController;
import com.jmc.libsystem.Controllers.Admin.ManageBookController;
import com.jmc.libsystem.HandleResultSet.HandleFeedback;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Information.Comment;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.AdminMenuOptions;
import com.jmc.libsystem.Views.ShowListComment;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BookEditAdmin extends BaseBookDetailController implements Initializable {
    public AnchorPane available_hbox;
    public Label available_lbl;

    public HBox state_hbox;
    public Label state_lbl;

    public VBox comment_list;

    public Label totalLoan_lbl;
    public Label quantity_lbl;

    public ToggleButton borrowHistory_btn;
    public ToggleButton commentToggle_btn;
    public ToggleButton overview_btn;

    public Button changeQuantity_btn;
    public Button deleteBook_btn;
    public Button saveEdit_btn;
    public Button cancel_btn;
    public Button editBook_btn;

    public VBox overview_vbox;
    public AnchorPane borrowHistory_vbox;
    public VBox comment_vbox;


    private List<Comment> feedbacks;
    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    private static BookEditAdmin instance;

    public static BookEditAdmin getInstance() {
        return instance;
    }

    public BookEditAdmin() {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

    }

    private void moveToCommentVBox() {
        commentToggle_btn.setSelected(true);
        overview_btn.setSelected(false);
        borrowHistory_btn.setSelected(false);

        overview_vbox.setDisable(true);
        overview_vbox.setVisible(false);

        comment_vbox.setDisable(false);
        comment_vbox.setVisible(true);

        borrowHistory_vbox.setDisable(true);
        borrowHistory_vbox.setVisible(false);
    }

    private void moveToOverviewVBox() {
        commentToggle_btn.setSelected(false);
        overview_btn.setSelected(true);
        borrowHistory_btn.setSelected(false);

        overview_vbox.setDisable(false);
        overview_vbox.setVisible(true);

        comment_vbox.setDisable(true);
        comment_vbox.setVisible(false);

        borrowHistory_vbox.setDisable(true);
        borrowHistory_vbox.setVisible(false);
    }

    private void moveToBorrowHistoryVBox() {
        commentToggle_btn.setSelected(false);
        overview_btn.setSelected(false);
        borrowHistory_btn.setSelected(true);

        overview_vbox.setDisable(true);
        overview_vbox.setVisible(false);

        comment_vbox.setDisable(true);
        comment_vbox.setVisible(false);

        borrowHistory_vbox.setDisable(false);
        borrowHistory_vbox.setVisible(true);
    }

    @Override
    public void moveMenuCurrent() {
        AdminMenuOptions menuCurrent = Model.getInstance().getViewFactory().getAdminSelectedMenuItem().getValue();
        switch (menuCurrent) {
            case DASHBOARD -> {
                AdminController.getInstance().admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminDashboardView());
                AdminDashboardController.getInstance().refreshData();
            }
            case BOOK -> {
                AdminController.getInstance().admin_parent.setCenter(Model.getInstance().getViewFactory().getManageBookView());
                ManageBookController.getInstance().resetBookLibrary();
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
            available_lbl.setTextFill(Color.BLACK);
        }
    }

    @Override
    public void modifyButton() {
        moveToOverviewVBox();
        setState();
    }

    public void setState() {
        if (book.getState().equals("deleted")) {
            available_hbox.setVisible(false);
            state_lbl.setText("Deleted");
            state_lbl.setTextFill(Color.RED);
        } else {
            available_hbox.setVisible(true);
            state_lbl.setText("Publishing");
            state_lbl.setTextFill(Color.web("#32CD32"));
        }
    }


}
