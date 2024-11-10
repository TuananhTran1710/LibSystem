package com.jmc.libsystem.Controllers.User;

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

public class ProfileController implements Initializable {
    private static ProfileController instance;

    public TextField user_id_fld;
    public TextField fullname_fld;
    public TextField email_fld;
    public TextField password_fld;
    public Button edit_profile_btn;
    public Label total_borrow_lbl;
    public Label overdue_book_lbl;
    public Label favorite_genre_lbl;
    public Label borrowing_lbl;
    public TextField new_password_fld;
    public TextField confirm_new_password_fld;
    public Button change_password_btn;
    public Button save_profile_btn;
    public TableView bookloan_tbl;
    public TableColumn bookname_clm;
    public TableColumn borroweddate_clm;
    public TableColumn returndate_clm;
    public TableColumn status_clm;

    public ProfileController() {
        instance = this;
    }

    public static ProfileController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshProfile();
        InitBorrowHistoryTable();
        addListensButton();
    }

    private void refreshProfile() {
        loadUserProfile();
        loadBorrowingStatistics();
        loadUserBookLoans();
    }

    //lấy sự kiện
    private void addListensButton() {
        edit_profile_btn.setOnAction(event -> onEditButtonClicked());
        save_profile_btn.setOnAction(event -> onSaveButtonClicked());
        change_password_btn.setOnAction(event -> onChangePasswordButtonClicked());
    }

    //cái này là để làm mới dữ liệu, sử dụng ở user controller khi mà case của sự kiện bằng profile
    public void showProfile() {
        refreshProfile();
    }

    //tải thông tin người dùng
    private void loadUserProfile() {
        User current_user = Model.getInstance().getMyUser();

        //gán thông tin cho các textfield
        user_id_fld.setText(current_user.getId());
        fullname_fld.setText(current_user.getFullName());
        email_fld.setText(current_user.getEmail());
        password_fld.setText(current_user.getPassword());

        //đặt các field không được chỉnh sửa
        user_id_fld.setEditable(false);
        fullname_fld.setEditable(false);
        email_fld.setEditable(false);
        password_fld.setEditable(false);

        //bật nút edit, tắt nút save
        // Hiện nút Edit và ẩn nút Save
        edit_profile_btn.setVisible(true);
        save_profile_btn.setVisible(false);
    }

    //cho chỉnh sửa khi bấm edit
    private void setFieldsEditable(boolean editable) {
        user_id_fld.setEditable(false);
        fullname_fld.setEditable(editable);
        email_fld.setEditable(editable);
        password_fld.setEditable(false);
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
    public void onSaveButtonClicked() {
        //lấy thaydodoiri từ field
        String updatedFullName = fullname_fld.getText();
        String updatedEmail = email_fld.getText();
        User current_user = Model.getInstance().getMyUser();

        //kiểm tra email tồn tại chưa
        if (!updatedEmail.equals(Model.getInstance().getMyUser().getEmail()) && QueryAccountData.isUserEmailExists(updatedEmail)) {
            //email tồn tại thì không thay đổi
            email_fld.setText(current_user.getEmail());
            QueryAccountData.updateUserInfo(current_user);
            showAlert("Error", "Oops...", "Email already exists. Please use a different email.");
            return;
        }

        //nhập nhật của user
        current_user.setFullName(updatedFullName);
        current_user.setEmail(updatedEmail);

        //cập nhật trong database
        QueryAccountData.updateUserInfo(current_user);

        //đổi trạng thái field
        setFieldsEditable(false);

        //nút
        edit_profile_btn.setVisible(true);
        save_profile_btn.setVisible(false);

        System.out.println("save");
    }

    //bấm nút đổi mật khẩu
    public void onChangePasswordButtonClicked() {
        String newPassword = new_password_fld.getText();
        String confirmNewPassword = confirm_new_password_fld.getText();
        User current_user = Model.getInstance().getMyUser();

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
    private void loadBorrowingStatistics() {
        User current_user = Model.getInstance().getMyUser();
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

    private void loadUserBookLoans() {
        List<BookLoan> bookLoans = QueryBookLoans.getBookLoansByUserId(Model.getInstance().getMyUser().getId());
        bookloan_tbl.getItems().setAll(bookLoans);
    }

}
