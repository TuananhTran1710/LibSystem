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
    public static int limitBookPopular;
    public static int limitBookReading;

    public static List<Book> bookSearch;
    public static List<Book> bookPopular;
    public static List<Book> bookReading;


    public AutoCompletionBinding<String> autoCompletionBinding;
    public Set<String> titleSuggest = new HashSet<>();
    public Set<String> categorySuggest = new HashSet<>();
    public Set<String> authorSuggest = new HashSet<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //
        criteriaBox.setItems(FXCollections.observableArrayList(SearchCriteria.TITLE, SearchCriteria.CATEGORY, SearchCriteria.AUTHORS));
        criteriaBox.setValue(SearchCriteria.TITLE);
        //
        num_show_search.setItems(FXCollections.observableArrayList(20, 50, 100));
        num_show_search.setValue(20);

        num_show_popular.setItems(FXCollections.observableArrayList(20, 50, 100));
        num_show_popular.setValue(20);

        num_show_reading.setItems(FXCollections.observableArrayList(20, 50, 100));
        num_show_reading.setValue(20);
        //
        typeSearch = SearchCriteria.TITLE;
        criteriaBox.valueProperty().addListener(observable -> typeSearch = criteriaBox.getValue());
        //
        limitBookSearch = 20;
        limitBookPopular = 20;
        limitBookReading = 20;
        //
        bookSearch = new ArrayList<>();
        bookPopular = new ArrayList<>();
        bookReading = new ArrayList<>();
        //
        num_show_search.valueProperty().addListener(observable -> modifyShowBookSearch());
        num_show_reading.valueProperty().addListener(observable -> modifyShowBookReading());
        num_show_popular.valueProperty().addListener(observable -> modifyShowBookPopular());

        //auto completion
        autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, titleSuggest);
        search_tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ENTER -> {
                        if (!search_tf.getText().trim().isEmpty()) {
                            autoCompletionLearnWord(search_tf.getText().trim());
                            searhBooks(limitBookSearch);
                            // move focus to something
                            resultList_hb.requestFocus();
                        }
                    }
                }
            }
        });
        //
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

    public void modifyShowBookSearch() {
        limitBookSearch = num_show_search.getValue();
        searhBooks(limitBookSearch);
    }

    public void modifyShowBookReading() {
        limitBookReading = num_show_reading.getValue();
        ShowListBookFound.show(bookReading, reading_hbox, limitBookReading);
    }

    public void modifyShowBookPopular() {
        limitBookPopular = num_show_popular.getValue();
        ShowListBookFound.show(bookPopular, popular_hbox, limitBookPopular);
    }

    public static void setListBookReading(List<Book> newListBook) {
        bookReading = newListBook;
    }

    public static void setListBookPopular(List<Book> newListBook) {
        bookPopular = newListBook;
    }


    public void searhBooks(int limit) {
        String keyWord = search_tf.getText();
        keyWord = keyWord.trim();
        if (!keyWord.isEmpty()) {

            autoCompletionLearnWord(keyWord);

            //gọi truy van de lay ket qua tu database
            bookSearch = SearchBookDatabase.getBookFromResultSet(keyWord);

            //đẩy lên giao diện
            ShowListBookFound.show(bookSearch, resultList_hb, limitBookSearch);
        }
    }


    //chua lam cai nay
    public void addTitleSetSuggestion() {
        // khi admin add sach vao thi cho truc tiep ten sach, ten tg, muc luc vao goi y luon

    }

    //this function is called when login successfully
    public static void reset() {
        Label name = (Label) Model.getInstance().getViewFactory().getDashboardView().lookup("#name_lbl");
        TextField search = (TextField) Model.getInstance().getViewFactory().getDashboardView().lookup("#search_tf");
        ChoiceBox<SearchCriteria> criteria = (ChoiceBox<SearchCriteria>) Model.getInstance().getViewFactory().getDashboardView().lookup("#criteriaBox");
        ChoiceBox<Integer> limitSearch = (ChoiceBox<Integer>) Model.getInstance().getViewFactory().getDashboardView().lookup("#num_show_search");
        HBox res = (HBox) Model.getInstance().getViewFactory().getDashboardView().lookup("#resultList_hb");

        ResetDashboard.resetDashboard(search, criteria, name, limitSearch, res);
    }

    public static void resetReading() {
        ChoiceBox<Integer> limitReading = (ChoiceBox<Integer>) Model.getInstance().getViewFactory().getDashboardView().lookup("#num_show_reading");
        HBox containResult = (HBox) Model.getInstance().getViewFactory().getDashboardView().lookup("#reading_hbox");
        ResetDashboard.updateReadingBox(limitReading, containResult);
    }

    public static void resetPopular() {
        ChoiceBox<Integer> limitPopular = (ChoiceBox<Integer>) Model.getInstance().getViewFactory().getDashboardView().lookup("#num_show_popular");
        HBox containResult = (HBox) Model.getInstance().getViewFactory().getDashboardView().lookup("#popular_hbox");
        ResetDashboard.updatePopularBox(limitPopular, containResult);
    }

}