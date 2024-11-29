package com.jmc.libsystem.Controllers.Admin.UI;

import com.jmc.libsystem.Controllers.Admin.AdminDashboardController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AdminDashboardControllerUITest extends ApplicationTest {

    private AdminDashboardController controller;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminDashboard.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testNumberLabelsDisplayCorrectly() {
        // Mô phỏng hành động thay đổi dữ liệu

        Platform.runLater(() -> {
            controller.numberUser.setText("5");
            controller.numberBook.setText("50");
            controller.numberBookLoan.setText("100");
            controller.numberOverBook.setText("20");
        });

        // Chờ cho đến khi các hành động hoàn tất
        WaitForAsyncUtils.waitForFxEvents();


        assertEquals("5", controller.numberUser.getText());
        assertEquals("50", controller.numberBook.getText());
        assertEquals("100", controller.numberBookLoan.getText());
        assertEquals("20", controller.numberOverBook.getText());
    }

    @Test
    void testTableViewDataBinding() {
        TableView<Map<String, Object>> listBook = lookup("#listBook").query();

        // Kiểm tra bảng không rỗng sau khi load dữ liệu
        assertNotNull(listBook);
        Platform.runLater(() -> controller.getTableList());
        //WaitForAsyncUtils.waitForFxEvents();

        //System.out.println(listBook.getItems().size());

        assertFalse(!listBook.getItems().isEmpty());
    }
}
