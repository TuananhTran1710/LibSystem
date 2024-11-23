package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.QueryDatabase.QueryBookrcm;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class ProposeListCell extends ListCell<Map<String, String>> {

    // phần dành cho book cell
    public Label title;
    public Label authors;
    public Button accept_bt;
    public Button reject_bt;
    public TextField number_tf;
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
            // Reset trạng thái
            title.setText("");
            authors.setText("");
            number_tf.setVisible(false);
            accept_bt.setVisible(false);
            reject_bt.setVisible(false);
            accept_bt.setDisable(false);
            reject_bt.setDisable(false);

            // Gán dữ liệu vào các thành phần trong cell
            title.setText(book.get("title"));
            authors.setText(book.get("authors"));
            number_tf.setVisible(false);

            String status = book.get("state");
            String book_id = book.get("id");
            switch (status) {
                case "In queue" -> {
                    showInQueue();
                }
                case "Accept" -> {
                    //System.out.println(book.get("id") + status);
                    showAccept();
                }
                case "Reject" -> {
                    //System.out.println(book.get("id") + status);
                    showReject();
                }
            }

            // Gán các hành động khi bấm nút
            accept_bt.setOnAction(event -> {
                //System.out.println("Accepted: " + book.get("title"));
                accept_bt.setVisible(false);
                accept_bt.setDisable(true);
                reject_bt.setVisible(false);
                reject_bt.setDisable(true);
                number_tf.setVisible(true);
                number_tf.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String text = number_tf.getText();
                        System.out.println("Bạn đã nhập: " + text);
                        QueryBookData.updateBook(book_id, Integer.parseInt(text));
                        number_tf.setVisible(false);
                        QueryBookrcm.updateStatePropose(book_id, "Accept");
                        QueryBookData.updateState(book_id, "publishing");
                        showAccept();
                    }
                });

                try {
                    ResponseController.getInstance().updateNumber();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            reject_bt.setOnAction(event -> {
                //System.out.println("Rejected: " + book_id);
                QueryBookrcm.updateStatePropose(book_id, "Reject");
                QueryBookData.updateState(book_id, "Reject");
                showReject();
                try {
                    ResponseController.getInstance().updateNumber();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            setGraphic(root);
        }
    }

    private void showAccept() {
        reject_bt.setVisible(false);
        accept_bt.setVisible(true);
        accept_bt.setDisable(true);
        AnchorPane.setRightAnchor(accept_bt, 30.0);
    }

    private void showReject() {
        accept_bt.setVisible(false);
        reject_bt.setVisible(true);
        reject_bt.setDisable(true);
    }

    private void showInQueue() {
        accept_bt.setVisible(true);
        accept_bt.setDisable(false);
        reject_bt.setVisible(true);
        reject_bt.setDisable(false);
    }
}
