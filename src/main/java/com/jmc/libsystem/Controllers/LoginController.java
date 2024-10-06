package com.jmc.libsystem.Controllers;

import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public ChoiceBox<AccountType> acc_selector;
    public PasswordField password_fld;
    public Button login_btn;
    public Label acc_lbl;
    public TextField acc_fld;
    public Button sign_up_btn;
    public Button forget_password_btn;
    public CheckBox show_password_cb;    // phan show password de lam sau
    public TextField password_visible_fld;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.USER, AccountType.ADMIN));
        // dong tren set list gia tri cho choiceBox (có tính thứ tự tu tren -> duoi)
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        // dong tren xet gia tri duoc chon mac dinh cho choiceBox

        acc_selector.valueProperty().addListener(
                observable -> Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue()));
        // de set gia tri cho type login moi khi co thay doi
        login_btn.setOnAction(event -> onLogin());
        sign_up_btn.setOnAction(event -> convertToSignUp());
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

    private void onLogin() {
        Stage stage = (Stage) login_btn.getScene().getWindow();

        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.USER) {
            // Evaluate User Login Credentials
            Model.getInstance().evaluateUserCredToLogin(acc_fld.getText(), password_fld.getText());
            if (Model.getInstance().getUserLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().showUserWindow();
                Model.getInstance().getViewFactory().closeStage(stage);
            }
        } else
            Model.getInstance().getViewFactory().showAdminWindow(); // Đang xư lý mẫu cho User nên phần Admin chỉ để
        // đơn giản như này
    }


    private void convertToSignUp() {
        Stage stage = (Stage) sign_up_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().showSignUpWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}
