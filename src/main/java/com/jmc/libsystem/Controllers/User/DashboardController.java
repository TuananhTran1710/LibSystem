package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import com.jmc.libsystem.QueryDatabase.RecommendationSystem;
import com.jmc.libsystem.Views.SearchCriteria;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.sql.ResultSet;
import java.util.*;

public class DashboardController implements Initializable {
    private static DashboardController instance;
    public ScrollPane scrollPane_popular;
    public ScrollPane scrollPane_search;
    public ScrollPane scrollPane_reading;


    public DashboardController() {
        instance = this;
    }

    public static DashboardController getInstance() {
        return instance;
    }


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

    public static boolean onBookPreview = false;
    public static boolean onBookLoanPreview = false;

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
        autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, titleSuggest);

        //auto completion
        search_tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ENTER -> {
                        if (search_tf.isFocused() && !search_tf.getText().trim().isEmpty()) {
                            searhBooks(limitBookSearch);
                            autoCompletionLearnWord(search_tf.getText().trim());
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
    public void reset() {
        scrollPane_search.setHvalue(0.0);
        search_tf.clear();
        criteriaBox.setValue(SearchCriteria.TITLE);
        name_lbl.setText("Hi " + Model.getInstance().getMyUser().getFullName() + "!");
        num_show_search.setValue(20);
        resultList_hb.getChildren().clear();
    }

    public void resetReading() {
        scrollPane_reading.setHvalue(0.0);
        ResultSet resultSet = QueryBookLoans.getListBorrowing(Model.getInstance().getMyUser().getId());
        bookReading = SearchBookDatabase.getBookFromResultSet(resultSet);

        if (num_show_reading.getValue() != 20)
            num_show_reading.setValue(20); // sau khi set thi lap tuc nhay vao ham modifyReading o Dashboard
        else ShowListBookFound.show(bookReading, reading_hbox, 20);
    }

    public void resetPopular() {
        scrollPane_popular.setHvalue(0.0);
        bookPopular = RecommendationSystem.getListRecommend();
        if (num_show_popular.getValue() != 20)
            num_show_popular.setValue(20);
        else ShowListBookFound.show(bookPopular, popular_hbox, 20);
    }

    public void resetAll() {
        reset();
        resetReading();
        resetPopular();
    }

}