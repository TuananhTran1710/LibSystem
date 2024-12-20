package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.Controllers.Account.AccountProfileController;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryAccountData;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
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

    public HashMap<String, User> userList = new HashMap<>();

    public ObservableList<Map<String, Object>> data;

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
        ScrollBar verticalScrollBar = (ScrollBar) userTable.lookup(".scroll-bar:vertical");
        if (verticalScrollBar != null) {
            verticalScrollBar.setValue(0); // Đưa thanh cuộn về đầu
        }
        data.clear();
        getData(QueryAccountData.getAllAccount());
        criteriaBox.setValue("ID");
        addButton();
        UserTable();
    }

    private void addButton() {
        addUserButton.setOnAction(event -> {
            String userId = userIdField.getText();
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            if (password.trim().isEmpty()) password = userId;
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

        search_tf.setOnKeyReleased(event -> searchAction());
    }

    public void searchAction() {
        String selectedValue = criteriaBox.getSelectionModel().getSelectedItem();
        String type = (selectedValue.equals("ID") ? "user_id" : (selectedValue.equals("Name") ? "fullName" : "email"));

        String text = search_tf.getText();
        ResultSet resultSet = QueryAccountData.getAccountForSearch(text, type);
        data.clear();
        getData(resultSet);
    }

    private void UserTable() {
        userIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("id")));
        fullNameColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("name")));
        emailColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("email")));
        stateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get("state")));

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Hyperlink editLink = new Hyperlink("Edit");

            {
                editLink.setOnAction(event -> {
                    Map<String, Object> rowData = getTableView().getItems().get(getIndex());
                    // Thực hiện hành động chỉnh sửa với dữ liệu hàng tương ứng
                    String user_id = (String) rowData.get("id");
                    if (!userList.containsKey(user_id)) {
                        ResultSet resultSet = QueryAccountData.getAccount(user_id);
                        try {
                            resultSet.next();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        User user = User.createUserFromResultSet(resultSet);
                        userList.put(user_id, user);
                    }
                    User user = userList.get(user_id);
                    AdminController.getInstance().admin_parent.setCenter(Model.getInstance().getViewFactory().getAccountProfile());
                    AccountProfileController.getInstance().setCurrent_user(user);
                    AccountProfileController.getInstance().showProfile(user);
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

    public void getData(ResultSet resultSet) {
        data.clear();
        try {
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                String id = resultSet.getString("user_id");
                String name = resultSet.getString("fullName");
                String email = resultSet.getString("email");
                String state = resultSet.getString("state");

                row.put("id", id);
                row.put("name", name);
                row.put("email", email);
                row.put("state", state);

                // Thêm dữ liệu vào ObservableList
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
