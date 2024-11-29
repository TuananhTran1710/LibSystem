package com.jmc.libsystem.Controllers.Admin.UI;

import com.jmc.libsystem.Controllers.Admin.AdminDashboardController;
import com.jmc.libsystem.Controllers.Admin.ManageUserController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ManageUserControllerUITest extends ApplicationTest {

    private ManageUserController controller;

    @Override
    public void start(javafx.stage.Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Admin/ManageUser.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        //System.out.println(root);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testSearchButton() throws Exception {
        // Giả lập từ khóa tìm kiếm
        Platform.runLater(() -> {
            clickOn(controller.search_tf).write("Test User");
            controller.criteriaBox.setValue("Name");
        });

        // Giả lập nhấn nút tìm kiếm
        Platform.runLater(() -> clickOn(controller.search_btn));

        // Đợi xử lý và kiểm tra kết quả
        org.testfx.util.WaitForAsyncUtils.waitForFxEvents();

        assertNotNull(controller.data, "Data should not be null after search");
        assertFalse(controller.data.isEmpty(), "Search result should not be empty");
    }


    @Test
    void testAddUserButton() throws Exception {
        // Điền thông tin vào các trường
        clickOn(controller.userIdField).write("3");
        clickOn(controller.fullNameField).write("New User");
        clickOn(controller.emailField).write("newuser@example.com");
        clickOn(controller.passwordField).write("password789");

        // Nhấn nút "Add User"
        clickOn(controller.addUserButton);

        // Đợi sự kiện JavaFX hoàn thành
        org.testfx.util.WaitForAsyncUtils.waitForFxEvents();

        // Kiểm tra kết quả
        assertTrue(controller.data.stream().anyMatch(row -> row.get("id").equals("3")
                && row.get("name").equals("New User")
                && row.get("email").equals("newuser@example.com")));
    }

}
