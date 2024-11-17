package com.jmc.libsystem.Controllers.Account;

import com.jmc.libsystem.Information.BookLoan;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryAccountData;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BaseAccountDetailController implements Initializable {
    private static BaseAccountDetailController instance;

    public Button back_btn;

    public TextField user_id_fld;
    public TextField fullname_fld;
    public TextField email_fld;
    public TextField password_fld;
    public Button edit_profile_btn;
    public Button save_profile_btn;

    public Label total_borrow_lbl;
    public Label overdue_book_lbl;
    public Label favorite_genre_lbl;
    public Label borrowing_lbl;
    public TextField new_password_fld;
    public TextField confirm_new_password_fld;
    public Button change_password_btn;

    public TableView bookloan_tbl;
    public TableColumn bookname_clm;
    public TableColumn borroweddate_clm;
    public TableColumn returndate_clm;
    public TableColumn status_clm;

    protected User current_user;

    public User getCurrent_user() {
        return this.current_user;
    }

    public void setCurrent_user(User current_user) {
        this.current_user = current_user;
    }

    public BaseAccountDetailController() {
        instance = this;
    }

    public static BaseAccountDetailController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitBorrowHistoryTable();
        back_btn.setOnAction(event -> moveMenuCurrent());
    }

    protected void moveMenuCurrent() {
    }

    //lấy sự kiện
    protected void addListensButton(User current_user) {
        edit_profile_btn.setOnAction(event -> onEditButtonClicked());
        save_profile_btn.setOnAction(event -> onSaveButtonClicked(current_user));
        change_password_btn.setOnAction(event -> onChangePasswordButtonClicked(current_user));
    }

    //tải thông tin người dùng
    protected void loadUserProfile(User current_user) {

    }

    //cho chỉnh sửa khi bấm edit
    protected void setFieldsEditable(boolean editable) {

    }

    //show cửa sổ lỗi
    public void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    //bấm nút edit profile
    public void onEditButtonClicked() {
        setFieldsEditable(true);

        edit_profile_btn.setVisible(false);
        save_profile_btn.setVisible(true);

        System.out.println("edit");
    }

    //bấm nút save profile
    public void onSaveButtonClicked(User current_user) {

    }

    //bấm nút đổi mật khẩu
    public void onChangePasswordButtonClicked(User current_user) {
        String newPassword = new_password_fld.getText();
        String confirmNewPassword = confirm_new_password_fld.getText();

        if (newPassword.equals(confirmNewPassword) && !newPassword.equals(current_user.getPassword())) {
            current_user.setPassword(newPassword);
            QueryAccountData.updateUserPassword(current_user);

            password_fld.setText(newPassword);
        } else if (!newPassword.equals(confirmNewPassword)) {
            showAlert("Password Mismatch", "Oops...", "The new password and confirmation do not match. Please try again.");
        } else {
            showAlert("Password Mismatch", "Oops...", "The new password matches current password. Please try again.");
        }
        new_password_fld.clear();
        confirm_new_password_fld.clear();
    }

    //tải các thông số về sách
    protected void loadBorrowingStatistics(User current_user) {
        String userId = current_user.getId();

        // Lấy thông tin từ cơ sở dữ liệu và cập nhật giao diện
        int totalBorrowed = QueryBookLoans.getTotalBookBorrowed(userId);
        int borrowing = QueryBookLoans.getCurrentBorrowing(userId);
        int overdueBooks = QueryBookLoans.getOverdueBooks(userId);
        String favoriteGenre = QueryBookLoans.getFavoriteGenre(userId);

        total_borrow_lbl.setText(String.valueOf(totalBorrowed));
        borrowing_lbl.setText(String.valueOf(borrowing));
        overdue_book_lbl.setText(String.valueOf(overdueBooks));
        favorite_genre_lbl.setText(favoriteGenre);
    }

    //gắn và căn chỉnh các trường trong bảng
    private void InitBorrowHistoryTable() {
        //cho các cột lấy giá trị tương ứng của class bookloan
        bookname_clm.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        borroweddate_clm.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        returndate_clm.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        status_clm.setCellValueFactory(new PropertyValueFactory<>("status"));

        //căn thẳng nội dung với cột
        bookname_clm.setStyle("-fx-alignment: CENTER-LEFT;");
        borroweddate_clm.setStyle("-fx-alignment: CENTER;");
        returndate_clm.setStyle("-fx-alignment: CENTER;");
        status_clm.setStyle("-fx-alignment: CENTER;");
    }

    protected void loadUserBookLoans(User current_user) {
        List<BookLoan> bookLoans = QueryBookLoans.getBookLoansByUserId(current_user.getId());
        bookloan_tbl.getItems().setAll(bookLoans);
    }

}
