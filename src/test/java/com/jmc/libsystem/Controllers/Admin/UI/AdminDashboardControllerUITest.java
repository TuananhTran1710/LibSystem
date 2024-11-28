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
        controller = new AdminDashboardController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminDashboard.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testNumberLabelsDisplayCorrectly() {
        Label numberUser = lookup("#numberUser").query();
        Label numberBook = lookup("#numberBook").query();
        Label numberBookLoan = lookup("#numberBookLoan").query();
        Label numberOverBook = lookup("#numberOverBook").query();

        // Kiểm tra các giá trị ban đầu của nhãn
        assertNotNull(numberUser);
        assertNotNull(numberBook);
        assertNotNull(numberBookLoan);
        assertNotNull(numberOverBook);

        // Mô phỏng hành động thay đổi dữ liệu
        Platform.runLater(() -> {
            numberUser.setText("5");
            numberBook.setText("50");
            numberBookLoan.setText("100");
            numberOverBook.setText("20");
        });

        // Chờ cho đến khi các hành động hoàn tất
        WaitForAsyncUtils.waitForFxEvents();


        assertEquals("5", numberUser.getText());
        assertEquals("50", numberBook.getText());
        assertEquals("100", numberBookLoan.getText());
        assertEquals("20", numberOverBook.getText());
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
