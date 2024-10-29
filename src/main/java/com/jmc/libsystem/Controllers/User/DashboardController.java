package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.SearchCriteria;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.*;

public class DashboardController implements Initializable {
    public TextField search_tf;
    public Button search_btn;
    public Label name_lbl;
    public HBox resultList_hb;
    public ChoiceBox<SearchCriteria> choice_box;
    public static SearchCriteria typeSearch;
    public static int limitBookSearch;
    public Button notice_btn;
    public HBox reading_hbox;
    public HBox popular_hbox;
    public ChoiceBox<Integer> num_show_reading;
    public ChoiceBox<Integer> num_show_popular;
    public ChoiceBox<Integer> num_show_search;
    private List<Book> books;

    public AutoCompletionBinding<String> autoCompletionBinding;
    public Set<String> possibleSuggestion = new HashSet<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choice_box.setItems(FXCollections.observableArrayList(SearchCriteria.TITLE, SearchCriteria.CATEGORY, SearchCriteria.AUTHORS));
        choice_box.setValue(SearchCriteria.TITLE);

        num_show_search.setItems(FXCollections.observableArrayList(20, 50, 100));
        num_show_search.setValue(20);

        limitBookSearch = 20;
        typeSearch = SearchCriteria.TITLE;
        books = new ArrayList<>();

        choice_box.valueProperty().addListener(observable -> typeSearch = choice_box.getValue());

        num_show_search.valueProperty().addListener(observable -> modifyShowBook());

        autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, possibleSuggestion);
        search_tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ENTER -> {
                        autoCompletionLearnWord(search_tf.getText().trim());
                        searhBooks();
                        resultList_hb.requestFocus();
                    }
                }
            }
        });

        name_lbl.setText("Hi " + Model.getInstance().getMyUser().getFullName() + "!");
        search_btn.setOnAction(event -> searhBooks());
    }

    private void autoCompletionLearnWord(String newWord) {
        if (!possibleSuggestion.contains(newWord)) {
            possibleSuggestion.add(newWord);
            if (autoCompletionBinding != null) {
                autoCompletionBinding.dispose();
            }
            autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, possibleSuggestion);
            autoCompletionBinding.setPrefWidth(search_tf.getWidth() - 160);
        }
    }

    private void modifyShowBook() {
        limitBookSearch = num_show_search.getValue();
        String keyWord = search_tf.getText().trim();

        if (!keyWord.isEmpty()) {
            books = SearchBookDatabase.getBookFromResultSet(keyWord);
            ShowListBookFound.show(books, resultList_hb, limitBookSearch);

            autoCompletionLearnWord(keyWord);
        }
    }

    public void searhBooks() {
        String keyWord = search_tf.getText();
        keyWord = keyWord.trim();
        if (!keyWord.isEmpty()) {

            autoCompletionLearnWord(keyWord);

            //gọi model để lấy kết quả từ api
            books = SearchBookDatabase.getBookFromResultSet(keyWord);

            //đẩy lên giao diện
            ShowListBookFound.show(books, resultList_hb, limitBookSearch);

        }
    }
}