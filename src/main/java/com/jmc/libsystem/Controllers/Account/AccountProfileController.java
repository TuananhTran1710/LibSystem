package com.jmc.libsystem.Controllers.Account;

import com.jmc.libsystem.Controllers.Admin.AdminController;
import com.jmc.libsystem.Controllers.Admin.ManageUserController;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryAccountData;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccountProfileController extends BaseAccountDetailController {

    public Button back_btn;
    public Button deleteBT;
    public TextField status_tfl;

    private static AccountProfileController instance;

    public AccountProfileController() {
        instance = this;
    }

    public static AccountProfileController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        back_btn.setOnAction(event -> moveMenuCurrent());
        deleteBT.setOnAction(event -> deleteAndMoveMenuCurrent());
        //statusChoice.setValue("Active");
    }

    private boolean confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Confirm to delete this account");
        alert.setContentText("Are you sure?");

        ButtonType buttonTypeContinue = new ButtonType("Continue");
        ButtonType buttonTypeCancel = new ButtonType("Close");

        // Thêm các ButtonType vào Alert
        alert.getButtonTypes().setAll(buttonTypeContinue, buttonTypeCancel);

        // Hiển thị Alert và chờ người dùng chọn
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeContinue) {
            return true;
        } else {
            return false;
        }
    }

    private void deleteAndMoveMenuCurrent() {
        if (!confirmDelete()) return;
        QueryAccountData.updateState(getCurrent_user().getId(), "Deleted");
        moveMenuCurrent();
        current_user.setState("Deleted");
    }

    @Override
    public void moveMenuCurrent() {
        AdminController.getInstance().admin_parent.setCenter(Model.getInstance().getViewFactory().getManageUserView());
        ManageUserController.getInstance().refreshData();
    }

    private void refreshProfile(User current_user) {
        //System.out.println("refresh " + this.getCurrent_user());
        loadUserProfile(current_user);
        loadBorrowingStatistics(current_user);
        loadUserBookLoans(current_user);
        addListensButton(current_user);
    }

    public void showProfile(User current_user) {
        refreshProfile(current_user);
    }

    public void loadUserProfile(User current_user) {
        //gán thông tin cho các textfield
        user_id_fld.setText(current_user.getId());
        fullname_fld.setText(current_user.getFullName());
        email_fld.setText(current_user.getEmail());
        password_fld.setText(current_user.getPassword());
        status_tfl.setText(current_user.getState());

        //đặt các field không được chỉnh sửa
        user_id_fld.setEditable(false);
        fullname_fld.setEditable(false);
        email_fld.setEditable(false);
        password_fld.setEditable(false);
        status_tfl.setEditable(false);

        //bật nút edit, tắt nút save
        // Hiện nút Edit và ẩn nút Save
        edit_profile_btn.setVisible(true);
        save_profile_btn.setVisible(false);
    }

    @Override
    protected void setFieldsEditable(boolean editable) {
        status_tfl.setEditable(false);
        user_id_fld.setEditable(false);
        fullname_fld.setEditable(editable);
        email_fld.setEditable(editable);
        password_fld.setEditable(false);
    }

    @Override
    public void onSaveButtonClicked(User current_user) {
        String updatedFullName = fullname_fld.getText();
        String updatedEmail = email_fld.getText();

        if (updatedEmail.isEmpty() || updatedFullName.isEmpty()) {
            showAlert("Error", "Information not filled in", "You need to fill in completely");
            return;
        }

        //kiểm tra email tồn tại chưa
        if (!updatedEmail.equals(current_user.getEmail()) && QueryAccountData.isUserEmailExists(updatedEmail)) {
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
}
