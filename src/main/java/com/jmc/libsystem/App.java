package com.jmc.libsystem;

import com.jmc.libsystem.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showLoginWindow();
//        Model.getInstance().getViewFactory().showCmtWindow();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
