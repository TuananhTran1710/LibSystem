package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.QueryDatabase.QueryBookRecommend;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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

    public ObservableList<Map<String, String>> dataBook;

    public ResponseController() {
        instance = this;
    }

    public static ResponseController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataBook = FXCollections.observableArrayList();
        ObservableList<String> choices = FXCollections.observableArrayList("In queue", "Accept", "Reject", "All");
        choice_state.setItems(choices);
    }

    public void refreshData() {
        ScrollBar verticalScrollBar = (ScrollBar) list_propose.lookup(".scroll-bar:vertical");
        if (verticalScrollBar != null) {
            verticalScrollBar.setValue(0); // Đưa thanh cuộn về đầu
        }
        choice_state.setValue("All");
        dataBook.clear();
        getDataList(QueryBookRecommend.getAllPropse());
        choice_state.setOnAction(event -> searchChoiceAction());
        UserTable();
        try {
            updateNumber();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void UserTable() {
        list_propose.setCellFactory(param -> new ProposeListCell());
        list_propose.setSelectionModel(null);
        list_propose.setItems(dataBook);
    }

    private void searchChoiceAction() {
        String selectedValue = choice_state.getSelectionModel().getSelectedItem();

        if (selectedValue == "All") {
            dataBook.clear();
            getDataList(QueryBookRecommend.getAllPropse());
        } else {
            ResultSet resultSet = QueryBookRecommend.getProposeForFilter(selectedValue);
            //System.out.println("Can access");
            dataBook.clear();
            getDataList(resultSet);
        }
        list_propose.setItems(dataBook);
    }

    public void getDataList(ResultSet resultSet) {
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

    public void updateNumber() throws SQLException {
        String total = getNumber(QueryBookRecommend.getCountAllPropose());
        total_proposal.setText(total);

        String pd = getNumber(QueryBookRecommend.getNumberofState("In queue"));
        pending.setText(pd);

        String apt = getNumber(QueryBookRecommend.getNumberofState("Accept"));
        accept.setText(apt);

        String rj = getNumber(QueryBookRecommend.getNumberofState("Reject"));
        rejected.setText(rj);
    }

    public String getNumber(ResultSet data) throws SQLException {
        if (data.next()) {
            String total = data.getString("count");
            return total;
        } else {
            System.out.println("You have nothing");
            return "0";
        }
    }
}
