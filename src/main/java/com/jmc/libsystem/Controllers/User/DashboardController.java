package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.BookSearchModel;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public TextField search_tf;
    public Button search_btn;
    public Label name_lbl;
    public HBox resultList_hb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name_lbl.setText("Hi " + Model.getInstance().getMyUser().getFullName() + "!");
        search_btn.setOnAction(event -> searhBooks());
    }

    public void searhBooks() {
        String keyWord = search_tf.getText();
        keyWord = keyWord.trim();
        if (!keyWord.isEmpty()) {

            try {
                //gọi model để lấy kết quả từ api
                List<Book> books = BookSearchModel.getListBookFromJson(keyWord);

                //đẩy lên giao diện
                ShowListBookFound.show(books, resultList_hb);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}