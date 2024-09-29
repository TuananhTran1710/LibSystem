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
    public Label error_lbl;
    public Label acc_lbl;
    public TextField acc_fld;
    public Button sign_up_btn;
    public Button forget_password_btn;

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

    }

    private void onLogin() {
        Stage stage = (Stage) login_btn.getScene().getWindow();

        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.USER) {
            // Evaluate Client Login Credentials
            Model.getInstance().evaluateClientCred(acc_fld.getText(), password_fld.getText());
            if (Model.getInstance().getUserLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().showUserWindow();

                Model.getInstance().getViewFactory().closeStage(stage);
            } else {
                // Truong hop khong dang nhap thanh cong
                error_lbl.setText("No Such Login Credentials");
            }
        } else
            Model.getInstance().getViewFactory().showAdminWindow(); // Đang xư lý mẫu cho User nên phần Admin chỉ để
        // đơn giản như này
    }
}
