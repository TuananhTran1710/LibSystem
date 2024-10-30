package com.jmc.libsystem.Controllers;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.HandleResultSet.EvaluateInfo;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import com.jmc.libsystem.Views.AccountType;
import com.jmc.libsystem.Views.UserMenuOptions;
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
    public TextField email_fld;
    public Button sign_up_btn;
    public Button forget_password_btn;
    public CheckBox show_password_cb;
    public TextField password_visible_fld;

    public static AccountType loginAccountType;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.USER, AccountType.ADMIN));
        // dong tren set list gia tri cho choiceBox (có tính thứ tự tu tren -> duoi)
        acc_selector.setValue(AccountType.USER);
        loginAccountType = AccountType.USER;
        // dong tren xet gia tri duoc chon mac dinh cho choiceBox va loginAccountType

        acc_selector.valueProperty().addListener(observable -> loginAccountType = acc_selector.getValue());
        // de set gia tri cho type login moi khi co thay doi
        login_btn.setOnAction(event -> onLogin());
        sign_up_btn.setOnAction(event -> convertToSignUp());
        show_password_cb.setOnAction(event -> showPassword());
        forget_password_btn.setOnAction(event -> convertToForgetPassWord());
    }

    private void convertToForgetPassWord() {
        Stage stage = (Stage) forget_password_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().showForgetPasswordWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
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
        if (show_password_cb.isSelected()) {
            password_fld.setText(password_visible_fld.getText());
        }
        // Evaluate Info Login Credentials
        EvaluateInfo.evaluateInfoToLogin(email_fld.getText(), password_fld.getText(), loginAccountType);

        if (Model.getInstance().getLoginFlag()) {
            if (loginAccountType == AccountType.USER) {
                Model.getInstance().getViewFactory().showUserWindow();
                Model.getInstance().getViewFactory().getUserSelectedMenuItem().setValue(UserMenuOptions.DASHBOARD);

                // notice about num of book which must return
                QueryBookLoans.noticeBookOverdue(Model.getInstance().getMyUser().getId());

                //call functions to reset window
                DashboardController.reset();

            } else Model.getInstance().getViewFactory().showAdminWindow();

            Model.getInstance().getViewFactory().closeStage(stage);
        }
    }


    private void convertToSignUp() {
        Stage stage = (Stage) sign_up_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().showSignUpWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}
