package com.jmc.libsystem.Controllers.Admin;

import javafx.fxml.Initializable;
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

    public ProfileController() {
        instance = this;
    }

    public static ProfileController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
