package com.jmc.libsystem.Controllers.Admin;

import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.QueryDatabase.QueryBookrcm;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class ProposeListCell extends ListCell<Map<String, String>> {

    // phần dành cho book cell
    public Label title;
    public Label authors;
    public Button accept_bt;
    public Button reject_bt;
    public TextField number_tf;
    private AnchorPane root;

    public ProposeListCell() {
        if (root == null) {
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
            accept_bt.setOnAction(event -> onAccept(book_id));

            reject_bt.setOnAction(event -> onReject(book_id));

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
        AnchorPane.setRightAnchor(accept_bt, 100.0);
    }

    private void onAccept(String book_id) {
        //System.out.println("Accepted: " + book.get("title"));
        accept_bt.setVisible(false);
        accept_bt.setDisable(true);
        reject_bt.setVisible(false);
        reject_bt.setDisable(true);
        number_tf.setVisible(true);
        number_tf.setText("1");
        number_tf.setAlignment(Pos.CENTER);
        number_tf.setStyle("-fx-font-size: 15px;");
        number_tf.setPrefWidth(80);
        number_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            // Kiểm tra nếu giá trị không phải là số hoặc là số âm
            try {
                int quantity = Integer.parseInt(newValue);
                if (quantity <= 0) {
                    // Nếu là số âm hoặc bằng 0, đặt lại giá trị về giá trị cũ
                    number_tf.setText(oldValue);
                }
            } catch (NumberFormatException e) {
                // Nếu không phải là số, đặt lại giá trị về giá trị cũ
                number_tf.setText(oldValue);
            }
        });
        number_tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ENTER -> {
                        String text = number_tf.getText().trim(); // Loại bỏ khoảng trắng thừa
                        if (text.isEmpty()) {
                            if (confirmBack()) {
                                showInQueue();
                                number_tf.setVisible(false);
                            } else {
                                number_tf.requestFocus(); // Đặt lại focus nếu người dùng không muốn quay lại
                            }
                        } else {
                            try {
                                int number = Integer.parseInt(text);
                                QueryBookData.updateBook(book_id, number);
                                QueryBookrcm.updateStatePropose(book_id, "Accept");
                                QueryBookData.updateState(book_id, "publishing");
                                showAccept();
                                ResponseController.getInstance().updateNumber();
                            } catch (NumberFormatException e) {
                                System.err.println("Input is not a valid number: " + e.getMessage());
                                number_tf.requestFocus(); // Nhắc nhở người dùng nhập số hợp lệ
                            } catch (SQLException e) {
                                throw new RuntimeException("Database update failed", e);
                            } finally {
                                number_tf.setVisible(false);
                            }
                        }
                    }
                }
            }
        });

    }

    private void onReject(String book_id) {
        //System.out.println("Rejected: " + book_id);
        QueryBookrcm.updateStatePropose(book_id, "Reject");
        showReject();
        try {
            ResponseController.getInstance().updateNumber();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean confirmBack() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Back");
        alert.setHeaderText("You have not added quantity");
        alert.setContentText("Are you sure you want to come back?");

        ButtonType buttonTypeBack = new ButtonType("Back");
        ButtonType buttonTypeContinue = new ButtonType("Continue");

        // Thêm các ButtonType vào Alert
        alert.getButtonTypes().setAll(buttonTypeBack, buttonTypeContinue);

        // Hiển thị Alert và chờ người dùng chọn
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeBack) {
            return true;
        } else {
            return false;
        }
    }
}
