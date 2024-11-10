package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import com.jmc.libsystem.QueryDatabase.RecommendationSystem;
import com.jmc.libsystem.SuggestionBox.SuggestionBook;
import com.jmc.libsystem.Views.SearchCriteria;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    public ChoiceBox num_show_reading;
    public ChoiceBox num_show_popular;
    public ChoiceBox num_show_search;

    public static SearchCriteria typeSearch;
    public static int limitBookSearch;
    public static int limitBookPopular;
    public static int limitBookReading;

    public static List<Book> bookSearch;
    public static List<Book> bookPopular;
    public static List<Book> bookReading;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        criteriaBox.setItems(FXCollections.observableArrayList(SearchCriteria.TITLE, SearchCriteria.CATEGORY, SearchCriteria.AUTHORS));
        criteriaBox.setValue(SearchCriteria.TITLE);
        //
        num_show_search.setItems(FXCollections.observableArrayList(20, 50, 100, "All"));
        num_show_search.setValue(20);

        num_show_popular.setItems(FXCollections.observableArrayList(20, 50, 100, "All"));
        num_show_popular.setValue(20);

        num_show_reading.setItems(FXCollections.observableArrayList(20, 50, 100, "All"));
        num_show_reading.setValue(20);
        //
        typeSearch = SearchCriteria.TITLE;

        criteriaBox.valueProperty().addListener(observable ->
        {
            typeSearch = criteriaBox.getValue();
            if (SuggestionBook.autoCompletionBinding != null) {
                SuggestionBook.autoCompletionBinding.dispose();
            }
            if (typeSearch == SearchCriteria.TITLE) {
                SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, SuggestionBook.titleSuggest);
            } else if (typeSearch == SearchCriteria.AUTHORS) {
                SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, SuggestionBook.authorSuggest);
            } else {
                SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, SuggestionBook.categorySuggest);
            }
            SuggestionBook.autoCompletionBinding.setPrefWidth(search_tf.getWidth() - 160);
        });
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
        //
        SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, SuggestionBook.titleSuggest);
        SuggestionBook.autoCompletionBinding.setPrefWidth(search_tf.getWidth() - 160);
        //auto completion
        search_tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ENTER -> {
                        if (search_tf.isFocused() && !search_tf.getText().trim().isEmpty()) {
                            searchBooks(limitBookSearch);

                            SuggestionBook.autoCompletionLearnWord(search_tf, search_tf.getText().trim(), typeSearch);

                            resultList_hb.requestFocus();
                        }
                    }
                }
            }
        });
        //
        name_lbl.setText("Hi " + Model.getInstance().getMyUser().getFullName() + "!");
        search_btn.setOnAction(event -> searchBooks(limitBookSearch));
    }

    public void modifyShowBookSearch() {
        if (!(num_show_search.getValue()).equals("All"))
            limitBookSearch = (int) num_show_search.getValue();
        else limitBookSearch = Integer.MAX_VALUE;
        searchBooks(limitBookSearch);
    }

    public void modifyShowBookReading() {
        if (!(num_show_reading.getValue()).equals("All"))
            limitBookReading = (int) num_show_reading.getValue();
        else limitBookReading = Integer.MAX_VALUE;
        ShowListBookFound.show(bookReading, reading_hbox, limitBookReading);
    }

    public void modifyShowBookPopular() {
        if (!(num_show_popular.getValue()).equals("All"))
            limitBookPopular = (int) num_show_popular.getValue();
        else limitBookPopular = Integer.MAX_VALUE;
        ShowListBookFound.show(bookPopular, popular_hbox, limitBookPopular);
    }


    public void searchBooks(int limit) {
        String keyWord = search_tf.getText();
        keyWord = keyWord.trim();
        if (!keyWord.isEmpty()) {

            //gọi truy van de lay ket qua tu database
            bookSearch = SearchBookDatabase.getBookFromResultSet(keyWord, typeSearch.toString());

            //đẩy lên giao diện
            ShowListBookFound.show(bookSearch, resultList_hb, limitBookSearch);
        }
    }

    //this function is called when login successfully
    public void reset() {
        scrollPane_search.setHvalue(0.0);
        if (SuggestionBook.autoCompletionBinding != null) {
            SuggestionBook.autoCompletionBinding.dispose();
        }
        SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, SuggestionBook.titleSuggest);
        SuggestionBook.autoCompletionBinding.setPrefWidth(search_tf.getWidth() - 160);
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

        if (num_show_reading.getValue().equals("All") || (int) num_show_reading.getValue() != 20)
            num_show_reading.setValue(20); // sau khi set thi lap tuc nhay vao ham modifyReading o Dashboard
        else ShowListBookFound.show(bookReading, reading_hbox, 20);
    }

    public void resetPopular() {
        scrollPane_popular.setHvalue(0.0);
        bookPopular = RecommendationSystem.getListRecommend();
        if (num_show_popular.getValue().equals("All") || (int) num_show_popular.getValue() != 20)
            num_show_popular.setValue(20);
        else ShowListBookFound.show(bookPopular, popular_hbox, 20);
    }

    public void resetAll() {
        reset();
        resetReading();
        resetPopular();
    }
}