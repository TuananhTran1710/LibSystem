package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.HandleJsonString.SearchBookAPI;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.SuggestionBox.SuggestionBook;
import com.jmc.libsystem.Views.SearchCriteria;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ProposeController implements Initializable {
    private static ProposeController instance;

    public TextField search_tf;
    public ChoiceBox<SearchCriteria> criteriaBox;
    public Button search_btn;
    public ScrollPane scrollPane_search;
    public HBox resultSearchList_hb;
    public ScrollPane scrollPane_suggest;
    public HBox resultSuggestList_hb;
    public ChoiceBox num_show_preSuggest;

    public static SearchCriteria typeSearch;
    public static int limitBookPreSuggest;
    public static List<Book> bookSearch;
    public static List<Book> bookPreSuggest;
    private ObservableList<Map<String, Object>> data;

    public static ProposeController getInstance() {
        return instance;
    }

    public ProposeController() {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        criteriaBox.setItems(FXCollections.observableArrayList(SearchCriteria.TITLE, SearchCriteria.CATEGORY, SearchCriteria.AUTHORS));
        criteriaBox.setValue(SearchCriteria.TITLE);
        //
        num_show_preSuggest.setItems(FXCollections.observableArrayList(20, 50, 100, "All"));
        num_show_preSuggest.setValue(20);
        limitBookPreSuggest = 20;
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
        bookSearch = new ArrayList<>();
        bookPreSuggest = new ArrayList<>();
        //
        num_show_preSuggest.valueProperty().addListener(observable -> modifyShowBookPreSuggest());

        if (SuggestionBook.autoCompletionBinding != null) {
            SuggestionBook.autoCompletionBinding.dispose();
        }
        //auto completion
        SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, SuggestionBook.titleSuggest);
        SuggestionBook.autoCompletionBinding.setPrefWidth(search_tf.getWidth() - 160);
        search_tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ENTER -> {
                        if (search_tf.isFocused() && !search_tf.getText().trim().isEmpty()) {
                            searchBooks();

                            SuggestionBook.autoCompletionLearnWord(search_tf, search_tf.getText().trim(), typeSearch);

                            resultSearchList_hb.requestFocus();
                        }
                    }
                }
            }
        });
        //
        search_btn.setOnAction(event -> searchBooks());
        data = FXCollections.observableArrayList();
    }

    public void modifyShowBookPreSuggest() {
        if (!(num_show_preSuggest.getValue()).equals("All"))
            limitBookPreSuggest = (int) num_show_preSuggest.getValue();
        else limitBookPreSuggest = Integer.MAX_VALUE;
        ShowListBookFound.show(bookPreSuggest, resultSuggestList_hb, limitBookPreSuggest);
    }

    private void searchBooks() {
        String keyWord = search_tf.getText();
        keyWord = keyWord.trim();
        if (!keyWord.isEmpty()) {
            String word = keyWord;
            Task<List<Book>> task = new Task<List<Book>>() {

                @Override
                protected List<Book> call() throws Exception {
                    Long start = System.currentTimeMillis();
                    bookSearch = SearchBookAPI.getListBookFromJson(word, typeSearch.toString());
                    Thread.sleep(100);
                    Long end = System.currentTimeMillis();
                    System.out.println(end - start);
                    return bookSearch;
                }
            };

            // tu day la chay tren luong chinh
            task.setOnSucceeded(e -> {
                List<Book> list = task.getValue();
                Platform.runLater(() -> ShowListBookFound.show(list, resultSearchList_hb));
            });
            new Thread(task).start();
        }
    }
}
