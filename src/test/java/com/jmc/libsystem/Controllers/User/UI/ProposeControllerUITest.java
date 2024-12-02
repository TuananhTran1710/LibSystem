package com.jmc.libsystem.Controllers.User.UI;

import com.jmc.libsystem.Controllers.User.ProposeController;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class ProposeControllerUITest extends ApplicationTest {
    private ProposeController controller;

    @Override
    public void start(Stage stage) throws Exception {
        User user = new User("12", "hehe", "heke", "dad", "availbe");
        Model.getInstance().setMyUser(user);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/User/Propose.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testSearchTextField() {
        TextField searchTf = lookup("#search_tf").query();
        assertNotNull(searchTf, "tf không tồn tại");

        clickOn(searchTf).write("Ronaldo");
        assertEquals("Ronaldo", searchTf.getText(), "văn bản không chính xác");
    }

    @Test
    void testSearchButton() {
        Button searchBtn = lookup("#search_btn").query();
        assertNotNull(searchBtn, "search btn không tồn tại");

        clickOn(searchBtn);

        HBox resultSearchListHb = lookup("#resultSearchList_hb").query();
        assertNotNull(resultSearchListHb, "không có kết quả");
    }

    @Test
    void testNumberOfBooksToShowChoiceBox() {
        ChoiceBox<?> numShowPreSuggest = lookup("#num_show_preSuggest").query();
        assertNotNull(numShowPreSuggest, "choiceBox sách đề xuất không tồn tại");

        assertEquals("20", numShowPreSuggest.getValue().toString(), "giá trị mặc định không đúng");

        clickOn(numShowPreSuggest);
        clickOn("50");

        assertEquals("50", numShowPreSuggest.getValue().toString(), "sai số lượng sách");
    }

    @Test
    void testScrollPaneForSuggestResults() {
        ScrollPane scrollPaneSuggest = lookup("#scrollPane_suggest").query();
        assertNotNull(scrollPaneSuggest, "scrollPanekhông tồn tại");

        assertTrue(scrollPaneSuggest.getContent() instanceof HBox, "scrollPane sai nội dung");
    }
}
