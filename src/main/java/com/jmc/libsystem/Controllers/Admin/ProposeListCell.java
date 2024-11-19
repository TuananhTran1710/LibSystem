package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.QueryDatabase.QueryBookrcm;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Map;

public class ProposeListCell extends ListCell<Map<String, String>> {

    // phần dành cho book cell
    public Label title;
    public Label authors;
    public Button accept_bt;
    public Button reject_bt;
    private AnchorPane root;

    public ProposeListCell() {
        if(root == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ProposeCell.fxml"));
                loader.setController(this); // Liên kết controller
                //System.out.println(getClass().getResource("/Fxml/Admin/ProposeCell.fxml"));
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateItem(Map<String, String> book, boolean empty) {
        super.updateItem(book, empty);

        if (empty || book == null) {
            setGraphic(null);
        } else {
            //System.out.println(book.get("id"));
            // Gán dữ liệu vào các thành phần trong cell
            title.setText(book.get("title"));
            authors.setText(book.get("authors"));

            String status = book.get("state");
            switch (status) {
                case "In queue" -> {
                    accept_bt.setVisible(true);
                    reject_bt.setVisible(true);
                }
                case "Accept" -> {
                    System.out.println(book.get("id") + status);
                    reject_bt.setVisible(false);
                    accept_bt.setDisable(true);
                    AnchorPane.setRightAnchor(accept_bt, 30.0);
                }
                case "Reject" -> {
                    System.out.println(book.get("id") + status);
                    accept_bt.setVisible(false);
                    reject_bt.setDisable(true);
                }
            }

            // Gán các hành động khi bấm nút
            accept_bt.setOnAction(event -> {
                System.out.println("Accepted: " + book.get("title"));
                QueryBookrcm.updateStatePropose(book.get("id"), "Accept");
                ResponseController.getInstance().refreshData();
            });

            reject_bt.setOnAction(event -> {
                System.out.println("Rejected: " + book.get("title"));
                QueryBookrcm.updateStatePropose(book.get("id"), "Reject");
                ResponseController.getInstance().refreshData();
            });

            // Hiển thị giao diện
            setGraphic(root);
        }
    }
}
