package com.jmc.libsystem.Controllers.Book;

import com.jmc.libsystem.Controllers.User.ProposeController;
import com.jmc.libsystem.Controllers.User.UserController;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.QueryDatabase.QueryBookRecommend;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class BookProposeDetailController extends BaseBookDetailController {
    public Button recommend_btn;
    public Text recommend_msg;

    private static BookProposeDetailController instance;

    public static BookProposeDetailController getInstance() {
        return instance;
    }

    public BookProposeDetailController() {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        back_btn.setOnAction(event -> moveMenuCurrent());

        recommend_btn.setOnAction(event -> {
            toMessage();
            QueryBookData.addBookRcm(book);
            recommend_msg.setText("Book is recommended");
            QueryBookRecommend.insertNewRecommend(book.getId(), "In queue");
        });
    }

    @Override
    public void moveMenuCurrent() {
        UserController.getInstance().user_parent.setCenter(Model.getInstance().getViewFactory().getProposeView());
        ProposeController.getInstance().resetPreSuggest();
    }

    public void toRecommendButton() {
        recommend_btn.setVisible(true);
        recommend_btn.setDisable(false);
        recommend_msg.setVisible(false);
    }

    public void toMessage() {
        recommend_btn.setVisible(false);
        recommend_msg.setVisible(true);
        recommend_btn.setDisable(true);
    }

    @Override
    public void modifyButton() {
        if (QueryBookData.isExist(book.getId())) {
            toMessage();
        } else {
            toRecommendButton();
        }
    }
}
