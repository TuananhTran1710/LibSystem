package com.jmc.libsystem.Controllers.Book;

import com.jmc.libsystem.Controllers.Admin.AdminController;
import com.jmc.libsystem.Controllers.Admin.ManageBookController;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.SuggestionBox.SuggestionBook;
import com.jmc.libsystem.Views.SearchCriteria;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class BookDetailAPI extends BaseBookDetailController {

    public Button addBook_btn;
    public VBox authorsContainer;
    public VBox categoriesContainer;

    private static BookDetailAPI instance;
    public TextField quantity_tf;
    public Button subtract_btn;
    public Button plus_btn;
    public Button update_btn;
    public Button delete_btn;
    public ToggleButton overview_btn;

    public static BookDetailAPI getInstance() {
        return instance;
    }

    public BookDetailAPI() {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        quantity_tf.setText("1");
        quantity_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            // Kiểm tra nếu giá trị không phải là số hoặc là số âm
            try {
                int quantity = Integer.parseInt(newValue);
                if (quantity <= 0) {
                    // Nếu là số âm hoặc bằng 0, đặt lại giá trị về giá trị cũ
                    quantity_tf.setText(oldValue);
                }
            } catch (NumberFormatException e) {
                // Nếu không phải là số, đặt lại giá trị về giá trị cũ
                quantity_tf.setText(oldValue);
            }
        });

        overview_btn.setOnAction(event -> {
            overview_btn.setSelected(true);
        });

        addBook_btn.setOnAction(event -> {
            int num = Integer.parseInt(quantity_tf.getText());
            QueryBookData.addBook(book, num);
            book.setQuantity(num);
            toUpdateButton();

            notice_lbl.setText("Add book successfully");
            notice_lbl.setTextFill(Color.web("#32CD32"));
            createTimeLine();

            // add data into suggestion box
            Set<String> typeSuggest;
            if (ManageBookController.typeSearchAddBook == SearchCriteria.TITLE) {
                typeSuggest = SuggestionBook.titleSuggest;
            } else if (ManageBookController.typeSearchAddBook == SearchCriteria.CATEGORY) {
                typeSuggest = SuggestionBook.categorySuggest;
            } else {
                typeSuggest = SuggestionBook.authorSuggest;
            }
            String newWord = ManageBookController.getInstance().getSearchAddBook_tf().getText();
            typeSuggest.add(newWord);
        });

        update_btn.setOnAction(event -> {
            int num = Integer.parseInt(quantity_tf.getText());
            if (num != book.getQuantity()) {
                QueryBookData.updateBook(book.getId(), num);
                notice_lbl.setText("Update book successfully");
                notice_lbl.setTextFill(Color.web("#32CD32"));
            } else {
                notice_lbl.setText("Have no change");
                notice_lbl.setTextFill(Color.RED);
            }
            createTimeLine();
        });

        delete_btn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete book");
            alert.setContentText("Are you sure you want to delete this book?");

            // Hiển thị hộp thoại và chờ người dùng chọn
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Nếu người dùng chọn OK, tiến hành xóa sách
                QueryBookData.deleteBook(book.getId());
                quantity_tf.setText("1");
                toAddButton();

                notice_lbl.setText("Delete book successfully");
                notice_lbl.setTextFill(Color.web("#32CD32"));

                createTimeLine();
            } else {
                // Nếu người dùng chọn Cancel, không làm gì cả
                notice_lbl.setText("Aborted deletion");
                notice_lbl.setTextFill(Color.web("#FF0000"));

                createTimeLine();
            }
        });


        plus_btn.setOnAction(event -> {
            int num = Integer.parseInt(quantity_tf.getText());
            num += 1;
            quantity_tf.setText("" + num);
        });

        subtract_btn.setOnAction(event -> {
            int num = Integer.parseInt(quantity_tf.getText());
            num -= 1;
            quantity_tf.setText("" + num);
        });
    }

    @Override
    public void moveMenuCurrent() {
        AdminController.getInstance().admin_parent.setCenter(Model.getInstance().getViewFactory().getManageBookView());
    }


    public void setUpInfo(Book book) {
        super.setUpInfo(book);
        notice_lbl.setText("");
    }

    public void toUpdateButton() {
        addBook_btn.setDisable(true);
        addBook_btn.setVisible(false);

        update_btn.setVisible(true);
        update_btn.setDisable(false);

        delete_btn.setVisible(true);
        delete_btn.setDisable(false);
    }

    public void toAddButton() {
        addBook_btn.setDisable(false);
        addBook_btn.setVisible(true);

        update_btn.setVisible(false);
        update_btn.setDisable(true);

        delete_btn.setVisible(false);
        delete_btn.setDisable(true);
    }

    @Override
    public void modifyButton() {
        if (QueryBookData.isExist(book.getId())) {
            quantity_tf.setText("" + book.getQuantity());
            toUpdateButton();
        } else {
            quantity_tf.setText("1");
            toAddButton();
        }
    }

}
