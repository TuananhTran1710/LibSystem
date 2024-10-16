package com.jmc.libsystem.Controllers;

import com.jmc.libsystem.Models.EvaluateInfo;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ForgetPasswordController implements Initializable {

    public TextField id_fld;
    public TextField email_fld;
    public Button confirm_btn;
    public Button login_btn;
    public ChoiceBox acc_selector;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.USER, AccountType.ADMIN));
        // dong tren set list gia tri cho choiceBox (có tính thứ tự tu tren -> duoi)
        acc_selector.setValue(AccountType.USER); // gan gia tri mac dinh

        confirm_btn.setOnAction(event -> getPassword());
        login_btn.setOnAction(event -> convertToLogin());
    }

    private void getPassword() {

        // Evaluate In4 To Get Password
        if (!email_fld.getText().trim().isEmpty() && !id_fld.getText().trim().isEmpty()) {
            EvaluateInfo.evaluateInfoToGetPassword(email_fld.getText(), id_fld.getText(), (AccountType) acc_selector.getValue());
        } else {
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
