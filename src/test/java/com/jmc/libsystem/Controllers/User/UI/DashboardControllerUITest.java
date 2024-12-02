package com.jmc.libsystem.Controllers.User.UI;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

class DashboardControllerUITest extends ApplicationTest {

    private DashboardController controller;

    @Override
    public void start(Stage stage) throws Exception {
        User user = new User("12", "hehe", "heke", "dad", "availbe");
        Model.getInstance().setMyUser(user);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/User/Dashboard.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testSearchTextField() {
        TextField searchTf = lookup("#search_tf").query();
        assertNotNull(searchTf, "tf không tồn tại");

        clickOn(searchTf).write("Cuong dep trai");
        assertEquals("Cuong dep trai", searchTf.getText(), "Văn bản không đúng");
    }

    @Test
    void testSearchButton() {
        Button searchBtn = lookup("#search_btn").query();
        assertNotNull(searchBtn, "nút search không tồn tại");

        clickOn(searchBtn);

        HBox resultListHb = lookup("#resultList_hb").query();
        assertNotNull(resultListHb, "kết quả không hiển thị");
    }

    @Test
    void testUserDataInitialization() {
        Label nameLbl = lookup("#name_lbl").query();
        assertNotNull(nameLbl, "không có tên");
        assertEquals("Hi hehe!", nameLbl.getText(), "tên không chính xác");
    }

    @Test
    void testSearchFunctionalityWithValidInput() {
        TextField searchTf = lookup("#search_tf").query();
        Button searchBtn = lookup("#search_btn").query();

        clickOn(searchTf).write("cuong xau trai");
        clickOn(searchBtn);

        HBox resultListHb = lookup("#resultList_hb").query();
        assertNotNull(resultListHb, "Kết quả tìm kiếm không hiển thị");

        assertTrue(resultListHb.getChildren().size() > 0, "Kết quả tìm kiếm không có dữ liệu");
    }
}
