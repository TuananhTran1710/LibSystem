package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.Information.User;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUserProfile();
    }

    private void loadUserProfile() {

    }
}
