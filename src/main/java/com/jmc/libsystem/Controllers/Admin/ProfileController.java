package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.Information.Admin;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryAccountData;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    private static ProfileController instance;

    public Label admin_id_lbl;
    public TextField fullname_fld;
    public TextField email_fld;
    public TextField password_fld;
    public Button edit_btn;
    public TextField new_password_fld;
    public TextField confirm_new_password_fld;
    public Button change_password_btn;
    public Button save_btn;
    public FontAwesomeIconView fullname_fontaws;
    public Label fullname_lbl;

    public ProfileController() {
        instance = this;
    }

    public static ProfileController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        save_btn.setFocusTraversable(false);
        change_password_btn.setFocusTraversable(false);
        edit_btn.setFocusTraversable(false);

        refreshProfile();
        addListenButton();
    }

    private void refreshProfile() {
        loadAdminProfile();
    }

    public void showProfile() {
        refreshProfile();
    }

    private void addListenButton() {
        edit_btn.setOnAction(event -> onEditButtonClicked());
        save_btn.setOnAction(event -> onSaveButtonClicked());
        change_password_btn.setOnAction(event -> onChangePasswordButtonClicked());
    }

    private void loadAdminProfile() {
        Admin current_admin = Model.getInstance().getMyAdmin();

        admin_id_lbl.setText(current_admin.getId());
        fullname_fld.setText(current_admin.getFullName());
        email_fld.setText(current_admin.getEmail());
        password_fld.setText(current_admin.getPassword());

        fullname_fld.setEditable(false);
        email_fld.setEditable(false);
        password_fld.setEditable(false);

        edit_btn.setVisible(true);
        save_btn.setVisible(false);
    }

    private void setFieldsEditable(boolean editable) {
        fullname_fld.setEditable(editable);
        email_fld.setEditable(false);
        password_fld.setEditable(false);
    }

    public void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void onEditButtonClicked() {
        fullname_lbl.getStyleClass().remove("bold_text");
        fullname_lbl.getStyleClass().add("edit_text");

        fullname_fontaws.getStyleClass().remove("fontaws");
        fullname_fontaws.getStyleClass().add("edit_fontaws");

        setFieldsEditable(true);

        edit_btn.setVisible(false);
        save_btn.setVisible(true);

        System.out.println("edit");
    }

    public void onSaveButtonClicked() {
        if (fullname_fld.getText().trim().isEmpty()) {
            showAlert("Invalid Full Name", "Oops...", "The full name cannot be empty. Please try again.");
            return;
        }
        fullname_lbl.getStyleClass().remove("edit_text");
        fullname_lbl.getStyleClass().add("bold_text");

        fullname_fontaws.getStyleClass().remove("edit_fontaws");
        fullname_fontaws.getStyleClass().add("fontaws");

        String updatedFullName = fullname_fld.getText();
        Admin current_admin = Model.getInstance().getMyAdmin();

        //nhập nhật của user
        current_admin.setFullName(updatedFullName);

        //cập nhật trong database
        //QueryAccountData.updateUserInfo(current_user);
        QueryAccountData.updateAdminInfo(current_admin);

        //đổi trạng thái field
        setFieldsEditable(false);

        //nút
        edit_btn.setVisible(true);
        save_btn.setVisible(false);

        System.out.println("save");
    }

    public void onChangePasswordButtonClicked() {
        String new_password = new_password_fld.getText();
        System.out.println(new_password);
        String confirm_new_password = confirm_new_password_fld.getText();
        Admin current_admin = Model.getInstance().getMyAdmin();

        if (new_password.trim().isEmpty()) {
            showAlert("Invalid Password", "Oops...", "The new password cannot be empty. Please try again.");
        } else if (new_password.equals(current_admin.getPassword())) {
            showAlert("Password Mismatch", "Oops...", "The new password matches current password. Please try again.");
        } else if (!new_password.equals(confirm_new_password)) {
            showAlert("Password Mismatch", "Oops...", "The new password and confirmation do not match. Please try again.");
        } else {
            current_admin.setPassword(new_password);
            QueryAccountData.updateAdminPassword(current_admin);

            password_fld.setText(new_password);
            showAlert("Success", "Password Changed", "Your password has been successfully updated.");
        }

        new_password_fld.clear();
        confirm_new_password_fld.clear();
    }
}
