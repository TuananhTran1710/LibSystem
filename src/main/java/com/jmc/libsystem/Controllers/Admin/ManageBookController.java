package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.HandleJsonString.SearchBookAPI;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.SuggestionBox.SuggestionBook;
import com.jmc.libsystem.SuggestionBox.SuggestionBookAPI;
import com.jmc.libsystem.Views.SearchCriteria;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageBookController implements Initializable {

    private static ManageBookController instance;

    public ManageBookController() {
        instance = this;
    }

    public static ManageBookController getInstance() {
        return instance;
    }

    public VBox addBooksView;
    public TextField searchAddBook_tf;
    public ChoiceBox<SearchCriteria> criteriaBoxAddBook;
    public Button searchAddBook_btn;
    public HBox BookAPI_hb;
    public TextField searchInLib_tf;
    public ChoiceBox<SearchCriteria> criteriaSearchLib;
    public Button searchInLib_btn;
    public ScrollPane scrollPane;

    public TextField getSearchAddBook_tf() {
        return searchAddBook_tf;
    }

    public TextField getSearchInLib_tf() {
        return searchInLib_tf;
    }

    public static SearchCriteria typeSearchAddBook;
    public static SearchCriteria typeSearchInLib;

    public static List<Book> bookSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        criteriaSearchLib.setItems(FXCollections.observableArrayList(SearchCriteria.TITLE, SearchCriteria.CATEGORY, SearchCriteria.AUTHORS));
        criteriaSearchLib.setValue(SearchCriteria.TITLE);

        criteriaBoxAddBook.setItems(FXCollections.observableArrayList(SearchCriteria.TITLE, SearchCriteria.CATEGORY, SearchCriteria.AUTHORS));
        criteriaBoxAddBook.setValue(SearchCriteria.TITLE);

        typeSearchInLib = SearchCriteria.TITLE;
        typeSearchAddBook = SearchCriteria.TITLE;

        criteriaBoxAddBook.valueProperty().addListener(observable ->
        {
            typeSearchAddBook = criteriaBoxAddBook.getValue();
            if (SuggestionBookAPI.autoCompletionBinding != null) {
                SuggestionBookAPI.autoCompletionBinding.dispose();
            }
            if (typeSearchAddBook == SearchCriteria.TITLE) {
                SuggestionBookAPI.autoCompletionBinding = TextFields.bindAutoCompletion(searchAddBook_tf, SuggestionBookAPI.titleSuggest);
            } else if (typeSearchAddBook == SearchCriteria.AUTHORS) {
                SuggestionBookAPI.autoCompletionBinding = TextFields.bindAutoCompletion(searchAddBook_tf, SuggestionBookAPI.authorSuggest);
            } else {
                SuggestionBookAPI.autoCompletionBinding = TextFields.bindAutoCompletion(searchAddBook_tf, SuggestionBookAPI.categorySuggest);
            }
            SuggestionBookAPI.autoCompletionBinding.setPrefWidth(searchAddBook_tf.getWidth() - 160);
        });

        criteriaSearchLib.valueProperty().addListener(observable ->
        {
            typeSearchInLib = criteriaSearchLib.getValue();
            if (SuggestionBook.autoCompletionBinding != null) {
                SuggestionBook.autoCompletionBinding.dispose();
            }
            if (typeSearchInLib == SearchCriteria.TITLE) {
                SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(searchInLib_tf, SuggestionBook.titleSuggest);
            } else if (typeSearchInLib == SearchCriteria.AUTHORS) {
                SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(searchInLib_tf, SuggestionBook.authorSuggest);
            } else {
                SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(searchInLib_tf, SuggestionBook.categorySuggest);
            }
            SuggestionBook.autoCompletionBinding.setPrefWidth(searchInLib_tf.getWidth() - 160);
        });

        bookSearch = new ArrayList<>();

        SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(searchInLib_tf, SuggestionBook.titleSuggest);
        SuggestionBook.autoCompletionBinding.setPrefWidth(searchInLib_tf.getWidth() - 160);

        SuggestionBookAPI.autoCompletionBinding = TextFields.bindAutoCompletion(searchAddBook_tf, SuggestionBookAPI.titleSuggest);
        SuggestionBookAPI.autoCompletionBinding.setPrefWidth(searchAddBook_tf.getWidth() - 160);

        searchAddBook_tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ENTER -> {
                        if (searchAddBook_tf.isFocused() && !searchAddBook_tf.getText().trim().isEmpty()) {
                            searchAddBooks();

                            SuggestionBookAPI.autoCompletionLearnWord(searchAddBook_tf, searchAddBook_tf.getText().trim(), typeSearchAddBook);

                            BookAPI_hb.requestFocus();
                        }
                    }
                }
            }
        });

        searchInLib_tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ENTER -> {
                        if (searchInLib_tf.isFocused() && !searchInLib_tf.getText().trim().isEmpty()) {
                            searchBookInLib();
                            // do something to suitable with table column
                        }
                    }
                }
            }
        });

        searchAddBook_btn.setOnAction(event -> searchAddBooks());
        searchInLib_btn.setOnAction(event -> searchBookInLib());
    }

    private void searchBookInLib() {

    }

    private void searchAddBooks() {
        String keyWord = searchAddBook_tf.getText();
        keyWord = keyWord.trim();
        if (!keyWord.isEmpty()) {

            //gọi truy van de lay ket qua tu API
            try {
                bookSearch = SearchBookAPI.getListBookFromJson(keyWord, typeSearchAddBook.toString());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //đẩy lên giao diện
            ShowListBookFound.show(bookSearch, BookAPI_hb);
        }
    }


    public void reset() {
        scrollPane.setHvalue(0.0);
        if (SuggestionBookAPI.autoCompletionBinding != null) {
            SuggestionBookAPI.autoCompletionBinding.dispose();
        }
        SuggestionBookAPI.autoCompletionBinding = TextFields.bindAutoCompletion(searchAddBook_tf, SuggestionBookAPI.titleSuggest);
        SuggestionBookAPI.autoCompletionBinding.setPrefWidth(searchAddBook_tf.getWidth() - 160);
        searchAddBook_tf.clear();
        criteriaBoxAddBook.setValue(SearchCriteria.TITLE);
        BookAPI_hb.getChildren().clear();
    }

    public void resetBookLibrary() {
        if (SuggestionBook.autoCompletionBinding != null) {
            SuggestionBook.autoCompletionBinding.dispose();
        }
        SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(searchInLib_tf, SuggestionBook.titleSuggest);
        SuggestionBook.autoCompletionBinding.setPrefWidth(searchInLib_tf.getWidth() - 160);
        searchInLib_tf.clear();
        criteriaSearchLib.setValue(SearchCriteria.TITLE);

    }
}
