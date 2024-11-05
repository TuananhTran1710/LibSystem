package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.QueryDatabase.QueryAccountData;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    private static AdminDashboardController instance;

    public TableView<Map<String, Object>> listBook;
    public TableColumn<Map<String, Object>, ImageView> imageColumn;
    public TableColumn<Map<String, Object>, String> titleColumn;
    public TableColumn<Map<String, Object>, String> authorsColumn;
    public TableColumn<Map<String, Object>, Integer> quantityColumn;
    public TableColumn<Map<String, Object>, Integer> loanedColumn;
    public TableColumn<Map<String, Object>, String> statusColumn;

    public Label numberUser;
    public Label numberBook;
    public Label numberBookLoan;
    public Label numberOverBook;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshData();
    }

    public AdminDashboardController() {
        instance = this;
    }

    public static AdminDashboardController getInstance() {
        return instance;
    }

    private void refreshData() {
        /* liên kết các cột */
        //imageColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("image")));
        titleColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("title")));
        authorsColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("authors")));
        quantityColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((Integer) data.getValue().get("quantity")));
        loanedColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((Integer) data.getValue().get("loaned")));
        statusColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("status")));

//        imageColumn.setCellValueFactory(data -> {
//            Image image = (Image) data.getValue().get("image"); // Lấy URL từ Map
//            ImageView imageView = new ImageView(image);
//            imageView.setFitHeight(50); // Đặt chiều cao hình ảnh
//            imageView.setFitWidth(50);  // Đặt chiều rộng hình ảnh
//            return new SimpleObjectProperty<>(imageView);
//        });

        ObservableList<Map<String, Object>> data = getBooks();
        listBook.setItems(data);
        addMouseClickedToTable();
        try {
            getNumberData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //add();
    }

    private void add() {
        statusColumn.setCellFactory(tc -> {
            TableCell<Map<String, Object>, String> cell = new TableCell<>();
            cell.getStyleClass().add("status-cell");

            cell.textProperty().addListener((obs, oldVal, newVal) -> {
                if ("Available".equals(newVal)) {
                    cell.getStyleClass().add("in-stock");
                    cell.getStyleClass().remove("out-of-stock");
                } else if ("Over".equals(newVal)) {
                    cell.getStyleClass().add("out-of-stock");
                    cell.getStyleClass().remove("in-stock");
                }
            });

            return cell;
        });
    }

    private void addMouseClickedToTable() {
        listBook.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Click đúp
                Map<String, Object> selected = listBook.getSelectionModel().getSelectedItem();
                if (selected != null) {

                    // Xử lý khi một hàng được chọn
                    System.out.println("Bạn đã chọn: " + selected.get("title"));
                    // Mở cửa sổ mới, cập nhật dữ liệu, ...
                }
            }
        });
    }

    // lấy dữ liệu từ database
    public static ObservableList<Map<String, Object>> getBooks() {
        ResultSet resultSet = QueryBookData.getBookStatistic();
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                String title = resultSet.getString("title");
                String imageUrl = resultSet.getString("thumbnail_url");
                String authors = resultSet.getString("authors");
                int quantity = resultSet.getInt("total_books");
                int loaned = resultSet.getInt("total_borrowing");

                //Image image = new Image(imageUrl);

                row.put("title", title);
                row.put("image", imageUrl);
                row.put("authors", authors);
                row.put("quantity", quantity);
                row.put("loaned", loaned);

                if (quantity > 0) row.put("status", "Available");
                else row.put("status", "Over");

                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void getNumberData() throws SQLException {

        ResultSet Users = QueryAccountData.getCountUser();
        int numberUsers = getNumber(Users);
        numberUser.setText(Integer.toString(numberUsers));

        ResultSet Books = QueryBookData.getCountBook();
        int numberBooks = getNumber(Books);
        numberBook.setText(Integer.toString(numberBooks));
    }

    private int getNumber(ResultSet data) throws SQLException {
        if (data.next()) {
            int total = data.getInt("count");
            return total;
        } else {
            System.out.println("You have nothing");
            return 0;
        }
    }
}
