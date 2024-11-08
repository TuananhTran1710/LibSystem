package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.HandleResultSet.EvaluateInfo;
import com.jmc.libsystem.Models.Model;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ManageUserController {

    public TextField userIdField;
    public TextField fullNameField;
    public TextField emailField;
    public PasswordField passwordField;
    public Button addUserButton;

    public void initialize() {
        addUserButton.setOnAction(event -> {
            String userId = userIdField.getText();
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (!email.trim().isEmpty() && !password.trim().isEmpty()
                    && !userId.trim().isEmpty() && !fullName.trim().isEmpty()) {
                EvaluateInfo.evaluateUserCredToSignup(email, password, userId, fullName);
            } else {
                System.out.println("Please fill in all information!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in all information!");
                alert.show();
            }
        });
    }
}
