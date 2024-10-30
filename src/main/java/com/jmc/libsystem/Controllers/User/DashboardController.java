package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.ResetWindow.ResetDashboard;
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
    public ChoiceBox<SearchCriteria> criteriaBox;
    public Button notice_btn;
    public HBox reading_hbox;
    public HBox popular_hbox;
    public ChoiceBox<Integer> num_show_reading;
    public ChoiceBox<Integer> num_show_popular;
    public ChoiceBox<Integer> num_show_search;

    public static SearchCriteria typeSearch;
    public static int limitBookSearch;
    public List<Book> books;

    public AutoCompletionBinding<String> autoCompletionBinding;
    public Set<String> titleSuggest = new HashSet<>();
    public Set<String> categorySuggest = new HashSet<>();
    public Set<String> authorSuggest = new HashSet<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        criteriaBox.setItems(FXCollections.observableArrayList(SearchCriteria.TITLE, SearchCriteria.CATEGORY, SearchCriteria.AUTHORS));
        criteriaBox.setValue(SearchCriteria.TITLE);

        num_show_search.setItems(FXCollections.observableArrayList(20, 50, 100));
        num_show_search.setValue(20);

        limitBookSearch = 20;
        typeSearch = SearchCriteria.TITLE;
        books = new ArrayList<>();

        criteriaBox.valueProperty().addListener(observable -> typeSearch = criteriaBox.getValue());

        num_show_search.valueProperty().addListener(observable -> modifyShowBook());

        autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, titleSuggest);
        search_tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ENTER -> {
                        autoCompletionLearnWord(search_tf.getText().trim());
                        searhBooks(limitBookSearch);
                        // move focus to something
                        resultList_hb.requestFocus();
                    }
                }
            }
        });

        name_lbl.setText("Hi " + Model.getInstance().getMyUser().getFullName() + "!");
        search_btn.setOnAction(event -> searhBooks(limitBookSearch));
    }

    public void autoCompletionLearnWord(String newWord) {
        if (!titleSuggest.contains(newWord)) {
            titleSuggest.add(newWord);
            if (autoCompletionBinding != null) {
                autoCompletionBinding.dispose();
            }
            autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, titleSuggest);
            autoCompletionBinding.setPrefWidth(search_tf.getWidth() - 160);
        }
    }

    public void modifyShowBook() {
        limitBookSearch = num_show_search.getValue();
        searhBooks(limitBookSearch);
    }

    public void searhBooks(int limit) {
        String keyWord = search_tf.getText();
        keyWord = keyWord.trim();
        if (!keyWord.isEmpty()) {

            autoCompletionLearnWord(keyWord);

            //gọi truy van de lay ket qua tu database
            books = SearchBookDatabase.getBookFromResultSet(keyWord);

            //đẩy lên giao diện
            ShowListBookFound.show(books, resultList_hb, limitBookSearch);
        }
    }

    public void addTitleSetSuggestion() {
        // khi admin add sach vao thi cho truc tiep ten sach, ten tg, muc luc vao goi y luon

    }

    //this function is called when login successfully
    public static void reset() {
        Label lb = (Label) Model.getInstance().getViewFactory().getDashboardView().lookup("#name_lbl");
        TextField search = (TextField) Model.getInstance().getViewFactory().getDashboardView().lookup("#search_tf");
        ChoiceBox<SearchCriteria> criteria = (ChoiceBox<SearchCriteria>) Model.getInstance().getViewFactory().getDashboardView().lookup("#criteriaBox");
        ChoiceBox<Integer> limit = (ChoiceBox<Integer>) Model.getInstance().getViewFactory().getDashboardView().lookup("#num_show_search");
        HBox res = (HBox) Model.getInstance().getViewFactory().getDashboardView().lookup("#resultList_hb");

        ResetDashboard.resetDashboard(search, criteria, lb, limit, res);
    }

}