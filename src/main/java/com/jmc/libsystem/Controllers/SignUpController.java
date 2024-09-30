package com.jmc.libsystem.Controllers;

import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.AccountType;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public TextField acc_fld;
    public PasswordField password_fld;
    public Button sign_up_btn;
    public Button login_btn;
    public CheckBox show_password_cb;
    public TextField password_visible_fld;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sign_up_btn.setOnAction(event -> onSignUp());
        login_btn.setOnAction(event -> convertToLogin());
        show_password_cb.setOnAction(event -> showPassword());
    }

    private void showPassword() {
        if (show_password_cb.isSelected()) {
            password_visible_fld.setVisible(true);
            password_visible_fld.setText(password_fld.getText());
            password_fld.setVisible(false);
        } else {
            password_visible_fld.setVisible(false);
            password_fld.setText(password_visible_fld.getText());
            password_fld.setVisible(true);
        }
    }

    private void onSignUp() {
        //xu ly xac nhan thong tin dang ky
        Stage stage = (Stage) sign_up_btn.getScene().getWindow();

        // Evaluate User Sign Up Credentials
        if(!acc_fld.getText().trim().isEmpty() && !password_fld.getText().trim().isEmpty()) {
            Model.getInstance().evaluateUserCredToSignup(acc_fld.getText(), password_fld.getText());
            if (Model.getInstance().getUserLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().showUserWindow();
                Model.getInstance().getViewFactory().closeStage(stage);
            }
        }
        else{
            System.out.println("Please fill in all information!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in all information!");
            alert.show();
        }
    }

    private void convertToLogin() {
        Stage stage = (Stage) login_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().showLoginWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}
