package com.jmc.libsystem;

import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.SuggestionBox.SuggestionBook;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.SQLException;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showLoginWindow();
        try {
            SuggestionBook.initData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
