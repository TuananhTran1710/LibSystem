package com.jmc.libsystem.Views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

// class nayf lam nhiem vu chuyen doi giua cac cua so
public class ViewFactory {
    //Users View
    private AnchorPane dashboardView;
    private final ObjectProperty<UserMenuOptions> userSelectedMenuItem;

    public ViewFactory() {
        userSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public ObjectProperty<UserMenuOptions> getUserSelectedMenuItem() {
        return userSelectedMenuItem;
    }

    //--------------------------------ANCHOR USERS----------------------
    public AnchorPane getDashboardView() {
        // anchorPane giong nhu la mot loai Parent
        // tuong duong voi moi khi ta se khai bao Parent root
        if (dashboardView == null) {
            try {
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/User/Dashboard.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dashboardView;
    }

    // -----------------------------------Function Show Window------------------------------------
    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void showSignUpWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Sign-up.fxml"));
        createStage(loader);
    }

    public void showForgetPasswordWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Forget-password.fxml"));
        createStage(loader);
    }

    public void showUserWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/User/User.fxml"));
        createStage(loader);
    }

    public void showAdminWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
        createStage(loader);
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/library_icon.png"))));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Library");
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

}
