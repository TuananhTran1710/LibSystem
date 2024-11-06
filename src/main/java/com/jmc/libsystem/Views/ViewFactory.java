package com.jmc.libsystem.Views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

// class nay lam nhiem vu chuyen doi giua cac cua so
public class ViewFactory {
    //Users View
    private AnchorPane dashboardView;
    private AnchorPane myBookView;
    private AnchorPane searchView;
    private AnchorPane reportView;
    private AnchorPane proposeView;
    private AnchorPane profileView;
    private VBox userMenu;
    //Admin View
    private AnchorPane adminDashboardView;
    private AnchorPane manageBookView;
    private AnchorPane manageUserView;
    private ScrollPane profileAdminView;
    private AnchorPane responseView;
    private VBox adminMenu;
    //book
    private AnchorPane bookPreview;
    private ScrollPane bookDetail;
    //
    private ObjectProperty<UserMenuOptions> userSelectedMenuItem;
    private ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;

    public ViewFactory() {
        userSelectedMenuItem = new SimpleObjectProperty<>();
        adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public ObjectProperty<UserMenuOptions> getUserSelectedMenuItem() {
        return userSelectedMenuItem;
    }

    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    public void setUserSelectedMenuItem(ObjectProperty<UserMenuOptions> userSelectedMenuItem) {
        this.userSelectedMenuItem = userSelectedMenuItem;
    }

    public void setAdminSelectedMenuItem(ObjectProperty<AdminMenuOptions> adminSelectedMenuItem) {
        this.adminSelectedMenuItem = adminSelectedMenuItem;
    }

    //--------------------------------ANCHOR USERS----------------------

//tat ca cac ham get Anchor, VBox nay muc dich de tranh load file fxml nhieu lan,
// vi khi load nhieu lan se goi lai initialize nhieu lan va dang ky nhieu su kien chong nhau

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

    public VBox getUserMenu() {
        if (userMenu == null) {
            try {
                userMenu = new FXMLLoader(getClass().getResource("/Fxml/User/UserMenu.fxml")).load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return userMenu;
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

    public ScrollPane getBookDetail() {
        if (bookDetail == null) {
            try {
                bookDetail = new FXMLLoader(getClass().getResource("/FXML/Book/BookDetail.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bookDetail;
    }

    //--------------------------------ANCHOR ADMIN----------------------

    public AnchorPane getAdminDashboardView() {
        // anchorPane giong nhu la mot loai Parent
        // tuong duong voi moi khi ta se khai bao Parent root
        if (adminDashboardView == null) {
            try {
                adminDashboardView = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminDashboard.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return adminDashboardView;
    }

    public AnchorPane getManageUserView() {
        if (manageUserView == null) {
            try {
                manageUserView = new FXMLLoader(getClass().getResource("/Fxml/Admin/ManageUser.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return manageUserView;
    }

    public AnchorPane getManageBookView() {
        if (manageBookView == null) {
            try {
                manageBookView = new FXMLLoader(getClass().getResource("/Fxml/Admin/ManageBook.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return manageBookView;
    }

    public AnchorPane getResponseView() {
        if (responseView == null) {
            try {
                responseView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Response.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseView;
    }

    public ScrollPane getProfileAdminView() {
        if (profileAdminView == null) {
            try {
                profileAdminView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Profile.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return profileAdminView;
    }

    public VBox getAdminMenu() {
        if (adminMenu == null) {
            try {
                adminMenu = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminMenu.fxml")).load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return adminMenu;
    }

    // -----------------------------------Function Show Window------------------------------------
    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void showCmtWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Book/test2.fxml"));
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
