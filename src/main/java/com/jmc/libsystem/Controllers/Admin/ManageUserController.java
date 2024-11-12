package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.HandleResultSet.EvaluateInfo;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryAccountData;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ManageUserController implements Initializable {

    public static ManageUserController instance;

    public TableView<Map<String, Object>> userTable;
    public TableColumn<Map<String, Object>, String> userIdColumn;
    public TableColumn<Map<String, Object>, String> fullNameColumn;
    public TableColumn<Map<String, Object>, String> emailColumn;
    public TableColumn<Map<String, Object>, String> passwordColumn;
    public TableColumn<Map<String, Object>, String> stateColumn;
    public TableColumn<Map<String, Object>, Void> actionColumn;

    public Button search_btn;
    public TextField search_tf;
    public ChoiceBox<String> criteriaBox;

    public TextField userIdField;
    public TextField fullNameField;
    public TextField emailField;
    public PasswordField passwordField;
    public Button addUserButton;


    private ObservableList<Map<String, Object>> data;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        criteriaBox.getItems().addAll("ID", "Name", "Email");
        data = FXCollections.observableArrayList();
        refreshData();
    }

    public ManageUserController() {
        instance = this;
    }

    public static ManageUserController getInstance() {
        return instance;
    }

    public void refreshData() {
        data.clear();
        getData(QueryAccountData.getAllAccount());
        criteriaBox.setValue("ID");
        addButton();
        UserTable();
    }

    private void addButton(){
        addUserButton.setOnAction(event -> {
            String userId = userIdField.getText();
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            if(password.trim().isEmpty()) password = userId;
            //System.out.println(userId + " " + fullName + " " + email + " " + password);
            if (!email.trim().isEmpty() && !password.trim().isEmpty()
                    && !userId.trim().isEmpty() && !fullName.trim().isEmpty()) {
                QueryAccountData.insertAccount(email, password, userId, fullName);
                System.out.println("account is active");
            } else {
                System.out.println("Please fill in all information!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in all information!");
                alert.show();
            }

            userIdField.setText("");
            fullNameField.setText("");
            emailField.setText("");
            passwordField.setText("");

            refreshData();
        });

        search_btn.setOnAction(event -> searchAction());

        search_tf.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchAction();
            }
        });

        search_tf.setOnKeyReleased(event -> searchAction());
    }

    private void searchAction(){
        String selectedValue = criteriaBox.getSelectionModel().getSelectedItem();
        String type = (selectedValue == "ID" ? "user_id" : (selectedValue == "Name" ? "fullName" : "email"));
        //System.out.println(type);

        String text = search_tf.getText();
        ResultSet resultSet = QueryAccountData.getAccountForSearch(text, type);
        //System.out.println("Can access");
        data.clear();
        getData(resultSet);
    }

    private void UserTable(){
        userIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("id")));
        fullNameColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("name")));
        emailColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("email")));
        passwordColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("password")));
        stateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("state")));

        actionColumn.setCellFactory(param -> new TableCell<>() {
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

        userTable.setItems(data);
    }

    // lấy dữ liệu từ database
    private void getData(ResultSet resultSet) {

        // Tạo một Task để load
        Task<Void> loadDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    while (resultSet.next()) {
                        Map<String, Object> row = new HashMap<>();
                        String id = resultSet.getString("user_id");
                        String name = resultSet.getString("fullName");
                        String email = resultSet.getString("email");
                        String password = resultSet.getString("password");
                        String state = resultSet.getString("state");

                        row.put("id", id);
                        row.put("name", name);
                        row.put("email", email);
                        row.put("password", password);
                        row.put("state", state);

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
