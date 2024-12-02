package com.jmc.libsystem.Controllers.User.UI;

import com.jmc.libsystem.Controllers.User.DashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

class DashboardControllerUITest extends ApplicationTest {

    private DashboardController controller;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Tải giao diện FXML và khởi tạo controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/User/Dashboard.fxml"));
            Parent root = loader.load();
            controller = loader.getController(); // Khởi tạo controller từ FXML
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            fail("Không thể tải tệp FXML: " + e.getMessage());
        }
    }

    @Test
    void testDashboardTitle() {
        try {
            // Kiểm tra xem tiêu đề của dashboard có hiển thị đúng không
            Label titleLabel = lookup("#titleLabel").query();
            assertNotNull(titleLabel, "titleLabel không tồn tại");
            assertEquals("Dashboard", titleLabel.getText(), "Tiêu đề không chính xác");
        } catch (Exception e) {
            fail("Lỗi khi kiểm tra tiêu đề: " + e.getMessage());
        }
    }

    @Test
    void testBookListTable() {
        try {
            // Kiểm tra xem bảng sách có được hiển thị và có ít nhất một mục trong đó không
            TableView tableView = lookup("#bookTable").query();
            assertNotNull(tableView, "Bảng sách không tồn tại");
            assertTrue(tableView.getItems().size() > 0, "Bảng sách không có dữ liệu");
        } catch (Exception e) {
            fail("Lỗi khi kiểm tra bảng sách: " + e.getMessage());
        }
    }

    @Test
    void testUserWelcomeMessage() {
        try {
            // Kiểm tra xem lời chào người dùng có hiển thị đúng thông tin hay không
            Label userLabel = lookup("#userLabel").query();
            assertNotNull(userLabel, "userLabel không tồn tại");
            assertTrue(userLabel.getText().contains("Welcome"), "Lời chào người dùng không chính xác");
        } catch (Exception e) {
            fail("Lỗi khi kiểm tra lời chào người dùng: " + e.getMessage());
        }
    }

    @Test
    void testAccountButtonNavigation() {
        try {
            // Kiểm tra xem nút "Account" có điều hướng đến trang thông tin tài khoản đúng không
            clickOn("#accountButton");
            waitForFxEvents();  // Chờ để đảm bảo các thay đổi UI được áp dụng
            Label accountLabel = lookup("#accountLabel").query();
            assertNotNull(accountLabel, "accountLabel không tồn tại");
            assertEquals("Account Info", accountLabel.getText(), "Không điều hướng đến thông tin tài khoản");
        } catch (Exception e) {
            fail("Lỗi khi kiểm tra điều hướng tài khoản: " + e.getMessage());
        }
    }
}
