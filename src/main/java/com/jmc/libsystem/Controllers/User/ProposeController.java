package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.HandleJsonString.SearchBookAPI;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookRecommend;
import com.jmc.libsystem.SuggestionBox.SuggestionBookAPI;
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
        bookPreSuggest = new ArrayList<>();
        initializePreSuggestControls();
        showBookSearch();
        refreshPropose();
    }

    private void refreshPropose() {
        resetPreSuggest();
        resetSearch();
    }

    public void showPropose() {
        refreshPropose();
    }

    private void initializePreSuggestControls() {
        num_show_preSuggest.setItems(FXCollections.observableArrayList(20, 50, 100, "All"));
        num_show_preSuggest.setValue(20);
        limitBookPreSuggest = 20;

        num_show_preSuggest.valueProperty().addListener(observable -> modifyShowBookPreSuggest());
    }

    // Hiển thị danh sách đề xuất
    public void showPreSuggest() {
        User current_user = Model.getInstance().getMyUser();
        bookPreSuggest = QueryBookRecommend.getPreSuggestBooks(current_user.getId());
        updatePreSuggestView();
    }

    // Làm mới danh sách đề xuất
    public void resetPreSuggest() {
        scrollPane_suggest.setHvalue(0.0);
        showPreSuggest();
    }

    private void updatePreSuggestView() {
        if (num_show_preSuggest.getValue().equals("All")) {
            ShowListBookFound.showAPIFromUser(bookPreSuggest, resultSuggestList_hb, false, true);
        } else {
            int limit = (int) num_show_preSuggest.getValue();
            ShowListBookFound.showAPIFromUser(bookPreSuggest, resultSuggestList_hb, limit, true);
        }
    }

    private void modifyShowBookPreSuggest() {
        if (bookPreSuggest == null || bookPreSuggest.isEmpty()) {
            System.out.println("No books to display in PreSuggest.");
            return;
        }
        updatePreSuggestView();
    }

    public void showBookSearch() {
        criteriaBox.setItems(FXCollections.observableArrayList(SearchCriteria.TITLE, SearchCriteria.CATEGORY, SearchCriteria.AUTHORS));
        criteriaBox.setValue(SearchCriteria.TITLE);
        //
        typeSearch = SearchCriteria.TITLE;

        criteriaBox.valueProperty().addListener(observable ->
        {
            typeSearch = criteriaBox.getValue();
            if (SuggestionBookAPI.autoCompletionBinding != null) {
                SuggestionBookAPI.autoCompletionBinding.dispose();
            }
            if (typeSearch == SearchCriteria.TITLE) {
                SuggestionBookAPI.autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, SuggestionBookAPI.titleSuggest);
            } else if (typeSearch == SearchCriteria.AUTHORS) {
                SuggestionBookAPI.autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, SuggestionBookAPI.authorSuggest);
            } else {
                SuggestionBookAPI.autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, SuggestionBookAPI.categorySuggest);
            }
            SuggestionBookAPI.autoCompletionBinding.setPrefWidth(search_tf.getWidth() - 160);
        });
        //
        bookSearch = new ArrayList<>();
        //
        if (SuggestionBookAPI.autoCompletionBinding != null) {
            SuggestionBookAPI.autoCompletionBinding.dispose();
        }
        //auto completion
        SuggestionBookAPI.autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, SuggestionBookAPI.titleSuggest);
        SuggestionBookAPI.autoCompletionBinding.setPrefWidth(search_tf.getWidth() - 160);
        search_tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ENTER -> {
                        if (search_tf.isFocused() && !search_tf.getText().trim().isEmpty()) {
                            searchBooks();

                            SuggestionBookAPI.autoCompletionLearnWord(search_tf, search_tf.getText().trim(), typeSearch);

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
                Platform.runLater(() -> ShowListBookFound.showAPIFromUser(list, resultSearchList_hb, true, false));
            });
            new Thread(task).start();
        }
    }

    private void resetSearch() {
        search_tf.clear();
        resultSearchList_hb.getChildren().clear();
    }
}
