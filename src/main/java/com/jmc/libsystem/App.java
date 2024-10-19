package com.jmc.libsystem;

import com.jmc.libsystem.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


//
//
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import org.controlsfx.control.CheckComboBox;
//
//public class App extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        // Tạo danh sách các môn học
//        ObservableList<String> subjects = FXCollections.observableArrayList(
//                "BTVN - kèm Toán Văn Anh", "Hóa Học", "KHTN", "Môn Toán", "Môn Văn", "Sinh học"
//        );
//
//        // Tạo CheckComboBox
//        CheckComboBox<String> checkComboBox = new CheckComboBox<>(subjects);
//        checkComboBox.setTitle("Đã chọn môn học");
//
//        // Nút Lọc
//        Button filterButton = new Button("Lọc");
//
//        // Bố cục
//        HBox filterBox = new HBox(10, checkComboBox, filterButton);
//        VBox root = new VBox(15, filterBox);
//
//        // Tạo scene
//        Scene scene = new Scene(root, 400, 200);
//        primaryStage.setTitle("Bộ lọc môn học");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
