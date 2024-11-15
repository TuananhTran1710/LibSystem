package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.HandleJsonString.SearchBookAPI;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.QueryDatabase.QueryAccountData;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.SuggestionBox.SuggestionBook;
import com.jmc.libsystem.SuggestionBox.SuggestionBookAPI;
import com.jmc.libsystem.Views.SearchCriteria;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.TextFields;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

    public TableColumn<Map<String, Object>, ImageView> ImageCol;
    public TableColumn<Map<String, Object>, String> titleCol;
    public TableColumn<Map<String, Object>, String> authorCol;
    public TableColumn<Map<String, Object>, String> categoriesCol;
    public TableColumn<Map<String, Object>, String> stateCol;
    public TableColumn<Map<String, Object>, Void> actionCol;
    public TableView<Map<String, Object>> tableView;

    public TextField getSearchAddBook_tf() {
        return searchAddBook_tf;
    }

    public TextField getSearchInLib_tf() {
        return searchInLib_tf;
    }

    public static SearchCriteria typeSearchAddBook;
    public static SearchCriteria typeSearchInLib;

    public static List<Book> bookSearch;

    private ObservableList<Map<String, Object>> data;

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

//        criteriaSearchLib.valueProperty().addListener(observable ->
//        {
//            typeSearchInLib = criteriaSearchLib.getValue();
//            if (SuggestionBook.autoCompletionBinding != null) {
//                SuggestionBook.autoCompletionBinding.dispose();
//            }
//            if (typeSearchInLib == SearchCriteria.TITLE) {
//                SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(searchInLib_tf, SuggestionBook.titleSuggest);
//            } else if (typeSearchInLib == SearchCriteria.AUTHORS) {
//                SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(searchInLib_tf, SuggestionBook.authorSuggest);
//            } else {
//                SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(searchInLib_tf, SuggestionBook.categorySuggest);
//            }
//            SuggestionBook.autoCompletionBinding.setPrefWidth(searchInLib_tf.getWidth() - 160);
//        });

        bookSearch = new ArrayList<>();

//        SuggestionBook.autoCompletionBinding = TextFields.bindAutoCompletion(searchInLib_tf, SuggestionBook.titleSuggest);
//        SuggestionBook.autoCompletionBinding.setPrefWidth(searchInLib_tf.getWidth() - 160);

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

//        searchInLib_tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent keyEvent) {
//                switch (keyEvent.getCode()) {
//                    case ENTER -> {
//                        if (searchInLib_tf.isFocused() && !searchInLib_tf.getText().trim().isEmpty()) {
//                            searchBookInLib();
//                            // do something to suitable with table column
//                        }
//                    }
//                }
//            }
//        });

        searchAddBook_btn.setOnAction(event -> searchAddBooks());
        //searchInLib_btn.setOnAction(event -> searchBookInLib());

        data = FXCollections.observableArrayList();
        refreshDataInLib();
    }

    private void searchBookInLib() {

    }

    private void searchAddBooks() {
        String keyWord = searchAddBook_tf.getText();
        keyWord = keyWord.trim();
        if (!keyWord.isEmpty()) {
            String word = keyWord;
            Task<List<Book>> task = new Task<List<Book>>() {

                @Override
                protected List<Book> call() throws Exception {
                    Long start = System.currentTimeMillis();
                    bookSearch = SearchBookAPI.getListBookFromJson(word, typeSearchAddBook.toString());
                    Thread.sleep(100);
                    Long end = System.currentTimeMillis();
                    System.out.println(end - start);
                    return bookSearch;
                }
            };

            // tu day la chay tren luong chinh
            task.setOnSucceeded(e -> {
                List<Book> list = task.getValue();
                Platform.runLater(() -> ShowListBookFound.show(list, BookAPI_hb));
            });
            new Thread(task).start();
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

    void refreshDataInLib(){
        data.clear();
        getData(QueryBookData.getAllBook());
        addButton();
        BookTable();
    }

    private void addButton(){
        searchInLib_btn.setOnAction(event -> searchAction());

//        search_tf.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                searchAction();
//            }
//        });

        searchInLib_tf.setOnKeyReleased(event -> searchAction());
    }

    private void searchAction(){
        SearchCriteria selectedValue = criteriaSearchLib.getValue();
        String type = (selectedValue == SearchCriteria.TITLE ? "title" : (selectedValue == SearchCriteria.AUTHORS ? "authors" : "category"));
        //System.out.println(type);

        String text = searchInLib_tf.getText();
        ResultSet resultSet = QueryBookData.getBookForSearch(text, type);
        //System.out.println("Can access");
        data.clear();
        getData(resultSet);
    }

    private void BookTable(){
        data = FXCollections.observableArrayList();
        titleCol.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("title")));
        authorCol.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("authors")));
        categoriesCol.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("category")));
        stateCol.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("state")));

        ImageCol.setCellValueFactory(data -> {
            Image image = (Image) data.getValue().get("image");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(50); // Đặt chiều cao hình ảnh
            imageView.setFitWidth(50);  // Đặt chiều rộng hình ảnh
            return new SimpleObjectProperty<>(imageView);
        });

        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Hyperlink editLink = new Hyperlink("Edit");

            {
                editLink.setOnAction(event -> {
                    Map<String, Object> rowData = getTableView().getItems().get(getIndex());
                    // Thực hiện hành động chỉnh sửa với dữ liệu hàng tương ứng
                    System.out.println("Edit: " + rowData.get("id"));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editLink);
                }
            }
        });

        tableView.setItems(data);
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

    private void getData(ResultSet resultSet) {
        data.clear();
        // Tạo một Task để load
        Task<Void> loadDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    while (resultSet.next()) {
                        Map<String, Object> row = new HashMap<>();
                        String title = resultSet.getString("title");
                        String authors = resultSet.getString("authors");
                        String category = resultSet.getString("category");
                        String state = resultSet.getString("state");
                        String id = resultSet.getString("google_book_id");

                        Blob thumbnailBlob = resultSet.getBlob("thumbnail"); // Get image as Blob
                        byte[] thumbnailImage = thumbnailBlob != null ? thumbnailBlob.getBytes(1, (int) thumbnailBlob.length()) : null;
                        Image bookCoverImage;
                        if (thumbnailImage != null) {
                            // Create Image from byte array
                            bookCoverImage = new Image(new ByteArrayInputStream(thumbnailImage));
                        } else {
                            bookCoverImage = ShowListBookFound.DEFAULT_BOOK_COVER;
                        }

                        row.put("title", title);
                        row.put("image", bookCoverImage);
                        row.put("authors", authors);
                        row.put("category", category);
                        row.put("state", state);
                        row.put("id", id);

                        // Thêm dữ liệu vào ObservableList trên JavaFX Application Thread
                        Platform.runLater(() -> data.add(row));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        // Khởi chạy Task trong background
        new Thread(loadDataTask).start();
    }
}