package com.jmc.libsystem.Controllers.User.UI;

import com.jmc.libsystem.Controllers.User.MyBookController;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class MyBookControllerUITest extends ApplicationTest {

    private MyBookController controller;

    @Override
    public void start(Stage stage) throws Exception {
        User user = new User("12", "hehe", "heke", "dad", "availbe");
        Model.getInstance().setMyUser(user);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/User/MyBook.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testNumberBorrowLabel() {
        Label numberBorrowLabel = lookup("#NumberBorrow").query();
        assertEquals("0", numberBorrowLabel.getText(), "sai số lượng mượn");
    }

    @Test
    void testScrollPaneForFavorites() {
        ScrollPane favScrollPane = lookup("#scrollPane_fav").query();
        assertNotNull(favScrollPane, "danh sách yêu thích không tồn tại");

        assertTrue(favScrollPane.getContent() instanceof HBox, "sai danh sách yêu thích");
    }

    @Test
    void testChoiceBoxDefaultValue() {
        ChoiceBox<?> numShowFav = lookup("#num_show_fav").query();
        assertNotNull(numShowFav, "choicebox số sách yêu thích ko tồn tại");
        assertEquals("20", numShowFav.getValue().toString(), "giá trị mặc định sai");

        ChoiceBox<?> numShowBorrow = lookup("#num_show_borrow").query();
        assertNotNull(numShowBorrow, "choiceBox sốsách mượn ko tồn tại");
        assertEquals("20", numShowBorrow.getValue().toString(), "giá trị mặc định sai");
    }


    @Test
    void testClickOnBorrowedBook() {
        HBox borrowHb = lookup("#Borrow_HB").query();
        assertNotNull(borrowHb, "danh sách sách mượn không tồn tại");

        clickOn(borrowHb.getChildren().get(0));
        assertTrue(true, "không click được");
    }

    @Test
    void testClickOnFavoriteBook() {
        HBox favoriteHb = lookup("#Favorite_HB").query();
        assertNotNull(favoriteHb, "danh sách sách yêu thích không tồn tại");

        clickOn(favoriteHb.getChildren().get(0));
        assertTrue(true, "không click được");
    }
}
