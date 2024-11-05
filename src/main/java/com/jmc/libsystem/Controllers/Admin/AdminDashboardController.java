package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.QueryDatabase.QueryAccountData;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.concurrent.Task;
import javafx.application.Platform;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    private static AdminDashboardController instance;
    /* bảng thống kê */
    public TableView<Map<String, Object>> listBook;
    public TableColumn<Map<String, Object>, ImageView> imageColumn;
    public TableColumn<Map<String, Object>, String> titleColumn;
    public TableColumn<Map<String, Object>, String> authorsColumn;
    public TableColumn<Map<String, Object>, Integer> quantityColumn;
    public TableColumn<Map<String, Object>, Integer> loanedColumn;
    public TableColumn<Map<String, Object>, String> statusColumn;

    /* phần 4 ô thống kê ở trên cùng */
    public Label numberUser;
    public Label numberBook;
    public Label numberBookLoan;
    public Label numberOverBook;

    /* thanh số lượng sách theo thể loại */
    public Label category1;
    public Label categoryNumber1;
    public ProgressBar progress1;

    public Label category2;
    public Label categoryNumber2;
    public ProgressBar progress2;

    public Label category3;
    public Label categoryNumber3;
    public ProgressBar progress3;
    private final int totalQuantity = 10;

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

        imageColumn.setCellValueFactory(data -> {
            Image image = (Image) data.getValue().get("image");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(50); // Đặt chiều cao hình ảnh
            imageView.setFitWidth(50);  // Đặt chiều rộng hình ảnh
            return new SimpleObjectProperty<>(imageView);
        });

        ObservableList<Map<String, Object>> data = getBooks();
        listBook.setItems(data);
        addMouseClickedToTable();
        try {
            getNumberData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        showSortCategory();
        //add();
    }

    private void add() {
        statusColumn.setCellFactory(tc -> {
            TableCell<Map<String, Object>, String> cell = new TableCell<>();
            cell.getStyleClass().add("status-cell");
            System.out.println("can use css");
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
        // ObservableList để giữ dữ liệu cho UI
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

        // Tạo một Task để load ảnh trong background
        Task<Void> loadDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ResultSet resultSet = QueryBookData.getBookStatistic();
                try {
                    while (resultSet.next()) {
                        Map<String, Object> row = new HashMap<>();
                        String title = resultSet.getString("title");
                        String imageUrl = resultSet.getString("thumbnail_url");
                        String authors = resultSet.getString("authors");
                        int quantity = resultSet.getInt("total_books");
                        int loaned = resultSet.getInt("total_borrowed_books");
                        String status = resultSet.getString("status");

                        // Tải ảnh trong background
                        Image image = new Image(imageUrl, true);

                        row.put("title", title);
                        row.put("image", image);
                        row.put("authors", authors);
                        row.put("quantity", quantity);
                        row.put("loaned", loaned);
                        row.put("status", status);

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

        return data;
    }


    private void getNumberData() throws SQLException {

        ResultSet Users = QueryAccountData.getCountUser();
        int numberUsers = getNumber(Users);
        numberUser.setText(Integer.toString(numberUsers));

        ResultSet Books = QueryBookData.getCountBook();
        int numberBooks = getNumber(Books);
        numberBook.setText(Integer.toString(numberBooks));

        ResultSet BookLoan = QueryBookData.getCountBookLoan();
        int numberBookLoans = getNumber(BookLoan);
        numberBookLoan.setText(Integer.toString(numberBookLoans));
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

    private void showSortCategory() {
        ResultSet resultSet = QueryBookData.getCategorySort3();
        try {
            int id = 1;
            while (resultSet.next()){
                String category = resultSet.getString("category");
                int total = resultSet.getInt("total_books");
                double average = (double)total / totalQuantity;
                if(id == 1){
                    category1.setText(category);
                    categoryNumber1.setText(Integer.toString(total));
                    progress1.setProgress(average);
                } else if (id == 2) {
                    category2.setText(category);
                    categoryNumber2.setText(Integer.toString(total));
                    progress2.setProgress(average);
                }
                else {
                    category3.setText(category);
                    categoryNumber3.setText(Integer.toString(total));
                    progress3.setProgress(average);
                }
                id ++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
