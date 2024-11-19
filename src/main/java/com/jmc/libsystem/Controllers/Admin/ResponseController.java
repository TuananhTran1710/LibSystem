package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.Controllers.Account.AccountProfileController;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryAccountData;
import com.jmc.libsystem.QueryDatabase.QueryBookrcm;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ResponseController implements Initializable {
    public static ResponseController instance;

    public Label total_proposal;
    public Label pending;
    public Label accept;
    public Label rejected;
    public ChoiceBox<String> choice_state;
    public ListView<Map<String, String>> list_propose;

    private ObservableList<Map<String, String>> dataBook;

    public ResponseController() {
        instance = this;
    }

    public static ResponseController getInstance(){
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataBook = FXCollections.observableArrayList();
        ObservableList<String> choices = FXCollections.observableArrayList("In queue", "Accept", "Reject");
        choice_state.setItems(choices);
        refreshData();
    }

    public void refreshData() {
        dataBook.clear();
        getData(QueryBookrcm.getAllPropse());
        choice_state.setOnAction(event -> searchAction());
        UserTable();
    }

    private void UserTable(){
        list_propose.setCellFactory(param -> new ProposeListCell());
        list_propose.setItems(dataBook);
    }

    private void searchAction(){
        String selectedValue = choice_state.getSelectionModel().getSelectedItem();

        ResultSet resultSet = QueryBookrcm.getProposeForFilter(selectedValue);
        //System.out.println("Can access");
        dataBook.clear();
        getData(resultSet);
        list_propose.setItems(dataBook);
    }

    private void getData(ResultSet resultSet) {
        dataBook.clear();
        // Tạo một Task để load
        Task<Void> loadDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    while (resultSet.next()) {
                        Map<String, String> row = new HashMap<>();
                        String id = resultSet.getString("google_book_id");
                        String title = resultSet.getString("title");
                        String authors = resultSet.getString("authors");
                        String state = resultSet.getString("state");

                        row.put("id", id);
                        row.put("title", title);
                        row.put("authors", authors);
                        row.put("state", state);

                        // Thêm dữ liệu vào ObservableList trên JavaFX Application Thread
                        Platform.runLater(() -> dataBook.add(row));
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
