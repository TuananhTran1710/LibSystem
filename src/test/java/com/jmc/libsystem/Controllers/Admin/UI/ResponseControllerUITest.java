package com.jmc.libsystem.Controllers.Admin.UI;

import com.jmc.libsystem.Controllers.Admin.ResponseController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ResponseControllerUITest extends ApplicationTest {

    private ResponseController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Admin/Response.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        //System.out.println(root);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testListViewPopulation() {
        Platform.runLater(() -> {
            controller.dataBook.add(java.util.Map.of(
                    "id", "1",
                    "title", "Test Book",
                    "authors", "Test Author",
                    "state", "In queue"
            ));
        });
        controller.list_propose.setItems(controller.dataBook);
        WaitForAsyncUtils.waitForFxEvents();

        // Kiểm tra ListView có phần tử
        assertEquals(1, controller.list_propose.getItems().size());
        assertEquals("Test Book", controller.list_propose.getItems().get(0).get("title"));
    }

    @Test
    void testChoiceBoxAction() throws Exception {
        // Thiết lập các lựa chọn
        Platform.runLater(() -> controller.choice_state.setItems(
                javafx.collections.FXCollections.observableArrayList("In queue", "Accept", "Reject", "All"))
        );

        // Giả lập lựa chọn
        Platform.runLater(() -> controller.choice_state.setValue("Accept"));

        WaitForAsyncUtils.waitForFxEvents();

        // Kiểm tra giá trị đã thay đổi
        assertEquals("Accept", controller.choice_state.getValue());
    }

}
