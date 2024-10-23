package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    public BorderPane user_parent;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        handleChangeMenu();
        showBookWindows();
    }

    private void showBookWindows() {
        Model.getInstance().getViewFactory().getBookWindowProperty()
                .addListener((observableValue, oldVal, newVal) -> {
                    switch (newVal) {
                        case BOOKLOANPREVIEW ->
                                user_parent.setCenter(Model.getInstance().getViewFactory().getBookLoanPreview());
                        default -> user_parent.setCenter(Model.getInstance().getViewFactory().getBookPreview());

                    }
                });
    }

    private void handleChangeMenu() {
        Model.getInstance().getViewFactory().getUserSelectedMenuItem()
                .addListener((observableValue, oldVal, newVal) -> {
                    switch (newVal) {
                        case LOGOUT -> {
                            Model.getInstance().getViewFactory().showLoginWindow();
                            Stage stage = (Stage) user_parent.getScene().getWindow();
                            Model.getInstance().getViewFactory().closeStage(stage);
                        }
                        case SEARCH -> user_parent.setCenter(Model.getInstance().getViewFactory().getSearchView());
                        case PROFILE -> user_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
                        case PROPOSE -> user_parent.setCenter(Model.getInstance().getViewFactory().getProposeView());
                        case MYBOOK -> user_parent.setCenter(Model.getInstance().getViewFactory().getMyBookView());
                        default -> user_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView()
                        );
                    }
                });
    }
}
