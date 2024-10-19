package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.SearchCriteria;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public TextField search_tf;
    public Button search_btn;
    public Label name_lbl;
    public HBox resultList_hb;
    public ChoiceBox<SearchCriteria> choice_box;
    public static SearchCriteria typeSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choice_box.setItems(FXCollections.observableArrayList(SearchCriteria.TITLE, SearchCriteria.CATEGORY, SearchCriteria.AUTHORS));
        choice_box.setValue(SearchCriteria.TITLE);
        typeSearch = SearchCriteria.TITLE;

        choice_box.valueProperty().addListener(observable -> typeSearch = choice_box.getValue());

        name_lbl.setText("Hi " + Model.getInstance().getMyUser().getFullName() + "!");
        search_btn.setOnAction(event -> searhBooks());
    }

    public void searhBooks() {
        String keyWord = search_tf.getText();
        keyWord = keyWord.trim();
        if (!keyWord.isEmpty()) {
            //gọi model để lấy kết quả từ api
            List<Book> books = null;

            books = SearchBookDatabase.getBookFromResultSet(keyWord);

            //đẩy lên giao diện
            ShowListBookFound.show(books, resultList_hb);

        }
    }
}