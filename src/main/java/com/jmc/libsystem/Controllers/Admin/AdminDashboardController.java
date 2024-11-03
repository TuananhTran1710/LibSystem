package com.jmc.libsystem.Controllers.Admin;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    private static AdminDashboardController instance;
//    @FXML
//    private Label numberUser;
//    @FXML
//    private Label numberBook;
//    @FXML
//    private TableView<Map<String, Object>> listBook;
//    @FXML
//    private TableColumn<Map<String, Object>, String> imageColumn;
//    @FXML
//    private TableColumn<Map<String, Object>, String> titleColumn;
//    @FXML
//    private TableColumn<Map<String, Object>, String> authorsColumn;
//    @FXML
//    private TableColumn<Map<String, Object>, Integer> quantityColumn;
//    @FXML
//    private TableColumn<Map<String, Object>, Integer> loanedColumn;
//    @FXML
//    private TableColumn<Map<String, Object>, Void> editColumn;

    //
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        refreshData();
    }
//
//    public AdminDashboardController() {
//        instance = this;
//    }
//
//    public static AdminDashboardController getInstance() {
//        return instance;
//    }
//
//    private void refreshData() {
//        /* liên kết các cột */
//        imageColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("image")));
//        titleColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("title")));
//        authorsColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("authors")));
//        quantityColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((Integer) data.getValue().get("quantity")));
//        loanedColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((Integer) data.getValue().get("loaned")));
//
//        ObservableList<Map<String, Object>> data = getBooks();
//        listBook.setItems(data);
//        addButtonToTable();
//        addImageToTable();
//    }
//
//    private void addButtonToTable() {
//        editColumn.setCellFactory(param -> new TableCell<>() {
//            private final Button actionButton = new Button("edit");
//
//            {
//                // Xử lý sự kiện khi nút bấm được nhấn
//                actionButton.setOnAction(event -> {
//                    Map<String, Object> item = getTableView().getItems().get(getIndex());
//                    System.out.println("Button clicked for " + item.get("name"));
//                    // Thực hiện hành động tùy chỉnh ở đây
//                });
//            }
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    setGraphic(actionButton);
//                }
//            }
//        });
//    }
//
//    private void addImageToTable() {
//        imageColumn.setCellFactory(param -> new TableCell<>() {
//            private final ImageView imageView = new ImageView();
//
//            @Override
//            protected void updateItem(String imagePath, boolean empty) {
//                super.updateItem(imagePath, empty);
//                if (empty || imagePath == null) {
//                    setGraphic(null);
//                } else {
//                    Image image = new Image(imagePath);
//                    imageView.setImage(image);
//                    imageView.setFitWidth(50);
//                    imageView.setFitHeight(50);
//                    imageView.setPreserveRatio(false);
//                    setGraphic(imageView);
//                }
//            }
//        });
//    }
//
//    // lấy dữ liệu từ database
//    public static ObservableList<Map<String, Object>> getBooks() {
//        ResultSet resultSet = QueryBookData.getBookStatistic();
//        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();
//
//        try {
//            while (resultSet.next()) {
//                Map<String, Object> row = new HashMap<>();
//                row.put("image", resultSet.getInt("thumbnail_url"));
//                row.put("title", resultSet.getString("title"));
//                row.put("quantity", resultSet.getString("total_books"));
//                row.put("loaned", resultSet.getString("total_borrowed_books"));
//                data.add(row);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return data;
//    }

}
