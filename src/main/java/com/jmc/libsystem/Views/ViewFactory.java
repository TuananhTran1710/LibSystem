package com.jmc.libsystem.Views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

// class nayf lam nhiem vu chuyen doi giua cac cua so
public class ViewFactory {
    //Users View
    private AnchorPane dashboardView;
    private AnchorPane myBookView;
    private AnchorPane searchView;
    private AnchorPane reportView;
    private AnchorPane proposeView;
    private AnchorPane profileView;
    //book
    private AnchorPane bookPreview;
    private AnchorPane bookLoanPreview;
    //
    private final ObjectProperty<UserMenuOptions> userSelectedMenuItem;
    private final ObjectProperty<BookWindow> bookWindow;

    public ViewFactory() {
        bookWindow = new SimpleObjectProperty<>();
        userSelectedMenuItem = new SimpleObjectProperty<>();
    }


    public ObjectProperty<BookWindow> getBookWindowProperty() {
        return bookWindow;
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

    public AnchorPane getMyBookView() {
        if (myBookView == null) {
            try {
                myBookView = new FXMLLoader(getClass().getResource("/Fxml/User/MyBook.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myBookView;
    }

    public AnchorPane getSearchView() {
        if (searchView == null) {
            try {
                searchView = new FXMLLoader(getClass().getResource("/Fxml/User/Search.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return searchView;
    }

    public AnchorPane getProposeView() {
        if (proposeView == null) {
            try {
                proposeView = new FXMLLoader(getClass().getResource("/Fxml/User/Propose.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return proposeView;
    }

    public AnchorPane getProfileView() {
        if (profileView == null) {
            try {
                profileView = new FXMLLoader(getClass().getResource("/Fxml/User/Profile.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return profileView;
    }

    public AnchorPane getReportView() {
        if (reportView == null) {
            try {
                reportView = new FXMLLoader(getClass().getResource("/Fxml/User/Report.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reportView;
    }

    public AnchorPane getBookPreview() {
        if (bookPreview == null) {
            try {
                bookPreview = new FXMLLoader(getClass().getResource("/Fxml/Book/BookPreview.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bookPreview;
    }

    public AnchorPane getBookLoanPreview() {
        if (bookLoanPreview == null) {
            try {
                bookLoanPreview = new FXMLLoader(getClass().getResource("/Fxml/Book/BookLoanPreview.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bookLoanPreview;
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

        stage.setOnCloseRequest(event -> {     // khong nhan su kien khi goi stage.close()
            try {
                event.consume();
                exit(stage);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        stage.show();
    }

    public void exit(Stage stage) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close Window");
        alert.setHeaderText("Confirm to exit program");
        alert.setContentText("Do you want to exit ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Exit successfully");
            stage.close();
        }
    }


    public void closeStage(Stage stage) {
        stage.close();
    }

}
