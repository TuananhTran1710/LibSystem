package com.jmc.libsystem.Controllers.Book;

import com.jmc.libsystem.Controllers.Admin.AdminController;
import com.jmc.libsystem.Controllers.Admin.AdminDashboardController;
import com.jmc.libsystem.Controllers.Admin.ManageBookController;
import com.jmc.libsystem.HandleResultSet.HandleFeedback;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Information.Comment;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import com.jmc.libsystem.Views.AdminMenuOptions;
import com.jmc.libsystem.Views.ShowListComment;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class BookEditAdmin extends BaseBookDetailController {
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

    public Button deleteBook_btn;
    public Button saveEdit_btn;
    public Button cancel_btn;
    public Button editBook_btn;

    public VBox overview_vbox;
    public AnchorPane borrowHistory_vbox;
    public VBox comment_vbox;
    public ScrollPane scrollPane_cmt;
    public Button restore_btn;
    public Label borrowing_lbl;
    public AnchorPane quantity_hbox;
    public TextField quantity_tf;
    public Button plus_btn;
    public Button subtract_btn;

    public TableView<Map<String, Object>> borrowHistory_table;
    public TableColumn<Map<String, Object>, String> userID_column;
    public TableColumn<Map<String, Object>, LocalDate> borrowDate_column;
    public TableColumn<Map<String, Object>, Object> returnDate_column;
    public TableColumn<Map<String, Object>, String> status_column;

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

        overview_btn.setOnAction(event -> {
            moveToOverviewVBox();
        });
        commentToggle_btn.setOnAction(event -> {
            moveToCommentVBox();
        });
        borrowHistory_btn.setOnAction(event -> {
            moveToBorrowHistoryVBox();
        });

        editBook_btn.setOnAction(event -> {
            onEditBook();
        });

        saveEdit_btn.setOnAction(event -> {
            int quantity = Integer.parseInt(quantity_tf.getText());
            if (quantity < book.getNumBorrowing()) {
                showAlert("Invalid Quantity", "Please enter a property number which is greater than the number of people borrowing.");
                return;
            }
            book.setQuantity(quantity);
            quantity_lbl.setText(quantity + "");
            toEditButton();

            notice_lbl.setText("Update book successfully");
            notice_lbl.setTextFill(Color.web("#32CD32"));
            createTimeLine();

            QueryBookData.updateQuantity(book.getId(), quantity);

            int availableNumber = book.getQuantity() - book.getNumBorrowing();
            if (availableNumber == 0) {
                available_lbl.setText("Out of stock");
                available_lbl.setTextFill(Color.RED);
            } else {
                available_lbl.setText("Available: " + availableNumber + " copies");
                available_lbl.setTextFill(Color.BLACK);
            }
        });

        cancel_btn.setOnAction(event -> {
            toEditButton();
        });

        restore_btn.setOnAction(event -> {
            // Tạo một box yêu cầu người dùng nhập số lượng
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Quantity");
            dialog.setHeaderText("Enter the quantity to add:");
            dialog.setContentText("Quantity:");

            // Hiển thị dialog và lấy giá trị người dùng nhập
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(quantityStr -> {
                try {
                    int quantityToAdd = Integer.parseInt(quantityStr);

                    // Kiểm tra số lượng nhập vào có hợp lệ không
                    // so luong them toi thieu = so nguoi dang muon chua tra
                    if (quantityToAdd >= book.getNumBorrowing()) {
                        book.setQuantity(quantityToAdd);
                        book.setState("publishing");
                        quantity_lbl.setText(quantityToAdd + "");
                        // Cập nhật thông tin sau khi thêm số lượng
                        toEditButton();
                        state_lbl.setText("Publishing");
//                        state_lbl.setTextFill(Color.web("#32CD32"));

                        state_hbox.getStyleClass().removeAll();
                        state_hbox.getStyleClass().remove("deleted");
                        state_hbox.getStyleClass().add("publishing");

                        available_hbox.setVisible(true);

                        notice_lbl.setText("Restore book successfully");
                        notice_lbl.setTextFill(Color.web("#32CD32"));
                        createTimeLine();

                        QueryBookData.updateStateAndQuantity(book.getId(), "publishing", quantityToAdd);

                        int availableNumber = book.getQuantity() - book.getNumBorrowing();
                        if (availableNumber == 0) {
                            available_lbl.setText("Out of stock");
                            available_lbl.setTextFill(Color.RED);
                        } else {
                            available_lbl.setText("Available: " + availableNumber + " copies");
                            available_lbl.setTextFill(Color.BLACK);
                        }
                    } else {
                        // Hiển thị thông báo nếu số lượng nhập vào không hợp lệ
                        showAlert("Invalid Quantity", "Please enter a property number.");
                    }
                } catch (NumberFormatException e) {
                    // Hiển thị thông báo nếu đầu vào không phải là số
                    showAlert("Invalid Input", "Please enter a valid number.");
                }
            });
        });

        deleteBook_btn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete book");
            alert.setContentText("Currently, there are still books on loan. Are you sure you want to delete this book?");

            // Hiển thị hộp thoại và chờ người dùng chọn
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Nếu người dùng chọn OK, tiến hành xóa sách
                // Xoa sach o day khong phai la xoa khoi database ma la
                // gan state = deleted
                QueryBookData.updateState(book.getId(), "deleted");
                book.setState("deleted");
                toRestoreButton();
                state_hbox.getStyleClass().removeAll();
                state_hbox.getStyleClass().remove("publishing");
                state_hbox.getStyleClass().add("deleted");
                notice_lbl.setText("Delete book successfully");
                notice_lbl.setTextFill(Color.web("#32CD32"));
                createTimeLine();

                state_lbl.setText("Deleted");
//                state_lbl.setTextFill(Color.RED);

                available_hbox.setVisible(false);

            } else {
                // Nếu người dùng chọn Cancel, không làm gì cả
                notice_lbl.setText("Aborted deletion");
                notice_lbl.setTextFill(Color.web("#FF0000"));

                createTimeLine();
            }
        });

        quantity_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int quantity = Integer.parseInt(newValue);
                if (quantity <= 0) {
                    quantity_tf.setText(String.valueOf(book.getNumBorrowing()));
                }
            } catch (NumberFormatException e) {
                // Nếu không phải là số, đặt lại giá trị về giá trị cũ
                quantity_tf.setText(oldValue);
            }
        });

        plus_btn.setOnAction(event -> {
            int num = Integer.parseInt(quantity_tf.getText());
            num += 1;
            quantity_tf.setText("" + num);
        });

        subtract_btn.setOnAction(event -> {
            int num = Integer.parseInt(quantity_tf.getText());
            num -= 1;
            quantity_tf.setText("" + num);
        });

    }

    // Hàm showAlert để hiển thị thông báo lỗi
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void moveToCommentVBox() {
        scrollPane_cmt.setVvalue(0.0);
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
        // show borrow history
        getTableList();
        //------------------
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
        borrowing_lbl.setText(book.getNumBorrowing() + "");
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

    public void toEditButton() {
        editBook_btn.setVisible(true);
        editBook_btn.setDisable(false);

        deleteBook_btn.setVisible(true);
        deleteBook_btn.setDisable(false);

        saveEdit_btn.setVisible(false);
        cancel_btn.setVisible(false);
        restore_btn.setVisible(false);

        saveEdit_btn.setDisable(true);
        cancel_btn.setDisable(true);
        restore_btn.setDisable(true);

        quantity_hbox.setVisible(false);
        quantity_hbox.setDisable(true);
    }

    public void toRestoreButton() {
        editBook_btn.setVisible(false);
        deleteBook_btn.setVisible(false);
        saveEdit_btn.setVisible(false);
        cancel_btn.setVisible(false);
        restore_btn.setVisible(true);

        editBook_btn.setDisable(true);
        deleteBook_btn.setDisable(true);
        saveEdit_btn.setDisable(true);
        cancel_btn.setDisable(true);
        restore_btn.setDisable(false);

        quantity_hbox.setVisible(false);
        quantity_hbox.setDisable(true);
    }

    public void onEditBook() {
        quantity_tf.setText(String.valueOf(book.getQuantity()));
        saveEdit_btn.setVisible(true);
        cancel_btn.setVisible(true);

        editBook_btn.setVisible(false);
        deleteBook_btn.setVisible(false);
        restore_btn.setVisible(false);

        saveEdit_btn.setDisable(false);
        cancel_btn.setDisable(false);
        editBook_btn.setDisable(true);
        deleteBook_btn.setDisable(true);
        restore_btn.setDisable(true);

        quantity_hbox.setVisible(true);
        quantity_hbox.setDisable(false);
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
//            state_lbl.setTextFill(Color.RED);

            state_hbox.getStyleClass().removeAll();
            state_hbox.getStyleClass().remove("publishing");
            state_hbox.getStyleClass().add("deleted");
            toRestoreButton();
        } else {
            available_hbox.setVisible(true);
            state_lbl.setText("Publishing");
//            state_lbl.setTextFill(Color.web("#32CD32"));

            state_hbox.getStyleClass().removeAll();
            state_hbox.getStyleClass().remove("deleted");
            state_hbox.getStyleClass().add("publishing");
            toEditButton();
        }
    }


    // lấy dữ liệu từ database cho phan borrow history
    public ObservableList<Map<String, Object>> getListBorrowHistory() {
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

        Task<Void> loadDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ResultSet resultSet = QueryBookLoans.getListBorrow(book.getId());
                try {
                    while (resultSet.next()) {
                        Map<String, Object> row = new HashMap<>();

                        String user_id = resultSet.getString("user_id");
                        LocalDate borrowDate = resultSet.getDate("borrow_date").toLocalDate();
                        LocalDate returnDate = resultSet.getDate("return_date") != null
                                ? resultSet.getDate("return_date").toLocalDate()
                                : null;

                        LocalDate dueDate = resultSet.getDate("due_date").toLocalDate();
                        String status = "on time"; // Mặc định là đúng hạn

                        if (returnDate == null && dueDate.isBefore(LocalDate.now())) {
                            status = "overdue";
                        } else if (returnDate != null && returnDate.isBefore(dueDate)) {
                            status = "on time";
                        } else if (returnDate != null && returnDate.isAfter(dueDate)) {
                            status = "overdue";
                        }

                        row.put("user_id", user_id);    // key phai khop voi ten o ham getTableList
                        row.put("borrow_date", borrowDate);
                        if (returnDate != null) {
                            row.put("return_date", returnDate);
                        } else {
                            row.put("return_date", "Not returned");
                        }
                        row.put("status", status);

                        // Thêm dữ liệu vào ObservableList trên JavaFX Application Thread
                        Platform.runLater(() -> data.add(row));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        // Khởi chạy Task trong background
        new Thread(loadDataTask).start();

        return data;
    }

    private void getTableList() {
        /* liên kết các cột */
        userID_column.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("user_id")));
        borrowDate_column.setCellValueFactory(data -> new SimpleObjectProperty<>((LocalDate) data.getValue().get("borrow_date")));
        returnDate_column.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().get("return_date")));
        status_column.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("status")));

        ObservableList<Map<String, Object>> data = getListBorrowHistory();
        borrowHistory_table.setItems(data);
    }

}
