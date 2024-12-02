package com.jmc.libsystem.Controllers.User.UI;

import com.jmc.libsystem.Controllers.User.ProfileController;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.service.query.PointQuery;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileControllerUITest extends ApplicationTest {
    private ProfileController controller;

    @Override
    public void start(Stage stage) throws Exception {
        User user = new User("12", "hehe", "heke", "dad", "availbe");
        Model.getInstance().setMyUser(user);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/User/Profile.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testProfileFieldsDisplay() {
        TextField userIdFld = lookup("#user_id_fld").query();
        TextField fullnameFld = lookup("#fullname_fld").query();
        TextField emailFld = lookup("#email_fld").query();
        TextField passwordFld = lookup("#password_fld").query();

        assertNotNull(userIdFld, "ID người dùng không tồn tại");
        assertNotNull(fullnameFld, "Họ tên người dùng không tồn tại");
        assertNotNull(emailFld, "Email người dùng không tồn tại");
        assertNotNull(passwordFld, "Mật khẩu người dùng không tồn tại");

        clickOn(fullnameFld).write("hehe");
        assertEquals("hehe", fullnameFld.getText(), "sai họ tên");
    }

    @Test
    void testEditProfileButton() {
        Button editProfileBtn = lookup("#edit_profile_btn").query();
        assertNotNull(editProfileBtn, "nút chỉnh sửa không tồn tại");

        clickOn(editProfileBtn);

        TextField fullnameFld = lookup("#fullname_fld").query();
        assertTrue(fullnameFld.isEditable(), "thông tin không thể chỉnh sửa");
    }
}
